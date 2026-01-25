package udpm.hn.server.infrastructure.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SlackNotificationService {

    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;

    @Value("${admin-alerts.slack.webhook-url:}")
    private String webhookUrl;

    /**
     * Send a message to Slack channel
     */
    public void sendMessage(String message) {
        if (webhookUrl == null || webhookUrl.isEmpty()) {
            log.warn("Slack webhook URL not configured, skipping notification");
            return;
        }

        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("text", message);
            payload.put("mrkdwn", true);

            String jsonPayload = objectMapper.writeValueAsString(payload);

            RequestBody body = RequestBody.create(
                    jsonPayload,
                    MediaType.parse("application/json"));

            Request request = new Request.Builder()
                    .url(webhookUrl)
                    .post(body)
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    log.error("Failed to send Slack notification: {}", response.code());
                } else {
                    log.info("Slack notification sent successfully");
                }
            }
        } catch (IOException e) {
            log.error("Error sending Slack notification", e);
        }
    }

    /**
     * Send a rich message with attachments to Slack
     */
    public void sendRichMessage(String text, Map<String, Object> attachment) {
        if (webhookUrl == null || webhookUrl.isEmpty()) {
            log.warn("Slack webhook URL not configured, skipping notification");
            return;
        }

        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("text", text);
            payload.put("attachments", new Object[] { attachment });

            String jsonPayload = objectMapper.writeValueAsString(payload);

            RequestBody body = RequestBody.create(
                    jsonPayload,
                    MediaType.parse("application/json"));

            Request request = new Request.Builder()
                    .url(webhookUrl)
                    .post(body)
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    log.error("Failed to send Slack rich message: {}", response.code());
                } else {
                    log.info("Slack rich message sent successfully");
                }
            }
        } catch (IOException e) {
            log.error("Error sending Slack rich message", e);
        }
    }
}
