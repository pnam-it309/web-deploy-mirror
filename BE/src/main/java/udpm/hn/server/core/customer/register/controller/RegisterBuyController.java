package udpm.hn.server.core.customer.register.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import udpm.hn.server.core.admin.order.dto.response.OrderResponse;
import udpm.hn.server.core.customer.register.dto.request.OrderRequest;
import udpm.hn.server.core.customer.register.service.OrderService;
import udpm.hn.server.infrastructure.core.constant.MappingConstants;

@RequestMapping(MappingConstants.API_CUSTOMER_REGISTER)
@RestController
@RequiredArgsConstructor
public class RegisterBuyController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest orderRequest) {
        OrderResponse response = orderService.createOrder(orderRequest);
        return ResponseEntity.ok(response);
    }
}
