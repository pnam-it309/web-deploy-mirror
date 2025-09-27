package udpm.hn.server.infrastructure.integration.api.analytics;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import udpm.hn.server.infrastructure.processing.analytics.MatomoService;

import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/matomo")
@RequiredArgsConstructor
@Tag(name = "Matomo Analytics", description = "Matomo Analytics API for tracking and retrieving analytics data")
public class MatomoController {

    private final MatomoService matomoService;

    @PostMapping("/track/page-view")
    @Operation(summary = "Track a page view", 
               description = "Track a page view in Matomo analytics")
    public CompletableFuture<ResponseEntity<Void>> trackPageView(
            @Parameter(description = "Page URL", required = true)
            @RequestParam String url,
            
            @Parameter(description = "Page title", required = true)
            @RequestParam String title,
            
            @Parameter(description = "User ID (optional)")
            @RequestParam(required = false) String userId) {
        
        // In a real app, you might want to get custom dimensions from request body
        return matomoService.trackPageView(url, title, userId, null)
                .thenApply(unused -> ResponseEntity.ok().build());
    }

    @PostMapping("/track/event")
    @Operation(summary = "Track a custom event",
               description = "Track a custom event in Matomo analytics")
    public CompletableFuture<ResponseEntity<Void>> trackEvent(
            @Parameter(description = "Event category", required = true)
            @RequestParam String category,
            
            @Parameter(description = "Event action", required = true)
            @RequestParam String action,
            
            @Parameter(description = "Event name (optional)")
            @RequestParam(required = false) String name,
            
            @Parameter(description = "Event value (optional)")
            @RequestParam(required = false) Integer value,
            
            @Parameter(description = "User ID (optional)")
            @RequestParam(required = false) String userId) {
        
        return matomoService.trackEvent(category, action, name, value, userId)
                .thenApply(unused -> ResponseEntity.ok().build());
    }

    @PostMapping("/track/goal")
    @Operation(summary = "Track a goal conversion",
               description = "Track a goal conversion in Matomo analytics")
    public CompletableFuture<ResponseEntity<Void>> trackGoal(
            @Parameter(description = "Goal ID", required = true)
            @RequestParam int goalId,
            
            @Parameter(description = "Revenue (optional)")
            @RequestParam(required = false) Double revenue,
            
            @Parameter(description = "User ID (optional)")
            @RequestParam(required = false) String userId) {
        
        return matomoService.trackGoal(goalId, revenue, userId)
                .thenApply(unused -> ResponseEntity.ok().build());
    }

    @GetMapping("/stats/visitors")
    @Operation(summary = "Get visitor statistics",
               description = "Get visitor statistics for a date range")
    public ResponseEntity<Map<String, Object>> getVisitorStats(
            @Parameter(description = "Start date in yyyy-MM-dd format")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            
            @Parameter(description = "End date in yyyy-MM-dd format")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        Map<String, Object> stats = matomoService.getVisitorStats(startDate, endDate);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/stats/pages")
    @Operation(summary = "Get page URL statistics",
               description = "Get statistics for page URLs for a date range")
    public ResponseEntity<Map<String, Object>> getPageUrlStats(
            @Parameter(description = "Start date in yyyy-MM-dd format")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            
            @Parameter(description = "End date in yyyy-MM-dd format")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            
            @Parameter(description = "Maximum number of results to return", example = "10")
            @RequestParam(defaultValue = "10") int limit) {
        
        Map<String, Object> stats = matomoService.getPageUrlStats(startDate, endDate, limit);
        return ResponseEntity.ok(stats);
    }
}
