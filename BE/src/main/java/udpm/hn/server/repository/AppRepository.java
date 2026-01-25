package udpm.hn.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.App;
import udpm.hn.server.infrastructure.constant.ApprovalStatus;

import java.util.List;

@Repository
@org.springframework.context.annotation.Primary
public interface AppRepository extends JpaRepository<App, String> {
    List<App> findByApprovalStatus(ApprovalStatus status);
}
