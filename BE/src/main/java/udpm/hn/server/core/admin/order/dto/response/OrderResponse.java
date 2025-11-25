package udpm.hn.server.core.admin.order.dto.response;

import lombok.Data;
import udpm.hn.server.entity.Order;
import udpm.hn.server.infrastructure.core.constant.EntityStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponse {
    private String id;
    private String orderCode;
    private String customerName;
    private String customerPhone;
    private String customerAddress;
    private BigDecimal totalAmount;
    private Order.OrderStatus orderStatus;
    private Order.PaymentStatus paymentStatus;
    private String paymentMethod;
    private LocalDate createdDate;
    private EntityStatus status;

    // Danh sách sản phẩm trong đơn
    private List<OrderItemResponse> items;

    @Data
    public static class OrderItemResponse {
        private String productId;
        private String productName;
        private String productSku;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal totalPrice;
    }
}