package udpm.hn.server.core.customer.dashboard.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import udpm.hn.server.core.customer.dashboard.model.response.CustomerDashboardCategoryResponse;
import udpm.hn.server.core.customer.dashboard.model.response.CustomerDashboardProductResponse;
import udpm.hn.server.core.customer.dashboard.model.response.CustomerDashboardStatsResponse;
import udpm.hn.server.core.customer.dashboard.repository.CustomerDashboardRepository;
import udpm.hn.server.core.customer.dashboard.service.CustomerDashboardService;
import udpm.hn.server.core.customer.order.repository.CustomerOrderRepository;
import udpm.hn.server.infrastructure.core.constant.EntityStatus;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerDashboardServiceImpl implements CustomerDashboardService {

    private final CustomerDashboardRepository customerDashboardRepository;
    private final CustomerOrderRepository customerOrderRepository;

    @Override
    public CustomerDashboardStatsResponse getStats(String email) {
        Long orderCount = customerOrderRepository.countByCustomerEmail(email);
        return new CustomerDashboardStatsResponse(orderCount, 5L); // 5 offers hardcoded as per UI
    }

    @Override
    public List<CustomerDashboardProductResponse> getFeaturedProducts() {
        return customerDashboardRepository.getFeaturedProducts(EntityStatus.ACTIVE, PageRequest.of(0, 8));
    }

    @Override
    public List<CustomerDashboardCategoryResponse> getCategories() {
        return customerDashboardRepository.getCategoriesWithCount(EntityStatus.ACTIVE);
    }
}
