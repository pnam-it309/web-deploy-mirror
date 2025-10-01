package udpm.hn.server.infrastructure.integration.api.analytics;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import udpm.hn.server.infrastructure.processing.analytics.GoogleAnalyticsService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
@Tag(name = "Analytics", description = "Analytics API for Google Analytics integration")
public class AnalyticsController {

//    private final GoogleAnalyticsService analyticsService;
//
//    @GetMapping("/page-views")
//    @Operation(summary = "Get page views data",
//               description = "Retrieve page views data for a specific date range")
//    public ResponseEntity<List<List<Object>>> getPageViews(
//            @Parameter(description = "Start date in yyyy-MM-dd format")
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
//            @Parameter(description = "End date in yyyy-MM-dd format")
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
//
//        List<List<Object>> result = analyticsService.getPageViews(
//            startDate.toString(),
//            endDate.toString()
//        );
//        return ResponseEntity.ok(result);
//    }
//
//    @GetMapping("/active-users")
//    @Operation(summary = "Get real-time active users",
//               description = "Get the number of active users in real-time")
//    public ResponseEntity<List<List<Object>>> getRealTimeActiveUsers() {
//        List<List<Object>> result = analyticsService.getRealTimeActiveUsers();
//        return ResponseEntity.ok(result);
//    }
//
//    @GetMapping("/top-pages")
//    @Operation(summary = "Get top pages by views",
//               description = "Get the most viewed pages for a specific date range")
//    public ResponseEntity<List<List<Object>>> getTopPages(
//            @Parameter(description = "Start date in yyyy-MM-dd format")
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
//            @Parameter(description = "End date in yyyy-MM-dd format")
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
//            @Parameter(description = "Number of top pages to return", example = "10")
//            @RequestParam(defaultValue = "10") int limit) {
//
//        List<List<Object>> result = analyticsService.getTopPages(
//            startDate.toString(),
//            endDate.toString(),
//            limit
//        );
//        return ResponseEntity.ok(result);
//    }
}
