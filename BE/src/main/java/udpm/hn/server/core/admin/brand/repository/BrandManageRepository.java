package udpm.hn.server.core.admin.brand.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Brand;
import udpm.hn.server.repository.BrandRepository;

import java.util.Optional;

@Repository
public interface BrandManageRepository extends BrandRepository {
    Optional<Brand> findByCode(String code);
    boolean existsByCode(String code);
    boolean existsByCodeAndIdNot(String code, String id);
}
