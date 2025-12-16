package udpm.hn.server.core.admin.product.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable; // Thêm import Pageable
import org.springframework.data.jpa.repository.Query; // Thêm import Query
import java.util.List; // Thêm import List
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Product;
import udpm.hn.server.infrastructure.core.constant.EntityStatus;
import udpm.hn.server.repository.ProductRepository;
@Repository
public interface ProductManageRepository extends ProductRepository, JpaSpecificationExecutor<Product> {

    Page<Product> findByStatus(EntityStatus status, Pageable pageable);

    boolean existsBySku(String sku);
    boolean existsBySkuAndIdNot(String sku, String id);
    @Query("SELECT p FROM Product p WHERE p.stockQuantity < 100 ORDER BY p.stockQuantity ASC")
    List<Product> findLowStockProducts(Pageable pageable);

}
