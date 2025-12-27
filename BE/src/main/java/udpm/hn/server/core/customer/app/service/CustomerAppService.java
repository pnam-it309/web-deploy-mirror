package udpm.hn.server.core.customer.app.service;

import udpm.hn.server.core.common.base.PageableObject;
import udpm.hn.server.core.customer.app.model.request.CustomerAppFilterRequest;
import udpm.hn.server.core.customer.app.model.response.CustomerAppResponse;

public interface CustomerAppService {
    PageableObject<CustomerAppResponse> filter(CustomerAppFilterRequest request);

    CustomerAppResponse getDetail(String id);

    void incrementViewCount(String id);

}
