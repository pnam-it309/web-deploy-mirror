package udpm.hn.server.core.customer.register.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;


import java.util.List;

@Data
public class OrderRequest {
    @NotBlank(message = "Customer ID is required")
    private String customerId;
    
    @NotBlank(message = "Shipping address is required")
    private String shippingAddress;
    
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;
    
    @NotBlank(message = "Email is required")
    private String email;
    
    @NotBlank(message = "Customer name is required")
    private String customerName;
    
    @NotEmpty(message = "Order items cannot be empty")
    private List<OrderItemRequest> items;
    
    private String note;
}
