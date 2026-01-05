package udpm.hn.server.core.admin.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import udpm.hn.server.entity.AppImage;
import java.util.List;

@Repository
public interface AppImageManageRepository extends JpaRepository<AppImage, String> {
    List<AppImage> findByAppId(String appId);

    @Modifying
    @Transactional
    @Query("DELETE FROM AppImage e WHERE e.app.id = :appId")
    void deleteByAppId(String appId);
}