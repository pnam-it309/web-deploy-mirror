package udpm.hn.server.core.admin.category.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryResponse {
    private String id;
    private String name;
    private String slug;
    private String description;
    private String parentId;
    private List<CategoryResponse> children;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
