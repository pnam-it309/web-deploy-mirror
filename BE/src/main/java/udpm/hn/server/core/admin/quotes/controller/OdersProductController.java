package udpm.hn.server.core.admin.quotes.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import udpm.hn.server.core.admin.quotes.model.request.OrderCreateRequest;
import udpm.hn.server.core.admin.quotes.model.request.OrderStatusUpdateRequest;
import udpm.hn.server.core.admin.quotes.model.request.OrderUpdateRequest;
import udpm.hn.server.core.admin.quotes.service.OrderService;
import udpm.hn.server.infrastructure.core.constant.EntityStatus;
import udpm.hn.server.infrastructure.core.constant.MappingConstants;
import udpm.hn.server.utils.Helper;

@RestController
@RequestMapping(MappingConstants.API_ADMIN_ORDER)
@RequiredArgsConstructor
public class OdersProductController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<?> createOrder(@Valid @RequestBody OrderCreateRequest request) {
        return Helper.createResponseEntity(orderService.createOrder(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(
            @PathVariable String id,
            @Valid @RequestBody OrderUpdateRequest request) {
        return Helper.createResponseEntity(orderService.updateOrder(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable String id) {
        return Helper.createResponseEntity(orderService.getOrderById(id));
    }

    @GetMapping
    public ResponseEntity<?> getAllOrders(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) EntityStatus status,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "20") int size) {
        return Helper.createResponseEntity(orderService.getAllOrders(search, status, page, size));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateOrderStatus(
            @PathVariable String id,
            @Valid @RequestBody OrderStatusUpdateRequest request) {
        return Helper.createResponseEntity(orderService.updateOrderStatus(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable String id) {
        return Helper.createResponseEntity(orderService.deleteOrder(id));
    }
}
