package udpm.hn.server.infrastructure.ratelimit;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Rate Limit Interceptor using Bucket4j
 * 
 * Implements token bucket algorithm to limit requests per IP/User
 * 
 * Configuration:
 * - Public endpoints: 100 requests/minute per IP
 * - Authenticated endpoints: 200 requests/minute per user
 * - Burst allowed: 20% over limit
 * 
 * Response Headers:
 * - X-RateLimit-Limit: Maximum requests allowed
 * - X-RateLimit-Remaining: Requests remaining
 * - X-RateLimit-Reset: Time until bucket refills
 */
@Slf4j
@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    // Cache of buckets per client (IP or User ID)
    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    // Rate limit configurations
    private static final int PUBLIC_RATE_LIMIT = 100; // requests per minute
    private static final int AUTHENTICATED_RATE_LIMIT = 200; // requests per minute
    private static final int STRICT_RATE_LIMIT = 20; // for sensitive endpoints

    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {

        String clientKey = getClientKey(request);
        String endpoint = request.getRequestURI();

        // Determine rate limit based on endpoint
        int rateLimit = determineRateLimit(endpoint, request);

        // Get or create bucket for this client
        Bucket bucket = cache.computeIfAbsent(clientKey, k -> createBucket(rateLimit));

        // Try to consume 1 token
        var probe = bucket.tryConsumeAndReturnRemaining(1);

        if (probe.isConsumed()) {
            // Request allowed
            addRateLimitHeaders(response, rateLimit, probe.getRemainingTokens());
            return true;
        } else {
            // Rate limit exceeded
            log.warn("Rate limit exceeded: client={}, endpoint={}, limit={}",
                    clientKey, endpoint, rateLimit);

            response.setStatus(429); // Too Many Requests
            response.setContentType("application/json");
            response.setHeader("X-RateLimit-Limit", String.valueOf(rateLimit));
            response.setHeader("X-RateLimit-Remaining", "0");
            response.setHeader("X-RateLimit-Reset",
                    String.valueOf(probe.getNanosToWaitForRefill() / 1_000_000_000));

            String errorJson = String.format(
                    "{\"error\":\"Rate limit exceeded\",\"message\":\"Maximum %d requests per minute. Try again in %d seconds.\"}",
                    rateLimit, probe.getNanosToWaitForRefill() / 1_000_000_000);
            response.getWriter().write(errorJson);

            return false;
        }
    }

    /**
     * Determine rate limit based on endpoint and authentication
     */
    private int determineRateLimit(String endpoint, HttpServletRequest request) {
        // Strict limits for sensitive endpoints
        if (endpoint.contains("/reviews") ||
                endpoint.contains("/subscription") ||
                endpoint.contains("/register") ||
                endpoint.contains("/login")) {
            return STRICT_RATE_LIMIT;
        }

        // Check if user is authenticated
        if (request.getUserPrincipal() != null) {
            return AUTHENTICATED_RATE_LIMIT;
        }

        // Default public limit
        return PUBLIC_RATE_LIMIT;
    }

    /**
     * Create a new token bucket with specified rate limit
     */
    private Bucket createBucket(int requestsPerMinute) {
        // Refill strategy: refill fully every minute
        Refill refill = Refill.intervally(requestsPerMinute, Duration.ofMinutes(1));

        // Bandwidth: limit + 20% burst capacity
        Bandwidth limit = Bandwidth.classic(
                (long) (requestsPerMinute * 1.2), // Allow 20% burst
                refill);

        return Bucket.builder()
                .addLimit(limit)
                .build();
    }

    /**
     * Get client identifier (IP or User ID)
     */
    private String getClientKey(HttpServletRequest request) {
        // Try to get user ID if authenticated
        if (request.getUserPrincipal() != null) {
            return "user:" + request.getUserPrincipal().getName();
        }

        // Otherwise use IP address
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty()) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }

        // Handle multiple IPs (use first one)
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }

        return "ip:" + ip;
    }

    /**
     * Add rate limit information to response headers
     */
    private void addRateLimitHeaders(HttpServletResponse response,
            int limit,
            long remaining) {
        response.setHeader("X-RateLimit-Limit", String.valueOf(limit));
        response.setHeader("X-RateLimit-Remaining", String.valueOf(remaining));
        response.setHeader("X-RateLimit-Reset", String.valueOf(60)); // 60 seconds
    }

    /**
     * Cleanup old buckets periodically to prevent memory leak
     * Called by scheduled task
     */
    public void cleanup() {
        int sizeBefore = cache.size();
        cache.clear(); // Simple approach: clear all
        log.info("Rate limit cache cleaned: {} buckets removed", sizeBefore);
    }
}
