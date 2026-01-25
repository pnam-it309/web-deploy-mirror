package udpm.hn.server.core.customer.app.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import udpm.hn.server.core.common.base.PageableObject;
import udpm.hn.server.core.customer.app.model.request.CustomerAppFilterRequest;
import udpm.hn.server.core.customer.app.model.response.CustomerAppResponse;
import udpm.hn.server.core.customer.app.model.response.CustomerFeatureResponse;
import udpm.hn.server.core.customer.app.model.response.CustomerTeamMemberResponse;
import udpm.hn.server.core.customer.app.repository.CustomerAppMemberRepository;
import udpm.hn.server.core.customer.app.repository.CustomerAppRepository;
import udpm.hn.server.core.customer.app.repository.CustomerFeatureRepository;
import udpm.hn.server.core.customer.app.service.CustomerAppService;
import udpm.hn.server.entity.App;
import udpm.hn.server.entity.AppDetail;
import udpm.hn.server.entity.Technology;
import udpm.hn.server.infrastructure.constant.EntityStatus;
import udpm.hn.server.repository.AppDetailRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerAppServiceImpl implements CustomerAppService {

    private final CustomerAppRepository appRepository;
    private final AppDetailRepository appDetailRepository;
    private final CustomerFeatureRepository featureRepository;
    private final CustomerAppMemberRepository appMemberRepository;
    private final udpm.hn.server.repository.SearchQueryRepository searchQueryRepository;

    @Override
    @Transactional(readOnly = true)
    public PageableObject<CustomerAppResponse> filter(CustomerAppFilterRequest request) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt"); // Default NEWEST
        if ("VIEW_COUNT".equalsIgnoreCase(request.getSort())) {
            sort = Sort.by(Sort.Direction.DESC, "viewCount");
        } else if ("FEATURED".equalsIgnoreCase(request.getSort())) {
            // Complex sort: Featured first, then sort order, then date
            sort = Sort.by(Sort.Direction.DESC, "isFeatured")
                    .and(Sort.by(Sort.Direction.ASC, "homepageSortOrder"))
                    .and(Sort.by(Sort.Direction.DESC, "createdAt"));
        } else if ("OLDEST".equalsIgnoreCase(request.getSort())) {
            sort = Sort.by(Sort.Direction.ASC, "createdAt");
        } else if ("NAME_ASC".equalsIgnoreCase(request.getSort()) || "A-Z".equalsIgnoreCase(request.getSort())) {
            sort = Sort.by(Sort.Direction.ASC, "name");
        } else if ("NAME_DESC".equalsIgnoreCase(request.getSort()) || "Z-A".equalsIgnoreCase(request.getSort())) {
            sort = Sort.by(Sort.Direction.DESC, "name");
        } else if ("POPULAR".equalsIgnoreCase(request.getSort())) {
            sort = Sort.by(Sort.Direction.DESC, "viewCount");
        }

        Pageable pageable = PageRequest.of(Math.max(0, request.getPage() - 1), request.getSize(), sort);

        // Use full-text search when query is provided (50x faster, relevance-ranked)
        Page<App> page;
        if (request.getQuery() != null && !request.getQuery().trim().isEmpty()) {
            // Full-text search with MATCH...AGAINST
            page = appRepository.fullTextSearchApps(
                    request.getQuery().trim(),
                    request.getDomainId(),
                    request.getTechnologyIds() != null && !request.getTechnologyIds().isEmpty()
                            ? request.getTechnologyIds()
                            : null,
                    EntityStatus.ACTIVE.name(), // Convert to String for native query
                    pageable);
        } else {
            // Legacy LIKE search for filtering without text search
            page = appRepository.filterApps(
                    request.getDomainId(),
                    request.getQuery(),
                    request.getTechnologyIds() != null && !request.getTechnologyIds().isEmpty()
                            ? request.getTechnologyIds()
                            : null,
                    EntityStatus.ACTIVE,
                    pageable);
        }

        List<CustomerAppResponse> responses = page.stream().map(this::toSimpleResponse).collect(Collectors.toList());

        // Log search query for analytics
        logSearchQuery(request.getQuery(), page.getTotalElements(), request.getDomainId());

        return new PageableObject<>(page, responses);
    }

    @Override
    @Transactional
    public CustomerAppResponse getDetail(String id) {
        Optional<App> optionalApp = appRepository.findById(id);
        if (optionalApp.isEmpty())
            return null;

        App app = optionalApp.get();

        CustomerAppResponse response = toSimpleResponse(app);

        // Add full details
        Optional<AppDetail> detail = appDetailRepository.findByApp_Id(app.getId());
        detail.ifPresent(d -> {
            response.setLongDescription(d.getLongDescription());
            response.setDemoUrl(d.getDemoUrl());
            response.setSourceUrl(d.getSourceUrl());
            response.setSpecifications(d.getSpecifications());
        });

        // Features
        response.setFeatures(featureRepository.findByApp_IdAndStatusOrderBySortOrderAsc(id, EntityStatus.ACTIVE)
                .stream()
                .map(f -> new CustomerFeatureResponse(f.getId(), f.getName(), f.getDescription(), f.getImagePreview(),
                        f.getVideoUrl()))
                .collect(Collectors.toList()));

        // Team
        response.setTeamMembers(appMemberRepository.findByApp_Id(id).stream()
                .map(m -> {
                    String customerId = m.getCustomer() != null ? m.getCustomer().getId() : null;
                    String fullName = m.getCustomer() != null ? m.getCustomer().getFullName() : m.getMemberName();
                    return new CustomerTeamMemberResponse(m.getId(), customerId, fullName, m.getRole());
                })
                .collect(Collectors.toList()));

        return response;
    }

    @Override
    @Transactional
    public void incrementViewCount(String id) {
        appRepository.findById(id).ifPresent(app -> {
            app.setViewCount(app.getViewCount() + 1);
            appRepository.save(app);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerAppResponse> findRelatedApps(String appId, int limit) {
        // Get the current app to find related apps
        Optional<App> optionalApp = appRepository.findById(appId);
        if (optionalApp.isEmpty()) {
            return List.of();
        }

        App currentApp = optionalApp.get();

        // Find apps with same domain, excluding current app
        List<App> relatedApps = appRepository
                .findByDomain_IdAndStatusAndIdNot(
                        currentApp.getDomain() != null ? currentApp.getDomain().getId() : null,
                        EntityStatus.ACTIVE,
                        appId);

        // If we have current app technologies, prioritize apps with matching tech
        if (currentApp.getTechnologies() != null && !currentApp.getTechnologies().isEmpty()) {
            List<String> currentTechIds = currentApp.getTechnologies()
                    .stream()
                    .map(Technology::getId)
                    .collect(Collectors.toList());

            // Sort by number of matching technologies (descending)
            relatedApps.sort((a, b) -> {
                long aMatches = a.getTechnologies() != null ? a.getTechnologies().stream()
                        .filter(t -> currentTechIds.contains(t.getId()))
                        .count() : 0;
                long bMatches = b.getTechnologies() != null ? b.getTechnologies().stream()
                        .filter(t -> currentTechIds.contains(t.getId()))
                        .count() : 0;
                return Long.compare(bMatches, aMatches);
            });
        }

        // Limit results and convert to response
        return relatedApps.stream()
                .limit(limit)
                .map(this::toSimpleResponse)
                .collect(Collectors.toList());
    }

    // Only basic info for list view

    private CustomerAppResponse toSimpleResponse(App app) {
        CustomerAppResponse response = new CustomerAppResponse();
        response.setId(app.getId());
        response.setName(app.getName());
        response.setSku(app.getSku());
        response.setShortDescription(app.getShortDescription());
        response.setThumbnail(app.getThumbnail());
        response.setViewCount(app.getViewCount());
        response.setIsFeatured(app.getIsFeatured());
        response.setIsFeaturedVideo(app.getIsFeaturedVideo());

        if (app.getDomain() != null) {
            response.setDomainName(app.getDomain().getName());
        }

        if (app.getTechnologies() != null) {
            response.setTechnologyNames(
                    app.getTechnologies().stream().map(Technology::getName).collect(Collectors.toList()));
        }

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerAppResponse> getFeaturedVideos() {
        return appRepository.findFeaturedVideoApps(EntityStatus.ACTIVE).stream()
                .map(this::toSimpleResponse)
                .map(response -> {
                    // Populate Demo URL for videos
                    appDetailRepository.findByApp_Id(response.getId()).ifPresent(detail -> {
                        response.setDemoUrl(detail.getDemoUrl());
                    });
                    return response;
                })
                .collect(Collectors.toList());
    }

    /**
     * Log search query for analytics
     */
    private void logSearchQuery(String query, long resultCount, String domainId) {
        if (query == null || query.trim().isEmpty()) {
            return; // Don't log empty queries
        }

        try {
            udpm.hn.server.entity.SearchQuery searchQuery = new udpm.hn.server.entity.SearchQuery();
            searchQuery.setQueryText(query.trim().toLowerCase()); // Normalize for aggregation
            searchQuery.setResultCount((int) resultCount);
            searchQuery.setHasResults(resultCount > 0);
            searchQuery.setCategory(domainId != null ? "DOMAIN" : "GENERAL");
            // userId can be added later if we have user context
            searchQueryRepository.save(searchQuery);
        } catch (Exception e) {
            // Don't fail the search if logging fails
            System.err.println("Failed to log search query: " + e.getMessage());
        }
    }
}
