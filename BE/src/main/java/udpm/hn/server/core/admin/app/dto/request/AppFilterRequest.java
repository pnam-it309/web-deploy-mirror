package udpm.hn.server.core.admin.app.dto.request;

import lombok.Data;
import udpm.hn.server.infrastructure.constant.ApprovalStatus;

@Data
public class AppFilterRequest {
    private String keyword;       // Tìm theo Tên hoặc Mã (SKU)
    private String domainId;      // Lọc theo Lĩnh vực
    private String brandId;       // Lọc theo Brand (nếu có)
    private ApprovalStatus status; // Lọc theo trạng thái duyệt (PENDING, APPROVED...)
}