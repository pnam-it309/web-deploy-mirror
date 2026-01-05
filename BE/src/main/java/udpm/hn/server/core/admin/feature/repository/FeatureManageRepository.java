package udpm.hn.server.core.admin.feature.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import udpm.hn.server.entity.Feature;
import udpm.hn.server.repository.FeatureRepository;

import java.util.List;

@Repository
public interface FeatureManageRepository extends udpm.hn.server.repository.FeatureRepository {
    List<Feature> findByAppIdOrderBySortOrderAsc(String appId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Feature e WHERE e.app.id = :appId")
    void deleteByAppId(String appId);
}