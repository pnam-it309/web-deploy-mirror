package udpm.hn.server.core.admin.app.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminUpdateAppConfigRequest {
    private Boolean isFeatured;
    private Integer homepageSortOrder;
}
