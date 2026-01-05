package udpm.hn.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.AppImage;

@Repository
public interface AppImageRepository extends JpaRepository<AppImage, String> {
    java.util.List<AppImage> findAllByAppId(String appId);
}
