package udpm.hn.server.core.admin.dashboard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RevenueChartResponse {
    private List<String> labels;
    private List<BigDecimal> revenue;
    private List<Long> orders;
}