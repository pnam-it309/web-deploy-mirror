package udpm.hn.server.core.admin.analytics.projection;

import java.time.LocalDate;

/**
 * Projection interface for search trend statistics
 * Replaces Object[] return type with type-safe projection
 */
public interface SearchTrendProjection {

    /**
     * Get the date for this trend data point
     * 
     * @return date
     */
    LocalDate getDate();

    /**
     * Get the search count for this date
     * 
     * @return count
     */
    Long getCount();
}
