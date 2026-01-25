package udpm.hn.server.infrastructure.ai.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AI Content Assistant Service
 * 
 * Provides:
 * - SEO metadata generation (using Hugging Face FREE API)
 * - Grammar checking (using LanguageTool FREE API)
 * - Content summarization
 * - Tag suggestions
 * 
 * All APIs used are FREE tier:
 * - Hugging Face Inference API: 30K characters/month FREE
 * - LanguageTool: 20 requests/minute FREE
 * 
 * For unlimited usage, can integrate Ollama (local LLM)
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AIContentService {

    private final RestTemplate restTemplate;

    @Value("${huggingface.api.key:}")
    private String huggingFaceApiKey;

    @Value("${huggingface.api.url:https://api-inference.huggingface.co/models}")
    private String huggingFaceUrl;

    /**
     * Generate SEO metadata using Hugging Face BART model
     * 
     * FREE Tier: 30,000 characters/month
     * Model: facebook/bart-large-cnn (summarization)
     * 
     * @param productDescription Full product description
     * @return Generated SEO meta description (max 160 chars)
     */
    public String generateSEOMetadata(String productDescription) {
        if (productDescription == null || productDescription.length() < 50) {
            return null;
        }

        try {
            String modelEndpoint = huggingFaceUrl + "/facebook/bart-large-cnn";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            if (huggingFaceApiKey != null && !huggingFaceApiKey.isEmpty()) {
                headers.set("Authorization", "Bearer " + huggingFaceApiKey);
            }
            
            Map<String, Object> request = new HashMap<>();
            request.put("inputs", productDescription);
            request.put("parameters", Map.of(
                "max_length", 160,
                "min_length", 50,
                "do_sample", false
            ));
            
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);
            
            // Hugging Face returns array of results
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> response = restTemplate.exchange(
                modelEndpoint,
                HttpMethod.POST,
                entity,
                List.class
            ).getBody();
            
            if (response != null && !response.isEmpty()) {
                Map<String, Object> firstResult = response.get(0);
                String summary = (String) firstResult.get("summary_text");
                
                // Trim to SEO meta description length (155-160 chars)
                if (summary != null && summary.length() > 160) {
                    summary = summary.substring(0, 157) + "...";
                }
                
                log.info("Generated SEO metadata: {}", summary);
                return summary;
            }
            
        } catch (Exception e) {
            log.error("Hugging Face API error: {}", e.getMessage());
            
            // Fallback: Simple extractive summarization
            return extractFirstSentence(productDescription, 160);
        }
        
        return null;
    }

    /**
     * Check grammar using LanguageTool FREE API
     * 
     * FREE Tier: 20 requests/minute
     * 
     * @param text Text to check
     * @return List of grammar errors/suggestions
     */
    public List<GrammarError> checkGrammar(String text) {
        if (text == null || text.trim().isEmpty()) {
            return List.of();
        }

        try {
            String languageToolUrl = "https://api.languagetool.org/v2/check";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            
            String body = "text=" + java.net.URLEncoder.encode(text, "UTF-8") +
                         "&language=auto" +
                         "&enabledOnly=false";
            
            HttpEntity<String> entity = new HttpEntity<>(body, headers);
            
            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.exchange(
                languageToolUrl,
                HttpMethod.POST,
                entity,
                Map.class
            ).getBody();
            
            if (response != null && response.containsKey("matches")) {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> matches = (List<Map<String, Object>>) response.get("matches");
                
                return matches.stream()
                    .map(match -> {
                        GrammarError error = new GrammarError();
                        error.setMessage((String) match.get("message"));
                        error.setOffset((Integer) match.get("offset"));
                        error.setLength((Integer) match.get("length"));
                        
                        if (match.containsKey("replacements")) {
                            @SuppressWarnings("unchecked")
                            List<Map<String, Object>> replacements = (List<Map<String, Object>>) match.get("replacements");
                            if (!replacements.isEmpty()) {
                                error.setSuggestion((String) replacements.get(0).get("value"));
                            }
                        }
                        
                        return error;
                    })
                    .toList();
            }
            
        } catch (Exception e) {
            log.error("LanguageTool API error: {}", e.getMessage());
        }
        
        return List.of();
    }

    /**
     * Generate keyword tags from text
     * 
     * Simple approach: Extract most frequent words (excluding stopwords)
     * Future: Use TF-IDF or TextRank algorithm
     */
    public List<String> generateKeywordTags(String text, int maxTags) {
        if (text == null || text.trim().isEmpty()) {
            return List.of();
        }

        // Tokenize and count word frequency
        Map<String, Integer> wordFreq = new HashMap<>();
        
        String[] words = text.toLowerCase()
            .replaceAll("[^a-zA-Z0-9\\s]", "")
            .split("\\s+");
        
        for (String word : words) {
            if (word.length() >= 4 && !isStopWord(word)) {
                wordFreq.put(word, wordFreq.getOrDefault(word, 0) + 1);
            }
        }
        
        // Return top N most frequent words
        return wordFreq.entrySet().stream()
            .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
            .limit(maxTags)
            .map(Map.Entry::getKey)
            .toList();
    }

    /**
     * Simple stopword check
     */
    private boolean isStopWord(String word) {
        String[] stopWords = {
            "the", "and", "for", "with", "this", "that", "from", "have", "will",
            "của", "và", "cho", "với", "này", "đó", "từ", "được"
        };
        
        for (String stop : stopWords) {
            if (word.equals(stop)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Fallback: Extract first sentence up to maxLength
     */
    private String extractFirstSentence(String text, int maxLength) {
        if (text == null) return "";
        
        // Find first period, exclamation, or question mark
        int endIndex = -1;
        for (char c : new char[]{'.', '!', '?'}) {
            int idx = text.indexOf(c);
            if (idx > 0 && (endIndex == -1 || idx < endIndex)) {
                endIndex = idx + 1;
            }
        }
        
        if (endIndex == -1 || endIndex > maxLength) {
            // No sentence boundary found, just truncate
            return text.substring(0, Math.min(text.length(), maxLength)) + "...";
        }
        
        return text.substring(0, endIndex).trim();
    }

    /**
     * Grammar error DTO
     */
    @lombok.Data
    public static class GrammarError {
        private String message;
        private Integer offset;
        private Integer length;
        private String suggestion;
    }
}
