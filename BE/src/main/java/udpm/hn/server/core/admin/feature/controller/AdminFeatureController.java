package udpm.hn.server.core.admin.feature.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import udpm.hn.server.core.admin.feature.model.request.AdminCreateFeatureRequest;
import udpm.hn.server.core.admin.feature.model.request.AdminUpdateFeatureRequest;
import udpm.hn.server.core.admin.feature.service.AdminFeatureService;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import udpm.hn.server.utils.Helper;

@RestController
@RequestMapping(MappingConstants.API_ADMIN_FEATURE)
@RequiredArgsConstructor
public class AdminFeatureController {

    private final AdminFeatureService adminFeatureService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        return Helper.createResponseEntity(ResponseObject.successForward(
                adminFeatureService.getAll(),
                "Lấy danh sách chức năng thành công"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDetail(@PathVariable String id) {
        return Helper.createResponseEntity(ResponseObject.successForward(
                adminFeatureService.getDetail(id),
                "Lấy thông tin chức năng thành công"));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody AdminCreateFeatureRequest request) {
        if (adminFeatureService.create(request)) {
            return Helper.createResponseEntity(ResponseObject.successForward(
                    true,
                    "Tạo chức năng thành công"));
        }
        return Helper.createResponseEntity(ResponseObject.errorForward(
                "Tạo chức năng thất bại (Kiểm tra App ID)",
                HttpStatus.BAD_REQUEST));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody AdminUpdateFeatureRequest request) {
        if (adminFeatureService.update(id, request)) {
            return Helper.createResponseEntity(ResponseObject.successForward(
                    true,
                    "Cập nhật chức năng thành công"));
        }
        return Helper.createResponseEntity(ResponseObject.errorForward(
                "Cập nhật chức năng thất bại",
                HttpStatus.BAD_REQUEST));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        if (adminFeatureService.delete(id)) {
            return Helper.createResponseEntity(ResponseObject.successForward(
                    true,
                    "Xóa chức năng thành công"));
        }
        return Helper.createResponseEntity(ResponseObject.errorForward(
                "Xóa chức năng thất bại",
                HttpStatus.BAD_REQUEST));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> toggleStatus(@PathVariable String id) {
        if (adminFeatureService.toggleStatus(id)) {
            return Helper.createResponseEntity(ResponseObject.successForward(
                    true,
                    "Thay đổi trạng thái chức năng thành công"));
        }
        return Helper.createResponseEntity(ResponseObject.errorForward(
                "Thay đổi trạng thái chức năng thất bại",
                HttpStatus.BAD_REQUEST));
    }
}
