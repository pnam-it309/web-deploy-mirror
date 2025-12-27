package udpm.hn.server.core.admin.app.model.request;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class AdminCreateAppRequest {
    private String name;
    private String domainId;
    private String brandId; // Developer/Student
    private String sku;
    private BigDecimal price;
    private String shortDescription;
    private String thumbnail;
}
