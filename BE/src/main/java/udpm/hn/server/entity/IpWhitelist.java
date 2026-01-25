package udpm.hn.server.entity;

import jakarta.persistence.*;
import lombok.*;
import udpm.hn.server.entity.base.PrimaryEntity;

@Entity
@Table(name = "ip_whitelist")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IpWhitelist extends PrimaryEntity {

    @Column(name = "ip_address", nullable = false)
    private String ipAddress; // Can be single IP or CIDR range

    @Column(name = "description")
    private String description;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "added_by")
    private String addedBy;

    @Column(name = "expires_at")
    private Long expiresAt; // null = never expires
}
