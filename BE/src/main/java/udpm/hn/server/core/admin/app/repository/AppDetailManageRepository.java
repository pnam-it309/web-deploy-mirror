package udpm.hn.server.core.admin.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import udpm.hn.server.entity.AppDetail;
import java.util.Optional;

@Repository
public interface AppDetailManageRepository extends JpaRepository<AppDetail, String> {
    Optional<AppDetail> findByAppId(String appId);

    @Modifying
    @Transactional
    @Query("DELETE FROM AppDetail e WHERE e.app.id = :appId")
    void deleteByAppId(String appId);
}