package udpm.hn.server.core.customer.ViewProduct.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import udpm.hn.server.entity.Product;

import java.util.List;

public interface ViewProductService {
    List<Product> getAllProducts();
    Page<Product> getAllProducts(Pageable pageable);
}
