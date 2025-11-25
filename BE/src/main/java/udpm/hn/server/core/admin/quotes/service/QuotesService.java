package udpm.hn.server.core.admin.quotes.service;

import udpm.hn.server.core.admin.quotes.model.request.QuotesCreateRequest;
import udpm.hn.server.core.admin.quotes.model.request.QuotesStatusUpdateRequest;
import udpm.hn.server.core.admin.quotes.model.request.QuotesUpdateRequest;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.infrastructure.core.constant.EntityStatus;

public interface QuotesService {
    ResponseObject<?> createOrder(QuotesCreateRequest request);
    
    ResponseObject<?> updateOrder(String id, QuotesUpdateRequest request);
    
    ResponseObject<?> getOrderById(String id);
    
    ResponseObject<?> getAllOrders(String search, EntityStatus status, int page, int size);
    
    ResponseObject<?> updateOrderStatus(String id, QuotesStatusUpdateRequest request);
    
    ResponseObject<?> deleteOrder(String id);
}
