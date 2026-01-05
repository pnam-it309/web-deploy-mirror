package udpm.hn.server.core.admin.app.dto.response;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppDetailResponse {
    private String id;
    private String appId;
    private String longDescription;
    private String demoUrl;
    private String sourceUrl;
    private JsonNode specifications;
}