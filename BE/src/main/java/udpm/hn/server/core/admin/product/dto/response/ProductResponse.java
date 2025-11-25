package udpm.hn.server.core.admin.product.dto.response;

import lombok.Data;
import udpm.hn.server.infrastructure.core.constant.EntityStatus;
import udpm.hn.server.infrastructure.core.constant.EntityUnit;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductResponse {
    private String id;
    private String name;
    private String slug;
    private String sku;
    private String shortDescription;
    private BigDecimal price;
    private Integer stockQuantity;
    private EntityUnit unit;
    private EntityStatus status;

    // Trả về ID và Tên của Brand/Category
    private String brandId;
    private String brandName;
    private String categoryId;
    private String categoryName;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}