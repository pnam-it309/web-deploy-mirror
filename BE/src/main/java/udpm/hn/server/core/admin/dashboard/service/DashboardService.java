package udpm.hn.server.core.admin.dashboard.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import udpm.hn.server.core.admin.app.repository.AppManageRepository;
import udpm.hn.server.core.admin.dashboard.dto.response.DashboardResponse;
import udpm.hn.server.core.admin.domain.repository.DomainManageRepository;
import udpm.hn.server.core.admin.technology.repository.TechnologyManageRepository;
import udpm.hn.server.core.admin.feature.repository.FeatureManageRepository;
import udpm.hn.server.repository.CustomerRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardService { // Không cần Interface vì logic đơn giản

    private final AppManageRepository appRepository;
    private final DomainManageRepository domainRepository;
    private final TechnologyManageRepository technologyRepository;
    private final FeatureManageRepository featureRepository;
    private final CustomerRepository customerRepository;
    private final udpm.hn.server.core.admin.app.repository.AppMemberManageRepository appMemberRepository;

    @Cacheable(value = "dashboard:stats", unless = "#result == null")
    public udpm.hn.server.core.admin.dashboard.dto.response.DashboardResponse getDashboardStatistics() {
        log.debug("Cache MISS: Calculating dashboard statistics from database");
        // Tính tổng lượt view bằng query (Giả sử bạn thêm method sumViews vào repo,
        // hoặc fetch all rồi sum ở java nếu ít data)
        // Ở đây demo count cơ bản
        long totalApps = appRepository.count();
        long totalDomains = domainRepository.count();
        long totalTechnologies = technologyRepository.count();
        long totalStudents = appMemberRepository.countUniqueMembers();
        long totalFeatures = featureRepository.count();

        // View count sum
        Long sumViews = appRepository.sumTotalViews();
        long totalViews = (sumViews != null) ? sumViews : 0L;

        log.debug("Dashboard stats calculated: apps={}, domains={}, tech={}",
                totalApps, totalDomains, totalTechnologies);

        return DashboardResponse.builder()
                .totalApps(totalApps)
                .totalDomains(totalDomains)
                .totalTechnologies(totalTechnologies)
                .totalStudents(totalStudents)
                .totalViews(totalViews)
                .totalFeatures(totalFeatures)
                .build();
    }
}