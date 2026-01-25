package udpm.hn.server.entity;

import jakarta.persistence.*;
import lombok.*;
import udpm.hn.server.infrastructure.constant.EntityStatus;

import java.util.Date;

/**
 * Badge Entity
 * 
 * Represents achievement badges that users can earn
 */
@Entity
@Table(name = "badges")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Badge {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @Column(name = "name", length = 100, unique = true, nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "icon_svg", columnDefinition = "TEXT")
    private String iconSvg; // SVG code for badge icon

    @Column(name = "category", length = 50)
    private String category; // e.g., "project_count", "views_count", "collaboration"

    @Column(name = "criteria_type", length = 50)
    private String criteriaType; // e.g., "projects_published", "total_views", "endorsements_received"

    @Column(name = "criteria_threshold")
    private Integer criteriaThreshold; // e.g., 10 projects, 1000 views

    @Column(name = "color", length = 7)
    @Builder.Default
    private String color = "#3B82F6"; // Hex color code

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private EntityStatus status = EntityStatus.ACTIVE;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    @Builder.Default
    private Date createdAt = new Date();
}
