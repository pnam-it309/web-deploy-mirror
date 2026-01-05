package udpm.hn.server.core.admin.app.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminAddMemberRequest {
    private String customerId;
    private String role; // "LEADER" | "MEMBER"
}
