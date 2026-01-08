package udpm.hn.server.core.admin.feature.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import udpm.hn.server.core.admin.feature.dto.request.FeatureRequest;
import udpm.hn.server.core.admin.feature.service.FeatureService;
import udpm.hn.server.infrastructure.constant.MappingConstants;

@RestController
@RequestMapping(MappingConstants.API_ADMIN_FEATURE)
@RequiredArgsConstructor
public class FeatureController {

    private final FeatureService featureService;

    // --- THÊM ĐOẠN NÀY ĐỂ HẾT LỖI 500 GET METHOD ---
    @GetMapping
    public ResponseEntity<?> getAllFeatures() {
        return ResponseEntity.ok(featureService.getAllFeatures());
    }
    // -----------------------------------------------

    // Lấy danh sách tính năng của 1 App cụ thể
    @GetMapping("/by-app/{appId}")
    public ResponseEntity<?> getFeaturesByApp(@PathVariable String appId) {
        return ResponseEntity.ok(featureService.getFeaturesByAppId(appId));
    }

    @PostMapping
    public ResponseEntity<?> createFeature(@RequestBody FeatureRequest request) {
        return ResponseEntity.ok(featureService.createFeature(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateFeature(@PathVariable String id, @RequestBody FeatureRequest request) {
        return ResponseEntity.ok(featureService.updateFeature(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFeature(@PathVariable String id) {
        featureService.deleteFeature(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/bulk-update-order")
    public ResponseEntity<Void> bulkUpdateOrder(
            @RequestBody java.util.List<udpm.hn.server.core.admin.feature.dto.request.FeatureOrderRequest> requests) {
        featureService.bulkUpdateOrder(requests);
        return ResponseEntity.ok().build();
    }
}