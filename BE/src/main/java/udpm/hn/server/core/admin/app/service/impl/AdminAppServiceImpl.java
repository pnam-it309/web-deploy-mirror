package udpm.hn.server.core.admin.app.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import udpm.hn.server.core.admin.app.model.request.*;
import udpm.hn.server.core.admin.app.model.response.AdminAppResponse;
import udpm.hn.server.core.admin.app.repository.*;
import udpm.hn.server.core.admin.feature.repository.AdminFeatureRepository;
import udpm.hn.server.core.admin.technology.repository.AdminTechnologyRepository;
import udpm.hn.server.core.admin.app.service.AdminAppService;
import udpm.hn.server.core.admin.domain.repository.AdminDomainRepository;
import udpm.hn.server.entity.*;
import udpm.hn.server.infrastructure.constant.EntityStatus;
import udpm.hn.server.repository.BrandRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminAppServiceImpl implements AdminAppService {

    private final AdminAppRepository appRepository;
    private final AdminAppDetailRepository appDetailRepository;
    private final AdminAppImageRepository appImageRepository;
    private final AdminFeatureRepository featureRepository;
    private final AdminDomainRepository domainRepository;
    private final BrandRepository brandRepository;
    private final AdminTechnologyRepository technologyRepository;

    @Override
    public List<AdminAppResponse> getAll() {
        return appRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AdminAppResponse getDetail(String id) {
        return appRepository.findById(id)
                .map(this::toResponse)
                .orElse(null);
    }

    @Override
    @Transactional
    public String create(AdminCreateAppRequest request) {
        App app = new App();
        app.setName(request.getName());
        app.setSku(request.getSku());
        app.setPrice(request.getPrice());
        app.setShortDescription(request.getShortDescription());
        app.setThumbnail(request.getThumbnail());
        app.setStatus(EntityStatus.ACTIVE);
        app.setApprovalStatus(udpm.hn.server.infrastructure.constant.ApprovalStatus.DRAFT);

        Optional<Domain> domain = domainRepository.findById(request.getDomainId());
        if (domain.isPresent()) {
            app.setDomain(domain.get());
        } else {
            throw new RuntimeException("Domain not found");
        }

        if (request.getBrandId() != null) {
            Optional<Brand> brand = brandRepository.findById(request.getBrandId());
            brand.ifPresent(app::setBrand);
        }

        App savedApp = appRepository.save(app);

        AppDetail detail = new AppDetail();
        detail.setApp(savedApp);
        appDetailRepository.save(detail);

        return savedApp.getId();
    }

    @Override
    @Transactional
    public Boolean update(String id, AdminUpdateAppRequest request) {
        Optional<App> optionalApp = appRepository.findById(id);
        if (optionalApp.isEmpty())
            return false;

        App app = optionalApp.get();
        app.setName(request.getName());
        app.setSku(request.getSku());
        app.setPrice(request.getPrice());
        app.setShortDescription(request.getShortDescription());
        app.setThumbnail(request.getThumbnail());

        if (request.getDomainId() != null) {
            domainRepository.findById(request.getDomainId()).ifPresent(app::setDomain);
        }

        if (request.getBrandId() != null) {
            brandRepository.findById(request.getBrandId()).ifPresent(app::setBrand);
        }

        if (request.getIsFeatured() != null) {
            app.setIsFeatured(request.getIsFeatured());
        }

        if (request.getHomepageSortOrder() != null) {
            app.setHomepageSortOrder(request.getHomepageSortOrder());
        }

        if (request.getApprovalStatus() != null) {
            try {
                app.setApprovalStatus(udpm.hn.server.infrastructure.constant.ApprovalStatus
                        .valueOf(request.getApprovalStatus().toUpperCase()));
            } catch (Exception e) {
            }
        }

        if (request.getTechnologyIds() != null) {
            List<Technology> techs = technologyRepository.findAllById(request.getTechnologyIds());
            app.setTechnologies(new java.util.HashSet<>(techs));
        }

        appRepository.save(app);

        Optional<AppDetail> optionalDetail = appDetailRepository.findByApp_Id(id);
        AppDetail detail;
        if (optionalDetail.isPresent()) {
            detail = optionalDetail.get();
        } else {
            detail = new AppDetail();
            detail.setApp(app);
        }

        detail.setLongDescription(request.getLongDescription());
        detail.setDemoUrl(request.getDemoUrl());
        detail.setSourceUrl(request.getSourceUrl());
        detail.setSpecifications(request.getSpecifications());

        appDetailRepository.save(detail);

        return true;
    }

    @Override
    public Boolean delete(String id) {
        if (appRepository.existsById(id)) {
            appRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Boolean toggleStatus(String id) {
        Optional<App> optionalApp = appRepository.findById(id);
        if (optionalApp.isPresent()) {
            App app = optionalApp.get();
            app.setStatus(app.getStatus() == EntityStatus.ACTIVE ? EntityStatus.INACTIVE : EntityStatus.ACTIVE);
            appRepository.save(app);
            return true;
        }
        return false;
    }

    @Override
    public String uploadImage(String id, MultipartFile file) {
        if (file.getSize() > 5 * 1024 * 1024)
            throw new RuntimeException("Max file size is 5MB");
        String fakeUrl = "https://placeholder.com/" + file.getOriginalFilename();

        AppImage appImage = new AppImage();
        appRepository.findById(id).ifPresent(app -> {
            appImage.setApp(app);
            appImage.setUrl(fakeUrl);
            appImage.setIsMain(false);
            appImageRepository.save(appImage);
        });

        return fakeUrl;
    }

    @Override
    public Boolean addFeature(String id, AdminCreateFeatureRequest request) {
        Optional<App> app = appRepository.findById(id);
        if (app.isPresent()) {
            Feature feature = new Feature();
            feature.setApp(app.get());
            feature.setName(request.getName());
            feature.setDescription(request.getDescription());
            feature.setImagePreview(request.getImagePreview());
            feature.setSortOrder(request.getSortOrder());
            featureRepository.save(feature);
            return true;
        }
        return false;
    }

    @Override
    public Boolean assignDeveloper(String id, String brandId) {
        Optional<App> app = appRepository.findById(id);
        Optional<Brand> brand = brandRepository.findById(brandId);
        if (app.isPresent() && brand.isPresent()) {
            app.get().setBrand(brand.get());
            appRepository.save(app.get());
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public Boolean updateHomepageConfig(String id, AdminUpdateAppConfigRequest request) {
        Optional<App> optionalApp = appRepository.findById(id);
        if (optionalApp.isPresent()) {
            App app = optionalApp.get();
            if (request.getIsFeatured() != null) {
                app.setIsFeatured(request.getIsFeatured());
            }
            if (request.getHomepageSortOrder() != null) {
                app.setHomepageSortOrder(request.getHomepageSortOrder());
            }
            appRepository.save(app);
            return true;
        }
        return false;
    }

    private AdminAppResponse toResponse(App app) {
        AdminAppResponse response = new AdminAppResponse();
        response.setId(app.getId());
        response.setName(app.getName());
        response.setSku(app.getSku());
        response.setPrice(app.getPrice());
        response.setShortDescription(app.getShortDescription());
        response.setThumbnail(app.getThumbnail());
        response.setStatus(app.getStatus());

        response.setIsFeatured(app.getIsFeatured());
        response.setHomepageSortOrder(app.getHomepageSortOrder());
        response.setViewCount(app.getViewCount());

        if (app.getApprovalStatus() != null) {
            response.setApprovalStatus(app.getApprovalStatus().name());
        }

        if (app.getTechnologies() != null) {
            response.setTechnologyNames(
                    app.getTechnologies().stream().map(Technology::getName).collect(Collectors.toList()));
        }

        if (app.getDomain() != null) {
            response.setDomainId(app.getDomain().getId());
            response.setDomainName(app.getDomain().getName());
        }

        if (app.getBrand() != null) {
            response.setBrandId(app.getBrand().getId());
            response.setBrandName(app.getBrand().getName());
        }

        Optional<AppDetail> detail = appDetailRepository.findByApp_Id(app.getId());
        detail.ifPresent(d -> {
            response.setLongDescription(d.getLongDescription());
            response.setDemoUrl(d.getDemoUrl());
            response.setSourceUrl(d.getSourceUrl());
            response.setSpecifications(d.getSpecifications());
        });

        response.setCreatedDate(app.getCreatedDate());
        response.setLastModifiedDate(app.getLastModifiedDate());

        return response;
    }
}
