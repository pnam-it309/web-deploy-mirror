package udpm.hn.server.core.admin.domain.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import udpm.hn.server.core.admin.domain.dto.request.DomainRequest;
import udpm.hn.server.core.admin.domain.service.DomainService;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import jakarta.validation.Valid;

@RestController
@RequestMapping(MappingConstants.API_ADMIN_DOMAIN)
@RequiredArgsConstructor
public class DomainController {

    private final DomainService domainService;

    @GetMapping
    public ResponseEntity<?> getAllDomains(
            @RequestParam(required = false, defaultValue = "false") boolean unpaged,
            @PageableDefault(size = 10) Pageable pageable) {
        if (unpaged) {
            return ResponseEntity.ok(domainService.getAllDomains()); // Cho dropdown
        }
        return ResponseEntity.ok(domainService.getDomains(pageable)); // Cho table
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDomainById(@PathVariable String id) {
        return ResponseEntity.ok(domainService.getDomainById(id));
    }

    @PostMapping
    public ResponseEntity<?> createDomain(@Valid @RequestBody DomainRequest request) {
        return ResponseEntity.ok(domainService.createDomain(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDomain(@PathVariable String id, @Valid @RequestBody DomainRequest request) {
        return ResponseEntity.ok(domainService.updateDomain(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDomain(@PathVariable String id) {
        domainService.deleteDomain(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/bulk-update-order")
    public ResponseEntity<Void> bulkUpdateOrder(
            @RequestBody java.util.List<udpm.hn.server.core.admin.domain.dto.request.DomainOrderRequest> requests) {
        domainService.bulkUpdateOrder(requests);
        return ResponseEntity.ok().build();
    }
}