package udpm.hn.server.core.admin.feature.repository;

import org.springframework.stereotype.Repository;

import udpm.hn.server.repository.FeatureRepository;

import udpm.hn.server.entity.Feature;

import java.util.List;

@Repository
public interface AdminFeatureRepository extends FeatureRepository {
    List<Feature> findAllByAppIdOrderBySortOrderAsc(String appId);
}
