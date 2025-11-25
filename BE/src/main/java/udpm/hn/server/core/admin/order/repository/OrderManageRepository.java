package udpm.hn.server.core.admin.order.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Order;
import udpm.hn.server.infrastructure.core.constant.EntityStatus;
import udpm.hn.server.repository.OrderRepository; // (Tạo interface này extends JpaRepository nếu chưa có)

@Repository
public interface OrderManageRepository extends OrderRepository {
    // Lấy danh sách đơn hàng ACTIVE
    Page<Order> findByStatus(EntityStatus status, Pageable pageable);

    // Tìm theo mã đơn hàng
    boolean existsByOrderCode(String orderCode);
}