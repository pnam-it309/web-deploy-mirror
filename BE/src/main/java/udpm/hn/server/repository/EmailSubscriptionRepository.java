package udpm.hn.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.EmailSubscription;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmailSubscriptionRepository extends JpaRepository<EmailSubscription, String> {

    Optional<EmailSubscription> findByEmail(String email);

    Optional<EmailSubscription> findByVerificationToken(String token);

    Optional<EmailSubscription> findByUnsubscribeToken(String token);

    List<EmailSubscription> findByIsVerifiedTrue();

    boolean existsByEmail(String email);
}
