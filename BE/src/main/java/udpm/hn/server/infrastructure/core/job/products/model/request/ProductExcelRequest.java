package udpm.hn.server.infrastructure.core.job.products.model.request;


import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductExcelRequest {

    private int orderNumber;
    private String sku;
    private String name;
    private String slug;
    private String shortDescription;
    private BigDecimal price;
    private Integer stockQuantity;
    private String brandName;
    private String categoryName;
}
