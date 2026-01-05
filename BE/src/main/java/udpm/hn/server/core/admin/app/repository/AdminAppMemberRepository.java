package udpm.hn.server.core.admin.app.repository;

import org.springframework.stereotype.Repository;
import udpm.hn.server.repository.AppMemberRepository;
import udpm.hn.server.entity.AppMember;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminAppMemberRepository extends AppMemberRepository {
    List<AppMember> findAllByAppId(String appId);

    Optional<AppMember> findByAppIdAndCustomerId(String appId, String customerId);
}
