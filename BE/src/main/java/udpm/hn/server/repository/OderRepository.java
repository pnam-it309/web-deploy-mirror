package udpm.hn.server.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.QuoteItem;

@Repository
public interface OderRepository extends JpaRepository<QuoteItem,String>{

}
