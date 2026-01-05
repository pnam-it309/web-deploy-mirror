package udpm.hn.server.core.admin.technology.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TechnologyRequest {
    @NotBlank(message = "Tên công nghệ không được để trống")
    private String name;

    private String icon; // Đường dẫn ảnh icon
}