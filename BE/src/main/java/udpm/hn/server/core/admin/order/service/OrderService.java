package udpm.hn.server.core.admin.order.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import udpm.hn.server.core.admin.order.dto.request.OrderFilterRequest;
import udpm.hn.server.core.admin.order.dto.response.OrderResponse;
import udpm.hn.server.entity.Order;

public interface OrderService {
    Page<OrderResponse> getAllOrders(OrderFilterRequest request, Pageable pageable);
    OrderResponse getOrderById(String id);
    void updateOrderStatus(String id, Order.OrderStatus newStatus);
}
