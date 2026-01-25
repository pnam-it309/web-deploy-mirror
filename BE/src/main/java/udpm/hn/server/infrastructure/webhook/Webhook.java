package udpm.hn.server.infrastructure.webhook;

import jakarta.persistence.*;
import lombok.*;
import udpm.hn.server.entity.base.PrimaryEntity;

@Entity
@Table(name = "webhook")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Webhook extends PrimaryEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "url", nullable = false, length = 500)
    private String url;

    @Column(name = "secret")
    private String secret; // For HMAC signature verification

    @Column(name = "events", length = 500)
    private String events; // Comma-separated: APP_CREATED,APP_UPDATED,APP_DELETED

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "last_triggered_at")
    private Long lastTriggeredAt;

    @Column(name = "failure_count")
    @Builder.Default
    private Integer failureCount = 0;

    // Event types
    public static final String EVENT_APP_CREATED = "APP_CREATED";
    public static final String EVENT_APP_UPDATED = "APP_UPDATED";
    public static final String EVENT_APP_DELETED = "APP_DELETED";
    public static final String EVENT_APP_PUBLISHED = "APP_PUBLISHED";
}
