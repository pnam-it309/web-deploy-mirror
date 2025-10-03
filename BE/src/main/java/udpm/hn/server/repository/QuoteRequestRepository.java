package udpm.hn.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.QuoteRequest;

@Repository
public interface QuoteRequestRepository extends JpaRepository<QuoteRequest,String> {
}
