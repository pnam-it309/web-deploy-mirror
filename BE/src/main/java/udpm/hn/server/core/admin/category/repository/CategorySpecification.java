package udpm.hn.server.core.admin.category.repository;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import udpm.hn.server.core.admin.category.dto.request.CategoryFilterRequest;
import udpm.hn.server.entity.Category;

import java.util.ArrayList;
import java.util.List;

public class CategorySpecification {

    public static Specification<Category> getFilter(CategoryFilterRequest req) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 1. Keyword (Tên hoặc Mô tả)
            if (req.getKeyword() != null && !req.getKeyword().trim().isEmpty()) {
                String keyword = "%" + req.getKeyword().trim().toLowerCase() + "%";
                Predicate namePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), keyword);

                // Kiểm tra nếu có field description
                Predicate descPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), keyword);

                predicates.add(criteriaBuilder.or(namePredicate, descPredicate));
            }

            // 2. Parent ID (Lọc theo cha)
            if (req.getParentId() != null && !req.getParentId().trim().isEmpty()) {
                // Join với bảng chính nó để lấy parent
                predicates.add(criteriaBuilder.equal(root.get("parent").get("id"), req.getParentId()));
            }

            // 3. Status
            if (req.getStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), req.getStatus()));
            }

            // 4. Type (Logic nâng cao)
            if (req.getType() != null && !req.getType().trim().isEmpty()) {
                switch (req.getType()) {
                    case "root": // Gốc: Parent IS NULL
                        predicates.add(criteriaBuilder.isNull(root.get("parent")));
                        break;
                    case "child": // Con: Parent IS NOT NULL
                        predicates.add(criteriaBuilder.isNotNull(root.get("parent")));
                        break;
                    case "has-children": // Có con: List children không rỗng
                        predicates.add(criteriaBuilder.isNotEmpty(root.get("children")));
                        break;
                }
            }

            // Sắp xếp mặc định (nếu cần thiết, tránh lỗi createdAt nếu không có)
            // query.orderBy(criteriaBuilder.desc(root.get("lastModifiedDate")));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}