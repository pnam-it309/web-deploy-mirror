package udpm.hn.server.core.admin.app.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminCreateFeatureRequest {
    private String name;
    private String description;
    private String imagePreview;
    private Integer sortOrder;
}
