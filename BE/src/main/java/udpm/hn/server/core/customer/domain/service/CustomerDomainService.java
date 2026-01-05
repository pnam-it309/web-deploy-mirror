package udpm.hn.server.core.customer.domain.service;

import udpm.hn.server.core.customer.domain.model.response.CustomerDomainResponse;
import java.util.List;

public interface CustomerDomainService {
    List<CustomerDomainResponse> getAllActiveInfo();
}
