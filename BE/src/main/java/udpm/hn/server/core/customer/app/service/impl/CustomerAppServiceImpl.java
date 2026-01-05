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

    @Override
    public PageableObject<CustomerAppResponse> filter(CustomerAppFilterRequest request) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdDate"); // Default NEWEST
        if ("VIEW_COUNT".equalsIgnoreCase(request.getSort())) {
            sort = Sort.by(Sort.Direction.DESC, "viewCount");
        } else if ("FEATURED".equalsIgnoreCase(request.getSort())) {
            // Complex sort: Featured first, then sort order, then date
            sort = Sort.by(Sort.Direction.DESC, "isFeatured")
                    .and(Sort.by(Sort.Direction.ASC, "homepageSortOrder"))
                    .and(Sort.by(Sort.Direction.DESC, "createdDate"));
        } else if ("OLDEST".equalsIgnoreCase(request.getSort())) {
            sort = Sort.by(Sort.Direction.ASC, "createdDate");
        }

        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(), sort);

        Page<App> page = appRepository.filterApps(
                request.getDomainId(),
                request.getQuery(),
                request.getTechnologyIds() != null && !request.getTechnologyIds().isEmpty() ? request.getTechnologyIds()
                        : null,
                EntityStatus.ACTIVE,
                pageable);

        List<CustomerAppResponse> responses = page.stream().map(this::toSimpleResponse).collect(Collectors.toList());

        return new PageableObject<>(page, responses);
    }

    @Override
    @Transactional
    public CustomerAppResponse getDetail(String id) {
        Optional<App> optionalApp = appRepository.findById(id);
        if (optionalApp.isEmpty())
            return null;

        App app = optionalApp.get();

        // Increase view count
        app.setViewCount(app.getViewCount() + 1);
        appRepository.save(app);

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
                .map(f -> new CustomerFeatureResponse(f.getId(), f.getName(), f.getDescription(), f.getImagePreview()))
                .collect(Collectors.toList()));

        // Team
        response.setTeamMembers(appMemberRepository.findByApp_Id(id).stream()
                .map(m -> new CustomerTeamMemberResponse(m.getCustomer().getId(), m.getCustomer().getFullName(),
                        m.getRole()))
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

        if (app.getDomain() != null) {
            response.setDomainName(app.getDomain().getName());
        }


        if (app.getTechnologies() != null) {
            response.setTechnologyNames(
                    app.getTechnologies().stream().map(Technology::getName).collect(Collectors.toList()));
        }

        return response;
    }
}
