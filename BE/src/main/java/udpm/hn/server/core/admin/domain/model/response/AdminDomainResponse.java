package udpm.hn.server.core.admin.domain.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import udpm.hn.server.infrastructure.constant.EntityStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminDomainResponse {
    private String id;
    private String name;
    private String slug;
    private String description;
    private String icon;
    private String parentId;
    private String parentName;
    private Integer sortOrder;
    private EntityStatus status;
    private Long createdDate;
    private Long lastModifiedDate;
}
