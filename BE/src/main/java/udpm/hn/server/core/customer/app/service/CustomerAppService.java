package udpm.hn.server.core.customer.app.service;

import udpm.hn.server.core.common.base.PageableObject;
import udpm.hn.server.core.customer.app.model.request.CustomerAppFilterRequest;
import udpm.hn.server.core.customer.app.model.response.CustomerAppResponse;

import java.util.List;

public interface CustomerAppService {
    PageableObject<CustomerAppResponse> filter(CustomerAppFilterRequest request);

    CustomerAppResponse getDetail(String id);

    void incrementViewCount(String id);

    /**
     * Find related apps based on domain and technologies
     * 
     * @param appId Current app ID to exclude from results
     * @param limit Maximum number of related apps to return
     * @return List of related apps
     */
    List<CustomerAppResponse> findRelatedApps(String appId, int limit);

    List<CustomerAppResponse> getFeaturedVideos();
}
