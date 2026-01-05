package udpm.hn.server.core.customer.app.service;

import udpm.hn.server.core.common.base.PageableObject;
import udpm.hn.server.core.customer.app.model.request.CustomerAppFilterRequest;
import udpm.hn.server.core.customer.app.model.response.CustomerAppResponse;

public interface CustomerAppService {
    /**
     * Filter apps with basic LIKE search
     */
    PageableObject<CustomerAppResponse> filter(CustomerAppFilterRequest request);

    /**
     * Full-text search using MySQL MATCH AGAINST
     */
    PageableObject<CustomerAppResponse> fullTextSearch(String domainId, String query, int page, int size);

    /**
     * Get featured apps for homepage
     */
    java.util.List<CustomerAppResponse> getFeaturedApps();

    /**
     * Get apps sorted by specific criteria
     */
    PageableObject<CustomerAppResponse> getAppsSorted(String domainId, String sortBy, int page, int size);

    /**
     * Get app detail
     */
    CustomerAppResponse getDetail(String id);

    /**
     * Increment view count
     */
    void incrementViewCount(String id);
}
