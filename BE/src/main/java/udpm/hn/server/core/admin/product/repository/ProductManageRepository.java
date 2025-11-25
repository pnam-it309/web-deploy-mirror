package udpm.hn.server.core.admin.product.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
}
