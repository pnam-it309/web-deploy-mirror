package udpm.hn.server.entity;

import jakarta.persistence.*;
import lombok.*;
import udpm.hn.server.entity.base.PrimaryEntity;

@Entity
@Table(name = "push_subscription")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PushSubscription extends PrimaryEntity {

    @Column(name = "endpoint", nullable = false, length = 1000)
    private String endpoint;

    @Column(name = "p256dh_key", length = 500)
    private String p256dhKey;

    @Column(name = "auth_key", length = 500)
    private String authKey;

    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "user_agent", length = 500)
    private String userAgent;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "last_used_at")
    private Long lastUsedAt;
}
