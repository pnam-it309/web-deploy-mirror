package udpm.hn.server.core.customer.ViewProduct.model.Request;

import lombok.Getter;
import lombok.Setter;
import udpm.hn.server.core.common.base.PageableRequest;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class ProductFilterRequest extends PageableRequest {
    private String search;
    private String sort;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private List<String> categories;
}
