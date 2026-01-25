package udpm.hn.server.core.admin.announcement.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnnouncementRequest {
    private String title;
    private String body;
    private String url;
}
