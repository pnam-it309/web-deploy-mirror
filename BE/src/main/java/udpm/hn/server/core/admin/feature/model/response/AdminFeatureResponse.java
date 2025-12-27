package udpm.hn.server.core.admin.feature.model.response;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import udpm.hn.server.entity.Feature;

@Getter
@Setter
@NoArgsConstructor
public class AdminFeatureResponse {
    private String id;
    private String name;
    private String description;
    private String imagePreview;
    private Integer sortOrder;
    private String appId;
    private String appName;

    public AdminFeatureResponse(Feature feature) {
        this.id = feature.getId();
        this.name = feature.getName();
        this.description = feature.getDescription();
        this.imagePreview = feature.getImagePreview();
        this.sortOrder = feature.getSortOrder();
        if (feature.getApp() != null) {
            this.appId = feature.getApp().getId();
            this.appName = feature.getApp().getName();
        }
    }
}
