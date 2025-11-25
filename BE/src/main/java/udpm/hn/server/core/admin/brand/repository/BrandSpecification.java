package udpm.hn.server.core.admin.brand.repository;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import udpm.hn.server.core.admin.brand.dto.request.BrandFilterRequest;
import udpm.hn.server.entity.Brand;

import java.util.ArrayList;
import java.util.List;

public class BrandSpecification {

    public static Specification<Brand> getFilter(BrandFilterRequest req) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 1. Tìm theo Keyword chung (Tên hoặc Mã)
            if (req.getKeyword() != null && !req.getKeyword().trim().isEmpty()) {
                String keyword = "%" + req.getKeyword().trim().toLowerCase() + "%";
                Predicate namePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), keyword);
                Predicate codePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("code")), keyword);
                predicates.add(criteriaBuilder.or(namePredicate, codePredicate));
            }

            // (Nếu muốn lọc riêng lẻ từng trường thì dùng đoạn dưới, ở đây ta dùng keyword chung cho tiện)
            /*
            if (req.getName() != null && !req.getName().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + req.getName().toLowerCase() + "%"));
            }
            if (req.getCode() != null && !req.getCode().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("code")), "%" + req.getCode().toLowerCase() + "%"));
            }
            */

            // 2. Filter theo Trạng thái
            if (req.getStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), req.getStatus()));
            }

            // Sắp xếp: Mới nhất lên đầu (Tránh lỗi createdAt nếu entity không có, nhưng Brand có AuditEntity nên ổn)
            query.orderBy(criteriaBuilder.desc(root.get("createdAt")));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}