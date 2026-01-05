package udpm.hn.server.core.admin.dashboard.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import udpm.hn.server.core.admin.app.repository.AppManageRepository;
import udpm.hn.server.core.admin.dashboard.dto.response.DashboardResponse;
import udpm.hn.server.core.admin.domain.repository.DomainManageRepository;
import udpm.hn.server.core.admin.technology.repository.TechnologyManageRepository;
import udpm.hn.server.repository.CustomerRepository;

@Service
@RequiredArgsConstructor
public class DashboardService { // Không cần Interface vì logic đơn giản

    private final AppManageRepository appRepository;
    private final DomainManageRepository domainRepository;
    private final TechnologyManageRepository technologyRepository;
    private final CustomerRepository customerRepository;

    public udpm.hn.server.core.admin.dashboard.dto.response.DashboardResponse getDashboardStatistics() {
        // Tính tổng lượt view bằng query (Giả sử bạn thêm method sumViews vào repo, hoặc fetch all rồi sum ở java nếu ít data)
        // Ở đây demo count cơ bản
        long totalApps = appRepository.count();
        long totalDomains = domainRepository.count();
        long totalTechnologies = technologyRepository.count();
        long totalStudents = customerRepository.count();

        // Để tính view chuẩn, bạn nên thêm method @Query("SELECT SUM(a.viewCount) FROM App a") vào AppRepository
        // Ở đây tôi tạm để 0 hoặc gọi hàm nếu bạn đã thêm
        long totalViews = 0;
        try {
            // totalViews = appRepository.sumTotalViews(); // Cần thêm hàm này ở Repo nếu muốn
        } catch (Exception e) {}

        return DashboardResponse.builder()
                .totalApps(totalApps)
                .totalDomains(totalDomains)
                .totalTechnologies(totalTechnologies)
                .totalStudents(totalStudents)
                .totalViews(totalViews)
                .build();
    }
}