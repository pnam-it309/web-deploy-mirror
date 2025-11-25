package udpm.hn.server.core.admin.order.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import udpm.hn.server.entity.Order;

import java.util.List;

@Data
public class OrderCreateRequest {

    @NotBlank(message = "Tên khách hàng không được trống")
    private String customerName;

    @NotBlank(message = "SĐT khách hàng không được trống")
    private String customerPhone;

    private String customerEmail;

    @NotBlank(message = "Địa chỉ không được trống")
    private String customerAddress;

    private String notes;

    private String paymentMethod; // COD, TRANSFER...

    @NotEmpty(message = "Đơn hàng phải có ít nhất 1 sản phẩm")
    private List<OrderItemRequest> items;

    @Data
    public static class OrderItemRequest {
        @NotBlank(message = "Product ID không được trống")
        private String productId;

        @NotNull(message = "Số lượng không được trống")
        private Integer quantity;
    }
}
