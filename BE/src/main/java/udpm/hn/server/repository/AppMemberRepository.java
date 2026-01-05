package udpm.hn.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.AppMember;

import java.util.Optional;

@Repository
public interface AppMemberRepository extends JpaRepository<AppMember, String> {
    Optional<AppMember> findByApp_IdAndCustomer_Id(String appId, String customerId);
}
