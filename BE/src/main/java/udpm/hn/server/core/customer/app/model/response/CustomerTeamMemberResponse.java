package udpm.hn.server.core.customer.app.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerTeamMemberResponse {
    private String id;
    private String customerId;
    private String fullName;
    private String role;
    // Avatar could be added if Customer has it
}
