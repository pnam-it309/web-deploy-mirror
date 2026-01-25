package udpm.hn.server.core.admin.role.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleResponse {

    private String id;
    private String name;
    private String description;
    private Set<PermissionResponse> permissions;
    private Long createdAt;
    private Long updatedAt;
}
