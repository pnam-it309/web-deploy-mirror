package udpm.hn.server.core.admin.category.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Category;
import udpm.hn.server.infrastructure.core.constant.EntityStatus;
import udpm.hn.server.repository.CategoryRepository;

@Repository
public interface CategoryManageRepository extends CategoryRepository, JpaSpecificationExecutor<Category> {

    boolean existsByName(String name);
    boolean existsBySlug(String slug);

    Page<Category> findByStatus(EntityStatus status, Pageable pageable);
}
