package udpm.hn.server.core.admin.brand.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor; // <-- Import
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Brand;
import udpm.hn.server.infrastructure.core.constant.EntityStatus;
import udpm.hn.server.repository.BrandRepository;

@Repository
// THÊM: extends JpaSpecificationExecutor<Brand>
public interface BrandManageRepository extends BrandRepository, JpaSpecificationExecutor<Brand> {
    boolean existsByCode(String code);
    Page<Brand> findByStatus(EntityStatus status, Pageable pageable);
}