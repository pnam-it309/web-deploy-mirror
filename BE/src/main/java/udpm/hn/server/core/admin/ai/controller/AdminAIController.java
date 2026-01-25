package udpm.hn.server.core.admin.ai.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.infrastructure.ai.service.AIContentService;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import udpm.hn.server.utils.Helper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Admin AI Assistant Controller
 * 
 * Provides AI-powered content generation and validation tools for admins
 */
@RestController
@RequestMapping(MappingConstants.API_ADMIN_PREFIX + "/ai")
@RequiredArgsConstructor
@Slf4j
public class AdminAIController {

    private final AIContentService aiContentService;

    /**
     * Generate SEO metadata using AI
     * 
     * POST /admin/ai/generate-seo
     * Body: { "description": "Full product description here..." }
     * 
     * Returns: { "metaDescription": "AI-generated SEO meta description" }
     */
    @PostMapping("/generate-seo")
    public ResponseEntity<?> generateSEO(@RequestBody Map<String, String> request) {
        try {
            String description = request.get("description");
            
            if (description == null || description.length() < 50) {
                return Helper.createResponseEntity(
                    ResponseObject.errorForward("Description must be at least 50 characters", HttpStatus.BAD_REQUEST)
                );
            }
            
            String seoMeta = aiContentService.generateSEOMetadata(description);
            
            Map<String, String> response = new HashMap<>();
            response.put("metaDescription", seoMeta);
            response.put("metaTitle", extractTitle(description));
            
            return Helper.createResponseEntity(
                ResponseObject.successForward(response, "SEO metadata generated successfully")
            );
            
        } catch (Exception e) {
            log.error("Failed to generate SEO: {}", e.getMessage());
            return Helper.createResponseEntity(
                ResponseObject.errorForward("AI service temporarily unavailable", HttpStatus.INTERNAL_SERVER_ERROR)
            );
        }
    }

    /**
     * Check grammar and get suggestions
     * 
     * POST /admin/ai/check-grammar
     * Body: { "text": "Text to check for grammar errors..." }
     * 
     * Returns: List of grammar errors with suggestions
     */
    @PostMapping("/check-grammar")
    public ResponseEntity<?> checkGrammar(@RequestBody Map<String, String> request) {
        try {
            String text = request.get("text");
            
            if (text == null || text.trim().isEmpty()) {
                return Helper.createResponseEntity(
                    ResponseObject.errorForward("Text is required", HttpStatus.BAD_REQUEST)
                );
            }
            
            List<AIContentService.GrammarError> errors = aiContentService.checkGrammar(text);
            
            Map<String, Object> response = new HashMap<>();
            response.put("errors", errors);
            response.put("errorCount", errors.size());
            response.put("hasErrors", !errors.isEmpty());
            
            return Helper.createResponseEntity(
                ResponseObject.successForward(response, "Grammar check completed")
            );
            
        } catch (Exception e) {
            log.error("Failed to check grammar: {}", e.getMessage());
            return Helper.createResponseEntity(
                ResponseObject.errorForward("Grammar check service unavailable", HttpStatus.INTERNAL_SERVER_ERROR)
            );
        }
    }

    /**
     * Generate keyword tags from text
     * 
     * POST /admin/ai/generate-tags
     * Body: { "text": "Content to extract tags from...", "maxTags": 5 }
     * 
     * Returns: { "tags": ["tag1", "tag2", ...] }
     */
    @PostMapping("/generate-tags")
    public ResponseEntity<?> generateTags(@RequestBody Map<String, Object> request) {
        try {
            String text = (String) request.get("text");
            int maxTags = request.containsKey("maxTags") 
                ? ((Number) request.get("maxTags")).intValue() 
                : 5;
            
            List<String> tags = aiContentService.generateKeywordTags(text, maxTags);
            
            Map<String, Object> response = new HashMap<>();
            response.put("tags", tags);
            
            return Helper.createResponseEntity(
                ResponseObject.successForward(response, "Tags generated successfully")
            );
            
        } catch (Exception e) {
            log.error("Failed to generate tags: {}", e.getMessage());
            return Helper.createResponseEntity(
                ResponseObject.errorForward("Tag generation failed", HttpStatus.INTERNAL_SERVER_ERROR)
            );
        }
    }

    /**
     * Extract potential meta title from description
     */
    private String extractTitle(String description) {
        if (description == null || description.isEmpty()) {
            return "";
        }
        
        // Get first sentence or first 60 characters
        int endIndex = description.indexOf('.');
        if (endIndex > 0 && endIndex <= 60) {
            return description.substring(0, endIndex).trim();
        }
        
        return description.substring(0, Math.min(60, description.length())).trim() + "...";
    }
}
