package udpm.hn.server.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import udpm.hn.server.entity.base.PrimaryEntity;
import udpm.hn.server.infrastructure.core.constant.EntityProperties;
import udpm.hn.server.infrastructure.core.constant.EntityStatus;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role extends PrimaryEntity implements Serializable {

    @Column(length = EntityProperties.LENGTH_CODE, nullable = false, unique = true)
    private String code;

    @Column(length = EntityProperties.LENGTH_NAME, nullable = false)
    private String name;

    @Column(length = EntityProperties.LENGTH_DESCRIPTION)
    private String description;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private EntityStatus status = EntityStatus.ACTIVE;
}
