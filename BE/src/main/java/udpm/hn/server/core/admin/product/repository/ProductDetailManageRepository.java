package udpm.hn.server.core.admin.product.repository;

import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.ProductDetail;
import udpm.hn.server.repository.ProductDetailRepository;

import java.util.Optional;

@Repository
public interface ProductDetailManageRepository extends ProductDetailRepository {
    // Tìm chi tiết theo ID sản phẩm cha
    Optional<ProductDetail> findByProductId(String productId);
}