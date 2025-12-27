package udpm.hn.server.core.customer.app.repository;

import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Feature;
import udpm.hn.server.infrastructure.constant.EntityStatus;
import udpm.hn.server.repository.FeatureRepository;

import java.util.List;

@Repository
public interface CustomerFeatureRepository extends FeatureRepository {
    List<Feature> findByApp_IdAndStatusOrderBySortOrderAsc(String appId, EntityStatus status);
}
