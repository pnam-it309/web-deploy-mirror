package udpm.hn.server.core.admin.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import udpm.hn.server.core.admin.app.model.request.AdminCreateAppRequest;
import udpm.hn.server.core.admin.app.model.request.AdminCreateFeatureRequest;
import udpm.hn.server.core.admin.app.model.request.AdminUpdateAppRequest;
import udpm.hn.server.core.admin.app.model.request.AdminUpdateAppConfigRequest;
import udpm.hn.server.core.admin.app.model.request.AdminAddMemberRequest;

import udpm.hn.server.core.admin.app.service.AdminAppService;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import udpm.hn.server.utils.Helper;

@RestController
@RequestMapping(MappingConstants.API_ADMIN_APP)
@RequiredArgsConstructor
public class AdminAppController {

    private final AdminAppService adminAppService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        return Helper.createResponseEntity(ResponseObject.successForward(
                adminAppService.getAll(),
                "Lấy danh sách sản phẩm thành công"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDetail(@PathVariable String id) {
        return Helper.createResponseEntity(ResponseObject.successForward(
                adminAppService.getDetail(id),
                "Lấy chi tiết sản phẩm thành công"));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody AdminCreateAppRequest request) {
        String id = adminAppService.create(request);
        return Helper.createResponseEntity(ResponseObject.successForward(
                id,
                "Tạo sản phẩm thành công"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody AdminUpdateAppRequest request) {
        if (adminAppService.update(id, request)) {
            return Helper.createResponseEntity(ResponseObject.successForward(
                    true,
                    "Cập nhật sản phẩm thành công"));
        }
        return Helper.createResponseEntity(ResponseObject.errorForward(
                "Cập nhật sản phẩm thất bại",
                HttpStatus.BAD_REQUEST));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        if (adminAppService.delete(id)) {
            return Helper.createResponseEntity(ResponseObject.successForward(
                    true,
                    "Xóa sản phẩm thành công"));
        }
        return Helper.createResponseEntity(ResponseObject.errorForward(
                "Xóa sản phẩm thất bại",
                HttpStatus.BAD_REQUEST));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> toggleStatus(@PathVariable String id) {
        if (adminAppService.toggleStatus(id)) {
            return Helper.createResponseEntity(ResponseObject.successForward(
                    true,
                    "Duyệt/Thay đổi trạng thái thành công"));
        }
        return Helper.createResponseEntity(ResponseObject.errorForward(
                "Thay đổi trạng thái thất bại",
                HttpStatus.BAD_REQUEST));
    }

    @PostMapping("/{id}/images")
    public ResponseEntity<?> uploadImage(@PathVariable String id, @RequestParam("file") MultipartFile file) {
        String url = adminAppService.uploadImage(id, file);
        return Helper.createResponseEntity(ResponseObject.successForward(
                url,
                "Upload ảnh thành công"));
    }

    @PostMapping("/{id}/features")
    public ResponseEntity<?> addFeature(@PathVariable String id, @RequestBody AdminCreateFeatureRequest request) {
        if (adminAppService.addFeature(id, request)) {
            return Helper.createResponseEntity(ResponseObject.successForward(
                    true,
                    "Thêm chức năng thành công"));
        }
        return Helper.createResponseEntity(ResponseObject.errorForward(
                "Thêm chức năng thất bại",
                HttpStatus.BAD_REQUEST));
    }

    @PutMapping("/{id}/assign-developer/{brandId}")

    public ResponseEntity<?> assignDeveloper(@PathVariable String id, @PathVariable String brandId) {
        if (adminAppService.assignDeveloper(id, brandId)) {
            return Helper.createResponseEntity(ResponseObject.successForward(
                    true,
                    "Gán sinh viên/nhà phát triển thành công"));
        }
        return Helper.createResponseEntity(ResponseObject.errorForward(
                "Gán thất bại",
                HttpStatus.BAD_REQUEST));
    }

    @PutMapping("/{id}/homepage-config")
    public ResponseEntity<?> updateHomepageConfig(@PathVariable String id,
            @RequestBody AdminUpdateAppConfigRequest request) {
        if (adminAppService.updateHomepageConfig(id, request)) {
            return Helper.createResponseEntity(ResponseObject.successForward(
                    true,
                    "Cập nhật cấu hình trang chủ thành công"));
        }
        return Helper.createResponseEntity(ResponseObject.errorForward(
                "Cập nhật cấu hình trang chủ thất bại",
                HttpStatus.BAD_REQUEST));
    }

    // --- Members ---

    @GetMapping("/users/search")
    public ResponseEntity<?> searchUsers(@RequestParam(defaultValue = "") String query,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Helper.createResponseEntity(ResponseObject.successForward(
                adminAppService.searchUsers(query, page, size),
                "Tìm kiếm người dùng thành công"));
    }

    @GetMapping("/{id}/members")
    public ResponseEntity<?> getMembers(@PathVariable String id) {
        return Helper.createResponseEntity(ResponseObject.successForward(
                adminAppService.getMembers(id),
                "Lấy danh sách thành viên thành công"));
    }

    @PostMapping("/{id}/members")
    public ResponseEntity<?> addMember(@PathVariable String id, @RequestBody AdminAddMemberRequest request) {
        if (adminAppService.addMember(id, request)) {
            return Helper.createResponseEntity(ResponseObject.successForward(
                    true,
                    "Thêm thành viên thành công"));
        }
        return Helper.createResponseEntity(ResponseObject.errorForward(
                "Thêm thành viên thất bại",
                HttpStatus.BAD_REQUEST));
    }

    @DeleteMapping("/{id}/members/{memberId}")
    public ResponseEntity<?> removeMember(@PathVariable String id, @PathVariable String memberId) {
        if (adminAppService.removeMember(id, memberId)) {
            return Helper.createResponseEntity(ResponseObject.successForward(
                    true,
                    "Xóa thành viên thành công"));
        }
        return Helper.createResponseEntity(ResponseObject.errorForward(
                "Xóa thành viên thất bại",
                HttpStatus.BAD_REQUEST));
    }

    // --- Gallery ---

    @PostMapping("/{id}/gallery")
    public ResponseEntity<?> uploadGallery(@PathVariable String id,
            @RequestParam("files") java.util.List<MultipartFile> files) {
        return Helper.createResponseEntity(ResponseObject.successForward(
                adminAppService.uploadGallery(id, files),
                "Upload thư viện ảnh thành công"));
    }

    @DeleteMapping("/{id}/gallery/{imageId}")
    public ResponseEntity<?> deleteImage(@PathVariable String id, @PathVariable String imageId) {
        if (adminAppService.deleteImage(imageId)) {
            return Helper.createResponseEntity(ResponseObject.successForward(
                    true,
                    "Xóa ảnh thành công"));
        }
        return Helper.createResponseEntity(ResponseObject.errorForward(
                "Xóa ảnh thất bại",
                HttpStatus.BAD_REQUEST));
    }

}
