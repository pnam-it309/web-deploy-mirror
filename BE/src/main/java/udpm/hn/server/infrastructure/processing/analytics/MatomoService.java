package udpm.hn.server.infrastructure.processing.analytics;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.piwik.java.tracking.*;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatomoService {

    private final PiwikTracker piwikTracker;
    private final int siteId;

    /**
     * Track a page view
     * @param url The URL of the page
     * @param title The title of the page
     * @param userId The user ID (optional)
     * @param customDimensions Custom dimensions (key-value pairs)
     */
    public CompletableFuture<Void> trackPageView(String url, String title, String userId, Map<String, String> customDimensions) {
        return CompletableFuture.runAsync(() -> {
            try {
                PiwikRequest request = new PiwikRequest(siteId, new URL(url).getPath());
                request.setActionName(title);
                
                if (userId != null) {
                    request.setUserId(userId);
                }
                
                // Add custom dimensions if provided
                if (customDimensions != null) {
                    customDimensions.forEach((key, value) -> {
                        try {
                            int dimensionId = Integer.parseInt(key.replace("dimension", "").trim());
                            request.addCustomTrackingParameter("dimension" + dimensionId, value);
                        } catch (NumberFormatException e) {
                            log.warn("Invalid dimension ID format: {}", key);
                        }
                    });
                }
                
                piwikTracker.sendRequest(request);
            } catch (Exception e) {
                log.error("Error tracking page view", e);
            }
        });
    }

    /**
     * Track a custom event
     * @param category Event category
     * @param action Event action
     * @param name Event name (optional)
     * @param value Event value (optional)
     * @param userId The user ID (optional)
     */
    public CompletableFuture<Void> trackEvent(String category, String action, String name, Integer value, String userId) {
        return CompletableFuture.runAsync(() -> {
            try {
                PiwikRequest request = new PiwikRequest(siteId, "/event");
                request.setActionName("Event: " + category + " - " + action);
                
                if (userId != null) {
                    request.setUserId(userId);
                }
                
                // Set event parameters
                request.addCustomTrackingParameter("e_c", category);
                request.addCustomTrackingParameter("e_a", action);
                
                if (name != null) {
                    request.addCustomTrackingParameter("e_n", name);
                }
                
                if (value != null) {
                    request.addCustomTrackingParameter("e_v", value.toString());
                }
                
                piwikTracker.sendRequest(request);
            } catch (Exception e) {
                log.error("Error tracking event", e);
            }
        });
    }

    /**
     * Track a goal conversion
     * @param goalId The ID of the goal
     * @param revenue The revenue generated (optional)
     * @param userId The user ID (optional)
     */
    public CompletableFuture<Void> trackGoal(int goalId, Double revenue, String userId) {
        return CompletableFuture.runAsync(() -> {
            try {
                PiwikRequest request = new PiwikRequest(siteId, "/goal");
                request.setActionName("Goal: " + goalId);
                
                if (userId != null) {
                    request.setUserId(userId);
                }
                
                request.addCustomTrackingParameter("idgoal", String.valueOf(goalId));
                
                if (revenue != null) {
                    request.addCustomTrackingParameter("revenue", String.format("%.2f", revenue));
                }
                
                piwikTracker.sendRequest(request);
            } catch (Exception e) {
                log.error("Error tracking goal", e);
            }
        });
    }

    /**
     * Get visitor count for a date range
     * @param startDate Start date
     * @param endDate End date
     * @return Map containing visitor statistics
     */
    public Map<String, Object> getVisitorStats(LocalDate startDate, LocalDate endDate) {
        // In a real implementation, this would call the Matomo API
        // For now, return a mock response
        Map<String, Object> stats = new HashMap<>();
        stats.put("nb_visits", 1500);
        stats.put("nb_actions", 4500);
        stats.put("nb_visits_returning", 350);
        stats.put("nb_visits_new", 1150);
        stats.put("bounce_count", 450);
        stats.put("avg_time_on_site", 245);
        
        return stats;
    }

    /**
     * Get page URL statistics for a date range
     * @param startDate Start date
     * @param endDate End date
     * @param limit Maximum number of results to return
     * @return Map containing page URL statistics
     */
    public Map<String, Object> getPageUrlStats(LocalDate startDate, LocalDate endDate, int limit) {
        // In a real implementation, this would call the Matomo API
        // For now, return a mock response
        Map<String, Object> response = new HashMap<>();
        response.put("nb_visits", 1500);
        
        Map<String, Object>[] pages = new Map[3];
        
        Map<String, Object> page1 = new HashMap<>();
        page1.put("label", "/home");
        page1.put("nb_visits", 1000);
        page1.put("nb_hits", 3000);
        page1.put("avg_time_on_page", 180);
        pages[0] = page1;
        
        Map<String, Object> page2 = new HashMap<>();
        page2.put("label", "/products");
        page2.put("nb_visits", 750);
        page2.put("nb_hits", 2000);
        page2.put("avg_time_on_page", 150);
        pages[1] = page2;
        
        Map<String, Object> page3 = new HashMap<>();
        page3.put("label", "/contact");
        page3.put("nb_visits", 500);
        page3.put("nb_hits", 1200);
        page3.put("avg_time_on_page", 200);
        pages[2] = page3;
        
        response.put("data", pages);
        return response;
    }
}
