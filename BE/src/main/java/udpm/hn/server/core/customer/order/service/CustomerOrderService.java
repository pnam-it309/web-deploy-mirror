package udpm.hn.server.core.customer.order.service;

import org.springframework.data.domain.Pageable;
import udpm.hn.server.core.admin.order.dto.response.OrderResponse;
import udpm.hn.server.core.common.base.PageableObject;
import udpm.hn.server.core.customer.order.model.response.CustomerOrderDetailResponse;
import udpm.hn.server.core.customer.order.model.response.CustomerOrderResponse;
import udpm.hn.server.core.customer.order.model.request.OrderRequest;

public interface CustomerOrderService {
    PageableObject<CustomerOrderResponse> getOrders(String email, String search, String status, Pageable pageable);
    CustomerOrderDetailResponse getOrderDetail(String id, String email);
    OrderResponse createOrder(OrderRequest orderRequest);
    Boolean cancelOrder(String id, String email, String reason);
}
