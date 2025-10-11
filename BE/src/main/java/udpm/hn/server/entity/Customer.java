package udpm.hn.server.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import udpm.hn.server.entity.base.PrimaryEntity;
import udpm.hn.server.infrastructure.core.constant.EntityProperties;

import java.io.Serializable;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "customer")
public class Customer extends PrimaryEntity implements Serializable {
    @Column(name = "code", length = EntityProperties.LENGTH_NAME)
    private String code;

    @Column(name = "name", length = EntityProperties.LENGTH_NAME)
    private String name;

    @Column(name = "email", length = EntityProperties.LENGTH_NAME)
    private String email;

    @Column(name = "picture", length = EntityProperties.LENGTH_PICTURE)
    private String picture;
}
