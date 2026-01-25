package udpm.hn.server.infrastructure.webhook;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebhookService {

    private final WebhookRepository webhookRepository;
    private final ObjectMapper objectMapper;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    @Async
    public void trigger(String event, Object payload) {
        List<Webhook> webhooks = webhookRepository.findActiveByEvent(event);

        for (Webhook webhook : webhooks) {
            try {
                sendWebhook(webhook, event, payload);

                // Update last triggered
                webhook.setLastTriggeredAt(System.currentTimeMillis());
                webhook.setFailureCount(0);
                webhookRepository.save(webhook);

                log.info("Webhook triggered successfully: {} -> {}", event, webhook.getUrl());
            } catch (Exception e) {
                log.error("Webhook failed: {} -> {} - {}", event, webhook.getUrl(), e.getMessage());

                // Increment failure count
                webhook.setFailureCount(webhook.getFailureCount() + 1);

                // Disable after 5 consecutive failures
                if (webhook.getFailureCount() >= 5) {
                    webhook.setIsActive(false);
                    log.warn("Webhook disabled after 5 failures: {}", webhook.getUrl());
                }

                webhookRepository.save(webhook);
            }
        }
    }

    private void sendWebhook(Webhook webhook, String event, Object payload) throws Exception {
        Map<String, Object> body = Map.of(
                "event", event,
                "timestamp", System.currentTimeMillis(),
                "data", payload);

        String jsonBody = objectMapper.writeValueAsString(body);

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(webhook.getUrl()))
                .timeout(Duration.ofSeconds(30))
                .header("Content-Type", "application/json")
                .header("X-Webhook-Event", event)
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody));

        // Add HMAC signature if secret is configured
        if (webhook.getSecret() != null && !webhook.getSecret().isEmpty()) {
            String signature = calculateHmac(jsonBody, webhook.getSecret());
            requestBuilder.header("X-Webhook-Signature", "sha256=" + signature);
        }

        HttpResponse<String> response = httpClient.send(
                requestBuilder.build(),
                HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 400) {
            throw new RuntimeException("Webhook returned error status: " + response.statusCode());
        }
    }

    private String calculateHmac(String data, String secret) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(secretKeySpec);
        byte[] hmacBytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hmacBytes);
    }

    // Convenience methods for common events
    public void onAppCreated(Object app) {
        trigger(Webhook.EVENT_APP_CREATED, app);
    }

    public void onAppUpdated(Object app) {
        trigger(Webhook.EVENT_APP_UPDATED, app);
    }

    public void onAppDeleted(String appId) {
        trigger(Webhook.EVENT_APP_DELETED, Map.of("id", appId));
    }

    public void onAppPublished(Object app) {
        trigger(Webhook.EVENT_APP_PUBLISHED, app);
    }
}
