package udpm.hn.server.core.admin.quotes.service;

import udpm.hn.server.core.admin.quotes.model.request.OrderCreateRequest;
import udpm.hn.server.core.admin.quotes.model.request.OrderStatusUpdateRequest;
import udpm.hn.server.core.admin.quotes.model.request.OrderUpdateRequest;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.infrastructure.core.constant.EntityStatus;

public interface OrderService {
    ResponseObject<?> createOrder(OrderCreateRequest request);
    
    ResponseObject<?> updateOrder(String id, OrderUpdateRequest request);
    
    ResponseObject<?> getOrderById(String id);
    
    ResponseObject<?> getAllOrders(String search, EntityStatus status, int page, int size);
    
    ResponseObject<?> updateOrderStatus(String id, OrderStatusUpdateRequest request);
    
    ResponseObject<?> deleteOrder(String id);
}
