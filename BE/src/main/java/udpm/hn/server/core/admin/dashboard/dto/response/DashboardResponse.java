package udpm.hn.server.core.admin.dashboard.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardResponse {
    private long totalApps;        // Tổng số dự án
    private long totalViews;       // Tổng lượt xem tất cả dự án
    private long totalDomains;     // Số lĩnh vực
    private long totalTechnologies;// Số công nghệ
    private long totalStudents;    // Số sinh viên tham gia (Customers)
}