package udpm.hn.server.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import udpm.hn.server.entity.base.PrimaryEntity;
import udpm.hn.server.infrastructure.core.constant.EntityProperties;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "product_details")
public class ProductDetail extends PrimaryEntity implements Serializable {
    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "long_description", length = EntityProperties.LENGTH_DESCRIPTION)
    private String longDescription;

    @Column(length = EntityProperties.LENGTH_DESCRIPTION)
    @JdbcTypeCode(SqlTypes.JSON)
    private JsonNode specification;

    @Column(length = EntityProperties.LENGTH_NAME)
    private String packaging;
    
    @Column(precision = 10, scale = 3)
    private BigDecimal weight;
    
    @Column(length = 100)
    private String dimensions;

    @OneToMany(mappedBy = "productDetail", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sortOrder ASC")
    private List<Image> images = new ArrayList<>();
}
