package udpm.hn.server.core.customer.order.model.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface CustomerOrderResponse {
    String getId();
    String getOrderCode();
    Long getCreatedDate();
    BigDecimal getTotalAmount();
    String getStatus();
    String getOrderStatus();
    String getPaymentStatus();
    LocalDate getEstimatedDeliveryDate();
    Integer getItemsCount();
}
