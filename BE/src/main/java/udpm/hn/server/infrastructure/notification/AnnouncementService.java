package udpm.hn.server.infrastructure.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import udpm.hn.server.entity.App;
import udpm.hn.server.entity.PushSubscription;
import udpm.hn.server.repository.PushSubscriptionRepository;

import java.security.Security;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnnouncementService {

    private final PushSubscriptionRepository pushSubscriptionRepository;
    private final ObjectMapper objectMapper;

    @Value("${vapid.public.key}")
    private String vapidPublicKey;

    @Value("${vapid.private.key}")
    private String vapidPrivateKey;

    @Value("${spring.application.base-url:http://localhost:6789}")
    private String baseUrl;

    /**
     * Send push notification when new product is approved
     */
    public void sendNewProductNotification(App app) {
        log.info("Sending push notifications for new product: {}", app.getName());

        String title = "Sản phẩm mới: " + app.getName();
        String body = app.getShortDescription() != null ? app.getShortDescription() : "Xem chi tiết sản phẩm mới";
        String url = baseUrl + "/apps/" + app.getId();

        sendNotificationToAll(title, body, url, app.getThumbnail());
    }

    /**
     * Send custom announcement to all subscribers
     */
    public void sendCustomAnnouncement(String title, String body, String url) {
        log.info("Sending custom announcement: {}", title);
        sendNotificationToAll(title, body, url, null);
    }

    /**
     * Send push notification to all active subscriptions
     */
    private void sendNotificationToAll(String title, String body, String url, String icon) {
        List<PushSubscription> subscriptions = pushSubscriptionRepository.findByIsActiveTrue();

        if (subscriptions.isEmpty()) {
            log.warn("No active push subscriptions found");
            return;
        }

        // Add BouncyCastle as Security Provider
        Security.addProvider(new BouncyCastleProvider());

        try {
            PushService pushService = new PushService();
            pushService.setPublicKey(vapidPublicKey);
            pushService.setPrivateKey(vapidPrivateKey);
            pushService.setSubject("mailto:admin@example.com");

            Map<String, Object> payload = new HashMap<>();
            payload.put("title", title);
            payload.put("body", body);
            payload.put("url", url);
            if (icon != null) {
                payload.put("icon", icon);
            }

            String payloadJson = objectMapper.writeValueAsString(payload);

            int successCount = 0;
            int failureCount = 0;

            for (PushSubscription subscription : subscriptions) {
                try {
                    nl.martijndwars.webpush.Subscription webPushSubscription = new nl.martijndwars.webpush.Subscription(
                            subscription.getEndpoint(),
                            new nl.martijndwars.webpush.Subscription.Keys(
                                    subscription.getP256dhKey(),
                                    subscription.getAuthKey()));

                    Notification notification = new Notification(
                            webPushSubscription,
                            payloadJson);

                    pushService.send(notification);
                    successCount++;

                    // Update last used timestamp
                    subscription.setLastUsedAt(System.currentTimeMillis());
                    pushSubscriptionRepository.save(subscription);

                } catch (Exception e) {
                    log.error("Failed to send push notification to subscription {}: {}",
                            subscription.getId(), e.getMessage());
                    failureCount++;

                    // Deactivate subscription if endpoint is invalid (410 Gone or 404 Not Found)
                    if (e.getMessage() != null &&
                            (e.getMessage().contains("410") || e.getMessage().contains("404"))) {
                        subscription.setIsActive(false);
                        pushSubscriptionRepository.save(subscription);
                        log.info("Deactivated invalid subscription: {}", subscription.getId());
                    }
                }
            }

            log.info("Push notifications sent: {} successful, {} failed", successCount, failureCount);

        } catch (Exception e) {
            log.error("Failed to initialize push service", e);
        }
    }

    /**
     * Send notification to specific domain followers
     */
    public void sendToDomainFollowers(String domainId, String title, String body, String url) {
        // This would require a domain_subscriptions table to track which users follow
        // which domains
        // For now, send to all as a simplified implementation
        log.info("Sending to domain {} followers (not yet implemented, sending to all)", domainId);
        sendNotificationToAll(title, body, url, null);
    }
}
