package udpm.hn.server.core.customer.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.customer.app.model.request.CustomerAppFilterRequest;
import udpm.hn.server.core.customer.app.service.CustomerAppService;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import udpm.hn.server.utils.Helper;

@RestController
@RequestMapping(MappingConstants.API_CUSTOMER_APP)
@RequiredArgsConstructor
public class CustomerAppController {

    private final CustomerAppService customerAppService;

    @GetMapping
    public ResponseEntity<?> filter(CustomerAppFilterRequest request) {
        return Helper.createResponseEntity(ResponseObject.successForward(
                customerAppService.filter(request),
                "Lấy danh sách sản phẩm thành công"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDetail(@PathVariable String id) {
        return Helper.createResponseEntity(ResponseObject.successForward(
                customerAppService.getDetail(id),
                "Lấy chi tiết sản phẩm thành công"));
    }

    @org.springframework.web.bind.annotation.PostMapping("/{id}/view")
    public ResponseEntity<?> incrementViewCount(@PathVariable String id) {
        customerAppService.incrementViewCount(id);
        return Helper.createResponseEntity(ResponseObject.successForward(
                true,
                "Tăng lượt xem thành công"));
    }

}
