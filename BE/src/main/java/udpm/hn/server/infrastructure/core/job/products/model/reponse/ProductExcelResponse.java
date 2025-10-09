package udpm.hn.server.infrastructure.core.job.products.model.reponse;

import java.math.BigDecimal;

public interface ProductExcelResponse {
    String getSku();
    String getName();
    String getSlug();
    String getBrandName();
    String getCategoryName();
    BigDecimal getPrice();
    Integer getStockQuantity();

}
