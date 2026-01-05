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
import udpm.hn.server.core.admin.feature.repository.FeatureManageRepository;
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
    private final FeatureManageRepository featureRepository; // Inject thêm để xoá Feature

    // Repositories danh mục
    private final DomainRepository domainRepository;
    private final TechnologyRepository technologyRepository;
    private final CustomerRepository customerRepository;

    private final ModelMapper modelMapper;

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

        // 4. Update Members (Logic Hybrid: User Hệ Thống vs Khách Mời Email)
        if (request.getMembers() != null) {
            // Bước A: Xoá sạch danh sách cũ
            List<AppMember> oldMembers = appMemberRepository.findByAppId(id);
            appMemberRepository.deleteAll(oldMembers);

            List<AppMember> newMembers = new ArrayList<>();
            for (AppUpdateRequest.MemberRequest mr : request.getMembers()) {
                String input = mr.getCustomerId(); // Đây có thể là UUID hoặc Email
                if (input == null || input.isEmpty()) continue;

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
                if(ir.getUrl() == null || ir.getUrl().isEmpty()) continue;

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
        if (!appRepository.existsById(id)) {
            throw new EntityNotFoundException("App not found: " + id);
        }
        featureRepository.deleteByAppId(id);
        appMemberRepository.deleteByAppId(id);
        appImageRepository.deleteByAppId(id);
        appDetailRepository.deleteByAppId(id);
        appRepository.flush();
        appRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public AppDetailResponse getAppDetail(String appId) {
        AppDetail detail = appDetailRepository.findByAppId(appId).orElse(null);
        if (detail == null) return null;

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

    // --- Helper Mapping ---
    private AppResponse convertToResponse(App app) {
        AppResponse res = modelMapper.map(app, AppResponse.class);

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
                    // Tạm thời set null nếu Entity Customer chưa có trường avatar để tránh lỗi compile
                    mr.setAvatar(null);
                } catch (Exception e) {
                    mr.setAvatar(null);
                }

                mr.setIsGuest(false);
            } else {
                // Là Khách Mời
                mr.setCustomerId(null);
                mr.setFullName(m.getMemberName()); // Lấy tên từ bảng AppMember
                mr.setEmail(m.getMemberEmail());   // Lấy email từ bảng AppMember
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

        return res;
    }
}