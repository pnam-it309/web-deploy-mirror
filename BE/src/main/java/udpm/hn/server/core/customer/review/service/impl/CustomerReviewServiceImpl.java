package udpm.hn.server.core.customer.review.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import udpm.hn.server.core.customer.review.model.CreateReviewRequest;
import udpm.hn.server.core.customer.review.model.ReviewResponse;
import udpm.hn.server.core.customer.review.model.ReviewSummaryResponse;
import udpm.hn.server.core.customer.review.service.CustomerReviewService;
import udpm.hn.server.entity.App;
import udpm.hn.server.entity.Review;
import udpm.hn.server.repository.AppRepository;
import udpm.hn.server.repository.ReviewRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerReviewServiceImpl implements CustomerReviewService {

    private final ReviewRepository reviewRepository;
    private final AppRepository appRepository;

    @Override
    public ReviewSummaryResponse getReviewsForApp(String appId) {
        List<Review> reviews = reviewRepository.findByAppIdOrderByCreatedAtDesc(appId);
        Double avgRating = reviewRepository.getAverageRatingByAppId(appId);
        Long totalReviews = reviewRepository.countByAppId(appId);

        List<ReviewResponse> reviewResponses = reviews.stream()
                .map(r -> new ReviewResponse(
                        r.getId(),
                        r.getRating(),
                        r.getComment(),
                        maskEmail(r.getUserEmail()),
                        r.getUserName(),
                        r.getCreatedAt()))
                .collect(Collectors.toList());

        return new ReviewSummaryResponse(
                avgRating != null ? Math.round(avgRating * 10.0) / 10.0 : 0.0,
                totalReviews,
                reviewResponses);
    }

    @Override
    @Transactional
    public Object createReview(CreateReviewRequest request) {
        // Validation
        if (request.getRating() < 1 || request.getRating() > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }

        // Check if user already reviewed
        if (reviewRepository.existsByAppIdAndUserEmail(request.getAppId(), request.getUserEmail())) {
            throw new IllegalStateException("User has already reviewed this product");
        }

        // Check if app exists
        App app = appRepository.findById(request.getAppId())
                .orElseThrow(() -> new IllegalArgumentException("App not found"));

        Review review = Review.builder()
                .rating(request.getRating())
                .comment(request.getComment())
                .userEmail(request.getUserEmail())
                .userName(request.getUserName())
                .app(app)
                .build();

        Review saved = reviewRepository.save(review);

        return new ReviewResponse(
                saved.getId(),
                saved.getRating(),
                saved.getComment(),
                maskEmail(saved.getUserEmail()),
                saved.getUserName(),
                saved.getCreatedAt());
    }

    @Override
    public boolean hasUserReviewed(String appId, String userEmail) {
        return reviewRepository.existsByAppIdAndUserEmail(appId, userEmail);
    }

    private String maskEmail(String email) {
        if (email == null || !email.contains("@"))
            return "***";
        String[] parts = email.split("@");
        String name = parts[0];
        if (name.length() <= 2)
            return "**@" + parts[1];
        return name.substring(0, 2) + "***@" + parts[1];
    }
}
