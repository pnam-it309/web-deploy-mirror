package udpm.hn.server.core.customer.dashboard.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDashboardStatsResponse {
    private Long orderCount;
    private Long offerCount; // Mock for now
}
