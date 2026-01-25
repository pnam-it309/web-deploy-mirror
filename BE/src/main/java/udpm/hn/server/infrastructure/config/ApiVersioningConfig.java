package udpm.hn.server.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * API Versioning Configuration
 * 
 * Adds /v1 prefix to all API endpoints for future-proofing
 * When we need breaking changes, we can create /v2 endpoints
 * 
 * Examples:
 * - /api/customer/apps → /api/v1/customer/apps
 * - /api/admin/domains → /api/v1/admin/domains
 */
@Configuration
public class ApiVersioningConfig implements WebMvcConfigurer {

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        // Add /v1 prefix to all /api/** endpoints
        // remove /v1 prefix configuration to match current frontend expectations
        // configurer.addPathPrefix("/v1",
        //        c -> c.getPackageName().startsWith("udpm.hn.server.core"));
    }
}
