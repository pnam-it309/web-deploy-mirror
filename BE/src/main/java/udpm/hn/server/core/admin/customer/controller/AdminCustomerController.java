package udpm.hn.server.core.admin.customer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import udpm.hn.server.repository.CustomerRepository; // Hoặc package chứa repo của bạn

@RestController
@RequestMapping(MappingConstants.API_ADMIN_PREFIX + "/customers")
@RequiredArgsConstructor
public class AdminCustomerController {

    private final CustomerRepository customerRepository;

    @GetMapping("/search")
    public ResponseEntity<?> searchCustomers(@RequestParam(name = "keyword", required = false, defaultValue = "") String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.ok(customerRepository.searchByEmailOrName(keyword.trim()));
    }
}