package udpm.hn.server.core.customer.register.service;

import udpm.hn.server.core.admin.order.dto.response.OrderResponse;
import udpm.hn.server.core.customer.register.dto.request.OrderRequest;

public interface OrderService {
    OrderResponse createOrder(OrderRequest orderRequest);
}