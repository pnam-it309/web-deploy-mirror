package udpm.hn.server.core.admin.app.repository;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import udpm.hn.server.core.admin.app.dto.request.AppFilterRequest;
import udpm.hn.server.entity.App;

import java.util.ArrayList;
import java.util.List;

import udpm.hn.server.infrastructure.constant.EntityStatus;

public class AppSpecification {

    public static Specification<App> getFilter(AppFilterRequest req) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Exclude DELETED records essentially
            predicates.add(cb.notEqual(root.get("status"), EntityStatus.DELETED));

            // 1. Tìm theo Keyword (Tên hoặc SKU)
            if (req.getKeyword() != null && !req.getKeyword().trim().isEmpty()) {
                String keyword = "%" + req.getKeyword().trim().toLowerCase() + "%";
                Predicate nameLike = cb.like(cb.lower(root.get("name")), keyword);
                Predicate skuLike = cb.like(cb.lower(root.get("sku")), keyword);
                predicates.add(cb.or(nameLike, skuLike));
            }

            // 2. Filter theo Domain
            if (req.getDomainId() != null && !req.getDomainId().trim().isEmpty()) {
                predicates.add(cb.equal(root.get("domain").get("id"), req.getDomainId()));
            }

            // 3. Filter theo Brand
            if (req.getBrandId() != null && !req.getBrandId().trim().isEmpty()) {
                predicates.add(cb.equal(root.get("brand").get("id"), req.getBrandId()));
            }

            // 4. Filter theo Trạng thái duyệt (Approval Status)
            if (req.getStatus() != null) {
                predicates.add(cb.equal(root.get("approvalStatus"), req.getStatus()));
            }

            // Mặc định sắp xếp: Mới nhất lên đầu
            // (Nếu entity App có field createdDate/createdAt, hãy đảm bảo tên field đúng)
            query.orderBy(cb.desc(root.get("createdAt")));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}