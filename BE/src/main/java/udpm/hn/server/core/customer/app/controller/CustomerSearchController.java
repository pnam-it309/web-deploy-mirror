package udpm.hn.server.core.customer.app.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import udpm.hn.server.infrastructure.search.service.MeilisearchService;
import com.meilisearch.sdk.model.SearchResult;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.utils.Helper;

import java.util.HashMap;
import java.util.Map;

/**
 * Customer Search API Controller
 * 
 * Provides instant, typo-tolerant product search powered by Meilisearch
 */
@RestController
@RequestMapping(MappingConstants.API_CUSTOMER_PREFIX + "/search")
@RequiredArgsConstructor
@Slf4j
public class CustomerSearchController {

    private final MeilisearchService meilisearchService;

    /**
     * Smart search endpoint
     * 
     * GET /customer/search?q=vuejs&domain=xxx&limit=20
     * 
     * Features:
     * - Typo tolerance (vuejs, vue.js, vu js all work)
     * - Instant results (<50ms)
     * - Highlighted matches
     * - Faceted filtering by domain/tech
     * 
     * @param query Search query
     * @param domainId Optional domain filter
     * @param techIds Optional technology filter (comma-separated)
     * @param limit Number of results (default: 20, max: 100)
     * @return SearchResult với hits và metadata
     */
    @GetMapping
    public ResponseEntity<?> search(
        @RequestParam(required = false, defaultValue = "") String q,
        @RequestParam(required = false) String domainId,
        @RequestParam(required = false) String techIds,
        @RequestParam(defaultValue = "20") int limit
    ) {
        try {
            // Build filters
            StringBuilder filterBuilder = new StringBuilder();
            
            if (domainId != null && !domainId.isEmpty()) {
                filterBuilder.append("domain_id = '").append(domainId).append("'");
            }
            
            if (techIds != null && !techIds.isEmpty()) {
                if (filterBuilder.length() > 0) {
                    filterBuilder.append(" AND ");
                }
                // Support multiple tech IDs: techIds=id1,id2,id3
                String[] ids = techIds.split(",");
                for (int i = 0; i < ids.length; i++) {
                    if (i > 0) filterBuilder.append(" OR ");
                    filterBuilder.append("technology_ids = '").append(ids[i].trim()).append("'");
                }
            }
            
            // Always filter by status = PUBLISHED
            if (filterBuilder.length() > 0) {
                filterBuilder.append(" AND ");
            }
            filterBuilder.append("status = 'PUBLISHED'");
            
            String filters = filterBuilder.toString();
            
            log.debug("Search query='{}', filters='{}', limit={}", q, filters, limit);
            
            SearchResult result = meilisearchService.search(q, filters, Math.min(limit, 100));
            
            // Build response
            Map<String, Object> response = new HashMap<>();
            response.put("hits", result.getHits());
            response.put("query", q);
            response.put("processingTimeMs", result.getProcessingTimeMs());
            response.put("estimatedTotalHits", result.getEstimatedTotalHits());
            
            return Helper.createResponseEntity(
                ResponseObject.successForward(response, "Search completed successfully")
            );
            
        } catch (Exception e) {
            log.error("Search error: {}", e.getMessage(), e);
            return Helper.createResponseEntity(
                ResponseObject.errorForward("Search failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR)
            );
        }
    }

    /**
     * Get search suggestions/autocomplete
     * 
     * GET /customer/search/suggest?q=vu
     * 
     * Returns top 5 product names for autocomplete
     */
    @GetMapping("/suggest")
    public ResponseEntity<?> getSuggestions(@RequestParam String q) {
        try {
            SearchResult result = meilisearchService.search(q, "status = 'PUBLISHED'", 5);
            
            return Helper.createResponseEntity(
                ResponseObject.successForward(result.getHits(), "Suggestions retrieved")
            );
        } catch (Exception e) {
            log.error("Suggestions error: {}", e.getMessage());
            return Helper.createResponseEntity(
                ResponseObject.errorForward("Failed to get suggestions", HttpStatus.INTERNAL_SERVER_ERROR)
            );
        }
    }
}
