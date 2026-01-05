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
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "brands")
public class Brand extends PrimaryEntity implements Serializable {

    @Column(nullable = false, length = EntityProperties.LENGTH_NAME)
    private String name;

    @Column(length = EntityProperties.LENGTH_PICTURE)
    private String logo;

    @Column(length = EntityProperties.LENGTH_URL)
    private String website;
}