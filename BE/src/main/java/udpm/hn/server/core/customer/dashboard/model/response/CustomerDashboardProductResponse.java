package udpm.hn.server.core.customer.dashboard.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDashboardProductResponse {
    private String id;
    private String name;
    private BigDecimal price;
    private String categoryName;
    private String imageUrl;
    private String description;
    private Integer reviewCount; // Mock or real
    private Double rating; // Mock or real
    private String badge; // Mock or real
}
