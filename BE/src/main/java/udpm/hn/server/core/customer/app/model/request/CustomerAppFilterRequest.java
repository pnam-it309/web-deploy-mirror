package udpm.hn.server.core.customer.app.model.request;

import lombok.Getter;
import lombok.Setter;
import udpm.hn.server.core.common.base.PageableRequest;

import java.util.List;

@Getter
@Setter
public class CustomerAppFilterRequest extends PageableRequest {
    private String query; // Search text
    private String domainId;
    private List<String> technologyIds;
    private String sort; // NEWEST, FEATURED, VIEW_COUNT
}
