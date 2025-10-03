package udpm.hn.server.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import udpm.hn.server.entity.base.PrimaryEntity;
import udpm.hn.server.infrastructure.core.constant.EntityStatus;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "admin_roles")
public class AdminRole extends PrimaryEntity implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private EntityStatus status = EntityStatus.ACTIVE;
}
