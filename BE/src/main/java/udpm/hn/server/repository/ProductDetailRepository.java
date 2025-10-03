package udpm.hn.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.ProductDetail;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail,String> {
}
