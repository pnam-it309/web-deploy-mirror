package udpm.hn.server.core.admin.category.dto.request;

import lombok.Data;
import udpm.hn.server.infrastructure.core.constant.EntityStatus;

@Data
public class CategoryFilterRequest {
    private String keyword;      // Tìm theo tên hoặc mô tả
    private String parentId;     // Lọc theo danh mục cha
    private String type;         // 'root', 'child', 'has-children'
    private EntityStatus status; // Lọc theo trạng thái (ACTIVE/INACTIVE)
}