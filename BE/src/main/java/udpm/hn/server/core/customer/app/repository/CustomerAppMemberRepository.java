package udpm.hn.server.core.customer.app.repository;

import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.AppMember;
import udpm.hn.server.repository.AppMemberRepository;

import java.util.List;

@Repository
public interface CustomerAppMemberRepository extends AppMemberRepository {
    List<AppMember> findByApp_Id(String appId);
}
