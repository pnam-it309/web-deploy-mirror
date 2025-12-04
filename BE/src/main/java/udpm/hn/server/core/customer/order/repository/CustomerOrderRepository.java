package udpm.hn.server.core.customer.order.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.core.customer.order.model.response.CustomerOrderResponse;
import udpm.hn.server.entity.Order;

import java.util.Optional;

@Repository
public interface CustomerOrderRepository extends JpaRepository<Order, String> {

    @Query(value = """
        SELECT 
            o.id as id,
            o.order_code as orderCode,
            o.created_date as createdDate,
            o.total_amount as totalAmount,
            o.status as status,
            o.order_status as orderStatus,
            o.payment_status as paymentStatus,
            o.estimated_delivery_date as estimatedDeliveryDate,
            (SELECT COUNT(*) FROM order_items oi WHERE oi.order_id = o.id) as itemsCount
        FROM orders o
        WHERE o.customer_email = :email
        AND (:search IS NULL OR o.order_code LIKE CONCAT('%', :search, '%'))
        AND (:status IS NULL OR :status = '' OR o.order_status = :status)
        ORDER BY o.created_date DESC
    """, nativeQuery = true)
    Page<CustomerOrderResponse> findAllByCustomerEmail(String email, String search, String status, Pageable pageable);

    Long countByCustomerEmail(String email);
    
    Optional<Order> findByIdAndCustomerEmail(String id, String email);
}
