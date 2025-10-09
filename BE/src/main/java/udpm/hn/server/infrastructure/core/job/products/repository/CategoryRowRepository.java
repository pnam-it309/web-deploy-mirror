package udpm.hn.server.infrastructure.core.job.products.repository;

import udpm.hn.server.entity.Category;
import udpm.hn.server.repository.CategoryRepository;

import java.util.Optional;

public interface CategoryRowRepository extends CategoryRepository {
    Optional<Category> findByName(String name);
}
