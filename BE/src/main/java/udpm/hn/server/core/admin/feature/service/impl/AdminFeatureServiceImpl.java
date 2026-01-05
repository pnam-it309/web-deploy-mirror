package udpm.hn.server.core.admin.feature.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import udpm.hn.server.core.admin.feature.model.request.AdminCreateFeatureRequest;
import udpm.hn.server.core.admin.feature.model.request.AdminUpdateFeatureRequest;
import udpm.hn.server.core.admin.feature.model.response.AdminFeatureResponse;
import udpm.hn.server.core.admin.feature.repository.AdminFeatureRepository;
import udpm.hn.server.core.admin.feature.service.AdminFeatureService;
import udpm.hn.server.entity.App;
import udpm.hn.server.entity.Feature;
import udpm.hn.server.repository.AppRepository;
import udpm.hn.server.infrastructure.constant.EntityStatus;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminFeatureServiceImpl implements AdminFeatureService {

    private final AdminFeatureRepository featureRepository;
    private final AppRepository appRepository;

    @Override
    @Transactional(readOnly = true)
    public List<AdminFeatureResponse> getAll() {
        return featureRepository.findAll().stream()
                .map(AdminFeatureResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminFeatureResponse> getAllByAppId(String appId) {
        return featureRepository.findAllByAppIdOrderBySortOrderAsc(appId).stream()
                .map(AdminFeatureResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AdminFeatureResponse getDetail(String id) {
        return featureRepository.findById(id).map(AdminFeatureResponse::new).orElse(null);
    }

    @Override
    public String create(AdminCreateFeatureRequest request) {
        if (request.getAppId() == null)
            return null;
        Optional<App> app = appRepository.findById(request.getAppId());
        if (app.isEmpty()) {
            return null;
        }
        Feature feature = new Feature();
        feature.setApp(app.get());
        feature.setName(request.getName());
        feature.setDescription(request.getDescription());
        feature.setImagePreview(request.getImagePreview());
        feature.setSortOrder(request.getSortOrder());
        Feature saved = featureRepository.save(feature);
        return saved.getId();
    }

    @Override
    public Boolean update(String id, AdminUpdateFeatureRequest request) {
        Optional<Feature> featureOptional = featureRepository.findById(id);
        if (featureOptional.isPresent()) {
            Feature feature = featureOptional.get();
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
    public Boolean delete(String id) {
        if (featureRepository.existsById(id)) {
            featureRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Boolean toggleStatus(String id) {
        Optional<Feature> optionalFeature = featureRepository.findById(id);
        if (optionalFeature.isPresent()) {
            Feature feature = optionalFeature.get();
            feature.setStatus(feature.getStatus() == EntityStatus.ACTIVE ? EntityStatus.INACTIVE : EntityStatus.ACTIVE);
            featureRepository.save(feature);
            return true;
        }
        return false;
    }

    @Override
    public String uploadImage(String id, org.springframework.web.multipart.MultipartFile file) {
        if (file.getSize() > 5 * 1024 * 1024)
            throw new RuntimeException("Max file size is 5MB");

        // Mock implementation
        String fakeUrl = "https://placeholder.com/feature/" + file.getOriginalFilename();

        // Optionally save to entity if it's the main preview
        // For now, we just return the URL for the frontend to attach to the update
        // request
        return fakeUrl;
    }
}
