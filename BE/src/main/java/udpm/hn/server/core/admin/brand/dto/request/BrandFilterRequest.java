package udpm.hn.server.core.admin.brand.dto.request;

import lombok.Data;
import udpm.hn.server.infrastructure.core.constant.EntityStatus;

@Data
public class BrandFilterRequest {
    private String keyword;      // Tìm chung cho Tên hoặc Mã
    private String name;         // Tìm cụ thể theo Tên (nếu cần tách biệt)
    private String code;         // Tìm cụ thể theo Mã
    private EntityStatus status; // Lọc theo trạng thái
}