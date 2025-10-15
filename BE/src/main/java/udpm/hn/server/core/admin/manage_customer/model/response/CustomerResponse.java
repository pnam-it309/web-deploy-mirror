package udpm.hn.server.core.admin.manage_customer.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import udpm.hn.server.infrastructure.core.constant.EntityStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponse {
    private String id;
    private String code;
    private String name;
    private String email;
    private String picture;
    private EntityStatus status;
}
