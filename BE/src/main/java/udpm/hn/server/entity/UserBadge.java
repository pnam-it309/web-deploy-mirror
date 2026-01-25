package udpm.hn.server.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

/**
 * User Badge Entity
 * 
 * Junction table storing which badges users have earned
 */
@Entity
@Table(name = "user_badges", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"portfolio_id", "badge_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserBadge {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id", nullable = false)
    private StudentPortfolio portfolio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "badge_id", nullable = false)
    private Badge badge;

    @Column(name = "earned_at")
    @Temporal(TemporalType.TIMESTAMP)
    @Builder.Default
    private Date earnedAt = new Date();

    @Column(name = "progress")
    private Integer progress; // Optional: Track progress towards next tier (e.g., 7/10 projects)
}
