package udpm.hn.server.core.admin.app.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.App;
import udpm.hn.server.repository.AppRepository;

import java.util.Optional;

@Repository
public interface AppManageRepository extends AppRepository, JpaSpecificationExecutor<App> {

    boolean existsByName(String name);

    boolean existsBySku(String sku);

    @Query("SELECT a FROM App a LEFT JOIN FETCH a.domain WHERE a.id = :id")
    Optional<App> findByIdWithDomain(String id);

    boolean existsByDomainId(String domainId);

    boolean existsByTechnologiesId(String technologyId);

    @Query("SELECT SUM(a.viewCount) FROM App a")
    Long sumTotalViews();
}