package udpm.hn.server.core.customer.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.App;
import udpm.hn.server.repository.AppRepository;

import java.util.List;

@Repository
public interface CustomerAppRepository extends AppRepository {

        // Using JPQL with joins for technologies is better.
        // QueryDSL would be best but manual JPQL:
        /*
         * @Query("SELECT DISTINCT a FROM App a " +
         * "LEFT JOIN a.technologies t " +
         * "WHERE a.status = 'ACTIVE' " +
         * "AND (:domainId IS NULL OR a.domain.id = :domainId) " +
         * "AND (:query IS NULL OR lower(a.name) LIKE lower(concat('%', :query, '%')) OR lower(a.shortDescription) LIKE lower(concat('%', :query, '%'))) "
         * +
         * "AND (:techIds IS NULL OR t.id IN :techIds)")
         * Page<App> filterApps(String domainId, String query, List<String> techIds,
         * Pageable pageable);
         * 
         * Actually techIds check needs to ensure ALL or ANY? Typically ANY or ALL.
         * If I pass null for list, it should be ignored.
         * Parameter validation is key.
         */
        /**
         * Advanced filter with search, domain, and technologies
         * Supports basic LIKE search (will be enhanced with full-text later)
         */
        @Query("SELECT DISTINCT a FROM App a " +
                        "LEFT JOIN a.technologies t " +
                        "WHERE a.status = :status " +
                        "AND (:domainId IS NULL OR a.domain.id = :domainId) " +
                        "AND (:query IS NULL OR lower(a.name) LIKE lower(concat('%', :query, '%')) OR lower(a.shortDescription) LIKE lower(concat('%', :query, '%'))) "
                        +
                        "AND (COALESCE(:techIds, NULL) IS NULL OR t.id IN :techIds)")
        Page<App> filterApps(String domainId, String query, List<String> techIds,
                        udpm.hn.server.infrastructure.constant.EntityStatus status, Pageable pageable);

        /**
         * Full-text search using MySQL MATCH AGAINST
         * Requires full-text index on name and short_description columns
         * Run: ALTER TABLE apps ADD FULLTEXT INDEX ft_search (name, short_description);
         */
        @Query(value = "SELECT * FROM apps a " +
                        "WHERE a.status = :status " +
                        "AND (:domainId IS NULL OR a.domain_id = :domainId) " +
                        "AND MATCH(a.name, a.short_description) AGAINST (:query IN NATURAL LANGUAGE MODE)", nativeQuery = true)
        Page<App> fullTextSearch(String domainId, String query, String status, Pageable pageable);

        /**
         * Filter by technologies only
         */
        @Query("SELECT DISTINCT a FROM App a " +
                        "JOIN a.technologies t " +
                        "WHERE a.status = :status " +
                        "AND t.id IN :techIds")
        Page<App> findByTechnologies(List<String> techIds,
                        udpm.hn.server.infrastructure.constant.EntityStatus status, Pageable pageable);

        /**
         * Get featured apps for homepage
         */
        @Query("SELECT a FROM App a " +
                        "WHERE a.status = :status " +
                        "AND a.isFeatured = true " +
                        "ORDER BY a.homepageSortOrder ASC, a.viewCount DESC")
        List<App> findFeaturedApps(udpm.hn.server.infrastructure.constant.EntityStatus status);

        /**
         * Sort by newest (created date)
         */
        @Query("SELECT a FROM App a " +
                        "WHERE a.status = :status " +
                        "AND (:domainId IS NULL OR a.domain.id = :domainId) " +
                        "ORDER BY a.createdDate DESC")
        Page<App> findByDomainOrderByNewest(String domainId,
                        udpm.hn.server.infrastructure.constant.EntityStatus status, Pageable pageable);

        /**
         * Sort by most popular (view count)
         */
        @Query("SELECT a FROM App a " +
                        "WHERE a.status = :status " +
                        "AND (:domainId IS NULL OR a.domain.id = :domainId) " +
                        "ORDER BY a.viewCount DESC")
        Page<App> findByDomainOrderByPopular(String domainId,
                        udpm.hn.server.infrastructure.constant.EntityStatus status, Pageable pageable);

        /**
         * Sort by featured first, then popular
         */
        @Query("SELECT a FROM App a " +
                        "WHERE a.status = :status " +
                        "AND (:domainId IS NULL OR a.domain.id = :domainId) " +
                        "ORDER BY a.isFeatured DESC, a.viewCount DESC")
        Page<App> findByDomainOrderByFeatured(String domainId,
                        udpm.hn.server.infrastructure.constant.EntityStatus status, Pageable pageable);

        /**
         * Increment view count
         */
        @Query("UPDATE App a SET a.viewCount = a.viewCount + 1 WHERE a.id = :appId")
        @org.springframework.data.jpa.repository.Modifying
        void incrementViewCount(String appId);
}
