package udpm.hn.server.core.admin.moderation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import udpm.hn.server.entity.Review;
import udpm.hn.server.infrastructure.constant.ModerationStatus;
import udpm.hn.server.repository.ReviewRepository;

@Service
@RequiredArgsConstructor
public class ModerationService {

    private final ReviewRepository reviewRepository;

    public Page<Review> getPendingReviews(Pageable pageable) {
        return reviewRepository.findByModerationStatus(ModerationStatus.PENDING, pageable);
    }

    public Page<Review> getAllReviews(ModerationStatus status, Pageable pageable) {
        if (status == null) {
            return reviewRepository.findAll(pageable);
        }
        return reviewRepository.findByModerationStatus(status, pageable);
    }

    @Transactional
    public void approveReview(String reviewId, String moderatorEmail) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        review.setModerationStatus(ModerationStatus.APPROVED);
        review.setModeratedAt(System.currentTimeMillis());
        review.setModeratedBy(moderatorEmail);
        reviewRepository.save(review);
    }

    @Transactional
    public void rejectReview(String reviewId, String moderatorEmail) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        review.setModerationStatus(ModerationStatus.REJECTED);
        review.setModeratedAt(System.currentTimeMillis());
        review.setModeratedBy(moderatorEmail);
        reviewRepository.save(review);
    }

    @Transactional
    public void batchApprove(String[] reviewIds, String moderatorEmail) {
        for (String id : reviewIds) {
            try {
                approveReview(id, moderatorEmail);
            } catch (Exception e) {
                // Log error but continue with others
                System.err.println("Failed to approve review " + id + ": " + e.getMessage());
            }
        }
    }

    @Transactional
    public void batchReject(String[] reviewIds, String moderatorEmail) {
        for (String id : reviewIds) {
            try {
                rejectReview(id, moderatorEmail);
            } catch (Exception e) {
                System.err.println("Failed to reject review " + id + ": " + e.getMessage());
            }
        }
    }
}
