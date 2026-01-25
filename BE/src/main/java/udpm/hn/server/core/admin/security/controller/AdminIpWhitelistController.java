package udpm.hn.server.core.admin.security.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.entity.IpWhitelist;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import udpm.hn.server.repository.IpWhitelistRepository;
import udpm.hn.server.utils.Helper;

import java.util.Map;

@RestController
@RequestMapping(MappingConstants.API_ADMIN_SECURITY)
@RequiredArgsConstructor
public class AdminIpWhitelistController {

    private final IpWhitelistRepository ipWhitelistRepository;

    @GetMapping
    public ResponseEntity<?> getAll() {
        return Helper.createResponseEntity(ResponseObject.successForward(
                ipWhitelistRepository.findAll(),
                "Lấy danh sách IP whitelist thành công"));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> add(@RequestBody Map<String, Object> request) {
        String ipAddress = (String) request.get("ipAddress");
        String description = (String) request.get("description");
        Long expiresAt = request.containsKey("expiresAt") ? ((Number) request.get("expiresAt")).longValue() : null;

        if (ipAddress == null || ipAddress.isEmpty()) {
            return Helper.createResponseEntity(ResponseObject.errorForward("IP address is required"));
        }

        if (ipWhitelistRepository.existsByIpAddress(ipAddress)) {
            return Helper.createResponseEntity(ResponseObject.errorForward("IP already in whitelist"));
        }

        IpWhitelist whitelist = IpWhitelist.builder()
                .ipAddress(ipAddress)
                .description(description)
                .expiresAt(expiresAt)
                .isActive(true)
                .build();

        IpWhitelist saved = ipWhitelistRepository.save(whitelist);
        return Helper.createResponseEntity(ResponseObject.successForward(saved, "Thêm IP thành công"));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody Map<String, Object> request) {
        IpWhitelist whitelist = ipWhitelistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("IP not found"));

        if (request.containsKey("description")) {
            whitelist.setDescription((String) request.get("description"));
        }
        if (request.containsKey("isActive")) {
            whitelist.setIsActive((Boolean) request.get("isActive"));
        }
        if (request.containsKey("expiresAt")) {
            whitelist.setExpiresAt(
                    request.get("expiresAt") != null ? ((Number) request.get("expiresAt")).longValue() : null);
        }

        IpWhitelist saved = ipWhitelistRepository.save(whitelist);
        return Helper.createResponseEntity(ResponseObject.successForward(saved, "Cập nhật thành công"));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> delete(@PathVariable String id) {
        ipWhitelistRepository.deleteById(id);
        return Helper.createResponseEntity(ResponseObject.successForward(null, "Xoá thành công"));
    }

    @GetMapping("/check")
    public ResponseEntity<?> checkIp(@RequestParam String ip) {
        boolean isAllowed = ipWhitelistRepository.findActiveNonExpired(System.currentTimeMillis())
                .stream()
                .anyMatch(w -> matchIp(ip, w.getIpAddress()));

        return Helper.createResponseEntity(ResponseObject.successForward(
                Map.of("ip", ip, "allowed", isAllowed),
                "Check completed"));
    }

    private boolean matchIp(String clientIp, String whitelistEntry) {
        // Simple match - can extend for CIDR support
        if (whitelistEntry.contains("/")) {
            // CIDR notation - simplified check
            String[] parts = whitelistEntry.split("/");
            return clientIp.startsWith(parts[0].substring(0, parts[0].lastIndexOf('.')));
        }
        return clientIp.equals(whitelistEntry);
    }
}
