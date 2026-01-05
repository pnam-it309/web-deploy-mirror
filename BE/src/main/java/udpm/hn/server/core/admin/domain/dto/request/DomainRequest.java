package udpm.hn.server.core.admin.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DomainRequest {
    @NotBlank(message = "Tên lĩnh vực không được để trống")
    private String name;

    private String description;
    private String icon;
    private String color;
}