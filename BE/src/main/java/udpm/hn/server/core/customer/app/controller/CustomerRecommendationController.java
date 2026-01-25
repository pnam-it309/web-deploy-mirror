package udpm.hn.server.core.customer.app.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.entity.App;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import udpm.hn.server.infrastructure.recommendation.service.ProductRecommendationService;
import udpm.hn.server.utils.Helper;
import udpm.hn.server.utils.UserContextHelper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Customer Recommendation API Controller
 * 
 * Provides smart product recommendations using content-based filtering
 */
@RestController
@RequestMapping(MappingConstants.API_CUSTOMER_PREFIX + "/recommendations")
@RequiredArgsConstructor
@Slf4j
public class CustomerRecommendationController {

    private final ProductRecommendationService recommendationService;
    private final UserContextHelper userContextHelper;

    /**
     * Get similar products
     * 
     * GET /customer/recommendations/similar/{productId}?limit=5
     * 
     * Returns products similar to the specified product based on:
     * - Text content similarity (name, description)
     * - Domain matching
     * - Technology overlap
     * 
     * @param productId Source product ID
     * @param limit Number of recommendations (default: 5, max: 20)
     * @return List of similar products
     */
    @GetMapping("/similar/{productId}")
    public ResponseEntity<?> getSimilarProducts(
        @PathVariable String productId,
        @RequestParam(defaultValue = "5") int limit
    ) {
        try {
            // Limit max to prevent abuse
            limit = Math.min(limit, 20);
            
            List<App> similar = recommendationService.getSimilarProducts(productId, limit);
            
            // Convert to lightweight response
            List<SimilarProductResponse> response = similar.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
            
            return Helper.createResponseEntity(
                ResponseObject.successForward(response, "Recommendations retrieved successfully")
            );
            
        } catch (Exception e) {
            log.error("Failed to get similar products for {}: {}", productId, e.getMessage());
            return Helper.createResponseEntity(
                ResponseObject.errorForward("Failed to get recommendations", HttpStatus.INTERNAL_SERVER_ERROR)
            );
        }
    }

    /**
     * Get personalized recommendations for user
     * 
     * GET /customer/recommendations/personalized?limit=10
     * 
     * Currently returns popular products.
     * Future: Use collaborative filtering based on user history.
     */
    @GetMapping("/personalized")
    public ResponseEntity<?> getPersonalizedRecommendations(
        @RequestParam(defaultValue = "10") int limit
    ) {
        try {
            limit = Math.min(limit, 20);
            
            String userId = null;
            try {
                userId = userContextHelper.getCurrentUserId();
            } catch (Exception e) {
                // Ignore, treat as anonymous
                log.debug("No user context found for personalized recommendations");
            }
            
            // For now, return popular products
            List<App> recommendations = recommendationService
                .getPersonalizedRecommendations(userId, limit);
            
            List<SimilarProductResponse> response = recommendations.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
            
            return Helper.createResponseEntity(
                ResponseObject.successForward(response, "Personalized recommendations")
            );
            
        } catch (Exception e) {
            log.error("Failed to get personalized recommendations: {}", e.getMessage());
            return Helper.createResponseEntity(
                ResponseObject.errorForward("Failed to get recommendations", HttpStatus.INTERNAL_SERVER_ERROR)
            );
        }
    }

    /**
     * Convert App entity to lightweight response DTO
     */
    private SimilarProductResponse toResponse(App app) {
        SimilarProductResponse res = new SimilarProductResponse();
        res.setId(app.getId());
        res.setName(app.getName());
        res.setSlug(app.getSlug());
        res.setSummary(app.getSummary());
        res.setThumbnail(app.getThumbnail());
        res.setViewCount(app.getViewCount());
        res.setIsFeatured(app.getIsFeatured());
        
        if (app.getDomain() != null) {
            res.setDomainName(app.getDomain().getName());
        }
        
        if (app.getTechnologies() != null) {
            res.setTechnologyNames(
                app.getTechnologies().stream()
                    .map(tech -> tech.getName())
                    .collect(Collectors.toList())
            );
        }
        
        return res;
    }

    /**
     * Response DTO for similar products
     */
    @lombok.Data
    public static class SimilarProductResponse {
        private String id;
        private String name;
        private String slug;
        private String summary;
        private String thumbnail;
        private Long viewCount;
        private Boolean isFeatured;
        private String domainName;
        private List<String> technologyNames;
    }
}
