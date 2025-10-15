package udpm.hn.server.core.admin.quotes.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import udpm.hn.server.infrastructure.core.constant.EntityStatus;

@Data
public class OrderStatusUpdateRequest {
    @NotNull(message = "Status is required")
    private EntityStatus status;
}
