package udpm.hn.server.infrastructure.core.job.products.repository;

import udpm.hn.server.entity.Brand;
import udpm.hn.server.repository.BrandRepository;

import java.util.Optional;

public interface BrandRowRepository extends BrandRepository {
    Optional<Brand> findByName(String name);

}
