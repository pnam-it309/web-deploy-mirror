package udpm.hn.server.core.admin.products.modal.request;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import udpm.hn.server.core.common.base.PageableRequest;
import udpm.hn.server.entity.Brand;
import udpm.hn.server.entity.Category;
import udpm.hn.server.entity.ProductDetail;
import udpm.hn.server.entity.QuoteItem;
import udpm.hn.server.infrastructure.core.constant.EntityProperties;
import udpm.hn.server.infrastructure.core.constant.EntityStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductsRequest extends PageableRequest {
         String sku;
         String name;
         String brandId;
         String categoryId;
         String status;

}
