package udpm.hn.server.core.admin.feature.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import udpm.hn.server.core.admin.app.repository.AppManageRepository;
import udpm.hn.server.core.admin.feature.dto.request.FeatureRequest;
import udpm.hn.server.core.admin.feature.dto.response.FeatureResponse;
import udpm.hn.server.core.admin.feature.repository.FeatureManageRepository;
import udpm.hn.server.core.admin.feature.service.FeatureService;
import udpm.hn.server.entity.App;
import udpm.hn.server.entity.Feature;
import org.springframework.data.domain.Sort; // Import thêm cái này để sort

import udpm.hn.server.infrastructure.constant.EntityStatus;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeatureServiceImpl implements FeatureService {

    private final FeatureManageRepository featureRepository;
    private final AppManageRepository appRepository;
    private final ModelMapper modelMapper;

    // --- BỔ SUNG HÀM NÀY ĐỂ HẾT LỖI 500/405 ---
    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<FeatureResponse> getAllFeatures() {
        // Lấy tất cả và sắp xếp theo thứ tự hiển thị (sortOrder)
        return featureRepository.findAll(Sort.by(Sort.Direction.ASC, "sortOrder")).stream()
                .map(f -> {
                    FeatureResponse res = modelMapper.map(f, FeatureResponse.class);
                    if (f.getApp() != null) {
                        res.setAppName(f.getApp().getName());
                    }
                    return res;
                })
                .collect(Collectors.toList());
    }
    // ------------------------------------------

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<FeatureResponse> getFeaturesByAppId(String appId) {
        return featureRepository.findByAppIdOrderBySortOrderAsc(appId).stream()
                .map(f -> {
                    FeatureResponse res = modelMapper.map(f, FeatureResponse.class);
                     if (f.getApp() != null) {
                        res.setAppName(f.getApp().getName());
                    }
                    return res;
                })
                .collect(Collectors.toList());
    }

    @Override
    public FeatureResponse createFeature(FeatureRequest request) {
        App app = appRepository.findById(request.getAppId())
                .orElseThrow(() -> new EntityNotFoundException("App not found"));

        Feature feature = modelMapper.map(request, Feature.class);
        feature.setApp(app);

        if (feature.getSortOrder() == null) {
            feature.setSortOrder(99);
        }

        // Set Default Status if null
        if (request.getStatus() != null) {
            try {
                feature.setStatus(EntityStatus.valueOf(request.getStatus()));
            } catch (Exception e) {
                feature.setStatus(EntityStatus.ACTIVE);
            }
        } else {
            feature.setStatus(EntityStatus.ACTIVE);
        }

        return modelMapper.map(featureRepository.save(feature), FeatureResponse.class);
    }

    @Override
    public FeatureResponse updateFeature(String id, FeatureRequest request) {
        Feature feature = featureRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Feature not found"));

        // Cập nhật các trường cơ bản
        feature.setName(request.getName());
        feature.setDescription(request.getDescription());
        feature.setImagePreview(request.getImagePreview());
        feature.setVideoUrl(request.getVideoUrl());
        feature.setSortOrder(request.getSortOrder());

        if (request.getStatus() != null) {
            try {
                feature.setStatus(EntityStatus.valueOf(request.getStatus()));
            } catch (Exception e) {
                // Keep old status
            }
        }

        // Nếu muốn cho phép đổi App của Feature
        if (request.getAppId() != null && !request.getAppId().equals(feature.getApp().getId())) {
            App newApp = appRepository.findById(request.getAppId())
                    .orElseThrow(() -> new EntityNotFoundException("App not found"));
            feature.setApp(newApp);
        }

        return modelMapper.map(featureRepository.save(feature), FeatureResponse.class);
    }

    @Override
    public void deleteFeature(String id) {
        featureRepository.deleteById(id);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void bulkUpdateOrder(
            java.util.List<udpm.hn.server.core.admin.feature.dto.request.FeatureOrderRequest> requests) {
        for (udpm.hn.server.core.admin.feature.dto.request.FeatureOrderRequest req : requests) {
            featureRepository.findById(req.getId()).ifPresent(feature -> {
                feature.setSortOrder(req.getSortOrder());
                featureRepository.save(feature);
            });
        }
    }
}