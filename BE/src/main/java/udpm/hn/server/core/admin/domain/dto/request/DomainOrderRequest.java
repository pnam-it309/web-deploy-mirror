package udpm.hn.server.core.admin.domain.dto.request;

import lombok.Data;

@Data
public class DomainOrderRequest {
    private String id;
    private Integer sortOrder;
}
