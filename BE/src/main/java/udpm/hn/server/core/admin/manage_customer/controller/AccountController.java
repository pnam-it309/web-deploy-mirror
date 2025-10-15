package udpm.hn.server.core.admin.manage_customer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import udpm.hn.server.core.admin.manage_customer.service.CustomerAccountService;
import udpm.hn.server.infrastructure.core.constant.MappingConstants;
import udpm.hn.server.infrastructure.core.constant.EntityStatus;
import udpm.hn.server.core.admin.manage_customer.model.request.CustomerRequest;
import udpm.hn.server.core.admin.manage_customer.model.response.CustomerResponse;

@RequestMapping(MappingConstants.API_ADMIN_CUSTOMER)
@RequiredArgsConstructor
@RestController
public class AccountController {
    private final CustomerAccountService customerAccountService;

    @GetMapping
    public ResponseEntity<Page<CustomerResponse>> getCustomers(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        EntityStatus entityStatus = null;
        if (status != null && !status.isBlank()) {
            try {
                entityStatus = EntityStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException ex) {
                return ResponseEntity.badRequest().build();
            }
        }
        Page<CustomerResponse> result = customerAccountService.search(search, entityStatus, PageRequest.of(page, size));
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<CustomerResponse> create(@RequestBody CustomerRequest request) {
        return ResponseEntity.ok(customerAccountService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> update(@PathVariable("id") String id, @RequestBody CustomerRequest request) {
        return ResponseEntity.ok(customerAccountService.update(id, request));
    }

    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<CustomerResponse> toggleStatus(@PathVariable("id") String id) {
        return ResponseEntity.ok(customerAccountService.toggleStatus(id));
    }
}
