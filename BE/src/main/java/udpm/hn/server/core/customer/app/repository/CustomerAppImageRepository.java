package udpm.hn.server.core.customer.app.repository;

import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.AppImage;
import udpm.hn.server.repository.AppImageRepository;

import java.util.List;

@Repository
public interface CustomerAppImageRepository extends AppImageRepository {
    List<AppImage> findByApp_Id(String appId);
}
