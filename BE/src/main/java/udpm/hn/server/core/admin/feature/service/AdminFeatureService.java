package udpm.hn.server.core.admin.feature.service;

import udpm.hn.server.core.admin.feature.model.request.AdminCreateFeatureRequest;
import udpm.hn.server.core.admin.feature.model.request.AdminUpdateFeatureRequest;
import udpm.hn.server.core.admin.feature.model.response.AdminFeatureResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AdminFeatureService {
    List<AdminFeatureResponse> getAll();

    List<AdminFeatureResponse> getAllByAppId(String appId);

    AdminFeatureResponse getDetail(String id);

    String create(AdminCreateFeatureRequest request);

    Boolean update(String id, AdminUpdateFeatureRequest request);

    Boolean delete(String id);

    Boolean toggleStatus(String id);

    String uploadImage(String id, MultipartFile file);
}
