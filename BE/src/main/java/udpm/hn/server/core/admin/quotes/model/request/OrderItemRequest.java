package udpm.hn.server.core.admin.quotes.model.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class OrderItemRequest {
    private UUID productId;

    @NotBlank(message = "Product name is required")
    @Size(max = 100, message = "Product name must not exceed 100 characters")
    private String productNameSnapshot;

    @NotNull(message = "Unit price is required")
    @Min(value = 0, message = "Unit price must be greater than or equal to 0")
    private BigDecimal unitPrice;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @Size(max = 20, message = "Unit must not exceed 20 characters")
    private String unitSnapshot;

    @Size(max = 500, message = "Notes must not exceed 500 characters")
    private String notes;
}
