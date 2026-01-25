package udpm.hn.server.core.admin.role.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PermissionResponse {

    private String id;
    private String name;
    private String code;
    private String description;
    private String category;
}
