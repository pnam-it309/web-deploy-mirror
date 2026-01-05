package udpm.hn.server.core.admin.feature.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminUpdateFeatureRequest {
    private String name;
    private String description;
    private String imagePreview;
    private Integer sortOrder;
}
