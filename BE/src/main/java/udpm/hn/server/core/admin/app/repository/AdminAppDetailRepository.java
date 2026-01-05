package udpm.hn.server.core.admin.app.repository;

import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.AppDetail;
import udpm.hn.server.repository.AppDetailRepository;
import java.util.Optional;

@Repository
public interface AdminAppDetailRepository extends AppDetailRepository {
    Optional<AppDetail> findByApp_Id(String appId);
}
