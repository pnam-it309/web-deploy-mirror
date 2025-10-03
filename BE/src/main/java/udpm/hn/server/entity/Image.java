package udpm.hn.server.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import udpm.hn.server.entity.base.PrimaryEntity;
import udpm.hn.server.infrastructure.core.constant.EntityProperties;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "images")
public class Image extends PrimaryEntity implements Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_detail_id")
    private ProductDetail productDetail;

    @Column(length = EntityProperties.LENGTH_URL, nullable = false)
    private String url;

    @Column(name = "alt_text", length = EntityProperties.LENGTH_NAME)
    private String altText;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;
}
