package udpm.hn.server.core.admin.order.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Order;
import udpm.hn.server.repository.OrderRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface OrderManageRepository extends OrderRepository, JpaSpecificationExecutor<Order> {
    boolean existsByOrderCode(String orderCode);
    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o WHERE o.createdDate BETWEEN :start AND :end AND o.orderStatus != 'CANCELLED'")
    BigDecimal sumTotalAmountByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    // Lấy danh sách đơn hàng mới nhất
    @Query("SELECT o FROM Order o ORDER BY o.createdDate DESC")
    List<Order> findRecentOrders(Pageable pageable);
}