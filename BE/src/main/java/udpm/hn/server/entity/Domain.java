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
@Table(name = "domains")
public class Domain extends PrimaryEntity implements Serializable {
    @Column(nullable = false, length = EntityProperties.LENGTH_NAME)
    private String name;

    @Column(length = EntityProperties.LENGTH_NAME)
    private String slug;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = EntityProperties.LENGTH_PICTURE)
    private String icon;

    @Column(name = "color")
    private String color;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Domain parent;

    @Column(name = "sort_order")
    private Integer sortOrder;
}
