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
@Table(name = "technologies", indexes = {
        @Index(name = "idx_technology_name", columnList = "name")
})
public class Technology extends PrimaryEntity implements Serializable {

    @Version
    private Long version;

    @Column(nullable = false, unique = true, length = EntityProperties.LENGTH_NAME)
    private String name;

    @Column(length = EntityProperties.LENGTH_PICTURE)
    private String icon;
}
