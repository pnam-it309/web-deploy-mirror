package udpm.hn.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.StudentPortfolio;

import java.util.Optional;

@Repository
public interface StudentPortfolioRepository extends JpaRepository<StudentPortfolio, String> {
    
    Optional<StudentPortfolio> findByCustomerId(String customerId);
    
    boolean existsByCustomerId(String customerId);
}
