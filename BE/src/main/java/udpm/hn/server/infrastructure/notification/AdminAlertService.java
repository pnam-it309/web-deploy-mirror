package udpm.hn.server.infrastructure.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import udpm.hn.server.entity.App;
import udpm.hn.server.infrastructure.config.email.EmailService;
import udpm.hn.server.infrastructure.constant.ApprovalStatus;
import udpm.hn.server.repository.AppRepository;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminAlertService {

    private final EmailService emailService;
    private final AppRepository appRepository;
    private final SlackNotificationService slackNotificationService;

    @Value("${admin-alerts.email.enabled:true}")
    private boolean emailAlertsEnabled;

    @Value("${admin-alerts.email.recipients:admin@example.com}")
    private String adminEmails;

    @Value("${admin-alerts.slack.enabled:false}")
    private boolean slackAlertsEnabled;

    @Value("${spring.application.base-url:http://localhost:3000}")
    private String baseUrl;

    /**
     * Send immediate alert when a product is submitted for approval
     */
    public void notifyProductPendingApproval(App app) {
        log.info("Sending admin alert for pending product: {}", app.getName());

        if (emailAlertsEnabled) {
            sendEmailAlert(app);
        }

        if (slackAlertsEnabled) {
            sendSlackAlert(app);
        }
    }

    /**
     * Batch notification - runs every hour to summarize pending products
     */
    @Scheduled(cron = "${admin-alerts.batch-cron:0 0 * * * ?}") // Every hour
    public void sendBatchAlerts() {
        List<App> pendingApps = appRepository.findByApprovalStatus(ApprovalStatus.PENDING);

        if (pendingApps.isEmpty()) {
            return;
        }

        // Filter apps pending for more than 1 hour
        Instant oneHourAgo = Instant.now().minus(1, ChronoUnit.HOURS);
        List<App> needsAttention = pendingApps.stream()
                .filter(app -> app.getLastModifiedDate() != null &&
                        app.getLastModifiedDate() < oneHourAgo.toEpochMilli())
                .toList();

        if (!needsAttention.isEmpty()) {
            sendBatchEmailAlert(needsAttention);
            if (slackAlertsEnabled) {
                sendBatchSlackAlert(needsAttention);
            }
        }
    }

    private void sendEmailAlert(App app) {
        String[] recipients = adminEmails.split(",");

        for (String recipient : recipients) {
            try {
                Map<String, Object> variables = new HashMap<>();
                variables.put("appName", app.getName());
                variables.put("appId", app.getId());
                variables.put("summary", app.getShortDescription());
                variables.put("adminUrl", baseUrl + "/admin/apps/" + app.getId());
                variables.put("isBatch", false);

                emailService.sendEmail(
                        recipient.trim(),
                        "[Cần duyệt] Sản phẩm mới: " + app.getName(),
                        "email/admin-alert",
                        variables);
            } catch (Exception e) {
                log.error("Failed to send email alert to {}", recipient, e);
            }
        }
    }

    private void sendBatchEmailAlert(List<App> apps) {
        String[] recipients = adminEmails.split(",");

        for (String recipient : recipients) {
            try {
                Map<String, Object> variables = new HashMap<>();
                variables.put("apps", apps);
                variables.put("count", apps.size());
                variables.put("adminUrl", baseUrl + "/admin/apps");
                variables.put("isBatch", true);

                emailService.sendEmail(
                        recipient.trim(),
                        "[Tổng hợp] " + apps.size() + " sản phẩm đang chờ duyệt",
                        "email/admin-alert",
                        variables);
            } catch (Exception e) {
                log.error("Failed to send batch email alert to {}", recipient, e);
            }
        }
    }

    private void sendSlackAlert(App app) {
        try {
            String message = String.format(
                    "🔔 *Sản phẩm mới cần duyệt*\n\n" +
                            "📦 *Tên:* %s\n" +
                            "📝 *Mô tả:* %s\n" +
                            "🔗 *Link:* %s/admin/apps/%s",
                    app.getName(),
                    app.getShortDescription(),
                    baseUrl,
                    app.getId());
            slackNotificationService.sendMessage(message);
        } catch (Exception e) {
            log.error("Failed to send Slack alert", e);
        }
    }

    private void sendBatchSlackAlert(List<App> apps) {
        try {
            StringBuilder message = new StringBuilder("📊 *Tổng hợp sản phẩm chờ duyệt*\n\n");
            message.append(String.format("Có *%d* sản phẩm đang chờ duyệt:\n\n", apps.size()));

            for (App app : apps) {
                message.append(String.format("• %s\n", app.getName()));
            }

            message.append(String.format("\n🔗 Xem tất cả: %s/admin/apps", baseUrl));

            slackNotificationService.sendMessage(message.toString());
        } catch (Exception e) {
            log.error("Failed to send batch Slack alert", e);
        }
    }
}
