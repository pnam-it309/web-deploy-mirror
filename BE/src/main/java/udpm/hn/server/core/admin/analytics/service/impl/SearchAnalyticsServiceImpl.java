package udpm.hn.server.core.admin.analytics.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import udpm.hn.server.core.admin.analytics.model.response.SearchKeywordResponse;
import udpm.hn.server.core.admin.analytics.model.response.SearchTrendResponse;
import udpm.hn.server.core.admin.analytics.projection.SearchKeywordProjection;
import udpm.hn.server.core.admin.analytics.projection.SearchTrendProjection;
import udpm.hn.server.core.admin.analytics.service.SearchAnalyticsService;
import udpm.hn.server.entity.SearchQuery;
import udpm.hn.server.repository.SearchQueryRepository;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchAnalyticsServiceImpl implements SearchAnalyticsService {

    private final SearchQueryRepository searchQueryRepository;

    /**
     * Get top search keywords by frequency
     * Now uses type-safe SearchKeywordProjection
     */
    @Override
    @Transactional(readOnly = true)
    public List<SearchKeywordResponse> getTopKeywords(String period, int limit) {
        long startTime = calculateStartTime(period);

        List<SearchKeywordProjection> results = searchQueryRepository.findTopKeywords(startTime, limit);

        return results.stream()
                .map(proj -> new SearchKeywordResponse(
                        proj.getQueryText(),
                        proj.getSearchCount()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SearchQuery> getNoResultQueries(String period) {
        long startTime = calculateStartTime(period);
        return searchQueryRepository.findNoResultQueries(startTime);
    }

    /**
     * Get search count trends by date
     * Now uses type-safe SearchTrendProjection
     */
    @Override
    @Transactional(readOnly = true)
    public List<SearchTrendResponse> getSearchTrends(String period) {
        long startTime = calculateStartTime(period);

        List<SearchTrendProjection> results = searchQueryRepository.findSearchTrends(startTime);

        return results.stream()
                .map(proj -> new SearchTrendResponse(
                        proj.getDate().toString(),
                        proj.getCount()))
                .collect(Collectors.toList());
    }

    /**
     * Calculate start time based on period
     */
    private long calculateStartTime(String period) {
        Instant now = Instant.now();
        return switch (period.toLowerCase()) {
            case "day" -> now.minus(1, ChronoUnit.DAYS).toEpochMilli();
            case "week" -> now.minus(7, ChronoUnit.DAYS).toEpochMilli();
            case "month" -> now.minus(30, ChronoUnit.DAYS).toEpochMilli();
            default -> now.minus(7, ChronoUnit.DAYS).toEpochMilli(); // Default to week
        };
    }
}
