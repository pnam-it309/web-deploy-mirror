package udpm.hn.server.core.admin.analytics.service;

import udpm.hn.server.core.admin.analytics.model.response.SearchKeywordResponse;
import udpm.hn.server.core.admin.analytics.model.response.SearchTrendResponse;
import udpm.hn.server.entity.SearchQuery;

import java.util.List;

public interface SearchAnalyticsService {

    /**
     * Get top search keywords within a period
     * 
     * @param period "day", "week", or "month"
     * @param limit  number of results
     */
    List<SearchKeywordResponse> getTopKeywords(String period, int limit);

    /**
     * Get searches that returned no results
     * 
     * @param period "day", "week", or "month"
     */
    List<SearchQuery> getNoResultQueries(String period);

    /**
     * Get search count trends over time
     * 
     * @param period "week" or "month"
     */
    List<SearchTrendResponse> getSearchTrends(String period);
}
