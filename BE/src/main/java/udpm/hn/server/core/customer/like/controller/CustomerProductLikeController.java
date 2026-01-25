package udpm.hn.server.core.customer.like.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.customer.like.service.ProductLikeService;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import udpm.hn.server.utils.Helper;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(MappingConstants.API_CUSTOMER_LIKE)
@RequiredArgsConstructor
public class CustomerProductLikeController {

    private final ProductLikeService productLikeService;

    @PostMapping("/{appId}/toggle")
    public ResponseEntity<?> toggleLike(@PathVariable String appId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return Helper.createResponseEntity(
                    ResponseObject.errorForward("Unauthorized", org.springframework.http.HttpStatus.UNAUTHORIZED));
        }

        String customerId = authentication.getName(); // Assuming principal is customer ID
        productLikeService.toggleLike(appId, customerId);

        Map<String, Object> response = new HashMap<>();
        response.put("likeCount", productLikeService.getLikeCount(appId));
        response.put("isLiked", productLikeService.isLikedByCustomer(appId, customerId));

        return Helper.createResponseEntity(ResponseObject.successForward(response, "Toggled successfully"));
    }

    @GetMapping("/{appId}/status")
    public ResponseEntity<?> getLikeStatus(@PathVariable String appId, Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        response.put("likeCount", productLikeService.getLikeCount(appId));

        if (authentication != null && authentication.isAuthenticated()) {
            String customerId = authentication.getName();
            response.put("isLiked", productLikeService.isLikedByCustomer(appId, customerId));
        } else {
            response.put("isLiked", false);
        }

        return Helper.createResponseEntity(ResponseObject.successForward(response, "Success"));
    }
}
