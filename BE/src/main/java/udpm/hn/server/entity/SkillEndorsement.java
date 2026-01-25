package udpm.hn.server.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

/**
 * Skill Endorsement Entity
 * 
 * Allows users to endorse each other's skills
 */
@Entity
@Table(name = "skill_endorsements",
       uniqueConstraints = @UniqueConstraint(columnNames = {"portfolio_id", "skill_name", "endorsed_by"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkillEndorsement {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id", nullable = false)
    private StudentPortfolio portfolio;

    @Column(name = "skill_name", length = 100, nullable = false)
    private String skillName; // e.g., "Vue.js", "Java", "UI/UX Design"

    @Column(name = "endorsed_by", length = 36, nullable = false)
    private String endorsedBy; // Customer ID of endorser

    @Column(name = "endorsed_at")
    @Temporal(TemporalType.TIMESTAMP)
    @Builder.Default
    private Date endorsedAt = new Date();
}
