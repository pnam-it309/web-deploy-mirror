package udpm.hn.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.QuoteItem;

@Repository
public interface QuoteItemRepository extends JpaRepository<QuoteItem,String> {

}
