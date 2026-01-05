package udpm.hn.server.core.admin.app.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import udpm.hn.server.infrastructure.constant.EntityProperties;
import java.util.Set;

@Data
public class AppCreateRequest {
    @NotBlank(message = "Tên sản phẩm không được để trống")
    @Size(max = EntityProperties.LENGTH_NAME, message = "Tên quá dài")
    private String name;

    @Size(max = EntityProperties.LENGTH_CODE)
    private String sku;

    @Size(max = EntityProperties.LENGTH_DESCRIPTION)
    private String shortDescription;

    private String thumbnail;

    @NotBlank(message = "Lĩnh vực (Domain) không được để trống")
    private String domainId;

    private Set<String> technologyIds;
}