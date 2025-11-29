package udpm.hn.server.core.admin.order.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import udpm.hn.server.core.admin.order.dto.request.OrderCreateRequest;
import udpm.hn.server.core.admin.order.dto.response.OrderResponse;

public interface OrderADService {
    Page<OrderResponse> getAllOrders(Pageable pageable);
    OrderResponse getOrderById(String id);
    OrderResponse createOrder(OrderCreateRequest request);
}
