package udpm.hn.server.core.admin.product.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import udpm.hn.server.infrastructure.core.constant.EntityStatus;
import udpm.hn.server.infrastructure.core.constant.EntityUnit;

import java.math.BigDecimal;

@Data
public class ProductUpdateRequest {
    @NotBlank(message = "Tên sản phẩm không được trống")
    private String name;
    // SKU thường không cho phép sửa
    // @NotBlank(message = "SKU không được trống")
    // private String sku;
    @Size(max = 1000, message = "Mô tả ngắn không quá 1000 ký tự")
    private String shortDescription;
    @NotNull(message = "Giá không được trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá phải lớn hơn 0")
    private BigDecimal price;
    @NotNull(message = "Số lượng tồn không được trống")
    @Min(value = 0, message = "Số lượng tồn không được âm")
    private Integer stockQuantity;

    @NotNull(message = "Đơn vị không được trống")
    private EntityUnit unit;

    @NotNull(message = "Trạng thái không được trống")
    private EntityStatus status;

    @NotBlank(message = "Brand ID không được trống")
    private String brandId;

    @NotBlank(message = "Category ID không được trống")
    private String categoryId;
}
