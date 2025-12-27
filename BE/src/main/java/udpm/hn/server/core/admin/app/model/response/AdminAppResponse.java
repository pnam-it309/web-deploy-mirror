package udpm.hn.server.core.admin.app.model.response;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import udpm.hn.server.infrastructure.constant.EntityStatus;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminAppResponse {
    private String id;
    private String name;
    private String sku;
    private BigDecimal price;
    private String shortDescription;
    private String thumbnail;
    private EntityStatus status;

    // Domain info
    private String domainId;
    private String domainName;

    // Brand info (Developer/Student)
    private String brandId;
    private String brandName;

    // Details
    private String longDescription;
    private String demoUrl;
    private String sourceUrl;
    private JsonNode specifications;

    // Homepage Config
    private Boolean isFeatured;
    private Integer homepageSortOrder;

    // Stats
    private Long viewCount;

    // Workflow
    private String approvalStatus;
    private java.util.List<String> technologyNames;
    private Long createdDate;
    private Long lastModifiedDate;
}
