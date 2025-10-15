package udpm.hn.server.core.admin.quotes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.QuoteItem;

@Repository
public interface OrderItemRepository extends JpaRepository<QuoteItem, String> {
}
