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
@Table(name = "apps", indexes = {
        @Index(name = "idx_app_name", columnList = "name"),
        @Index(name = "idx_app_domain", columnList = "domain_id"),
        @Index(name = "idx_app_featured", columnList = "is_featured"),
        @Index(name = "idx_app_status", columnList = "approval_status")
})
public class App extends PrimaryEntity implements Serializable {

    @Version
    private Long version;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "domain_id", nullable = false)
    private Domain domain;
    @Column(nullable = false, length = EntityProperties.LENGTH_NAME)
    private String name;

    @Column(length = EntityProperties.LENGTH_CODE)
    private String sku;

    @Column(name = "short_description", length = EntityProperties.LENGTH_DESCRIPTION)
    private String shortDescription;

    @Column(length = EntityProperties.LENGTH_PICTURE)
    private String thumbnail;

    @Column(name = "view_count")
    private Long viewCount = 0L;

    @Column(name = "is_featured")
    private Boolean isFeatured = false;

    @Column(name = "homepage_sort_order")
    private Integer homepageSortOrder;

    @Column(name = "is_featured_video")
    private Boolean isFeaturedVideo = false;

    @Column(name = "approval_status")
    @Enumerated(EnumType.STRING)
    private udpm.hn.server.infrastructure.constant.ApprovalStatus approvalStatus;

    // SEO Meta Fields
    @Column(name = "meta_title", length = 100)
    private String metaTitle;

    @Column(name = "meta_description", length = 300)
    private String metaDescription;

    @Column(name = "meta_keywords", length = 200)
    private String metaKeywords;

    @Column(name = "og_image", length = 500)
    private String ogImage;

    // Soft Delete
    @Column(name = "deleted_at")
    private Long deletedAt;

    // Scheduled Publish
    @Column(name = "publish_at")
    private Long publishAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "app_technologies", joinColumns = @JoinColumn(name = "app_id"), inverseJoinColumns = @JoinColumn(name = "technology_id"))
    private java.util.Set<Technology> technologies = new java.util.HashSet<>();

    @OneToOne(mappedBy = "app", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private AppDetail appDetail;

    @OneToMany(mappedBy = "app", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private java.util.Set<Feature> features = new java.util.HashSet<>();

    // Convenience methods for accessing AppDetail properties
    public String getSlug() {
        if (this.name == null) return null;
        return this.name.toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "");
    }

    public String getSummary() {
        return this.shortDescription;
    }

    public String getDescription() {
        return this.appDetail != null ? this.appDetail.getLongDescription() : null;
    }

    public String getLiveDemo() {
        return this.appDetail != null ? this.appDetail.getDemoUrl() : null;
    }

    public String getSourceCode() {
        return this.appDetail != null ? this.appDetail.getSourceUrl() : null;
    }
}
