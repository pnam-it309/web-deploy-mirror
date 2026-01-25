package udpm.hn.server.core.admin.domain.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DomainResponse {
    private String id;
    private String name;
    private String slug;
    private String description;
    private String icon;
    private String color;
    private Integer sortOrder;
    private udpm.hn.server.infrastructure.constant.EntityStatus status;
    private Long createdAt;
}