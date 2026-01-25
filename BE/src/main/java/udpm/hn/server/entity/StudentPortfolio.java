package udpm.hn.server.entity;

import jakarta.persistence.*;
import lombok.*;
import udpm.hn.server.infrastructure.constant.EntityStatus;

import java.util.Date;

/**
 * Student Portfolio Entity
 * 
 * Stores professional portfolio information for students
 */
@Entity
@Table(name = "student_portfolios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentPortfolio {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", unique = true)
    private Customer customer;

    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio;

    @Column(name = "github_username", length = 255)
    private String githubUsername;

    @Column(name = "linkedin_url", length = 500)
    private String linkedinUrl;

    @Column(name = "portfolio_url", length = 500)
    private String portfolioUrl;

    @Column(name = "skill_tags", columnDefinition = "TEXT")
    private String skillTags; // JSON array stored as string

    @Column(name = "total_projects")
    @Builder.Default
    private Integer totalProjects = 0;

    @Column(name = "total_views")
    @Builder.Default
    private Long totalViews = 0L;

    @Column(name = "reputation_score")
    @Builder.Default
    private Integer reputationScore = 0;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private EntityStatus status = EntityStatus.ACTIVE;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    @Builder.Default
    private Date createdAt = new Date();

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }
}
