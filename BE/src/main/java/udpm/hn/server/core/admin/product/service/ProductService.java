package udpm.hn.server.core.admin.product.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import udpm.hn.server.core.admin.product.dto.request.ProductCreateRequest;
import udpm.hn.server.core.admin.product.dto.request.ProductDetailRequest;
import udpm.hn.server.core.admin.product.dto.request.ProductFilterRequest;
import udpm.hn.server.core.admin.product.dto.request.ProductUpdateRequest;
import udpm.hn.server.core.admin.product.dto.response.ProductDetailResponse;
import udpm.hn.server.core.admin.product.dto.response.ProductResponse;

public interface ProductService {
    Page<ProductResponse> getAllProducts(ProductFilterRequest request, Pageable pageable);
    ProductResponse getProductById(String id);
    ProductResponse createProduct(ProductCreateRequest request);
    ProductResponse updateProduct(String id, ProductUpdateRequest request);
    void deleteProduct(String id);
    ProductDetailResponse getProductDetail(String productId);
    ProductDetailResponse updateProductDetail(String productId, ProductDetailRequest request);
}