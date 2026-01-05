package udpm.hn.server.core.admin.feature.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeatureResponse {
    private String id;
    private String name;
    private String description;
    private String imagePreview;
    private Integer sortOrder;
    private String appId;
}