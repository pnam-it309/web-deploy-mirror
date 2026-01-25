package udpm.hn.server.core.customer.review.service;

import udpm.hn.server.core.customer.review.model.CreateReviewRequest;
import udpm.hn.server.core.customer.review.model.ReviewSummaryResponse;

public interface CustomerReviewService {
    ReviewSummaryResponse getReviewsForApp(String appId);

    Object createReview(CreateReviewRequest request);

    boolean hasUserReviewed(String appId, String userEmail);
}
