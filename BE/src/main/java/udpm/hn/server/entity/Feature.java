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
@Table(name = "features")
public class Feature extends PrimaryEntity implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_id", nullable = false)
    private App app;

    @Column(nullable = false, length = EntityProperties.LENGTH_NAME)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "image_preview", length = EntityProperties.LENGTH_PICTURE)
    private String imagePreview;

    @Column(name = "media_url", length = EntityProperties.LENGTH_URL)
    private String mediaUrl;

    @Column(name = "sort_order")
    private Integer sortOrder;
}
