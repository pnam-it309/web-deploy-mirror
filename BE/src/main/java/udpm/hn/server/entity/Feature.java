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
@Table(name = "features", indexes = {
        @Index(name = "idx_feature_app", columnList = "app_id")
})
public class Feature extends PrimaryEntity implements Serializable {

    @Version
    private Long version;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_id", nullable = false)
    private App app;

    @Column(nullable = false, length = EntityProperties.LENGTH_NAME)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "image_preview", length = EntityProperties.LENGTH_PICTURE)
    private String imagePreview;

    @Column(name = "video_url", length = EntityProperties.LENGTH_PICTURE)
    private String videoUrl;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @OneToMany(mappedBy = "feature", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.Set<FeatureMedia> media = new java.util.HashSet<>();

    // Convenience method for backward compatibility
    public String getTitle() {
        return this.name;
    }
}
