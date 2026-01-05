package udpm.hn.server.core.admin.feature.service;

import udpm.hn.server.core.admin.feature.dto.request.FeatureRequest;
import udpm.hn.server.core.admin.feature.dto.response.FeatureResponse;

import java.util.List;

public interface FeatureService {
    List<FeatureResponse> getAllFeatures();
    List<FeatureResponse> getFeaturesByAppId(String appId);
    FeatureResponse createFeature(FeatureRequest request);
    FeatureResponse updateFeature(String id, FeatureRequest request);
    void deleteFeature(String id);
}