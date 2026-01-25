package udpm.hn.server.core.customer.subscription.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.entity.EmailSubscription;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import udpm.hn.server.repository.EmailSubscriptionRepository;
import udpm.hn.server.utils.Helper;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(MappingConstants.API_CUSTOMER_SUBCRIPTION)
@RequiredArgsConstructor
public class CustomerSubscriptionController {

    private final EmailSubscriptionRepository subscriptionRepository;
    private final udpm.hn.server.infrastructure.config.email.EmailService emailService;

    @org.springframework.beans.factory.annotation.Value("${frontend.url}")
    private String frontendUrl;

    @PostMapping
    @Transactional
    public ResponseEntity<?> subscribe(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String name = request.get("name");

        if (email == null || !email.contains("@")) {
            return Helper.createResponseEntity(ResponseObject.errorForward("Email không hợp lệ"));
        }

        // Check if already subscribed
        if (subscriptionRepository.existsByEmail(email)) {
            return Helper.createResponseEntity(ResponseObject.errorForward("Email đã được đăng ký"));
        }

        EmailSubscription subscription = EmailSubscription.builder()
                .email(email)
                .name(name)
                .verificationToken(UUID.randomUUID().toString())
                .unsubscribeToken(UUID.randomUUID().toString())
                .subscribedAt(System.currentTimeMillis())
                .isVerified(false)
                .preferences("{\"newProducts\":true,\"updates\":true}")
                .build();

        subscriptionRepository.save(subscription);

        // Send verification email
        String verificationUrl = frontendUrl + "/verify-subscription?token=" + subscription.getVerificationToken();
        emailService.sendVerificationEmail(subscription.getEmail(), subscription.getName(), verificationUrl);

        return Helper.createResponseEntity(ResponseObject.successForward(
                Map.of("message", "Vui lòng kiểm tra email để xác nhận đăng ký"),
                "Đăng ký thành công"));
    }

    @GetMapping("/verify/{token}")
    @Transactional
    public ResponseEntity<?> verify(@PathVariable String token) {
        return subscriptionRepository.findByVerificationToken(token)
                .<ResponseEntity<?>>map(sub -> {
                    sub.setIsVerified(true);
                    sub.setVerificationToken(null);
                    subscriptionRepository.save(sub);
                    return Helper.createResponseEntity(ResponseObject.successForward(null, "Xác nhận thành công!"));
                })
                .orElseGet(() -> Helper.createResponseEntity(ResponseObject.errorForward("Token không hợp lệ")));
    }

    @GetMapping("/unsubscribe/{token}")
    @Transactional
    public ResponseEntity<?> unsubscribe(@PathVariable String token) {
        return subscriptionRepository.findByUnsubscribeToken(token)
                .<ResponseEntity<?>>map(sub -> {
                    subscriptionRepository.delete(sub);
                    return Helper.createResponseEntity(ResponseObject.successForward(null, "Huỷ đăng ký thành công"));
                })
                .orElseGet(() -> Helper.createResponseEntity(ResponseObject.errorForward("Token không hợp lệ")));
    }

    @PutMapping("/preferences/{token}")
    @Transactional
    public ResponseEntity<?> updatePreferences(
            @PathVariable String token,
            @RequestBody Map<String, Object> preferences) {
        return subscriptionRepository.findByUnsubscribeToken(token)
                .<ResponseEntity<?>>map(sub -> {
                    sub.setPreferences(preferences.toString());
                    subscriptionRepository.save(sub);
                    return Helper.createResponseEntity(ResponseObject.successForward(null, "Cập nhật thành công"));
                })
                .orElseGet(() -> Helper.createResponseEntity(ResponseObject.errorForward("Token không hợp lệ")));
    }
}
