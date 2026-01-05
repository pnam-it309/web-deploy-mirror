package udpm.hn.server.core.admin.app.dto.request;

import lombok.Data;
import udpm.hn.server.infrastructure.constant.ApprovalStatus;
import java.util.List;
import java.util.Set;

@Data
public class AppUpdateRequest {
    private String name;
    private String sku;
    private String shortDescription;
    private String thumbnail;
    private String domainId;
    private Set<String> technologyIds;
    private ApprovalStatus approvalStatus;
    private List<MemberRequest> members; // Danh sách sinh viên
    private List<ImageRequest> images;   // Bộ sưu tập ảnh

    @Data
    public static class MemberRequest {
        private String customerId; // ID của sinh viên
        private String role;       // "LEADER" hoặc "MEMBER"
    }

    @Data
    public static class ImageRequest {
        private String url;
        private Boolean isMain;
    }
}