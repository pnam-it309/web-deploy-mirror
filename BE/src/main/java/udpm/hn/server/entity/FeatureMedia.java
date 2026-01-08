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
@Table(name = "feature_media")
public class FeatureMedia extends PrimaryEntity implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feature_id", nullable = false)
    private Feature feature;

    @Column(length = EntityProperties.LENGTH_PICTURE)
    private String url;

    @Column(length = 50)
    private String type; // IMAGE or VIDEO

    @Column(name = "sort_order")
    private Integer sortOrder;
}
