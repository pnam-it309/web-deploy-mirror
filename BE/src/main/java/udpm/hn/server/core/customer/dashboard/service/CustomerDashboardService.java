package udpm.hn.server.core.customer.dashboard.service;

import udpm.hn.server.core.customer.dashboard.model.response.CustomerDashboardCategoryResponse;
import udpm.hn.server.core.customer.dashboard.model.response.CustomerDashboardProductResponse;
import udpm.hn.server.core.customer.dashboard.model.response.CustomerDashboardStatsResponse;

import java.util.List;

public interface CustomerDashboardService {
    CustomerDashboardStatsResponse getStats(String email);
    List<CustomerDashboardProductResponse> getFeaturedProducts();
    List<CustomerDashboardCategoryResponse> getCategories();
}
