package udpm.hn.server.core.admin.manage_customer.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import udpm.hn.server.core.admin.manage_customer.dto.request.CustomerFilterRequest;
import udpm.hn.server.core.admin.manage_customer.dto.response.CustomerResponse;
import udpm.hn.server.core.admin.manage_customer.repository.CustomerManageRepository;
import udpm.hn.server.core.admin.manage_customer.repository.CustomerSpecification;
import udpm.hn.server.core.admin.manage_customer.service.CustomerService;
import udpm.hn.server.entity.Customer;
import udpm.hn.server.infrastructure.core.constant.EntityStatus;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerManageRepository customerRepository;
    private final ModelMapper modelMapper;

    @Override
    public Page<CustomerResponse> getAllCustomers(CustomerFilterRequest request, Pageable pageable) {
        Specification<Customer> spec = CustomerSpecification.getFilter(request);
        return customerRepository.findAll(spec, pageable)
                .map(customer -> modelMapper.map(customer, CustomerResponse.class));
    }

    @Override
    public CustomerResponse getCustomerById(String id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found: " + id));
        return modelMapper.map(customer, CustomerResponse.class);
    }

    @Override
    public void updateStatus(String id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found: " + id));

        // Toggle status (Nếu đang Active -> Inactive và ngược lại)
        if (customer.getStatus() == EntityStatus.ACTIVE) {
            customer.setStatus(EntityStatus.INACTIVE);
        } else {
            customer.setStatus(EntityStatus.ACTIVE);
        }
        customerRepository.save(customer);
    }
}