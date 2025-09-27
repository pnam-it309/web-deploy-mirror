package udpm.hn.server.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import udpm.hn.server.entity.base.PrimaryEntity;
import udpm.hn.server.infrastructure.core.constant.EntityProperties;
import udpm.hn.server.infrastructure.core.constant.EntityStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "quote_requests")
public class QuoteRequest extends PrimaryEntity implements Serializable {
    @Column(name = "customer_name", length = EntityProperties.LENGTH_NAME, nullable = false)
    private String customerName;

    @Column(name = "customer_email", length = EntityProperties.LENGTH_NAME)
    private String customerEmail;

    @Column(name = "customer_phone", length = EntityProperties.LENGTH_CODE)
    private String customerPhone;

    @Column(name = "customer_company", length = EntityProperties.LENGTH_NAME)
    private String customerCompany;

    @Column(length = EntityProperties.LENGTH_DESCRIPTION)
    private String notes;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private EntityStatus status = EntityStatus.ACTIVE;
    
    // Additional status specific to quote requests
    public enum QuoteStatus {
        NEW,
        IN_PROGRESS,
        CONTACTED,
        CLOSED;
        
        public static QuoteStatus fromStatus(EntityStatus status) {
            if (status == null) return null;
            return switch (status) {
                case ACTIVE -> NEW;
                case INACTIVE -> CLOSED;
                default -> null;
            };
        }
    }

    @Column(name = "total_estimated", precision = 14, scale = 2)
    private BigDecimal totalEstimated;

    @OneToMany(mappedBy = "quoteRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuoteItem> items = new ArrayList<>();
}
