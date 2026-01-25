package udpm.hn.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.SkillEndorsement;

import java.util.List;

@Repository
public interface SkillEndorsementRepository extends JpaRepository<SkillEndorsement, String> {
    
    List<SkillEndorsement> findByPortfolioId(String portfolioId);
    
    @Query("SELECT se.skillName, COUNT(se) FROM SkillEndorsement se WHERE se.portfolio.id = :portfolioId GROUP BY se.skillName ORDER BY COUNT(se) DESC")
    List<Object[]> getSkillEndorsementCounts(String portfolioId);
    
    boolean existsByPortfolioIdAndSkillNameAndEndorsedBy(String portfolioId, String skillName, String endorsedBy);
}
