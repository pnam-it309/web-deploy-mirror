package udpm.hn.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Feature;

@Repository
public interface FeatureRepository extends JpaRepository<Feature, String> {
}
