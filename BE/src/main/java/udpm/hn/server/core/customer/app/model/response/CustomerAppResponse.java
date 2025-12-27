package udpm.hn.server.core.customer.app.model.response;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerAppResponse {
    private String id;
    private String name;
    private String sku;
    private BigDecimal price;
    private String shortDescription;
    private String thumbnail;
    private Long viewCount;
    private Boolean isFeatured;

    // Details
    private String longDescription;
    private String demoUrl;
    private String sourceUrl;
    private JsonNode specifications;

    // Related
    private String domainName;
    private String brandName;

    // Collections
    private List<CustomerFeatureResponse> features;
    private List<CustomerTeamMemberResponse> teamMembers;
    private List<String> technologyNames;
}
