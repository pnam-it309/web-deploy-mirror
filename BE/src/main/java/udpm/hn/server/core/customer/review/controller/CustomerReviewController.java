package udpm.hn.server.core.customer.review.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.customer.review.model.CreateReviewRequest;
import udpm.hn.server.core.customer.review.service.CustomerReviewService;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import udpm.hn.server.utils.Helper;

@RestController
@RequestMapping(MappingConstants.API_CUSTOMER_REVIEW)
@RequiredArgsConstructor
public class CustomerReviewController {

    private final CustomerReviewService reviewService;

    @GetMapping("/{appId}")
    public ResponseEntity<?> getReviews(@PathVariable String appId) {
        return Helper.createResponseEntity(ResponseObject.successForward(
                reviewService.getReviewsForApp(appId),
                "Lấy đánh giá thành công"));
    }

    @PostMapping
    public ResponseEntity<?> createReview(@RequestBody CreateReviewRequest request) {
        try {
            return Helper.createResponseEntity(ResponseObject.successForward(
                    reviewService.createReview(request),
                    "Đánh giá thành công"));
        } catch (IllegalStateException e) {
            return Helper.createResponseEntity(ResponseObject.errorForward(e.getMessage()));
        } catch (IllegalArgumentException e) {
            return Helper.createResponseEntity(ResponseObject.errorForward(e.getMessage()));
        }
    }

    @GetMapping("/check/{appId}")
    public ResponseEntity<?> checkUserReview(
            @PathVariable String appId,
            @RequestParam String userEmail) {
        return Helper.createResponseEntity(ResponseObject.successForward(
                reviewService.hasUserReviewed(appId, userEmail),
                "Kiểm tra thành công"));
    }
}
