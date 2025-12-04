package udpm.hn.server.core.customer.order.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import udpm.hn.server.core.admin.order.dto.response.OrderResponse;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.customer.order.model.request.OrderRequest;
import udpm.hn.server.core.customer.order.service.CustomerOrderService;
import udpm.hn.server.infrastructure.core.constant.MappingConstants;
import udpm.hn.server.infrastructure.core.security.user.UserPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping(MappingConstants.API_CUSTOMER_ORDER)
@RequiredArgsConstructor
public class CustomerOrderController {

    private final CustomerOrderService customerOrderService;

    @GetMapping
    public ResponseEntity<?> getOrders(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status,
            Pageable pageable) {
        return ResponseEntity.ok(ResponseObject.successForward(customerOrderService.getOrders(userPrincipal.getEmail(), search, status, pageable), "Lấy danh sách đơn hàng thành công"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetail(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable String id) {
        return ResponseEntity.ok(ResponseObject.successForward(customerOrderService.getOrderDetail(id, userPrincipal.getEmail()), "Lấy chi tiết đơn hàng thành công"));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelOrder(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable String id, @RequestBody String reason) {
        return ResponseEntity.ok(ResponseObject.successForward(customerOrderService.cancelOrder(id, userPrincipal.getEmail(), reason), "Hủy đơn hàng thành công"));
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest orderRequest) {
        OrderResponse response = customerOrderService.createOrder(orderRequest);
        return ResponseEntity.ok(response);
    }
}
