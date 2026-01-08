package udpm.hn.server.core.admin.app.dto.request;

import lombok.Data;

@Data
public class HomepageOrderRequest {
    private String id;
    private Integer homepageSortOrder;
}
