package udpm.hn.server.core.admin.feature.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeatureRequest {
    @NotBlank(message = "Tên tính năng không được để trống")
    private String name;

    private String description;

    private String imagePreview;

    @NotBlank(message = "Phải chọn thuộc về App nào")
    private String appId;

    private Integer sortOrder;
}