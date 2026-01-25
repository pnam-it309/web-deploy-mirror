package udpm.hn.server.entity;

import jakarta.persistence.*;
import lombok.*;
import udpm.hn.server.entity.base.PrimaryEntity;

@Entity
@Table(name = "email_subscription")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailSubscription extends PrimaryEntity {

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "name")
    private String name;

    @Column(name = "is_verified")
    @Builder.Default
    private Boolean isVerified = false;

    @Column(name = "verification_token")
    private String verificationToken;

    @Column(name = "unsubscribe_token")
    private String unsubscribeToken;

    @Column(name = "preferences")
    private String preferences; // JSON: {"newProducts": true, "updates": true}

    @Column(name = "last_email_sent_at")
    private Long lastEmailSentAt;

    @Column(name = "subscribed_at")
    private Long subscribedAt;
}
