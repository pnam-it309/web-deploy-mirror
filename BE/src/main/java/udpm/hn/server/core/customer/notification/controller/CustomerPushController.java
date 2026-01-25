package udpm.hn.server.core.customer.notification.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.entity.PushSubscription;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import udpm.hn.server.repository.PushSubscriptionRepository;
import udpm.hn.server.utils.Helper;

import java.util.Map;

@RestController
@RequestMapping(MappingConstants.API_CUSTOMER_NOTIFICATION)
@RequiredArgsConstructor
public class CustomerPushController {

    private final PushSubscriptionRepository pushSubscriptionRepository;

    @PostMapping("/subscribe")
    @Transactional
    public ResponseEntity<?> subscribe(@RequestBody Map<String, Object> request) {
        String endpoint = (String) request.get("endpoint");

        @SuppressWarnings("unchecked")
        Map<String, String> keys = (Map<String, String>) request.get("keys");
        String p256dh = keys != null ? keys.get("p256dh") : null;
        String auth = keys != null ? keys.get("auth") : null;
        String userEmail = (String) request.get("userEmail");

        if (endpoint == null) {
            return Helper.createResponseEntity(ResponseObject.errorForward("Endpoint is required"));
        }

        // Check if already subscribed
        if (pushSubscriptionRepository.existsByEndpoint(endpoint)) {
            return Helper.createResponseEntity(ResponseObject.successForward(null, "Already subscribed"));
        }

        PushSubscription subscription = PushSubscription.builder()
                .endpoint(endpoint)
                .p256dhKey(p256dh)
                .authKey(auth)
                .userEmail(userEmail)
                .isActive(true)
                .lastUsedAt(System.currentTimeMillis())
                .build();

        pushSubscriptionRepository.save(subscription);

        return Helper.createResponseEntity(ResponseObject.successForward(null, "Đăng ký thông báo thành công"));
    }

    @PostMapping("/unsubscribe")
    @Transactional
    public ResponseEntity<?> unsubscribe(@RequestBody Map<String, String> request) {
        String endpoint = request.get("endpoint");

        pushSubscriptionRepository.findByEndpoint(endpoint)
                .ifPresent(pushSubscriptionRepository::delete);

        return Helper.createResponseEntity(ResponseObject.successForward(null, "Huỷ đăng ký thành công"));
    }

    @org.springframework.beans.factory.annotation.Value("${vapid.public.key}")
    private String publicKey;

    @GetMapping("/vapid-key")
    public ResponseEntity<?> getVapidKey() {
        return Helper.createResponseEntity(ResponseObject.successForward(
                Map.of("publicKey", publicKey),
                "OK"));
    }
}
