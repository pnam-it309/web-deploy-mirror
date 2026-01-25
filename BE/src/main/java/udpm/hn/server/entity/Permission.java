package udpm.hn.server.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import udpm.hn.server.entity.base.PrimaryEntity;
import udpm.hn.server.infrastructure.constant.EntityProperties;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "permissions")
@NoArgsConstructor
@AllArgsConstructor
public class Permission extends PrimaryEntity implements Serializable {

    @Column(nullable = false, length = EntityProperties.LENGTH_NAME)
    private String name;

    @Column(nullable = false, unique = true, length = EntityProperties.LENGTH_CODE)
    private String code;

    @Column(length = EntityProperties.LENGTH_DESCRIPTION)
    private String description;

    @Column(length = 50)
    private String category; // e.g., "APP_MANAGEMENT", "DOMAIN_MANAGEMENT", "SYSTEM"
}
