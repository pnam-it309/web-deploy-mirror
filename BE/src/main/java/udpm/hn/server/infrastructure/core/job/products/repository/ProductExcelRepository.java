// ProductExcelRepository.java
package udpm.hn.server.infrastructure.core.job.products.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Product;

import java.util.Optional;

@Repository
public interface ProductExcelRepository extends JpaRepository<Product, String> {
    Optional<Product> findBySku(String sku);
    boolean existsBySku(String sku);
}