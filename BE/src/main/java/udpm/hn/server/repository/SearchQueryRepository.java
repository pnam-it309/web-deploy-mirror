package udpm.hn.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import udpm.hn.server.core.admin.analytics.projection.SearchKeywordProjection;
import udpm.hn.server.core.admin.analytics.projection.SearchTrendProjection;
import udpm.hn.server.entity.SearchQuery;

import java.util.List;

@Repository
public interface SearchQueryRepository extends JpaRepository<SearchQuery, String> {

        /**
         * Get top N search keywords by frequency within a time period
         * Now returns type-safe projection instead of Object[]
         */
        @Query(value = """
                        SELECT query_text as queryText, COUNT(*) as searchCount
                        FROM search_queries
                        WHERE created_at >= :startTime
                        GROUP BY query_text
                        ORDER BY searchCount DESC
                        LIMIT :limit
                        """, nativeQuery = true)
        List<SearchKeywordProjection> findTopKeywords(@Param("startTime") Long startTime, @Param("limit") int limit);

        /**
         * Get searches that returned no results within a time period
         */
        @Query("SELECT sq FROM SearchQuery sq WHERE sq.hasResults = false AND sq.createdAt >= :startTime ORDER BY sq.createdAt DESC")
        List<SearchQuery> findNoResultQueries(@Param("startTime") Long startTime);

        /**
         * Get search count trends by date
         * Now returns type-safe projection instead of Object[]
         */
        @Query(value = """
                        SELECT DATE(FROM_UNIXTIME(created_at/1000)) as date, COUNT(*) as count
                        FROM search_queries
                        WHERE created_at >= :startTime
                        GROUP BY date
                        ORDER BY date DESC
                        """, nativeQuery = true)
        List<SearchTrendProjection> findSearchTrends(@Param("startTime") Long startTime);
}
