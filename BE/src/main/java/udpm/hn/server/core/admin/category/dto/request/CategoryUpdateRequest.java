package udpm.hn.server.core.admin.category.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import udpm.hn.server.infrastructure.core.constant.EntityProperties;

@Data
public class CategoryUpdateRequest {
    @NotBlank(message = "Category name is required")
    @Size(max = EntityProperties.LENGTH_NAME, message = "Category name must not exceed {max} characters")
    private String name;

    @Size(max = EntityProperties.LENGTH_DESCRIPTION, message = "Description must not exceed {max} characters")
    private String description;

    private String parentId;
}
