package udpm.hn.server.core.admin.feature.dto.request;

import lombok.Data;

@Data
public class FeatureOrderRequest {
    private String id;
    private Integer sortOrder;
}
