package udpm.hn.server.infrastructure.integration.notification;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class OneSignalService {

    @Value("${onesignal.app-id}")
    private String oneSignalAppId;

    @Value("${onesignal.api-key}")
    private String oneSignalApiKey;

    @Value("${onesignal.api-url:https://onesignal.com/api/v1/notifications}")
    private String oneSignalApiUrl;

    private final OkHttpClient httpClient = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    public void sendNotification(String title, String message, String playerId) throws IOException {
        NotificationRequest request = new NotificationRequest(
                oneSignalAppId,
                new NotificationRequest.NotificationContent(title, message),
                Collections.singletonList(playerId)
        );

        String jsonBody = objectMapper.writeValueAsString(request);

        RequestBody body = RequestBody.create(
                jsonBody,
                MediaType.parse("application/json; charset=utf-8")
        );

        Request httpRequest = new Request.Builder()
                .url(oneSignalApiUrl)
                .addHeader("Authorization", "Basic " + oneSignalApiKey)
                .post(body)
                .build();

        try (Response response = httpClient.newCall(httpRequest).execute()) {
            if (!response.isSuccessful()) {
                log.error("Failed to send OneSignal notification: {}", response.body() != null ? response.body().string() : "No response body");
                throw new IOException("Unexpected response code: " + response.code());
            }
            log.info("Successfully sent OneSignal notification");
        }
    }

    @lombok.Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class NotificationRequest {
        private final String app_id;
        private final NotificationContent contents;
        private final java.util.List<String> include_player_ids;
        private final String name = "INTERNAL_CAMPAIGN_NAME";

        @lombok.Data
        public static class NotificationContent {
            private final String en;

            public NotificationContent(String title, String message) {
                this.en = title + "\n" + message;
            }
        }
    }
}
