package udpm.hn.server.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import udpm.hn.server.entity.base.PrimaryEntity;
import udpm.hn.server.infrastructure.core.constant.EntityProperties;
import udpm.hn.server.infrastructure.core.constant.EntityStatus;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "admins")
public class Admin extends PrimaryEntity implements Serializable {
    @Column(length = EntityProperties.LENGTH_CODE, nullable = false, unique = true)
    private String username;

    @Column(name = "password_hash", length = EntityProperties.LENGTH_CODE * 2, nullable = false)
    private String passwordHash;

    @Column(name = "display_name", length = EntityProperties.LENGTH_NAME)
    private String displayName;

    @Column(length = EntityProperties.LENGTH_NAME)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role = Role.ADMIN;
    
    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private EntityStatus status = EntityStatus.ACTIVE;

    @Column(name = "last_login")
    private Long lastLogin;

    public enum Role {
        ADMIN,
        CUSTOMER
    }

    // Helper method to set last login timestamp
    public void setLastLoginNow() {
        this.lastLogin = Instant.now().getEpochSecond();
    }
}
