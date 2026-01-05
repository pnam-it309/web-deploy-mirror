package udpm.hn.server.core.admin.app.model.response;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserResponse {
    private String id;
    private String fullName;
    private String email;
    private String code;
}
