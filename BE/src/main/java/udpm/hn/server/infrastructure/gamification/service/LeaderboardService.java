package udpm.hn.server.infrastructure.gamification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import udpm.hn.server.entity.StudentPortfolio;
import udpm.hn.server.repository.StudentPortfolioRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Leaderboard Service using Redis Sorted Sets
 * 
 * Features:
 * - Global leaderboard (all users)
 * - Per-domain leaderboards
 * - Real-time ranking updates
 * - Fast queries (O(log N))
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class LeaderboardService {

    private final RedisTemplate<String, String> redisTemplate;
    private final StudentPortfolioRepository portfolioRepository;

    private static final String GLOBAL_LEADERBOARD_KEY = "leaderboard:global";
    private static final String DOMAIN_LEADERBOARD_PREFIX = "leaderboard:domain:";

    /**
     * Update user's score in leaderboard
     * 
     * @param portfolioId Student portfolio ID
     * @param score Reputation score
     */
    public void updateScore(String portfolioId, double score) {
        redisTemplate.opsForZSet().add(GLOBAL_LEADERBOARD_KEY, portfolioId, score);
        log.debug("Updated leaderboard score for {}: {}", portfolioId, score);
    }

    /**
     * Update domain-specific leaderboard
     */
    public void updateDomainScore(String domainId, String portfolioId, double score) {
        String key = DOMAIN_LEADERBOARD_PREFIX + domainId;
        redisTemplate.opsForZSet().add(key, portfolioId, score);
    }

    /**
     * Get top N users from global leaderboard
     * 
     * @param limit Number of top users (default: 10)
     * @return List of leaderboard entries with rank, portfolio, score
     */
    public List<LeaderboardEntry> getTopN(int limit) {
        // Get top N with scores (reverse order = highest first)
        Set<ZSetOperations.TypedTuple<String>> top = redisTemplate
            .opsForZSet()
            .reverseRangeWithScores(GLOBAL_LEADERBOARD_KEY, 0, limit - 1);

        if (top == null) {
            return List.of();
        }

        List<LeaderboardEntry> entries = new ArrayList<>();
        int rank = 1;
        
        for (ZSetOperations.TypedTuple<String> tuple : top) {
            String portfolioId = tuple.getValue();
            Double score = tuple.getScore();
            
            if (portfolioId != null && score != null) {
                StudentPortfolio portfolio = portfolioRepository.findById(portfolioId).orElse(null);
                
                if (portfolio != null) {
                    LeaderboardEntry entry = LeaderboardEntry.builder()
                        .rank(rank++)
                        .portfolioId(portfolioId)
                        .customerName(portfolio.getCustomer() != null ? portfolio.getCustomer().getFullName() : "Unknown")
                        .score(score.intValue())
                        .totalProjects(portfolio.getTotalProjects())
                        .totalViews(portfolio.getTotalViews())
                        .build();
                    
                    entries.add(entry);
                }
            }
        }
        
        return entries;
    }

    /**
     * Get user's rank in global leaderboard
     * 
     * @param portfolioId Student portfolio ID
     * @return Rank (1-indexed), or -1 if not in leaderboard
     */
    public long getUserRank(String portfolioId) {
        Long rank = redisTemplate.opsForZSet().reverseRank(GLOBAL_LEADERBOARD_KEY, portfolioId);
        return rank != null ? rank + 1 : -1; // Convert to 1-indexed
    }

    /**
     * Get users around a specific user (neighbors)
     * 
     * @param portfolioId Center user portfolio ID
     * @param range Number of users above/below (e.g., 5 = ±5 positions)
     * @return List of nearby users
     */
    public List<LeaderboardEntry> getNeighbors(String portfolioId, int range) {
        Long userRank = redisTemplate.opsForZSet().reverseRank(GLOBAL_LEADERBOARD_KEY, portfolioId);
        
        if (userRank == null) {
            return List.of();
        }

        long start = Math.max(0, userRank - range);
        long end = userRank + range;
        
        Set<ZSetOperations.TypedTuple<String>> neighbors = redisTemplate
            .opsForZSet()
            .reverseRangeWithScores(GLOBAL_LEADERBOARD_KEY, start, end);

        if (neighbors == null) {
            return List.of();
        }

        List<LeaderboardEntry> entries = new ArrayList<>();
        int rank = (int) start + 1;
        
        for (ZSetOperations.TypedTuple<String> tuple : neighbors) {
            String pId = tuple.getValue();
            Double score = tuple.getScore();
            
            if (pId != null && score != null) {
                StudentPortfolio portfolio = portfolioRepository.findById(pId).orElse(null);
                
                if (portfolio != null) {
                    LeaderboardEntry entry = LeaderboardEntry.builder()
                        .rank(rank++)
                        .portfolioId(pId)
                        .customerName(portfolio.getCustomer().getFullName())
                        .score(score.intValue())
                        .totalProjects(portfolio.getTotalProjects())
                        .isCurrentUser(pId.equals(portfolioId))
                        .build();
                    
                    entries.add(entry);
                }
            }
        }
        
        return entries;
    }

    /**
     * Rebuild entire leaderboard from database
     * (Run this if Redis data is lost)
     */
    public void rebuildLeaderboard() {
        log.info("Rebuilding leaderboard from database...");
        
        // Clear existing leaderboard
        redisTemplate.delete(GLOBAL_LEADERBOARD_KEY);
        
        // Load all portfolios and add to leaderboard
        List<StudentPortfolio> allPortfolios = portfolioRepository.findAll();
        
        for (StudentPortfolio portfolio : allPortfolios) {
            updateScore(portfolio.getId(), portfolio.getReputationScore());
        }
        
        log.info("Leaderboard rebuilt with {} entries", allPortfolios.size());
    }

    /**
     * Leaderboard Entry DTO
     */
    @lombok.Data
    @lombok.Builder
    public static class LeaderboardEntry {
        private Integer rank;
        private String portfolioId;
        private String customerName;
        private Integer score;
        private Integer totalProjects;
        private Long totalViews;
        private Boolean isCurrentUser;
    }
}
