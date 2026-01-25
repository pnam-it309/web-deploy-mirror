package udpm.hn.server.core.admin.analytics.projection;

/**
 * Projection interface for search keyword statistics
 * Replaces Object[] return type with type-safe projection
 */
public interface SearchKeywordProjection {

    /**
     * Get the search query text
     * 
     * @return query text
     */
    String getQueryText();

    /**
     * Get the number of times this keyword was searched
     * 
     * @return search count
     */
    Long getSearchCount();
}
