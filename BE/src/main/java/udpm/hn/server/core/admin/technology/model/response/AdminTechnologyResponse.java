package udpm.hn.server.core.admin.technology.model.response;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import udpm.hn.server.entity.Technology;

@Getter
@Setter
@NoArgsConstructor
public class AdminTechnologyResponse {
    private String id;
    private String name;
    private String icon;

    public AdminTechnologyResponse(Technology technology) {
        this.id = technology.getId();
        this.name = technology.getName();
        this.icon = technology.getIcon();
    }
}
