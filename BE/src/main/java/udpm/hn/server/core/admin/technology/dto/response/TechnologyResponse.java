package udpm.hn.server.core.admin.technology.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TechnologyResponse {
    private String id;
    private String name;
    private String slug;
    private String description;
    private String icon;
}