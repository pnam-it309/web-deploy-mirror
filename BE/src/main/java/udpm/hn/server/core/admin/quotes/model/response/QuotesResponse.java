package udpm.hn.server.core.admin.quotes.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import udpm.hn.server.infrastructure.core.constant.EntityStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuotesResponse {
    private String id;
    private String orderNumber;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String customerCompany;
    private String notes;
    private EntityStatus status;
    private String statusText;
    private BigDecimal totalEstimated;
    @Builder.Default
    private List<QuotesItemResponse> items = new ArrayList<>();
    private Long createdAt;
    private Long updatedAt;
}
