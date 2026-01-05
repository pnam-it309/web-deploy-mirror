package udpm.hn.server.core.admin.dashboard.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminDashboardResponse {
    private Long totalDomains;
    private Long totalApps;
    private Long totalFeatures;
    private Long totalActiveUsers;

    // Charts
    private java.util.Map<String, Long> domainDistribution;
    private java.util.Map<String, Long> statusDistribution;
    private java.util.List<AdminTopAppResponse> topViewedApps;
}
