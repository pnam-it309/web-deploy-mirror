package udpm.hn.server.infrastructure.webhook;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WebhookRepository extends JpaRepository<Webhook, String> {

    List<Webhook> findByIsActiveTrue();

    @Query("SELECT w FROM Webhook w WHERE w.isActive = true AND w.events LIKE %:event%")
    List<Webhook> findActiveByEvent(@Param("event") String event);

    List<Webhook> findByFailureCountLessThan(int maxFailures);
}
