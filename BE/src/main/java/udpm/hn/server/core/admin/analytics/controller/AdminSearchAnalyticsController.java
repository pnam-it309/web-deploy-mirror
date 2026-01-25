package udpm.hn.server.core.admin.analytics.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import udpm.hn.server.core.admin.analytics.model.response.SearchKeywordResponse;
import udpm.hn.server.core.admin.analytics.model.response.SearchTrendResponse;
import udpm.hn.server.core.admin.analytics.service.SearchAnalyticsService;
import udpm.hn.server.entity.SearchQuery;
import udpm.hn.server.infrastructure.constant.MappingConstants;

import java.util.List;

@RestController
@RequestMapping(MappingConstants.API_ADMIN_PREFIX + "/analytics/search")
@RequiredArgsConstructor
public class AdminSearchAnalyticsController {

    private final SearchAnalyticsService searchAnalyticsService;

    @GetMapping("/top-keywords")
    public ResponseEntity<List<SearchKeywordResponse>> getTopKeywords(
            @RequestParam(defaultValue = "week") String period,
            @RequestParam(defaultValue = "20") int limit) {
        return ResponseEntity.ok(searchAnalyticsService.getTopKeywords(period, limit));
    }

    @GetMapping("/no-results")
    public ResponseEntity<List<SearchQuery>> getNoResultQueries(
            @RequestParam(defaultValue = "week") String period) {
        return ResponseEntity.ok(searchAnalyticsService.getNoResultQueries(period));
    }

    @GetMapping("/trends")
    public ResponseEntity<List<SearchTrendResponse>> getSearchTrends(
            @RequestParam(defaultValue = "week") String period) {
        return ResponseEntity.ok(searchAnalyticsService.getSearchTrends(period));
    }
}
