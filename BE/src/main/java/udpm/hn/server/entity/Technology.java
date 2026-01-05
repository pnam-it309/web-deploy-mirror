package udpm.hn.server.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import udpm.hn.server.entity.base.PrimaryEntity;
import udpm.hn.server.infrastructure.constant.EntityProperties;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "technologies")
public class Technology extends PrimaryEntity implements Serializable {
    @Column(nullable = false, unique = true, length = EntityProperties.LENGTH_NAME)
    private String name;

    @Column(length = EntityProperties.LENGTH_PICTURE)
    private String icon;
}
