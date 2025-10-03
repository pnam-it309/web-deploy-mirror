package udpm.hn.server.core.admin.products.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import udpm.hn.server.core.admin.products.modal.request.ProductsCreAndUpdateRequest;
import udpm.hn.server.core.admin.products.modal.request.ProductsRequest;
import udpm.hn.server.core.admin.products.service.ManageProductService;
import udpm.hn.server.infrastructure.core.constant.MappingConstants;
import udpm.hn.server.utils.Helper;

@RestController
@RequestMapping(MappingConstants.API_ADMIN_PRODUCT)
@RequiredArgsConstructor
public class ProductsController {
    private final ManageProductService manageProductService;
    @GetMapping("/get-all-products")
    public ResponseEntity<?> getAllProducts(ProductsRequest request) {
        return Helper.createResponseEntity(manageProductService.getAllProducts(request));
    }
    @PostMapping("/add-product")
    public ResponseEntity<?> addProduct(@RequestBody ProductsCreAndUpdateRequest request) {
return Helper.createResponseEntity(manageProductService.createProduct(request));
    }
   @PutMapping("/update-product/{id}")
    public ResponseEntity<?> updateProduct(@RequestBody ProductsCreAndUpdateRequest request,@PathVariable String productId) {
        return Helper.createResponseEntity(manageProductService.updateProduct(productId,request));
    }
    @PutMapping("/change-status-product/{id}")
    public ResponseEntity<?> changeStatusProduct( @PathVariable String productId) {
        return Helper.createResponseEntity(manageProductService.changeStatusProduct(productId));
    }
}
