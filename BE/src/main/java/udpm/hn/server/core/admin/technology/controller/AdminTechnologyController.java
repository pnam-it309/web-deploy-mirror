package udpm.hn.server.core.admin.technology.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import udpm.hn.server.core.admin.technology.model.request.AdminCreateTechnologyRequest;
import udpm.hn.server.core.admin.technology.model.request.AdminUpdateTechnologyRequest;
import udpm.hn.server.core.admin.technology.service.AdminTechnologyService;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import udpm.hn.server.utils.Helper;

@RestController
@RequestMapping(MappingConstants.API_ADMIN_TECHNOLOGY)
@RequiredArgsConstructor
public class AdminTechnologyController {

    private final AdminTechnologyService adminTechnologyService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        return Helper.createResponseEntity(ResponseObject.successForward(
                adminTechnologyService.getAll(),
                "Lấy danh sách công nghệ thành công"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDetail(@PathVariable String id) {
        return Helper.createResponseEntity(ResponseObject.successForward(
                adminTechnologyService.getDetail(id),
                "Lấy thông tin công nghệ thành công"));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody AdminCreateTechnologyRequest request) {
        if (adminTechnologyService.create(request)) {
            return Helper.createResponseEntity(ResponseObject.successForward(
                    true,
                    "Tạo công nghệ thành công"));
        }
        return Helper.createResponseEntity(ResponseObject.errorForward(
                "Tạo công nghệ thất bại",
                HttpStatus.BAD_REQUEST));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody AdminUpdateTechnologyRequest request) {
        if (adminTechnologyService.update(id, request)) {
            return Helper.createResponseEntity(ResponseObject.successForward(
                    true,
                    "Cập nhật công nghệ thành công"));
        }
        return Helper.createResponseEntity(ResponseObject.errorForward(
                "Cập nhật công nghệ thất bại",
                HttpStatus.BAD_REQUEST));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        if (adminTechnologyService.delete(id)) {
            return Helper.createResponseEntity(ResponseObject.successForward(
                    true,
                    "Xóa công nghệ thành công"));
        }
        return Helper.createResponseEntity(ResponseObject.errorForward(
                "Xóa công nghệ thất bại",
                HttpStatus.BAD_REQUEST));
    }
}
