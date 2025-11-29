package udpm.hn.server.core.admin.manage_customer.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import udpm.hn.server.core.admin.manage_customer.dto.request.CustomerFilterRequest;
import udpm.hn.server.core.admin.manage_customer.dto.request.CustomerRequest;
import udpm.hn.server.core.admin.manage_customer.dto.response.CustomerResponse;

public interface CustomerService {
    Page<CustomerResponse> getAllCustomers(CustomerFilterRequest request, Pageable pageable);
    CustomerResponse getCustomerById(String id);
    void updateStatus(String id);
}
