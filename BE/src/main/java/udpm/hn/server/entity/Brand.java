package udpm.hn.server.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import udpm.hn.server.entity.base.PrimaryEntity;
import udpm.hn.server.infrastructure.core.constant.EntityProperties;
import udpm.hn.server.infrastructure.core.constant.EntityStatus;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "brands")
@EntityListeners(AuditingEntityListener.class)
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "code", unique = true, nullable = false, length = 50)
    private String code;
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    @Column(name = "slug", nullable = false, unique = true)
    private String slug;
    @Column(name = "description", length = 500)
    private String description;
    @Column(name = "logo_url")
    private String logoUrl;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private EntityStatus status;
    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdAt;
    @LastModifiedDate
    @Column(name = "last_modified_date")
    private LocalDateTime updatedAt;
    // @CreatedBy
    // @Column(name = "created_by")
    // private String createdBy;
    //
    // @LastModifiedBy
    // @Column(name = "updated_by")
    // private String updatedBy;
}