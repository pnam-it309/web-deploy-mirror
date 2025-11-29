package udpm.hn.server.core.admin.manage_customer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import udpm.hn.server.core.admin.manage_customer.dto.request.CustomerFilterRequest;
import udpm.hn.server.core.admin.manage_customer.dto.response.CustomerResponse;
import udpm.hn.server.core.admin.manage_customer.service.CustomerService;
import udpm.hn.server.infrastructure.core.constant.MappingConstants;

@RestController
@RequestMapping(MappingConstants.API_ADMIN_CUSTOMER) // /admin/customers
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/get-all-customers")
    public ResponseEntity<Page<CustomerResponse>> getAllCustomers(
            @ModelAttribute CustomerFilterRequest request,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(customerService.getAllCustomers(request, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable String id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable String id) {
        customerService.updateStatus(id);
        return ResponseEntity.noContent().build();
    }
}