package udpm.hn.server.core.customer.ViewProduct.model.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import udpm.hn.server.entity.Product;
import udpm.hn.server.entity.ProductDetail;
import udpm.hn.server.entity.Image;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailResponse {
    private String id;
    private String name;
    private String description;
    private String fullDescription;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private BigDecimal youSave;
    private List<String> images;
    private Double rating;
    private Integer reviewCount;
    private Boolean inStock;
    private Integer inventory;
    private String sku;
    private Boolean onSale;
    private Map<String, String> specifications;

    public ProductDetailResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getShortDescription();
        this.price = product.getPrice();
        this.inventory = product.getStockQuantity();
        this.inStock = product.getStockQuantity() > 0;
        this.sku = product.getSku();
        
        // Default values for fields not yet in DB or mocked
        this.rating = 4.5; // Mock
        this.reviewCount = 10; // Mock
        this.originalPrice = product.getPrice().multiply(new BigDecimal("1.2")); // Mock 20% markup
        this.youSave = this.originalPrice.subtract(this.price);
        this.onSale = true; // Mock

        if (product.getProductDetail() != null) {
            ProductDetail detail = product.getProductDetail();
            this.fullDescription = detail.getLongDescription();
            
            this.images = detail.getImages().stream()
                    .map(Image::getUrl)
                    .collect(Collectors.toList());
            
            // Handle specifications from JSON if needed, or mock for now
            this.specifications = new HashMap<>();
            if (detail.getSpecification() != null) {
                // simple conversion if it's a flat JSON object
                // For now, let's just put some dummy data or parse if we had a utility
                this.specifications.put("Brand", product.getBrand() != null ? product.getBrand().getName() : "Unknown");
                this.specifications.put("Category", product.getCategory() != null ? product.getCategory().getName() : "Unknown");
                this.specifications.put("Weight", detail.getWeight() != null ? detail.getWeight().toString() + " kg" : "N/A");
            }
        } else {
            this.images = new ArrayList<>();
            this.specifications = new HashMap<>();
        }
    }
}
