package udpm.hn.server.core.admin.manage_customer.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import udpm.hn.server.infrastructure.core.constant.EntityStatus;
import udpm.hn.server.core.admin.manage_customer.model.request.CustomerRequest;
import udpm.hn.server.core.admin.manage_customer.model.response.CustomerResponse;

public interface CustomerAccountService {
    Page<CustomerResponse> search(String search, EntityStatus status, Pageable pageable);

    CustomerResponse create(CustomerRequest request);

    CustomerResponse update(String id, CustomerRequest request);

    CustomerResponse toggleStatus(String id);
}
