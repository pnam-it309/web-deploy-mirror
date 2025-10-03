package udpm.hn.server.core.admin.products.modal.reponse;

import jakarta.persistence.*;
import udpm.hn.server.core.common.base.IsIdentify;
import udpm.hn.server.entity.Brand;
import udpm.hn.server.entity.Category;
import udpm.hn.server.entity.ProductDetail;
import udpm.hn.server.infrastructure.core.constant.EntityProperties;
import udpm.hn.server.infrastructure.core.constant.EntityStatus;

import java.math.BigDecimal;

public interface ProductsReponse extends IsIdentify {
    String getSku();
    String getName();
  String getShortDescription();
    BigDecimal price() ;
Integer getStockQuantity();
   String getBrandId();
   String getCategoryId();
  String getStatus();
  String  getProductDetailId();
  String getImagesId();
}
