package udpm.hn.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Badge;

@Repository
public interface BadgeRepository extends JpaRepository<Badge, String> {
    
    boolean existsByName(String name);
}
