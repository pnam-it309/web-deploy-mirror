package udpm.hn.server.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import udpm.hn.server.entity.base.PrimaryEntity;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "product_likes", indexes = {
        @Index(name = "idx_like_app", columnList = "app_id"),
        @Index(name = "idx_like_customer", columnList = "customer_id"),
        @Index(name = "idx_like_composite", columnList = "app_id,customer_id", unique = true)
})
public class ProductLike extends PrimaryEntity implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_id", nullable = false)
    private App app;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "created_at", nullable = false)
    private Long createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = System.currentTimeMillis();
        }
    }
}
