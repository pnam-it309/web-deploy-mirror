package udpm.hn.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Review;

import java.util.List;
import java.util.Optional;

/**
 * Review Repository
 * Now supports JPA Specifications for advanced filtering
 * 
 * Usage with Specification:
 * 
 * <pre>
 * Specification<Review> spec = ReviewSpecification.approvedForApp(appId);
 * List<Review> reviews = repository.findAll(spec);
 * </pre>
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, String>, JpaSpecificationExecutor<Review> {

    List<Review> findByAppIdOrderByCreatedAtDesc(String appId);

    Optional<Review> findByAppIdAndUserEmail(String appId, String userEmail);

    /**
     * Aggregate function - cannot be replaced by Specification
     */
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.app.id = :appId")
    Double getAverageRatingByAppId(@Param("appId") String appId);

    /**
     * Aggregate function - cannot be replaced by Specification
     */
    @Query("SELECT COUNT(r) FROM Review r WHERE r.app.id = :appId")
    Long countByAppId(@Param("appId") String appId);

    boolean existsByAppIdAndUserEmail(String appId, String userEmail);

    /**
     * Moderation query - can also use ReviewSpecification.hasStatus()
     */
    org.springframework.data.domain.Page<Review> findByModerationStatus(
            udpm.hn.server.infrastructure.constant.ModerationStatus status,
            org.springframework.data.domain.Pageable pageable);

    // Removed: findApprovedByAppId - use ReviewSpecification.approvedForApp()
    // instead
}
