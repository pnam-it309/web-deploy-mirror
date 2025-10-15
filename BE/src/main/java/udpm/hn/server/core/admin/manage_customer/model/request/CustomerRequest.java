package udpm.hn.server.core.admin.manage_customer.model.request;

import lombok.Data;

@Data
public class CustomerRequest {
    private String code;
    private String name;
    private String email;
    private String picture;
}
