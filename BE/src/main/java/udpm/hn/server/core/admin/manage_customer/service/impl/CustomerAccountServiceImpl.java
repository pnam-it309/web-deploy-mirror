package udpm.hn.server.core.admin.manage_customer.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import udpm.hn.server.core.admin.manage_customer.reposiotry.CustomerSearchRepo;
import udpm.hn.server.core.admin.manage_customer.service.CustomerAccountService;
import udpm.hn.server.entity.Customer;
import udpm.hn.server.infrastructure.core.constant.EntityStatus;
import udpm.hn.server.core.admin.manage_customer.model.request.CustomerRequest;
import udpm.hn.server.core.admin.manage_customer.model.response.CustomerResponse;

@Service
@RequiredArgsConstructor
public class CustomerAccountServiceImpl implements CustomerAccountService {

    private final CustomerSearchRepo customerRepository;

    @Override
    public Page<CustomerResponse> search(String search, EntityStatus status, Pageable pageable) {
        return customerRepository.search(search, status, pageable).map(this::toResponse);
    }

    @Override
    public CustomerResponse toggleStatus(String id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + id));
        if (customer.getStatus() == EntityStatus.ACTIVE) {
            customer.setStatus(EntityStatus.INACTIVE);
        } else {
            customer.setStatus(EntityStatus.ACTIVE);
        }
        return toResponse(customerRepository.save(customer));
    }

    @Override
    public CustomerResponse create(CustomerRequest request) {
        Customer c = new Customer();
        c.setCode(request.getCode());
        c.setName(request.getName());
        c.setEmail(request.getEmail());
        c.setPicture(request.getPicture());
        c.setStatus(EntityStatus.ACTIVE);
        return toResponse(customerRepository.save(c));
    }

    @Override
    public CustomerResponse update(String id, CustomerRequest request) {
        Customer c = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + id));
        c.setCode(request.getCode());
        c.setName(request.getName());
        c.setEmail(request.getEmail());
        c.setPicture(request.getPicture());
        return toResponse(customerRepository.save(c));
    }

    private CustomerResponse toResponse(Customer c) {
        CustomerResponse resp = new CustomerResponse();
        resp.setId(c.getId());
        resp.setCode(c.getCode());
        resp.setName(c.getName());
        resp.setEmail(c.getEmail());
        resp.setPicture(c.getPicture());
        resp.setStatus(c.getStatus());
        return resp;
    }
}
