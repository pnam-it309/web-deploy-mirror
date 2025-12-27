package udpm.hn.server.core.admin.app.model.request;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class AdminUpdateAppRequest {
    private String name;
    private String domainId;
    private String brandId;
    private String sku;
    private BigDecimal price;
    private String shortDescription;
    private String thumbnail;

    // Details
    private String longDescription;
    private String demoUrl;
    private String sourceUrl;
    private JsonNode specifications; // Technologies

    // Homepage Config
    private Boolean isFeatured;
    private Integer homepageSortOrder;

    // Workflow
    private String approvalStatus;

    // IDs of technologies
    private java.util.List<String> technologyIds; // Many-to-many product_technology
}
