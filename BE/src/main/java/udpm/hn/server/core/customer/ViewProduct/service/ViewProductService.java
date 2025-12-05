package udpm.hn.server.core.customer.ViewProduct.service;

import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.customer.ViewProduct.model.Request.ProductFilterRequest;

public interface ViewProductService {
    ResponseObject<?> getAllProducts(ProductFilterRequest request);
    ResponseObject<?> getProductDetail(String id);
    ResponseObject<?> addToWishlist(String productId);
    ResponseObject<?> removeFromWishlist(String productId);
    ResponseObject<?> getWishlist();
}
