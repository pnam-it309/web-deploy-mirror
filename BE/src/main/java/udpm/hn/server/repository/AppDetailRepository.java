package udpm.hn.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.AppDetail;

@Repository
public interface AppDetailRepository extends JpaRepository<AppDetail, String> {
    java.util.Optional<AppDetail> findByApp_Id(String appId);
}
