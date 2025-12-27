package udpm.hn.server.core.admin.dashboard.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import udpm.hn.server.core.admin.dashboard.model.response.AdminDashboardResponse;
import udpm.hn.server.core.admin.dashboard.service.AdminDashboardService;
import udpm.hn.server.repository.AppRepository;
import udpm.hn.server.repository.CustomerRepository;
import udpm.hn.server.repository.DomainRepository;
import udpm.hn.server.repository.FeatureRepository;

@Service
@RequiredArgsConstructor
public class AdminDashboardServiceImpl implements AdminDashboardService {

    private final DomainRepository domainRepository;
    private final AppRepository appRepository;
    private final FeatureRepository featureRepository;
    private final CustomerRepository customerRepository;

    @Override
    public AdminDashboardResponse getDashboardStats() {
        AdminDashboardResponse response = new AdminDashboardResponse();
        response.setTotalDomains(domainRepository.count());
        response.setTotalApps(appRepository.count());
        response.setTotalFeatures(featureRepository.count());
        response.setTotalActiveUsers(customerRepository.count());
        return response;
    }
}
