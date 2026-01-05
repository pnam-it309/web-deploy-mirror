package udpm.hn.server.core.admin.dashboard.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import udpm.hn.server.core.admin.app.repository.AdminAppRepository;
import udpm.hn.server.core.admin.app.repository.AdminCustomerRepository;
import udpm.hn.server.core.admin.dashboard.model.response.AdminDashboardResponse;
import udpm.hn.server.core.admin.dashboard.model.response.AdminTopAppResponse;
import udpm.hn.server.core.admin.dashboard.service.AdminDashboardService;
import udpm.hn.server.core.admin.domain.repository.AdminDomainRepository;
import udpm.hn.server.core.admin.feature.repository.AdminFeatureRepository;

@Service
@RequiredArgsConstructor
public class AdminDashboardServiceImpl implements AdminDashboardService {

        private final AdminDomainRepository domainRepository;
        private final AdminAppRepository appRepository;
        private final AdminFeatureRepository featureRepository;
        private final AdminCustomerRepository customerRepository;

        @Override
        @Transactional(readOnly = true)
        public AdminDashboardResponse getDashboardStats() {
                AdminDashboardResponse response = new AdminDashboardResponse();

                // Counts
                response.setTotalDomains(domainRepository.count());
                response.setTotalApps(appRepository.count());
                response.setTotalFeatures(featureRepository.count());
                response.setTotalActiveUsers(customerRepository.count());

                // Domain Distribution
                java.util.Map<String, Long> domainDist = appRepository.findAll().stream()
                                .filter(a -> a.getDomain() != null)
                                .collect(java.util.stream.Collectors.groupingBy(
                                                a -> a.getDomain().getName(),
                                                java.util.stream.Collectors.counting()));
                response.setDomainDistribution(domainDist);

                // Status Distribution
                java.util.Map<String, Long> statusDist = appRepository.findAll().stream()
                                .filter(a -> a.getApprovalStatus() != null)
                                .collect(java.util.stream.Collectors.groupingBy(
                                                a -> a.getApprovalStatus().name(),
                                                java.util.stream.Collectors.counting()));
                response.setStatusDistribution(statusDist);

                // Top Viewed (Need custom query or just sort all if small dataset. Assuming
                // small for MVP)
                // Ideally should use repository query `findTop5ByOrderByViewCountDesc`
                // I will use stream for now if repo method missing, or add repo method.
                // Let's use stream for safety as I can't check repo easily right now without
                // view_file.
                // Wait, appRepository is AdminAppRepository -> JpaRepository.

                response.setTopViewedApps(appRepository.findAll(Sort.by(Sort.Direction.DESC, "viewCount"))
                                .stream()
                                .limit(5)
                                .map(a -> new AdminTopAppResponse(
                                                a.getId(), a.getName(), a.getViewCount(), a.getThumbnail()))
                                .collect(java.util.stream.Collectors.toList()));

                return response;
        }
}
