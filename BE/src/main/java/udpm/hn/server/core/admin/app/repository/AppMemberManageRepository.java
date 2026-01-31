package udpm.hn.server.core.admin.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import udpm.hn.server.entity.AppMember;
import java.util.List;

@Repository
public interface AppMemberManageRepository extends JpaRepository<AppMember, String> {
    List<AppMember> findByAppId(String appId);
    @Modifying
    @Transactional
    @Query("DELETE FROM AppMember e WHERE e.app.id = :appId")
    void deleteByAppId(String appId);

    @Query("SELECT COUNT(DISTINCT e.memberEmail) FROM AppMember e")
    long countUniqueMembers();
}