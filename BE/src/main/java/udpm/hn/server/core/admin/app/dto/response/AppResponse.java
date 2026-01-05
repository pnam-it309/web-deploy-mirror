package udpm.hn.server.core.admin.app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import udpm.hn.server.infrastructure.constant.ApprovalStatus;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppResponse {

    // --- Basic Info ---
    private String id;
    private String name;
    private String sku;
    private String shortDescription;
    private String thumbnail;
    private Long viewCount;
    private ApprovalStatus approvalStatus;

    // --- Domain (Lĩnh vực) ---
    private String domainId;
    private String domainName;

    // --- Technologies (Công nghệ) ---
    private Set<TechnologyResponse> technologies;

    // --- Members (Quan trọng: Đã cập nhật cho logic Hybrid) ---
    private List<MemberResponse> members;

    // --- Images (Thư viện ảnh) ---
    private List<ImageResponse> images;

    // ================= INNER CLASSES =================

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TechnologyResponse {
        private String id;
        private String name;
        private String icon;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberResponse {
        private String id;
        private String customerId;
        private String fullName;
        private String email;
        private String avatar;
        private String role;
        private Boolean isGuest;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ImageResponse {
        private String id;
        private String url;
        private Boolean isMain;
    }
}