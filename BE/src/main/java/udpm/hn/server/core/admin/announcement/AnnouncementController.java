package udpm.hn.server.core.admin.announcement;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import udpm.hn.server.core.admin.announcement.dto.AnnouncementRequest;
import udpm.hn.server.infrastructure.notification.AnnouncementService;

@RestController
@RequestMapping("/api/admin/announcements")
@RequiredArgsConstructor
public class AnnouncementController {

    private final AnnouncementService announcementService;

    /**
     * Send custom announcement to all users
     */
    @PostMapping("/send")
    public ResponseEntity<String> sendAnnouncement(@RequestBody AnnouncementRequest request) {
        announcementService.sendCustomAnnouncement(
                request.getTitle(),
                request.getBody(),
                request.getUrl());
        return ResponseEntity.ok("Announcement sent successfully");
    }
}
