package udpm.hn.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Brand;

@Repository
public interface BrandRepository extends JpaRepository<Brand, String> {
    boolean existsByCode(String code);
}
