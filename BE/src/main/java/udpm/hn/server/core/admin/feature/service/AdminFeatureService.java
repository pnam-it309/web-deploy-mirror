package udpm.hn.server.core.admin.feature.service;

import udpm.hn.server.core.admin.feature.model.request.AdminCreateFeatureRequest;
import udpm.hn.server.core.admin.feature.model.request.AdminUpdateFeatureRequest;
import udpm.hn.server.core.admin.feature.model.response.AdminFeatureResponse;

import java.util.List;

public interface AdminFeatureService {
    List<AdminFeatureResponse> getAll();

    AdminFeatureResponse getDetail(String id);

    Boolean create(AdminCreateFeatureRequest request);

    Boolean update(String id, AdminUpdateFeatureRequest request);

    Boolean delete(String id);

    Boolean toggleStatus(String id);
}
