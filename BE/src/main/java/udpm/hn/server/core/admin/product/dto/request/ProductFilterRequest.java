package udpm.hn.server.core.admin.product.dto.request;

import lombok.Data;
import udpm.hn.server.infrastructure.core.constant.EntityStatus;

import java.math.BigDecimal;

@Data
public class ProductFilterRequest {
    private String keyword; // Tìm theo tên hoặc mã SKU
    private String brandId;
    private String categoryId;
    private EntityStatus status;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
}