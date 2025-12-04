package udpm.hn.server.core.admin.order.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Order;
import udpm.hn.server.repository.OrderRepository;

@Repository
public interface OrderManageRepository extends OrderRepository, JpaSpecificationExecutor<Order> {
    boolean existsByOrderCode(String orderCode);
}