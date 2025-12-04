package udpm.hn.server.core.customer.register.dto.response;

import lombok.Data;

@Data
public class OrderItemResponse {
    private String productId;
    private String productName;
    private Integer quantity;
    private Double unitPrice;
    private Double totalPrice;
    private String note;
}
