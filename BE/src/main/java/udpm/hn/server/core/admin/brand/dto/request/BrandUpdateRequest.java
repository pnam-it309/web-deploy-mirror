package udpm.hn.server.core.admin.brand.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;
import udpm.hn.server.infrastructure.core.constant.EntityStatus;


@Data
public class BrandUpdateRequest {
    @Size(max = 100, message = "Brand name must not exceed 100 characters")
    private String name;
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;
    private String logoUrl;
    private EntityStatus status;
}
