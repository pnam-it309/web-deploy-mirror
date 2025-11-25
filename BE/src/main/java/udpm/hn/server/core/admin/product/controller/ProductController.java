package udpm.hn.server.core.admin.product.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import udpm.hn.server.core.admin.product.dto.request.ProductCreateRequest;
import udpm.hn.server.core.admin.product.dto.request.ProductDetailRequest;
import udpm.hn.server.core.admin.product.dto.request.ProductFilterRequest;
import udpm.hn.server.core.admin.product.dto.request.ProductUpdateRequest;
import udpm.hn.server.core.admin.product.dto.response.ProductDetailResponse;
import udpm.hn.server.core.admin.product.dto.response.ProductResponse;
import udpm.hn.server.core.admin.product.service.ProductService;
import udpm.hn.server.infrastructure.core.constant.MappingConstants;

@RestController
@RequestMapping(MappingConstants.API_ADMIN_PRODUCT)
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/get-all-products")
    public ResponseEntity<Page<ProductResponse>> getAllProducts(
            @ModelAttribute ProductFilterRequest request,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(productService.getAllProducts(request, pageable));
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody ProductCreateRequest request) {
        return new ResponseEntity<>(
                productService.createProduct(request),
                HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(
            @PathVariable String id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable String id,
            @Valid @RequestBody ProductUpdateRequest request) {
        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{id}/details")
    public ResponseEntity<ProductDetailResponse> getProductDetail(@PathVariable String id) {
        return ResponseEntity.ok(productService.getProductDetail(id));
    }

    // 2. Cập nhật (hoặc Tạo mới) chi tiết
    @PutMapping("/{id}/details")
    public ResponseEntity<ProductDetailResponse> updateProductDetail(
            @PathVariable String id,
            @RequestBody ProductDetailRequest request) {
        return ResponseEntity.ok(productService.updateProductDetail(id, request));
    }
}