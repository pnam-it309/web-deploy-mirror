package udpm.hn.server.core.admin.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Category;
import udpm.hn.server.repository.CategoryRepository;

@Repository
public interface CategoryManageRepository extends CategoryRepository {
    boolean existsByName(String name);
    boolean existsBySlug(String slug);
}
