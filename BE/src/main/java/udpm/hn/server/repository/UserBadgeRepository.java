package udpm.hn.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.UserBadge;

import java.util.List;

@Repository
public interface UserBadgeRepository extends JpaRepository<UserBadge, String> {
    
    List<UserBadge> findByPortfolioId(String portfolioId);
    
    boolean existsByPortfolioIdAndBadgeId(String portfolioId, String badgeId);
}
