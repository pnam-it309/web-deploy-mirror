package udpm.hn.server.core.admin.import_data.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public abstract class BaseImportRequest {
    private MultipartFile file;
    private String sheetUrl;
    private String sheetName;
    private boolean hasHeader = true;
    private int headerRow = 0;
    private int dataStartRow = 1;
}
