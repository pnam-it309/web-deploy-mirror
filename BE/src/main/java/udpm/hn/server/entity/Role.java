package udpm.hn.server.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import udpm.hn.server.entity.base.PrimaryEntity;

import udpm.hn.server.infrastructure.constant.EntityProperties;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role extends PrimaryEntity implements Serializable {

    @Column(nullable = false, length = EntityProperties.LENGTH_CODE)
    private String name;

    @Column(length = EntityProperties.LENGTH_NAME)
    private String description;
}
