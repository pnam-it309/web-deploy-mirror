package udpm.hn.server.core.admin.app.service;

import org.springframework.web.multipart.MultipartFile;
import udpm.hn.server.core.admin.app.model.request.AdminCreateAppRequest;
import udpm.hn.server.core.admin.app.model.request.AdminCreateFeatureRequest;
import udpm.hn.server.core.admin.app.model.request.AdminUpdateAppRequest;
import udpm.hn.server.core.admin.app.model.request.AdminUpdateAppConfigRequest;

import udpm.hn.server.core.admin.app.model.response.AdminAppResponse;

import java.util.List;

public interface AdminAppService {
    List<AdminAppResponse> getAll();

    AdminAppResponse getDetail(String id);

    String create(AdminCreateAppRequest request);

    Boolean update(String id, AdminUpdateAppRequest request);

    Boolean delete(String id);

    Boolean toggleStatus(String id);

    String uploadImage(String id, MultipartFile file);

    Boolean addFeature(String id, AdminCreateFeatureRequest request);

    Boolean assignDeveloper(String id, String brandId);

    Boolean updateHomepageConfig(String id, AdminUpdateAppConfigRequest request);

    // Members
    udpm.hn.server.core.common.base.PageableObject<udpm.hn.server.core.admin.app.model.response.AdminUserResponse> searchUsers(
            String query, int page, int size);

    Boolean addMember(String id, udpm.hn.server.core.admin.app.model.request.AdminAddMemberRequest request);

    Boolean removeMember(String id, String memberId);

    java.util.List<udpm.hn.server.core.admin.app.model.response.AdminMemberResponse> getMembers(String id);

    // Gallery
    java.util.List<String> uploadGallery(String id, List<MultipartFile> files);

    Boolean deleteImage(String imageId);
}
