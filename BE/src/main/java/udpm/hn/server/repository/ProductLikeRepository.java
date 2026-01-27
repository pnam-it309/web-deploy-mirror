package udpm.hn.server.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.ProductLike;

import java.util.Optional;

@Repository
public interface ProductLikeRepository extends JpaRepository<ProductLike, String> {

    @Query("SELECT COUNT(pl) FROM ProductLike pl WHERE pl.app.id = :appId")
    Long countByAppId(@Param("appId") String appId);

    @Query("SELECT pl FROM ProductLike pl WHERE pl.app.id = :appId AND pl.customer.id = :customerId")
    Optional<ProductLike> findByAppIdAndCustomerId(@Param("appId") String appId,
            @Param("customerId") String customerId);

    @Query("SELECT CASE WHEN COUNT(pl) > 0 THEN true ELSE false END FROM ProductLike pl WHERE pl.app.id = :appId AND pl.customer.id = :customerId")
    boolean existsByAppIdAndCustomerId(@Param("appId") String appId, @Param("customerId") String customerId);

    void deleteByAppIdAndCustomerId(String appId, String customerId);

    /**
     * Find apps liked by users who have similar tastes (collaborative filtering)
     * Logic:
     * 1. Find other users who liked at least one app that current user also liked
     * 2. Find other apps that those users liked
     * 3. Exclude apps current user already liked
     * 4. Order by popularity (count of likes from similar users)
     */
    @Query("SELECT pl2.app.id " +
           "FROM ProductLike pl1 " +
           "JOIN ProductLike pl2 ON pl1.customer.id = pl2.customer.id " +
           "WHERE pl1.app.id IN (SELECT my.app.id FROM ProductLike my WHERE my.customer.id = :customerId) " +
           "AND pl1.customer.id != :customerId " +
           "AND pl2.app.id NOT IN (SELECT my2.app.id FROM ProductLike my2 WHERE my2.customer.id = :customerId) " +
           "GROUP BY pl2.app.id " +
           "ORDER BY COUNT(pl2.customer.id) DESC")
    java.util.List<String> findRecommendedAppIds(@Param("customerId") String customerId, Pageable pageable);

    @Query("SELECT pl FROM ProductLike pl JOIN FETCH pl.app a LEFT JOIN FETCH a.domain WHERE pl.customer.id = :customerId")
    java.util.List<ProductLike> findAllByCustomerId(@Param("customerId") String customerId);
}
