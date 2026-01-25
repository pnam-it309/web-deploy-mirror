package udpm.hn.server.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Pagination Configuration
 * 
 * Protects against DOS attacks via large page sizes
 * - Max page size: 100 items
 * - Default page size: 20 items
 * - User cannot request size > 100
 */
@Configuration
public class PaginationConfig implements WebMvcConfigurer {

    private static final int MAX_PAGE_SIZE = 100;
    private static final int DEFAULT_PAGE_SIZE = 20;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();

        // Set maximum page size (prevent DOS)
        resolver.setMaxPageSize(MAX_PAGE_SIZE);

        // Set default page size
        resolver.setFallbackPageable(PageRequest.of(0, DEFAULT_PAGE_SIZE));

        // Allow one-indexed parameters (?page=1 instead of ?page=0)
        resolver.setOneIndexedParameters(true);

        argumentResolvers.add(resolver);
    }

    /**
     * Helper method to create safe pageable
     * Use this in services if needed
     */
    public static Pageable createSafePageable(int page, int size, Sort sort) {
        // Enforce maximum page size
        int safeSize = Math.min(size, MAX_PAGE_SIZE);
        int safePage = Math.max(0, page);

        return PageRequest.of(safePage, safeSize, sort);
    }
}
