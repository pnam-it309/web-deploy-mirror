package udpm.hn.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.PushSubscription;

import java.util.List;
import java.util.Optional;

@Repository
public interface PushSubscriptionRepository extends JpaRepository<PushSubscription, String> {

    List<PushSubscription> findByIsActiveTrue();

    Optional<PushSubscription> findByEndpoint(String endpoint);

    List<PushSubscription> findByUserEmail(String userEmail);

    boolean existsByEndpoint(String endpoint);
}
