package udpm.hn.server.core.admin.domain.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import udpm.hn.server.core.admin.domain.model.request.AdminCreateDomainRequest;
import udpm.hn.server.core.admin.domain.model.request.AdminUpdateDomainRequest;
import udpm.hn.server.core.admin.domain.service.AdminDomainService;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import udpm.hn.server.utils.Helper;

@RestController
@RequestMapping(MappingConstants.API_ADMIN_DOMAIN)
@RequiredArgsConstructor
public class AdminDomainController {

    private final AdminDomainService adminDomainService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        return Helper.createResponseEntity(ResponseObject.successForward(
                adminDomainService.getAll(),
                "Lấy danh sách lĩnh vực thành công"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDetail(@PathVariable String id) {
        return Helper.createResponseEntity(ResponseObject.successForward(
                adminDomainService.getDetail(id),
                "Lấy thông tin lĩnh vực thành công"));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody AdminCreateDomainRequest request) {
        if (adminDomainService.create(request)) {
            return Helper.createResponseEntity(ResponseObject.successForward(
                    true,
                    "Tạo lĩnh vực thành công"));
        }
        return Helper.createResponseEntity(ResponseObject.errorForward(
                "Tạo lĩnh vực thất bại",
                HttpStatus.BAD_REQUEST));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody AdminUpdateDomainRequest request) {
        if (adminDomainService.update(id, request)) {
            return Helper.createResponseEntity(ResponseObject.successForward(
                    true,
                    "Cập nhật lĩnh vực thành công"));
        }
        return Helper.createResponseEntity(ResponseObject.errorForward(
                "Cập nhật lĩnh vực thất bại",
                HttpStatus.BAD_REQUEST));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        if (adminDomainService.delete(id)) {
            return Helper.createResponseEntity(ResponseObject.successForward(
                    true,
                    "Xóa lĩnh vực thành công"));
        }
        return Helper.createResponseEntity(ResponseObject.errorForward(
                "Xóa lĩnh vực thất bại",
                HttpStatus.BAD_REQUEST));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> toggleStatus(@PathVariable String id) {
        if (adminDomainService.toggleStatus(id)) {
            return Helper.createResponseEntity(ResponseObject.successForward(
                    true,
                    "Thay đổi trạng thái thành công"));
        }
        return Helper.createResponseEntity(ResponseObject.errorForward(
                "Thay đổi trạng thái thất bại",
                HttpStatus.BAD_REQUEST));
    }
}
