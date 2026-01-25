package udpm.hn.server.core.customer.gamification.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import udpm.hn.server.infrastructure.gamification.service.LeaderboardService;
import udpm.hn.server.utils.Helper;

import java.util.List;

/**
 * Customer Leaderboard Controller
 * 
 * Public endpoints for viewing leaderboards
 */
@RestController
@RequestMapping(MappingConstants.API_CUSTOMER_PREFIX + "/leaderboard")
@RequiredArgsConstructor
@Slf4j
public class CustomerLeaderboardController {

    private final LeaderboardService leaderboardService;

    /**
     * Get global leaderboard
     * 
     * GET /customer/leaderboard/top?limit=10
     * 
     * Returns top N users by reputation score
     */
    @GetMapping("/top")
    public ResponseEntity<?> getTopUsers(@RequestParam(defaultValue = "10") int limit) {
        try {
            limit = Math.min(limit, 100); // Max 100
            
            List<LeaderboardService.LeaderboardEntry> leaderboard = 
                leaderboardService.getTopN(limit);
            
            return Helper.createResponseEntity(
                ResponseObject.successForward(leaderboard, "Leaderboard retrieved")
            );
        } catch (Exception e) {
            log.error("Failed to get leaderboard: {}", e.getMessage());
            return Helper.createResponseEntity(
                ResponseObject.errorForward("Failed to retrieve leaderboard", HttpStatus.INTERNAL_SERVER_ERROR)
            );
        }
    }

    /**
     * Get user's leaderboard rank
     * 
     * GET /customer/leaderboard/my-rank?portfolioId=xxx
     */
    @GetMapping("/my-rank")
    public ResponseEntity<?> getMyRank(@RequestParam String portfolioId) {
        try {
            long rank = leaderboardService.getUserRank(portfolioId);
            
            if (rank == -1) {
                return Helper.createResponseEntity(
                    ResponseObject.errorForward("User not in leaderboard", HttpStatus.NOT_FOUND)
                );
            }
            
            // Get neighbors (±5 positions)
            List<LeaderboardService.LeaderboardEntry> neighbors = 
                leaderboardService.getNeighbors(portfolioId, 5);
            
            return Helper.createResponseEntity(
                ResponseObject.successForward(
                    java.util.Map.of(
                        "rank", rank,
                        "neighbors", neighbors
                    ),
                    "Rank retrieved"
                )
            );
        } catch (Exception e) {
            log.error("Failed to get rank: {}", e.getMessage());
            return Helper.createResponseEntity(
                ResponseObject.errorForward("Failed to get rank", HttpStatus.INTERNAL_SERVER_ERROR)
            );
        }
    }
}
