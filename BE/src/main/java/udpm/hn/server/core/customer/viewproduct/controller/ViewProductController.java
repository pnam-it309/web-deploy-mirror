package udpm.hn.server.core.customer.ViewProduct.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import udpm.hn.server.core.customer.ViewProduct.Service.ViewProductService;
import udpm.hn.server.entity.Product;
import udpm.hn.server.infrastructure.core.constant.MappingConstants;

import java.util.List;

@RestController
@RequestMapping(MappingConstants.API_CUSTOMER_VIEW_PRODUCT)
@RequiredArgsConstructor
public class ViewProductController {

    private final ViewProductService viewProductService;

    /**
     * Get all products with pagination
     * @param pageable Pagination information (page, size, sort)
     * @return Page of products
     */
    @GetMapping
    public ResponseEntity<Page<Product>> getAllProducts(
            @PageableDefault(size = 10) Pageable pageable) {
        Page<Product> products = viewProductService.getAllProducts(pageable);
        return ResponseEntity.ok(products);
    }

    /**
     * Get all products without pagination
     * @return List of all products
     */
    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = viewProductService.getAllProducts();
        return ResponseEntity.ok(products);
    }
}
