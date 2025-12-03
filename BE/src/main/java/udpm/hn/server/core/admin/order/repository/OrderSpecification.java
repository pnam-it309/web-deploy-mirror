package udpm.hn.server.core.admin.order.repository;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import udpm.hn.server.core.admin.order.dto.request.OrderFilterRequest;
import udpm.hn.server.entity.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderSpecification {

    public static Specification<Order> getFilter(OrderFilterRequest req) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 1. Keyword: Tìm theo Mã đơn OR Tên khách OR SĐT
            if (req.getKeyword() != null && !req.getKeyword().trim().isEmpty()) {
                String keyword = "%" + req.getKeyword().trim().toLowerCase() + "%";
                Predicate code = cb.like(cb.lower(root.get("orderCode")), keyword);
                Predicate customerName = cb.like(cb.lower(root.get("customerName")), keyword);
                Predicate phone = cb.like(cb.lower(root.get("customerPhone")), keyword);

                predicates.add(cb.or(code, customerName, phone));
            }

            // 2. Status
            if (req.getStatus() != null) {
                predicates.add(cb.equal(root.get("orderStatus"), req.getStatus()));
            }

            // Sắp xếp: Mới nhất lên đầu
            query.orderBy(cb.desc(root.get("createdDate")));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}