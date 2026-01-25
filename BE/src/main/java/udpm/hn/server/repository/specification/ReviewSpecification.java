package udpm.hn.server.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import udpm.hn.server.entity.Review;
import udpm.hn.server.infrastructure.constant.ModerationStatus;

/**
 * JPA Specification for Review filtering
 * Provides type-safe, reusable specifications for review moderation
 */
public class ReviewSpecification {

    /**
     * Filter by moderation status
     * 
     * @param status - moderation status (PENDING, APPROVED, REJECTED)
     * @return Specification for status filtering
     */
    public static Specification<Review> hasStatus(ModerationStatus status) {
        return (root, query, cb) -> {
            if (status == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("moderationStatus"), status);
        };
    }

    /**
     * Filter by app ID
     * 
     * @param appId - application ID
     * @return Specification for app filtering
     */
    public static Specification<Review> forApp(String appId) {
        return (root, query, cb) -> {
            if (appId == null || appId.trim().isEmpty()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("app").get("id"), appId);
        };
    }

    /**
     * Filter by rating range
     * 
     * @param minRating - minimum rating (inclusive)
     * @param maxRating - maximum rating (inclusive)
     * @return Specification for rating filtering
     */
    public static Specification<Review> ratingBetween(Integer minRating, Integer maxRating) {
        return (root, query, cb) -> {
            if (minRating == null && maxRating == null) {
                return cb.conjunction();
            }
            if (minRating != null && maxRating != null) {
                return cb.between(root.get("rating"), minRating, maxRating);
            }
            if (minRating != null) {
                return cb.greaterThanOrEqualTo(root.get("rating"), minRating);
            }
            return cb.lessThanOrEqualTo(root.get("rating"), maxRating);
        };
    }

    /**
     * Filter by customer ID
     * 
     * @param customerId - customer ID
     * @return Specification for customer filtering
     */
    public static Specification<Review> byCustomer(String customerId) {
        return (root, query, cb) -> {
            if (customerId == null || customerId.trim().isEmpty()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("customer").get("id"), customerId);
        };
    }

    /**
     * Filter reviews created after a certain date
     * 
     * @param timestamp - start timestamp (milliseconds)
     * @return Specification for date filtering
     */
    public static Specification<Review> createdAfter(Long timestamp) {
        return (root, query, cb) -> {
            if (timestamp == null) {
                return cb.conjunction();
            }
            return cb.greaterThanOrEqualTo(root.get("createdAt"), timestamp);
        };
    }

    /**
     * Order by creation date descending
     * 
     * @return Specification with ordering
     */
    public static Specification<Review> orderByCreatedAtDesc() {
        return (root, query, cb) -> {
            if (query != null) {
                query.orderBy(cb.desc(root.get("createdAt")));
            }
            return cb.conjunction();
        };
    }

    /**
     * Get approved reviews for an app, ordered by creation date
     * 
     * @param appId - application ID
     * @return Combined specification
     */
    public static Specification<Review> approvedForApp(String appId) {
        return Specification
                .where(hasStatus(ModerationStatus.APPROVED))
                .and(forApp(appId))
                .and(orderByCreatedAtDesc());
    }
}
