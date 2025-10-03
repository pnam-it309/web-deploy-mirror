package udpm.hn.server.core.admin.import_data.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ProductImportRequest extends BaseImportRequest {
    // Additional product-specific import parameters can be added here
}
