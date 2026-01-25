package udpm.hn.server.core.admin.webhook.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import udpm.hn.server.infrastructure.webhook.Webhook;
import udpm.hn.server.infrastructure.webhook.WebhookRepository;
import udpm.hn.server.utils.Helper;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(MappingConstants.API_ADMIN_PREFIX + "/webhooks")
@RequiredArgsConstructor
public class AdminWebhookController {

    private final WebhookRepository webhookRepository;

    @GetMapping
    public ResponseEntity<?> getAll() {
        return Helper.createResponseEntity(ResponseObject.successForward(
                webhookRepository.findAll(),
                "Lấy danh sách webhooks thành công"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        return webhookRepository.findById(id)
                .<ResponseEntity<?>>map(w -> Helper.createResponseEntity(ResponseObject.successForward(w, "OK")))
                .orElseGet(() -> Helper.createResponseEntity(ResponseObject.errorForward("Webhook not found")));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> create(@RequestBody Webhook request) {
        // Generate secret if not provided
        if (request.getSecret() == null || request.getSecret().isEmpty()) {
            request.setSecret(UUID.randomUUID().toString().replace("-", ""));
        }

        Webhook saved = webhookRepository.save(request);
        return Helper.createResponseEntity(ResponseObject.successForward(saved, "Tạo webhook thành công"));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody Map<String, Object> updates) {
        Webhook webhook = webhookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Webhook not found"));

        if (updates.containsKey("name")) {
            webhook.setName((String) updates.get("name"));
        }
        if (updates.containsKey("url")) {
            webhook.setUrl((String) updates.get("url"));
        }
        if (updates.containsKey("events")) {
            webhook.setEvents((String) updates.get("events"));
        }
        if (updates.containsKey("isActive")) {
            webhook.setIsActive((Boolean) updates.get("isActive"));
        }

        Webhook saved = webhookRepository.save(webhook);
        return Helper.createResponseEntity(ResponseObject.successForward(saved, "Cập nhật thành công"));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> delete(@PathVariable String id) {
        webhookRepository.deleteById(id);
        return Helper.createResponseEntity(ResponseObject.successForward(null, "Xoá thành công"));
    }

    @PostMapping("/{id}/regenerate-secret")
    @Transactional
    public ResponseEntity<?> regenerateSecret(@PathVariable String id) {
        Webhook webhook = webhookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Webhook not found"));

        webhook.setSecret(UUID.randomUUID().toString().replace("-", ""));
        webhookRepository.save(webhook);

        return Helper.createResponseEntity(ResponseObject.successForward(
                Map.of("secret", webhook.getSecret()),
                "Secret regenerated"));
    }

    @PostMapping("/{id}/reset-failures")
    @Transactional
    public ResponseEntity<?> resetFailures(@PathVariable String id) {
        Webhook webhook = webhookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Webhook not found"));

        webhook.setFailureCount(0);
        webhook.setIsActive(true);
        webhookRepository.save(webhook);

        return Helper.createResponseEntity(ResponseObject.successForward(null, "Reset thành công"));
    }
}
