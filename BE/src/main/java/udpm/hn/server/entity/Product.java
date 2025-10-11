package udpm.hn.server.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import udpm.hn.server.entity.base.PrimaryEntity;
import udpm.hn.server.infrastructure.core.constant.EntityProperties;
import udpm.hn.server.infrastructure.core.constant.EntityStatus;
import udpm.hn.server.infrastructure.core.constant.EntityUnit;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "products")
public class Product extends PrimaryEntity implements Serializable {
    @Column(length = EntityProperties.LENGTH_CODE, unique = true)
    private String sku;

    @Column(length = EntityProperties.LENGTH_NAME, nullable = false)
    private String name;

    @Column(length = EntityProperties.LENGTH_NAME + 50, nullable = false, unique = true)
    private String slug;

    @Column(name = "short_description", length = EntityProperties.LENGTH_DESCRIPTION / 5)
    private String shortDescription;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal price = BigDecimal.ZERO;

    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity = 0;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private EntityStatus status = EntityStatus.ACTIVE;

    @Enumerated(EnumType.STRING)
    @Column(length = EntityProperties.LENGTH_CODE)
    private EntityUnit unit;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private ProductDetail productDetail;

    @OneToMany(mappedBy = "product")
    private List<QuoteItem> quoteItems = new ArrayList<>();
}
