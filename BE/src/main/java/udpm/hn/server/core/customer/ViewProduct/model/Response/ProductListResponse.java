package udpm.hn.server.core.customer.ViewProduct.model.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import udpm.hn.server.entity.Product;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductListResponse {
    private String id;
    private String name;
    private BigDecimal price;
    private String category;
    private String image;
    private String description;
    private Integer stockQuantity;
    private String status;
    private Boolean isFavorite;

    public ProductListResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.category = product.getCategory() != null ? product.getCategory().getName() : null;
        this.stockQuantity = product.getStockQuantity();
        this.status = product.getStatus() != null ? product.getStatus().name() : "ACTIVE";
        this.description = product.getShortDescription();
        
        // Handle image from ProductDetail if available
        if (product.getProductDetail() != null && !product.getProductDetail().getImages().isEmpty()) {
            this.image = product.getProductDetail().getImages().get(0).getUrl();
        } else {
            this.image = null; // Or default image
        }
    }
}
