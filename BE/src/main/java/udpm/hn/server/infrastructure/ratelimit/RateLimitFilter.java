package udpm.hn.server.infrastructure.ratelimit;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class RateLimitFilter implements Filter {

    // In-memory rate limit storage (use Redis in production)
    private final Map<String, RateLimitInfo> rateLimitMap = new ConcurrentHashMap<>();

    // Configuration
    private static final int MAX_REQUESTS_PER_MINUTE = 60;
    private static final long WINDOW_SIZE_MS = 60_000; // 1 minute

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String clientIp = getClientIp(httpRequest);
        String path = httpRequest.getRequestURI();

        // Skip rate limiting for static resources
        if (path.startsWith("/static") || path.startsWith("/assets")) {
            chain.doFilter(request, response);
            return;
        }

        String key = clientIp + ":" + path.split("/")[1]; // Group by IP and first path segment

        RateLimitInfo info = rateLimitMap.computeIfAbsent(key, k -> new RateLimitInfo());

        long now = System.currentTimeMillis();

        // Reset window if expired
        if (now - info.windowStart > WINDOW_SIZE_MS) {
            info.windowStart = now;
            info.requestCount.set(0);
        }

        int currentCount = info.requestCount.incrementAndGet();

        // Add rate limit headers
        httpResponse.setHeader("X-RateLimit-Limit", String.valueOf(MAX_REQUESTS_PER_MINUTE));
        httpResponse.setHeader("X-RateLimit-Remaining",
                String.valueOf(Math.max(0, MAX_REQUESTS_PER_MINUTE - currentCount)));
        httpResponse.setHeader("X-RateLimit-Reset", String.valueOf((info.windowStart + WINDOW_SIZE_MS) / 1000));

        if (currentCount > MAX_REQUESTS_PER_MINUTE) {
            log.warn("Rate limit exceeded for client: {} on path: {}", clientIp, path);
            httpResponse.setStatus(429);
            httpResponse.setContentType("application/json");
            httpResponse.getWriter().write("{\"error\":\"Too many requests. Please try again later.\",\"retryAfter\":" +
                    ((info.windowStart + WINDOW_SIZE_MS - now) / 1000) + "}");
            return;
        }

        chain.doFilter(request, response);
    }

    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private static class RateLimitInfo {
        volatile long windowStart = System.currentTimeMillis();
        AtomicInteger requestCount = new AtomicInteger(0);
    }
}
