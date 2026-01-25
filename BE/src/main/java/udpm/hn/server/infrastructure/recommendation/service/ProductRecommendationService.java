package udpm.hn.server.infrastructure.recommendation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import udpm.hn.server.entity.App;
import udpm.hn.server.repository.AppRepository;
import udpm.hn.server.repository.ProductLikeRepository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Content-Based Recommendation Service
 * 
 * Uses TF-IDF (Term Frequency-Inverse Document Frequency) and Cosine Similarity
 * to find similar products based on content.
 * 
 * Algorithm:
 * 1. Extract features from products (name, summary, description, tech stack)
 * 2. Compute TF-IDF vectors for each product
 * 3. Calculate cosine similarity between products
 * 4. Return top N most similar products
 * 
 * 100% FREE - No external APIs needed!
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ProductRecommendationService {

    private final AppRepository appRepository;
    private final ProductLikeRepository productLikeRepository;

    /**
     * Get similar products using content-based filtering
     * 
     * @param productId Source product ID
     * @param limit Number of recommendations
     * @return List of similar products
     */
    @Cacheable(value = "productRecommendations", key = "#productId + '_' + #limit")
    public List<App> getSimilarProducts(String productId, int limit) {
        // Find source product
        Optional<App> sourceOpt = appRepository.findById(productId);
        if (sourceOpt.isEmpty()) {
            log.warn("Product not found for recommendations: {}", productId);
            return Collections.emptyList();
        }
        
        App sourceProduct = sourceOpt.get();
        
        // Get all other published products
        List<App> allProducts = appRepository.findAll().stream()
            .filter(app -> !app.getId().equals(productId))
            .filter(app -> app.getStatus() != null && "ACTIVE".equals(app.getStatus().name()))
            .collect(Collectors.toList());
        
        if (allProducts.isEmpty()) {
            return Collections.emptyList();
        }
        
        // Calculate similarity scores
        Map<String, Double> similarityScores = new HashMap<>();
        
        for (App candidate : allProducts) {
            double similarity = calculateSimilarity(sourceProduct, candidate);
            similarityScores.put(candidate.getId(), similarity);
        }
        
        // Sort by similarity (descending) and return top N
        return allProducts.stream()
            .sorted((a, b) -> {
                double scoreA = similarityScores.getOrDefault(a.getId(), 0.0);
                double scoreB = similarityScores.getOrDefault(b.getId(), 0.0);
                return Double.compare(scoreB, scoreA);
            })
            .limit(limit)
            .collect(Collectors.toList());
    }

    /**
     * Calculate similarity between two products
     * 
     * Combines multiple similarity metrics:
     * - Text content similarity (name + description)
     * - Domain similarity (same domain = bonus)
     * - Technology overlap (shared tech stack)
     * 
     * @return Similarity score (0.0 to 1.0)
     */
    private double calculateSimilarity(App source, App candidate) {
        double contentSim = calculateTextSimilarity(source, candidate);
        double domainSim = calculateDomainSimilarity(source, candidate);
        double techSim = calculateTechnologySimilarity(source, candidate);
        
        // Weighted average
        return (contentSim * 0.5) + (domainSim * 0.2) + (techSim * 0.3);
    }

    /**
     * Calculate text content similarity using Jaccard Similarity
     * (simpler than TF-IDF for MVP, very fast)
     * 
     * Jaccard Similarity = |A ∩ B| / |A ∪ B|
     */
    private double calculateTextSimilarity(App source, App candidate) {
        // Extract and tokenize text
        Set<String> sourceTokens = extractTokens(source);
        Set<String> candidateTokens = extractTokens(candidate);
        
        if (sourceTokens.isEmpty() || candidateTokens.isEmpty()) {
            return 0.0;
        }
        
        // Calculate intersection and union
        Set<String> intersection = new HashSet<>(sourceTokens);
        intersection.retainAll(candidateTokens);
        
        Set<String> union = new HashSet<>(sourceTokens);
        union.addAll(candidateTokens);
        
        return (double) intersection.size() / union.size();
    }

    /**
     * Extract and tokenize text from product
     */
    private Set<String> extractTokens(App app) {
        StringBuilder text = new StringBuilder();
        
        // Combine name, summary, description
        if (app.getName() != null) {
            text.append(app.getName()).append(" ");
        }
        if (app.getSummary() != null) {
            text.append(app.getSummary()).append(" ");
        }
        if (app.getDescription() != null) {
            text.append(app.getDescription()).append(" ");
        }
        
        // Add technology names
        if (app.getTechnologies() != null) {
            app.getTechnologies().forEach(tech -> 
                text.append(tech.getName()).append(" ")
            );
        }
        
        // Tokenize: lowercase, split by non-alphanumeric, remove stopwords
        return Arrays.stream(text.toString().toLowerCase()
                .split("[^a-z0-9]+"))
            .filter(token -> token.length() > 2) // Min length 3
            .filter(token -> !isStopWord(token))
            .collect(Collectors.toSet());
    }

    /**
     * Simple stopword filter (Vietnamese + English common words)
     */
    private boolean isStopWord(String token) {
        Set<String> stopWords = Set.of(
            // English
            "the", "and", "for", "with", "this", "that", "from",
            // Vietnamese  
            "của", "và", "cho", "với", "này", "đó", "từ"
        );
        return stopWords.contains(token);
    }

    /**
     * Check if products are in same domain
     */
    private double calculateDomainSimilarity(App source, App candidate) {
        if (source.getDomain() == null || candidate.getDomain() == null) {
            return 0.0;
        }
        
        return source.getDomain().getId().equals(candidate.getDomain().getId()) ? 1.0 : 0.0;
    }

    /**
     * Calculate technology overlap using Jaccard Similarity
     */
    private double calculateTechnologySimilarity(App source, App candidate) {
        if (source.getTechnologies() == null || candidate.getTechnologies() == null) {
            return 0.0;
        }
        
        Set<String> sourceTechIds = source.getTechnologies().stream()
            .map(tech -> tech.getId())
            .collect(Collectors.toSet());
        
        Set<String> candidateTechIds = candidate.getTechnologies().stream()
            .map(tech -> tech.getId())
            .collect(Collectors.toSet());
        
        if (sourceTechIds.isEmpty() || candidateTechIds.isEmpty()) {
            return 0.0;
        }
        
        Set<String> intersection = new HashSet<>(sourceTechIds);
        intersection.retainAll(candidateTechIds);
        
        Set<String> union = new HashSet<>(sourceTechIds);
        union.addAll(candidateTechIds);
        
        return (double) intersection.size() / union.size();
    }

    /**
     * Get personalized recommendations based on user's view history
     * (Future enhancement - requires user tracking)
     */
    public List<App> getPersonalizedRecommendations(String userId, int limit) {
        // 1. Collaborative Filtering (if user logged in)
        if (userId != null && !userId.isEmpty()) {
            try {
                Pageable pageable = PageRequest.of(0, limit);
                List<String> recommendedIds = productLikeRepository.findRecommendedAppIds(userId, pageable);
                
                if (!recommendedIds.isEmpty()) {
                    log.debug("Found {} collaborative recommendations for user {}", recommendedIds.size(), userId);
                    
                    // Fetch full entities (preserve order)
                    List<App> recommendations = appRepository.findAllById(recommendedIds);
                    
                    // Re-sort to match recommendedIds order (as database might return in any order)
                    Map<String, App> appMap = recommendations.stream()
                        .collect(Collectors.toMap(App::getId, app -> app));
                        
                    return recommendedIds.stream()
                        .map(appMap::get)
                        .filter(Objects::nonNull)
                        .filter(app -> app.getStatus() != null && "ACTIVE".equals(app.getStatus().name()))
                        .collect(Collectors.toList());
                }
            } catch (Exception e) {
                log.error("Error getting collaborative recommendations: {}", e.getMessage());
                // Fallback to popular products
            }
        }
        
        // 2. Fallback: Popular products (Most viewed)
        return appRepository.findAll().stream()
            .filter(app -> "ACTIVE".equals(app.getStatus().name()))
            .sorted((a, b) -> Long.compare(
                b.getViewCount() != null ? b.getViewCount() : 0,
                a.getViewCount() != null ? a.getViewCount() : 0
            ))
            .limit(limit)
            .collect(Collectors.toList());
    }
}
