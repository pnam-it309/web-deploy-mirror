package udpm.hn.server.core.customer.ViewProduct.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.customer.ViewProduct.service.ViewProductService;
import udpm.hn.server.infrastructure.core.constant.MappingConstants;

@RequestMapping(MappingConstants.API_CUSTOMER_WHISLIST)
@RestController
@RequiredArgsConstructor
public class WishlistController {

    private final ViewProductService viewProductService;

    @GetMapping
    public ResponseEntity<?> getWishlist() {
        ResponseObject<?> response = viewProductService.getWishlist();
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PostMapping("/{productId}")
    public ResponseEntity<?> addToWishlist(@PathVariable String productId) {
        ResponseObject<?> response = viewProductService.addToWishlist(productId);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> removeFromWishlist(@PathVariable String productId) {
        ResponseObject<?> response = viewProductService.removeFromWishlist(productId);
        return new ResponseEntity<>(response, response.getStatus());
    }
}
