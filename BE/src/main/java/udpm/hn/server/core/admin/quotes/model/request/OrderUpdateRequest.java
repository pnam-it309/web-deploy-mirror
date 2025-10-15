package udpm.hn.server.core.admin.quotes.model.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OrderUpdateRequest {
    @NotBlank(message = "Customer name is required")
    @Size(max = 100, message = "Customer name must not exceed 100 characters")
    private String customerName;

    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String customerEmail;

    @Size(max = 50, message = "Phone must not exceed 50 characters")
    private String customerPhone;

    @Size(max = 100, message = "Company name must not exceed 100 characters")
    private String customerCompany;

    @Size(max = 500, message = "Notes must not exceed 500 characters")
    private String notes;

    @Valid
    private List<OrderItemRequest> items = new ArrayList<>();
}
