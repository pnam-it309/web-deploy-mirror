package udpm.hn.server.core.admin.quotes.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuotesItemResponse {
    private String id;
    private String productId;
    private String productNameSnapshot;
    private BigDecimal unitPrice;
    private Integer quantity;
    private String unitSnapshot;
    private String notes;
    private BigDecimal total;
    private Long createdAt;
    private Long updatedAt;
}
