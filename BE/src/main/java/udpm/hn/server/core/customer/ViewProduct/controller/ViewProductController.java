package udpm.hn.server.core.customer.ViewProduct.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.customer.ViewProduct.model.Request.ProductFilterRequest;
import udpm.hn.server.core.customer.ViewProduct.service.ViewProductService;
import udpm.hn.server.infrastructure.core.constant.MappingConstants;

@RequestMapping({MappingConstants.API_CUSTOMER_VIEW_PRODUCT, MappingConstants.API_PUBLIC_PRODUCTS})
@RestController
@RequiredArgsConstructor
public class ViewProductController {

    private final ViewProductService viewProductService;

    @GetMapping
    public ResponseEntity<?> getAllProducts(@ModelAttribute ProductFilterRequest request) {
        ResponseObject<?> response = viewProductService.getAllProducts(request);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductDetail(@PathVariable String id) {
        ResponseObject<?> response = viewProductService.getProductDetail(id);
        return new ResponseEntity<>(response, response.getStatus());
    }
}
