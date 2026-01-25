package udpm.hn.server.entity;

import jakarta.persistence.*;
import lombok.*;
import udpm.hn.server.entity.base.PrimaryEntity;

@Entity
@Table(name = "two_factor_auth")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TwoFactorAuth extends PrimaryEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false, unique = true)
    private Admin admin;

    @Column(name = "secret", nullable = false, length = 500)
    private String secret;

    @Column(name = "backup_codes", columnDefinition = "TEXT")
    private String backupCodes; // JSON array of backup codes

    @Column(name = "enabled", nullable = false)
    @Builder.Default
    private Boolean enabled = false;

    @Column(name = "verified_at")
    private Long verifiedAt;
}
