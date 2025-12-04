package udpm.hn.server.core.customer.dashboard.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.customer.dashboard.service.CustomerDashboardService;
import udpm.hn.server.infrastructure.core.security.user.UserPrincipal;

import udpm.hn.server.infrastructure.core.constant.MappingConstants;

@RestController
@RequestMapping(MappingConstants.API_CUSTOMER_DASHBOARD)
@RequiredArgsConstructor
public class CustomerDashboardController {

    private final CustomerDashboardService customerDashboardService;

    @GetMapping("/stats")
    public ResponseEntity<?> getStats(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok(ResponseObject.successForward(customerDashboardService.getStats(userPrincipal.getEmail()), "Lấy thống kê thành công"));
    }

    @GetMapping("/products")
    public ResponseEntity<?> getFeaturedProducts() {
        return ResponseEntity.ok(ResponseObject.successForward(customerDashboardService.getFeaturedProducts(), "Lấy sản phẩm nổi bật thành công"));
    }

    @GetMapping("/categories")
    public ResponseEntity<?> getCategories() {
        return ResponseEntity.ok(ResponseObject.successForward(customerDashboardService.getCategories(), "Lấy danh mục thành công"));
    }
}
