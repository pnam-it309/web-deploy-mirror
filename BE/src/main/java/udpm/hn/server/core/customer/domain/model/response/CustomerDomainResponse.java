package udpm.hn.server.core.customer.domain.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDomainResponse {
    private String id;
    private String name;
    private String slug;
    private String description;
    private String icon;
    private long productCount;
}
