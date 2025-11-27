package udpm.hn.server.core.admin.manage_customer.repository;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import udpm.hn.server.core.admin.manage_customer.dto.request.CustomerFilterRequest;
import udpm.hn.server.entity.Customer;

import java.util.ArrayList;
import java.util.List;

public class CustomerSpecification {
    public static Specification<Customer> getFilter(CustomerFilterRequest req) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (req.getKeyword() != null && !req.getKeyword().isEmpty()) {
                String keyword = "%" + req.getKeyword().toLowerCase() + "%";
                Predicate name = cb.like(cb.lower(root.get("name")), keyword);
                Predicate email = cb.like(cb.lower(root.get("email")), keyword);
                Predicate code = cb.like(cb.lower(root.get("code")), keyword);
                predicates.add(cb.or(name, email, code));
            }

            if (req.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), req.getStatus()));
            }

            query.orderBy(cb.desc(root.get("createdDate"))); // Sắp xếp mới nhất
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}