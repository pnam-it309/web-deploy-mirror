package udpm.hn.server.core.admin.order.dto.request;

import lombok.Data;
import udpm.hn.server.entity.Order; // Import Enum OrderStatus

@Data
public class OrderFilterRequest {
    private String keyword; // Tìm theo Mã đơn hoặc Tên khách/SĐT
    private Order.OrderStatus status; // Lọc theo trạng thái đơn
}