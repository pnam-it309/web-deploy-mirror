package udpm.hn.server.core.customer.order.model.response;

import lombok.Getter;
import lombok.Setter;
import udpm.hn.server.entity.Order;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class CustomerOrderDetailResponse {
    private String id;
    private String orderCode;
    private Long createdDate;
    private BigDecimal totalAmount;
    private String status;
    private String orderStatus;
    private String paymentStatus;
    private String paymentMethod;
    private LocalDate estimatedDeliveryDate;
    private LocalDate actualDeliveryDate;
    private String cancellationReason;
    private String notes;
    
    private String customerName;
    private String customerPhone;
    private String customerAddress;
    
    private List<CustomerOrderItemResponse> items;
    
    public CustomerOrderDetailResponse(Order order, List<CustomerOrderItemResponse> items) {
        this.id = order.getId();
        this.orderCode = order.getOrderCode();
        this.createdDate = order.getCreatedDate();
        this.totalAmount = order.getTotalAmount();
        this.status = order.getStatus().name();
        this.orderStatus = order.getOrderStatus().name();
        this.paymentStatus = order.getPaymentStatus().name();
        this.paymentMethod = order.getPaymentMethod();
        this.estimatedDeliveryDate = order.getEstimatedDeliveryDate();
        this.actualDeliveryDate = order.getActualDeliveryDate();
        this.cancellationReason = order.getCancellationReason();
        this.notes = order.getNotes();
        this.customerName = order.getCustomerName();
        this.customerPhone = order.getCustomerPhone();
        this.customerAddress = order.getCustomerAddress();
        this.items = items;
    }
}
