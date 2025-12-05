package udpm.hn.server.core.customer.ViewProduct.repository;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import udpm.hn.server.core.customer.ViewProduct.model.Request.ProductFilterRequest;
import udpm.hn.server.entity.Product;
import udpm.hn.server.infrastructure.core.constant.EntityStatus;

import java.util.ArrayList;
import java.util.List;

public class ViewProductSpecification {
    public static Specification<Product> getFilter(ProductFilterRequest req) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Active products only
            // predicates.add(cb.equal(root.get("status"), EntityStatus.ACTIVE));

            // Search (Name or Description)
            if (req.getSearch() != null && !req.getSearch().trim().isEmpty()) {
                String keyword = "%" + req.getSearch().trim().toLowerCase() + "%";
                Predicate name = cb.like(cb.lower(root.get("name")), keyword);
                Predicate desc = cb.like(cb.lower(root.get("shortDescription")), keyword);
                predicates.add(cb.or(name, desc));
            }

            // Price Range
            if (req.getMinPrice() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), req.getMinPrice()));
            }
            if (req.getMaxPrice() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), req.getMaxPrice()));
            }

            // Categories
            if (req.getCategories() != null && !req.getCategories().isEmpty()) {
                // Assuming category is a relationship and we filter by ID
                // Check if categories list contains IDs
                 predicates.add(root.get("category").get("id").in(req.getCategories()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
