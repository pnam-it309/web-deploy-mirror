package udpm.hn.server.core.admin.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import udpm.hn.server.core.admin.order.dto.request.OrderFilterRequest;
import udpm.hn.server.core.admin.order.dto.response.OrderResponse;
import udpm.hn.server.core.admin.order.service.OrderADService;
import udpm.hn.server.entity.Order;
import udpm.hn.server.infrastructure.core.constant.MappingConstants;

@RestController
@RequestMapping(MappingConstants.API_ADMIN_ORDER)
@RequiredArgsConstructor
public class OrderADController {

    private final OrderADService orderService;

    @GetMapping("/get-all-orders")
    public ResponseEntity<Page<OrderResponse>> getAllOrders(
            @ModelAttribute OrderFilterRequest request,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(orderService.getAllOrders(request, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable String id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    // API đổi trạng thái (Dùng RequestParam cho nhanh vì chỉ gửi 1 chuỗi status)
    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable String id,
            @RequestParam Order.OrderStatus status) {
        orderService.updateOrderStatus(id, status);
        return ResponseEntity.noContent().build();
    }
}