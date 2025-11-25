package udpm.hn.server.core.admin.brand.dto.response;

import lombok.Data;
import udpm.hn.server.infrastructure.core.constant.EntityStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class BrandResponse {
    private UUID id;
    private String code;
    private String name;
    private String slug;
    private String description;
    private String logoUrl;
    private EntityStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}
