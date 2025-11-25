package udpm.hn.server.core.admin.quotes.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import udpm.hn.server.infrastructure.core.constant.EntityStatus;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuotesSummaryResponse {
    private String id;
    private String orderNumber;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private EntityStatus status;
    private String statusText;
    private BigDecimal totalEstimated;
    private Integer itemCount;
    private Long createdAt;
    private Long updatedAt;
}
