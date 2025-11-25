package udpm.hn.server.core.admin.product.dto.request;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Map;

@Data
public class ProductDetailRequest {

    // Không cần productId ở trong body vì sẽ lấy từ URL

    private String longDescription; // HTML String từ Rich Text Editor

    // Frontend gửi JSON object: { "CPU": "M1", "RAM": "8GB" }
    private Map<String, Object> specification;

    private String packaging;

    private BigDecimal weight;

    private String dimensions;

    // (List ảnh chúng ta sẽ làm chức năng upload riêng sau)
}
