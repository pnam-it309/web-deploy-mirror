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

    /**
     * Full-text search endpoint
     * GET /api/customer/apps/search?query=java&domainId=xxx&page=1&size=10
     */
    @GetMapping("/search")
    public ResponseEntity<?> fullTextSearch(
            @org.springframework.web.bind.annotation.RequestParam(required = false) String domainId,
            @org.springframework.web.bind.annotation.RequestParam String query,
            @org.springframework.web.bind.annotation.RequestParam(defaultValue = "1") int page,
            @org.springframework.web.bind.annotation.RequestParam(defaultValue = "10") int size) {
        return Helper.createResponseEntity(ResponseObject.successForward(
                customerAppService.fullTextSearch(domainId, query, page, size),
                "Tìm kiếm sản phẩm thành công"));
    }

    /**
     * Get featured apps for homepage
     * GET /api/customer/apps/featured
     */
    @GetMapping("/featured")
    public ResponseEntity<?> getFeaturedApps() {
        return Helper.createResponseEntity(ResponseObject.successForward(
                customerAppService.getFeaturedApps(),
                "Lấy danh sách sản phẩm nổi bật thành công"));
    }

    /**
     * Get apps sorted by criteria
     * GET /api/customer/apps/sorted?sortBy=NEWEST&domainId=xxx&page=1&size=10
     * sortBy: NEWEST, POPULAR, FEATURED
     */
    @GetMapping("/sorted")
    public ResponseEntity<?> getAppsSorted(
            @org.springframework.web.bind.annotation.RequestParam(required = false) String domainId,
            @org.springframework.web.bind.annotation.RequestParam String sortBy,
            @org.springframework.web.bind.annotation.RequestParam(defaultValue = "1") int page,
            @org.springframework.web.bind.annotation.RequestParam(defaultValue = "10") int size) {
        return Helper.createResponseEntity(ResponseObject.successForward(
                customerAppService.getAppsSorted(domainId, sortBy, page, size),
                "Lấy danh sách sản phẩm theo sắp xếp thành công"));
    }

}
