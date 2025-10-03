package udpm.hn.server.core.admin.products.modal.request;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import udpm.hn.server.entity.Brand;
import udpm.hn.server.entity.Category;
import udpm.hn.server.entity.ProductDetail;
import udpm.hn.server.entity.QuoteItem;
import udpm.hn.server.infrastructure.core.constant.EntityProperties;
import udpm.hn.server.infrastructure.core.constant.EntityStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductsCreAndUpdateRequest {
    // Thông tin Product
    @NotBlank(message = "Mã sản phẩm không được để trống")
    String sku;

    @NotBlank(message = "Tên sản phẩm không được để trống")
    String name;

    String shortDescription;
    BigDecimal price = BigDecimal.ZERO;
    Integer stockQuantity = 0;

    String brandId;
    String categoryID;
    EntityStatus status = EntityStatus.ACTIVE;

    // Thông tin ProductDetail
    String longDescription;
    JsonNode specification; // Hoặc bạn có thể dùng Map<String, Object>
    String packaging;
    BigDecimal weight;
    String dimensions;

    // Danh sách ảnh
    List<ImageRequest> images;
}
