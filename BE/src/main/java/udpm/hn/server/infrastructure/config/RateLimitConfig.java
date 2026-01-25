package udpm.hn.server.infrastructure.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import udpm.hn.server.infrastructure.ratelimit.RateLimitInterceptor;

/**
 * Rate Limit Configuration
 * 
 * Applies rate limiting to public and customer endpoints
 * Admin endpoints are not rate-limited (protected by authentication)
 */
@Configuration
@RequiredArgsConstructor
public class RateLimitConfig implements WebMvcConfigurer {

    private final RateLimitInterceptor rateLimitInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(rateLimitInterceptor)
                // Apply to customer endpoints
                .addPathPatterns("/customer/**")

                // Apply to public endpoints
                .addPathPatterns("/public/**")

                // Apply to authentication endpoints
                .addPathPatterns("/auth/**")

                // Exclude static resources
                .excludePathPatterns("/static/**", "/assets/**", "/images/**", "/css/**", "/js/**", "/favicon.ico")

                // Exclude actuator endpoints
                .excludePathPatterns("/actuator/**")

                // Exclude admin endpoints (already protected by auth)
                .excludePathPatterns("/admin/**");
    }
}
