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

    @Column(name = "approval_status")
    @Enumerated(EnumType.STRING)
    private udpm.hn.server.infrastructure.constant.ApprovalStatus approvalStatus;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "app_technologies", joinColumns = @JoinColumn(name = "app_id"), inverseJoinColumns = @JoinColumn(name = "technology_id"))
    private java.util.Set<Technology> technologies = new java.util.HashSet<>();
}
