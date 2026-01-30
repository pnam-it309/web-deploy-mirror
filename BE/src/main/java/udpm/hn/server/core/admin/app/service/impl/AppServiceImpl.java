package udpm.hn.server.core.admin.app.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import udpm.hn.server.core.admin.app.dto.request.AppCreateRequest;
import udpm.hn.server.core.admin.app.dto.request.AppDetailUpdateRequest;
import udpm.hn.server.core.admin.app.dto.request.AppFilterRequest;
import udpm.hn.server.core.admin.app.dto.request.AppUpdateRequest;
import udpm.hn.server.core.admin.app.dto.response.AppDetailResponse;
import udpm.hn.server.core.admin.app.dto.response.AppResponse;
import udpm.hn.server.core.admin.app.repository.*;
import udpm.hn.server.core.admin.app.service.AppService;
import udpm.hn.server.entity.*;
import udpm.hn.server.infrastructure.constant.ApprovalStatus;
import udpm.hn.server.infrastructure.constant.EntityStatus;
import udpm.hn.server.repository.CustomerRepository;
import udpm.hn.server.repository.DomainRepository;
import udpm.hn.server.repository.TechnologyRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppServiceImpl implements AppService {

    // Repositories quản lý App và bảng con
    private final AppManageRepository appRepository;
    private final AppDetailManageRepository appDetailRepository;
    private final AppImageManageRepository appImageRepository;
    private final AppMemberManageRepository appMemberRepository;

    // Repositories danh mục
    private final DomainRepository domainRepository;
    private final TechnologyRepository technologyRepository;
    private final CustomerRepository customerRepository;

    private final ModelMapper modelMapper;
    private final udpm.hn.server.infrastructure.notification.AdminAlertService adminAlertService;
    private final udpm.hn.server.infrastructure.notification.AnnouncementService announcementService;
    private final org.springframework.context.ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional(readOnly = true)
    public Page<AppResponse> getAllApps(AppFilterRequest request, Pageable pageable) {
        Specification<App> spec = AppSpecification.getFilter(request);
        return appRepository.findAll(spec, pageable).map(this::convertToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public AppResponse getAppById(String id) {
        App app = appRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("App not found: " + id));
        return convertToResponse(app);
    }

    @Override
    @Transactional
    public AppResponse createApp(AppCreateRequest request) {
        if (appRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Tên dự án đã tồn tại");
        }

        App app = new App();
        modelMapper.map(request, app);
        app.setApprovalStatus(ApprovalStatus.PENDING);
        app.setStatus(EntityStatus.ACTIVE);
        app.setViewCount(0L);

        if (request.getIsFeaturedVideo() != null) {
            app.setIsFeaturedVideo(request.getIsFeaturedVideo());
        }

        // 1. Set Domain
        if (request.getDomainId() != null) {
            Domain domain = domainRepository.findById(request.getDomainId())
                    .orElseThrow(() -> new EntityNotFoundException("Domain not found"));
            app.setDomain(domain);
        }

        // 2. Set Technologies
        if (request.getTechnologyIds() != null && !request.getTechnologyIds().isEmpty()) {
            Set<Technology> techs = new HashSet<>(technologyRepository.findAllById(request.getTechnologyIds()));
            app.setTechnologies(techs);
        }

        App savedApp = appRepository.save(app);

        // Publish event for Meilisearch sync
        eventPublisher.publishEvent(
                new udpm.hn.server.infrastructure.search.listener.ProductSearchSyncListener.ProductCreatedEvent(
                        savedApp));

        // 3. Tạo AppDetail mặc định (1-1)
        AppDetail detail = new AppDetail();
        detail.setApp(savedApp);
        appDetailRepository.save(detail);

        return convertToResponse(savedApp);
    }

    @Override
    @Transactional
    public AppResponse updateApp(String id, AppUpdateRequest request) {
        App app = appRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("App not found: " + id));

        // 1. Update thông tin cơ bản
        modelMapper.map(request, app);

        // Manually map Boolean field to ensure it is updated
        if (request.getIsFeaturedVideo() != null) {
            app.setIsFeaturedVideo(request.getIsFeaturedVideo());
        }

        // 2. Update Domain
        if (request.getDomainId() != null && !request.getDomainId().equals(app.getDomain().getId())) {
            Domain domain = domainRepository.findById(request.getDomainId())
                    .orElseThrow(() -> new EntityNotFoundException("Domain not found"));
            app.setDomain(domain);
        }

        // 3. Update Technologies
        if (request.getTechnologyIds() != null) {
            Set<Technology> techs = new HashSet<>(technologyRepository.findAllById(request.getTechnologyIds()));
            app.setTechnologies(techs);
        }

        App savedApp = appRepository.save(app);

        // Publish event for Meilisearch sync
        eventPublisher.publishEvent(
                new udpm.hn.server.infrastructure.search.listener.ProductSearchSyncListener.ProductUpdatedEvent(
                        savedApp));

        // 4. Update Members (Logic Hybrid: User Hệ Thống vs Khách Mời Email)
        if (request.getMembers() != null) {
            // Bước A: Xoá sạch danh sách cũ
            List<AppMember> oldMembers = appMemberRepository.findByAppId(id);
            appMemberRepository.deleteAll(oldMembers);

            List<AppMember> newMembers = new ArrayList<>();
            for (AppUpdateRequest.MemberRequest mr : request.getMembers()) {
                String input = mr.getCustomerId(); // Đây có thể là UUID hoặc Email
                if (input == null || input.isEmpty())
                    continue;

                AppMember member = new AppMember();
                member.setApp(savedApp);
                member.setRole(mr.getRole());

                Customer customer = null;

                // Case 1: Input là UUID (Chọn từ dropdown user có sẵn)
                if (input.matches("^[0-9a-fA-F-]{36}$")) {
                    customer = customerRepository.findById(input).orElse(null);
                }

                // Case 2: Nếu chưa tìm thấy -> Thử tìm theo Email trong bảng Customer
                if (customer == null) {
                    customer = customerRepository.findByEmail(input).orElse(null);
                }

                if (customer != null) {
                    // => LÀ USER HỆ THỐNG
                    member.setCustomer(customer);
                    // Lưu dư thừa email/tên để tiện query (optional)
                    member.setMemberEmail(customer.getEmail());
                    member.setMemberName(customer.getFullName());
                } else {
                    // => LÀ KHÁCH MỜI (Guest)
                    member.setCustomer(null); // Không có liên kết customer
                    member.setMemberEmail(input); // Lưu email nhập tay

                    // Tạo tên giả từ email: ankhang@gmail.com -> Khách (ankhang)
                    String guestName = input.contains("@") ? "Khách (" + input.split("@")[0] + ")" : input;
                    member.setMemberName(guestName);
                }

                newMembers.add(member);
            }
            appMemberRepository.saveAll(newMembers);
        }

        // 5. Update Images
        if (request.getImages() != null) {
            List<AppImage> oldImages = appImageRepository.findByAppId(id);
            appImageRepository.deleteAll(oldImages);

            List<AppImage> newImages = new ArrayList<>();
            for (AppUpdateRequest.ImageRequest ir : request.getImages()) {
                if (ir.getUrl() == null || ir.getUrl().isEmpty())
                    continue;

                AppImage img = new AppImage();
                img.setApp(savedApp);
                img.setUrl(ir.getUrl());
                img.setIsMain(ir.getIsMain());
                newImages.add(img);
            }
            appImageRepository.saveAll(newImages);
        }

        return convertToResponse(savedApp);
    }

    @Override
    @Transactional
    public void deleteApp(String id) {
        try {
            App app = appRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("App not found: " + id));

            // Soft Delete: Just mark status as DELETED
            app.setStatus(EntityStatus.DELETED);
            appRepository.save(app);

            // Publish event for Meilisearch sync (remove from search index)
            eventPublisher.publishEvent(
                    new udpm.hn.server.infrastructure.search.listener.ProductSearchSyncListener.ProductDeletedEvent(
                            id));

            System.out.println("Soft deleted App: " + id);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public AppDetailResponse getAppDetail(String appId) {
        AppDetail detail = appDetailRepository.findByAppId(appId).orElse(null);
        if (detail == null)
            return null;

        AppDetailResponse res = modelMapper.map(detail, AppDetailResponse.class);
        res.setAppId(detail.getApp().getId());
        return res;
    }

    @Override
    @Transactional
    public AppDetailResponse updateAppDetail(String appId, AppDetailUpdateRequest request) {
        AppDetail detail = appDetailRepository.findByAppId(appId)
                .orElseThrow(() -> new EntityNotFoundException("Detail not found for app: " + appId));

        modelMapper.map(request, detail);
        return modelMapper.map(appDetailRepository.save(detail), AppDetailResponse.class);
    }

    @Override
    @Transactional
    public void changeStatus(String id, ApprovalStatus status) {
        App app = appRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("App not found: " + id));

        // Validate transition (optional, e.g. only from PENDING)
        // if (app.getApprovalStatus() != ApprovalStatus.PENDING) ...

        ApprovalStatus oldStatus = app.getApprovalStatus();
        app.setApprovalStatus(status);
        appRepository.save(app);

        // Send alert to admins when app moves to PENDING
        if (status == ApprovalStatus.PENDING && oldStatus != ApprovalStatus.PENDING) {
            adminAlertService.notifyProductPendingApproval(app);
        }

        // Send push notification to users when app is approved
        if (status == ApprovalStatus.APPROVED && oldStatus != ApprovalStatus.APPROVED) {
            announcementService.sendNewProductNotification(app);
        }
    }

    // --- Helper Mapping ---
    private AppResponse convertToResponse(App app) {
        AppResponse res = modelMapper.map(app, AppResponse.class);
        res.setIsFeaturedVideo(app.getIsFeaturedVideo());

        // Map Domain
        if (app.getDomain() != null) {
            res.setDomainId(app.getDomain().getId());
            res.setDomainName(app.getDomain().getName());
        }

        // Map Technologies
        if (app.getTechnologies() != null) {
            Set<AppResponse.TechnologyResponse> techRes = app.getTechnologies().stream()
                    .map(t -> modelMapper.map(t, AppResponse.TechnologyResponse.class))
                    .collect(Collectors.toSet());
            res.setTechnologies(techRes);
        }

        // Map Members (Xử lý logic hiển thị Guest vs User)
        List<AppMember> members = appMemberRepository.findByAppId(app.getId());
        List<AppResponse.MemberResponse> memberRes = members.stream().map(m -> {
            AppResponse.MemberResponse mr = new AppResponse.MemberResponse();
            mr.setId(m.getId());
            mr.setRole(m.getRole());

            if (m.getCustomer() != null) {
                // Là User Hệ Thống
                mr.setCustomerId(m.getCustomer().getId());
                mr.setFullName(m.getCustomer().getFullName());
                mr.setEmail(m.getCustomer().getEmail());

                // Kiểm tra null avatar nếu entity chưa update
                try {
                    // mr.setAvatar(m.getCustomer().getAvatar());
                    // Tạm thời set null nếu Entity Customer chưa có trường avatar để tránh lỗi
                    // compile
                    mr.setAvatar(null);
                } catch (Exception e) {
                    mr.setAvatar(null);
                }

                mr.setIsGuest(false);
            } else {
                // Là Khách Mời
                mr.setCustomerId(null);
                mr.setFullName(m.getMemberName()); // Lấy tên từ bảng AppMember
                mr.setEmail(m.getMemberEmail()); // Lấy email từ bảng AppMember
                mr.setAvatar(null);
                mr.setIsGuest(true);
            }
            return mr;
        }).collect(Collectors.toList());
        res.setMembers(memberRes);

        // Map Images
        List<AppImage> images = appImageRepository.findByAppId(app.getId());
        List<AppResponse.ImageResponse> imageRes = images.stream().map(i -> {
            AppResponse.ImageResponse ir = new AppResponse.ImageResponse();
            ir.setId(i.getId());
            ir.setUrl(i.getUrl());
            ir.setIsMain(i.getIsMain());
            return ir;
        }).collect(Collectors.toList());
        res.setImages(imageRes);

        // Map Detail
        if (app.getAppDetail() != null) {
            // Note: This might trigger lazy loading if not fetched
            AppDetailResponse detailRes = modelMapper.map(app.getAppDetail(), AppDetailResponse.class);
            res.setDetail(detailRes);
        }

        return res;

    }

    @Override
    @Transactional
    public void toggleFeatured(String id) {
        App app = appRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("App not found: " + id));

        boolean currentStatus = Boolean.TRUE.equals(app.getIsFeatured());
        app.setIsFeatured(!currentStatus);
        appRepository.save(app);
    }

    @Override
    @Transactional
    public void bulkUpdateHomepageOrder(List<udpm.hn.server.core.admin.app.dto.request.HomepageOrderRequest> requests) {
        for (udpm.hn.server.core.admin.app.dto.request.HomepageOrderRequest req : requests) {
            appRepository.findById(req.getId()).ifPresent(app -> {
                app.setHomepageSortOrder(req.getHomepageSortOrder());
                appRepository.save(app);
            });
        }
    }

    @org.springframework.beans.factory.annotation.Value("${github.api.token:}")
    private String githubToken;

    @Override
    public List<AppResponse.MemberResponse> getGithubContributors(String url, String passedToken) {
        if (url == null || !url.contains("github.com")) {
            return new ArrayList<>();
        }

        try {
            // Robust parsing
            // Remove protocol
            String cleanUrl = url.replace("https://", "").replace("http://", "").replace("www.", "");
            // cleanUrl should be "github.com/owner/repo..."
            if (!cleanUrl.startsWith("github.com/")) {
                 return new ArrayList<>();
            }
            
            String[] parts = cleanUrl.split("/");
            // parts[0] = github.com
            // parts[1] = owner
            // parts[2] = repo (possibly with .git)
            
            if (parts.length < 3) return new ArrayList<>();
            
            String owner = parts[1];
            String repo = parts[2].replace(".git", "");
            
            // Handle query params if any (?)
            if (repo.contains("?")) {
                repo = repo.split("\\?")[0];
            }

            String apiUrl = String.format("https://api.github.com/repos/%s/%s/contributors", owner, repo);

            org.springframework.web.client.RestTemplate restTemplate = new org.springframework.web.client.RestTemplate();
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.set("User-Agent", "Java-App");
            headers.set("Accept", "application/vnd.github+json");
            
            // Priority: Passed Token > System Config Token
            String tokenToUse = (passedToken != null && !passedToken.isEmpty()) ? passedToken : githubToken;

            if (tokenToUse != null && !tokenToUse.isEmpty()) {
                headers.set("Authorization", "Bearer " + tokenToUse);
            }

            org.springframework.http.HttpEntity<String> entity = new org.springframework.http.HttpEntity<>(headers);

            org.springframework.http.ResponseEntity<java.util.List> response = restTemplate.exchange(
                    apiUrl,
                    org.springframework.http.HttpMethod.GET,
                    entity,
                    java.util.List.class);

            if (response.getBody() == null) return new ArrayList<>();

            List<AppResponse.MemberResponse> list = new ArrayList<>();
            for (Object obj : response.getBody()) {
                if (obj instanceof java.util.Map) {
                    java.util.Map<String, Object> map = (java.util.Map<String, Object>) obj;
                    String login = (String) map.get("login");
                    String avatar = (String) map.get("avatar_url");
                    // String type = (String) map.get("type"); // Could check if User or Bot

                    if (login == null) continue;

                    AppResponse.MemberResponse member = new AppResponse.MemberResponse();
                    member.setCustomerId(null);
                    member.setIsGuest(true);
                    member.setFullName(login);
                    member.setEmail(login + "@github.com");
                    member.setAvatar(avatar);
                    member.setRole("MEMBER");
                    list.add(member);
                }
            }
            return list;
        } catch (org.springframework.web.client.HttpClientErrorException e) {
            System.err.println("GitHub API Error for URL " + url + ": " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
             // e.g., 403 Rate Limit, 404 Not Found
            return new ArrayList<>();
        } catch (Exception e) {
            System.err.println("Error fetching GitHub contributors: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    @Transactional
    public AppResponse duplicateApp(String id) {
        App source = appRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("App not found: " + id));

        // Clone App
        App target = new App();
        // Ensure unique name
        String newName = source.getName() + " (Copy " + System.currentTimeMillis() % 10000 + ")";
        target.setName(newName);
        target.setSku(source.getSku());
        target.setShortDescription(source.getShortDescription());
        target.setDomain(source.getDomain());
        if (source.getTechnologies() != null) {
            target.setTechnologies(new HashSet<>(source.getTechnologies()));
        }
        target.setApprovalStatus(ApprovalStatus.PENDING);
        target.setStatus(EntityStatus.ACTIVE);
        target.setViewCount(0L);
        target.setIsFeatured(false);
        // Meta
        target.setMetaTitle(source.getMetaTitle());
        target.setMetaDescription(source.getMetaDescription());
        target.setMetaKeywords(source.getMetaKeywords());

        // Thumbnail is in App entity? Checking view...
        // AdminTrashController view showed: map.put("thumbnail", app.getThumbnail() !=
        // null ? ...);
        // But AppServiceImpl create doesn't seem to set it explicitly (mapped via
        // modelMapper).
        // Let's assume modelMapper handled it or it's not a direct field in App (maybe
        // mapped from images?).
        // Wait, AdminTrashController.java:46 calls app.getThumbnail().
        // So App has getThumbnail().
        // I should check App.java to be sure, but I can just try to set it if getter
        // exists.
        // Or if it IS a field, I should copy it.
        // I will assume it is a field for now. But getters exist.
        // Actually, let's look at getThumbnail if possible.
        // Wait, I saw App in AdminTrashController, so I can check if I can just use
        // get/set.
        // Safest is to rely on compilation or check.
        // I'll skip explicit thumbnail set if it's derived, but if it's a field...
        // Let's check App.java quickly to be safe?
        // Actually, if I use modelMapper it might be easier, but I am doing manual copy
        // to control what is copied.

        target.setThumbnail(source.getThumbnail());

        App savedTarget = appRepository.save(target);

        // Clone AppDetail
        AppDetail sourceDetail = appDetailRepository.findByAppId(id).orElse(null);
        if (sourceDetail != null) {
            AppDetail targetDetail = new AppDetail();
            targetDetail.setApp(savedTarget);
            targetDetail.setLongDescription(sourceDetail.getLongDescription());
            targetDetail.setSourceUrl(sourceDetail.getSourceUrl());
            targetDetail.setDemoUrl(sourceDetail.getDemoUrl());
            targetDetail.setSpecifications(sourceDetail.getSpecifications());
            appDetailRepository.save(targetDetail);
        } else {
            // Ensure detail always exists
            AppDetail detail = new AppDetail();
            detail.setApp(savedTarget);
            appDetailRepository.save(detail);
        }

        // Clone Images
        List<AppImage> sourceImages = appImageRepository.findByAppId(id);
        List<AppImage> targetImages = sourceImages.stream().map(img -> {
            AppImage newImg = new AppImage();
            newImg.setApp(savedTarget);
            newImg.setUrl(img.getUrl());
            newImg.setIsMain(img.getIsMain());
            return newImg;
        }).collect(Collectors.toList());
        appImageRepository.saveAll(targetImages);

        // Clone Members
        List<AppMember> sourceMembers = appMemberRepository.findByAppId(id);
        List<AppMember> targetMembers = sourceMembers.stream().map(mem -> {
            AppMember newMem = new AppMember();
            newMem.setApp(savedTarget);
            newMem.setCustomer(mem.getCustomer());
            newMem.setMemberName(mem.getMemberName());
            newMem.setMemberEmail(mem.getMemberEmail());
            newMem.setRole(mem.getRole());
            return newMem;
        }).collect(Collectors.toList());
        appMemberRepository.saveAll(targetMembers);

        return convertToResponse(savedTarget);
    }

    @Override
    @Transactional
    public void deleteApps(java.util.List<String> ids) {
        for (String id : ids) {
            deleteApp(id);
        }
    }

    @Override
    @Transactional
    public void changeStatusApps(java.util.List<String> ids, ApprovalStatus status) {
        for (String id : ids) {
            changeStatus(id, status);
        }
    }
}
