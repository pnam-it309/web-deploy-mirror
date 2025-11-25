package udpm.hn.server.core.admin.brand.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import udpm.hn.server.infrastructure.core.constant.EntityStatus;

@Data
public class BrandCreateRequest {
    @NotBlank(message = "Brand code is required")
    @Size(max = 50, message = "Brand code must not exceed 50 characters")
    private String code;
    @NotBlank(message = "Brand name is required")
    @Size(max = 100, message = "Brand name must not exceed 100 characters")
    private String name;
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;
    private String logoUrl;
    @NotNull(message = "Status is required")
    private EntityStatus status;
}
