package udpm.hn.server.core.admin.order.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import udpm.hn.server.core.admin.order.dto.request.OrderCreateRequest;
import udpm.hn.server.core.admin.order.dto.response.OrderResponse;
import udpm.hn.server.core.admin.order.service.OrderADService;
import udpm.hn.server.infrastructure.core.constant.MappingConstants;

@RestController
@RequestMapping(MappingConstants.API_ADMIN_ORDER) // /admin/orders
@RequiredArgsConstructor
public class OrderADController {

    private final OrderADService orderADService;

    @GetMapping("/get-all-orders")
    public ResponseEntity<Page<OrderResponse>> getAllOrders(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(orderADService.getAllOrders(pageable));
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @Valid @RequestBody OrderCreateRequest request) {
        return new ResponseEntity<>(
                orderADService.createOrder(request),
                HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable String id) {
        return ResponseEntity.ok(orderADService.getOrderById(id));
    }
}