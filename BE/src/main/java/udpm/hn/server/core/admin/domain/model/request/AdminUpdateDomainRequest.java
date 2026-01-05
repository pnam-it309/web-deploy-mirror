package udpm.hn.server.core.admin.domain.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminUpdateDomainRequest {
    private String name;
    private String slug;
    private String description;
    private String icon;
    private String parentId;
    private Integer sortOrder;
}
