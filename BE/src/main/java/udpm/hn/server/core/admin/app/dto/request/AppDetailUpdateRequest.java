package udpm.hn.server.core.admin.app.dto.request;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

@Data
public class AppDetailUpdateRequest {
    private String longDescription; // Bài viết chi tiết
    private String demoUrl;
    private String sourceUrl;
    private JsonNode specifications; // Thông tin kỹ thuật thêm (JSON)
}