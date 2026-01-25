package udpm.hn.server.entity;

import jakarta.persistence.*;
import lombok.*;
import udpm.hn.server.entity.base.PrimaryEntity;

@Entity
@Table(name = "review")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review extends PrimaryEntity {

    @Column(name = "rating", nullable = false)
    private Integer rating; // 1-5 stars

    @Column(name = "comment", length = 1000)
    private String comment;

    @Column(name = "user_email", nullable = false)
    private String userEmail;

    @Column(name = "user_name")
    private String userName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_id", nullable = false)
    private App app;

    @Column(name = "moderation_status")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private udpm.hn.server.infrastructure.constant.ModerationStatus moderationStatus = udpm.hn.server.infrastructure.constant.ModerationStatus.PENDING;

    @Column(name = "moderated_at")
    private Long moderatedAt;

    @Column(name = "moderated_by")
    private String moderatedBy; // Admin email/ID who approved/rejected
}
