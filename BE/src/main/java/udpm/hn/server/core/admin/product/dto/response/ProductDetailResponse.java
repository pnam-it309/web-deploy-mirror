package udpm.hn.server.core.admin.product.dto.response;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Map;

@Data
public class ProductDetailResponse {
    private String id;
    private String productId;
    private String longDescription;

    // Trả về Map để Frontend dễ hiển thị
    private Map<String, Object> specification;

    private String packaging;
    private BigDecimal weight;
    private String dimensions;
}
