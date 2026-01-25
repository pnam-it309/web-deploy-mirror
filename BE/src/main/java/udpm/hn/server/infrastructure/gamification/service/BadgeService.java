package udpm.hn.server.infrastructure.gamification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import udpm.hn.server.entity.*;
import udpm.hn.server.repository.BadgeRepository;
import udpm.hn.server.repository.StudentPortfolioRepository;
import udpm.hn.server.repository.UserBadgeRepository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Badge Service
 * 
 * Handles:
 * - SVG badge generation
 * - Auto-awarding badges based on criteria
 * - Badge progress tracking
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class BadgeService {

    private final BadgeRepository badgeRepository;
    private final UserBadgeRepository userBadgeRepository;
    private final StudentPortfolioRepository portfolioRepository;
    private final RedisTemplate<String, String> redisTemplate;

    /**
     * Generate SVG badge icon programmatically
     * 
     * @param badgeName Badge display name
     * @param color Badge color (hex code)
     * @param category Badge category
     * @return SVG markup string
     */
    public String generateBadgeSVG(String badgeName, String color, String category) {
        // Map category to icon
        String icon = getIconForCategory(category);
        
        return String.format("""
            <svg xmlns="http://www.w3.org/2000/svg" width="120" height="120" viewBox="0 0 120 120">
              <!-- Background circle -->
              <circle cx="60" cy="60" r="55" fill="%s" opacity="0.1"/>
              <circle cx="60" cy="60" r="50" fill="none" stroke="%s" stroke-width="3"/>
              
              <!-- Icon/Symbol -->
              <text x="60" y="70" text-anchor="middle" font-size="40" fill="%s">%s</text>
              
              <!-- Badge name -->
              <text x="60" y="110" text-anchor="middle" font-size="10" fill="#333" font-weight="600">%s</text>
            </svg>
            """, color, color, color, icon, badgeName.toUpperCase());
    }

    /**
     * Get emoji icon for badge category
     */
    private String getIconForCategory(String category) {
        return switch (category != null ? category.toLowerCase() : "") {
            case "project_count" -> "🚀";
            case "views_count" -> "👁️";
            case "collaboration" -> "🤝";
            case "quality" -> "⭐";
            case "innovation" -> "💡";
            case "achievement" -> "🏆";
            default -> "🎖️";
        };
    }

    /**
     * Check and award badges for a user
     * Called after significant events (product publish, views milestone, etc.)
     * 
     * @param portfolioId Student portfolio ID
     */
    public void checkAndAwardBadges(String portfolioId) {
        StudentPortfolio portfolio = portfolioRepository.findById(portfolioId).orElse (null);
        if (portfolio == null) {
            return;
        }

        List<Badge> allBadges = badgeRepository.findAll();
        List<UserBadge> currentBadges = userBadgeRepository.findByPortfolioId(portfolioId);
        Set<String> earnedBadgeIds = currentBadges.stream()
            .map(ub -> ub.getBadge().getId())
            .collect(Collectors.toSet());

        for (Badge badge : allBadges) {
            // Skip if already earned
            if (earnedBadgeIds.contains(badge.getId())) {
                continue;
            }

            // Check if user meets criteria
            if (meetsCriteria(portfolio, badge)) {
                awardBadge(portfolio, badge);
                log.info("Awarded badge '{}' to portfolio {}", badge.getName(), portfolioId);
            }
        }
    }

    /**
     * Check if user meets badge criteria
     */
    private boolean meetsCriteria(StudentPortfolio portfolio, Badge badge) {
        if (badge.getCriteriaType() == null || badge.getCriteriaThreshold() == null) {
            return false;
        }

        return switch (badge.getCriteriaType()) {
            case "projects_published" -> 
                portfolio.getTotalProjects() >= badge.getCriteriaThreshold();
                
            case "total_views" -> 
                portfolio.getTotalViews() >= badge.getCriteriaThreshold();
                
            case "reputation_score" -> 
                portfolio.getReputationScore() >= badge.getCriteriaThreshold();
                
            default -> false;
        };
    }

    /**
     * Award badge to user
     */
    private void awardBadge(StudentPortfolio portfolio, Badge badge) {
        UserBadge userBadge = UserBadge.builder()
            .id(UUID.randomUUID().toString())
            .portfolio(portfolio)
            .badge(badge)
            .earnedAt(new Date())
            .build();
        
        userBadgeRepository.save(userBadge);
        
        // Increase reputation score
        portfolio.setReputationScore(portfolio.getReputationScore() + 10);
        portfolioRepository.save(portfolio);
        
        // Update leaderboard
        updateLeaderboard(portfolio);
    }

    /**
     * Update Redis leaderboard
     */
    private void updateLeaderboard(StudentPortfolio portfolio) {
        String leaderboardKey = "leaderboard:global";
        double score = portfolio.getReputationScore();
        
        redisTemplate.opsForZSet().add(leaderboardKey, portfolio.getId(), score);
    }

    /**
     * Scheduled job: Check badges for all users every hour
     */
    @Scheduled(cron = "0 0 * * * *") // Every hour
    public void scheduledBadgeCheck() {
        log.info("Running scheduled badge check...");
        
        List<StudentPortfolio> allPortfolios = portfolioRepository.findAll();
        int badgesAwarded = 0;
        
        for (StudentPortfolio portfolio : allPortfolios) {
            int before = userBadgeRepository.findByPortfolioId(portfolio.getId()).size();
            checkAndAwardBadges(portfolio.getId());
            int after = userBadgeRepository.findByPortfolioId(portfolio.getId()).size();
            badgesAwarded += (after - before);
        }
        
        log.info("Scheduled badge check complete. Awarded {} new badges", badgesAwarded);
    }

    /**
     * Initialize default badges
     * Should be called on application startup or via admin endpoint
     */
    public void initializeDefaultBadges() {
        List<Badge> defaultBadges = List.of(
            Badge.builder()
                .id(UUID.randomUUID().toString())
                .name("First Steps")
                .description("Published your first project")
                .category("project_count")
                .criteriaType("projects_published")
                .criteriaThreshold(1)
                .color("#10B981")
                .build(),
                
            Badge.builder()
                .id(UUID.randomUUID().toString())
                .name("Prolific Creator")
                .description("Published 10 projects")
                .category("project_count")
                .criteriaType("projects_published")
                .criteriaThreshold(10)
                .color("#F59E0B")
                .build(),
                
            Badge.builder()
                .id(UUID.randomUUID().toString())
                .name("Popular")
                .description("Reached 1,000 total views")
                .category("views_count")
                .criteriaType("total_views")
                .criteriaThreshold(1000)
                .color("#3B82F6")
                .build(),
                
            Badge.builder()
                .id(UUID.randomUUID().toString())
                .name("Rising Star")
                .description("Reputation score of 100+")
                .category("achievement")
                .criteriaType("reputation_score")
                .criteriaThreshold(100)
                .color("#8B5CF6")
                .build()
        );

        for (Badge badge : defaultBadges) {
            // Check if badge already exists
            if (!badgeRepository.existsByName(badge.getName())) {
                // Generate SVG
                String svg = generateBadgeSVG(badge.getName(), badge.getColor(), badge.getCategory());
                badge.setIconSvg(svg);
                
                badgeRepository.save(badge);
                log.info("Created default badge: {}", badge.getName());
            }
        }
    }
}
