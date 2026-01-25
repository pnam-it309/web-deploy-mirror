package udpm.hn.server.core.admin.moderation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import udpm.hn.server.core.admin.moderation.service.ModerationService;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.entity.Review;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import udpm.hn.server.infrastructure.constant.ModerationStatus;
import udpm.hn.server.utils.Helper;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(MappingConstants.API_ADMIN_MODERATION)
@RequiredArgsConstructor
public class AdminModerationController {

    private final ModerationService moderationService;

    @GetMapping("/reviews")
    public ResponseEntity<?> getReviews(
            @RequestParam(required = false) ModerationStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<Review> reviews = moderationService.getAllReviews(status, PageRequest.of(page, size));

        Map<String, Object> response = new HashMap<>();
        response.put("content", reviews.getContent());
        response.put("totalPages", reviews.getTotalPages());
        response.put("totalElements", reviews.getTotalElements());
        response.put("currentPage", reviews.getNumber());

        return Helper.createResponseEntity(ResponseObject.successForward(response, "Success"));
    }

    @PostMapping("/reviews/{id}/approve")
    public ResponseEntity<?> approveReview(@PathVariable String id, Authentication authentication) {
        String moderatorEmail = authentication.getName();
        moderationService.approveReview(id, moderatorEmail);
        return Helper.createResponseEntity(ResponseObject.successForward(null, "Review approved"));
    }

    @PostMapping("/reviews/{id}/reject")
    public ResponseEntity<?> rejectReview(@PathVariable String id, Authentication authentication) {
        String moderatorEmail = authentication.getName();
        moderationService.rejectReview(id, moderatorEmail);
        return Helper.createResponseEntity(ResponseObject.successForward(null, "Review rejected"));
    }

    @PostMapping("/reviews/batch-approve")
    public ResponseEntity<?> batchApprove(@RequestBody String[] reviewIds, Authentication authentication) {
        String moderatorEmail = authentication.getName();
        moderationService.batchApprove(reviewIds, moderatorEmail);
        return Helper.createResponseEntity(ResponseObject.successForward(null, "Reviews approved"));
    }

    @PostMapping("/reviews/batch-reject")
    public ResponseEntity<?> batchReject(@RequestBody String[] reviewIds, Authentication authentication) {
        String moderatorEmail = authentication.getName();
        moderationService.batchReject(reviewIds, moderatorEmail);
        return Helper.createResponseEntity(ResponseObject.successForward(null, "Reviews rejected"));
    }
}
