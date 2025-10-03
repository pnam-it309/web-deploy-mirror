package udpm.hn.server.core.admin.products.service;

import jakarta.validation.Valid;
import udpm.hn.server.core.admin.products.modal.request.ProductsCreAndUpdateRequest;
import udpm.hn.server.core.admin.products.modal.request.ProductsRequest;
import udpm.hn.server.core.common.base.ResponseObject;

public interface ManageProductService {
ResponseObject<?>getAllProducts(ProductsRequest productsRequest);

    ResponseObject<?> createProduct(@Valid ProductsCreAndUpdateRequest productsCreAndUpdateRequest);
    ResponseObject<?> updateProduct(String productId,@Valid ProductsCreAndUpdateRequest productsCreAndUpdateRequest);
    ResponseObject<?> changeStatusProduct(String productId);
}
