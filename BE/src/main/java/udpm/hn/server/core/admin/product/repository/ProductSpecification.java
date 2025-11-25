package udpm.hn.server.core.admin.product.repository;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import udpm.hn.server.core.admin.product.dto.request.ProductFilterRequest;
import udpm.hn.server.entity.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {

    public static Specification<Product> getFilter(ProductFilterRequest req) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 1. Tìm theo Keyword (Tên hoặc SKU)
            if (req.getKeyword() != null && !req.getKeyword().trim().isEmpty()) {
                String keyword = "%" + req.getKeyword().trim().toLowerCase() + "%";
                Predicate namePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), keyword);
                Predicate skuPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("sku")), keyword);
                predicates.add(criteriaBuilder.or(namePredicate, skuPredicate));
            }

            // 2. Filter theo Brand
            if (req.getBrandId() != null && !req.getBrandId().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("brand").get("id"), req.getBrandId()));
            }

            // 3. Filter theo Category
            if (req.getCategoryId() != null && !req.getCategoryId().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("category").get("id"), req.getCategoryId()));
            }

            // 4. Filter theo Status
            if (req.getStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), req.getStatus()));
            }

            // 5. Filter theo Khoảng giá (Min - Max)
            if (req.getMinPrice() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), req.getMinPrice()));
            }
            if (req.getMaxPrice() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), req.getMaxPrice()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}