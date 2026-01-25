package udpm.hn.server.infrastructure.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import udpm.hn.server.core.admin.app.repository.AppManageRepository;
import udpm.hn.server.entity.App;
import udpm.hn.server.infrastructure.constant.ApprovalStatus;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduledTaskRunner {

    private final AppManageRepository appRepository;

    // Run every minute to check for scheduled publishes
    @Scheduled(fixedRate = 60000)
    @Transactional
    public void publishScheduledApps() {
        long now = System.currentTimeMillis();
        List<App> scheduledApps = appRepository.findAll().stream()
                .filter(app -> app.getPublishAt() != null
                        && app.getPublishAt() <= now
                        && app.getApprovalStatus() != ApprovalStatus.APPROVED)
                .toList();

        for (App app : scheduledApps) {
            log.info("Auto-publishing scheduled app: {} (ID: {})", app.getName(), app.getId());
            app.setApprovalStatus(ApprovalStatus.APPROVED);
            app.setPublishAt(null); // Clear scheduled time
            appRepository.save(app);
        }

        if (!scheduledApps.isEmpty()) {
            log.info("Published {} scheduled apps", scheduledApps.size());
        }
    }

    // Run daily at 3 AM to purge items deleted over 30 days ago
    @Scheduled(cron = "0 0 3 * * ?")
    @Transactional
    public void purgeOldDeletedItems() {
        long thirtyDaysAgo = System.currentTimeMillis() - (30L * 24 * 60 * 60 * 1000);
        List<App> toDelete = appRepository.findAll().stream()
                .filter(app -> app.getDeletedAt() != null && app.getDeletedAt() < thirtyDaysAgo)
                .toList();

        for (App app : toDelete) {
            log.info("Permanently deleting app: {} (ID: {}) - deleted {} days ago",
                    app.getName(), app.getId(),
                    (System.currentTimeMillis() - app.getDeletedAt()) / (24 * 60 * 60 * 1000));
            appRepository.delete(app);
        }

        if (!toDelete.isEmpty()) {
            log.info("Purged {} apps from trash", toDelete.size());
        }
    }
}
