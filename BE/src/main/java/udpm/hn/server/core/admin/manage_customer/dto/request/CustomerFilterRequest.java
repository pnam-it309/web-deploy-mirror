package udpm.hn.server.core.admin.manage_customer.dto.request;

import lombok.Data;
import udpm.hn.server.infrastructure.core.constant.EntityStatus;

@Data
public class CustomerFilterRequest {
    private String keyword; // Tìm theo tên, email, mã
    private EntityStatus status;
}