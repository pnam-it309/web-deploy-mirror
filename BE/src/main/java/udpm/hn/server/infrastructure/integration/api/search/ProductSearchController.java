package udpm.hn.server.infrastructure.integration.api.search;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import udpm.hn.server.infrastructure.processing.search.model.ProductDocument;
import udpm.hn.server.infrastructure.processing.search.service.ProductSearchService;

import java.util.List;

@RestController
@RequestMapping("/api/products/search")
@Tag(name = "Product Search", description = "API for searching and filtering products")
@Slf4j
public class ProductSearchController {

    private final ProductSearchService productSearchService;
    
    @Autowired(required = false)
    public ProductSearchController(ProductSearchService productSearchService) {
        this.productSearchService = productSearchService;
        if (productSearchService == null) {
            log.warn("ProductSearchService is not available. Search endpoints will return empty results.");
        }
    }

    @GetMapping
    @Operation(summary = "Search products by query",
               description = "Search products by name or description with pagination")
    public ResponseEntity<Page<ProductDocument>> searchProducts(
            @Parameter(description = "Search query", required = true)
            @RequestParam String query,
            
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        
        if (productSearchService == null) {
            log.warn("ProductSearchService not available, returning empty results");
            return ResponseEntity.ok(Page.empty());
        }
        
        return ResponseEntity.ok(productSearchService.searchProducts(query, page, size));
    }

    @GetMapping("/filter")
    @Operation(summary = "Filter products",
               description = "Filter products by category and/or price range with pagination")
    public ResponseEntity<Page<ProductDocument>> filterProducts(
            @Parameter(description = "Category to filter by")
            @RequestParam(required = false) String category,
            
            @Parameter(description = "Minimum price")
            @RequestParam(required = false) Double minPrice,
            
            @Parameter(description = "Maximum price")
            @RequestParam(required = false) Double maxPrice,
            
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        
        if (productSearchService == null) {
            log.warn("ProductSearchService not available, returning empty results");
            return ResponseEntity.ok(Page.empty());
        }
        
        return ResponseEntity.ok(
                productSearchService.filterProducts(category, minPrice, maxPrice, page, size)
        );
    }

    @GetMapping("/in-stock")
    @Operation(summary = "Get in-stock products",
               description = "Get a paginated list of products that are in stock")
    public ResponseEntity<Page<ProductDocument>> getInStockProducts(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        
        if (productSearchService == null) {
            log.warn("ProductSearchService not available, returning empty results");
            return ResponseEntity.ok(Page.empty());
        }
        
        return ResponseEntity.ok(productSearchService.findInStockProducts(page, size));
    }

    @GetMapping("/suggestions")
    @Operation(summary = "Get product suggestions",
               description = "Get product name suggestions based on search term")
    public ResponseEntity<List<String>> getSuggestions(
            @Parameter(description = "Search term for suggestions", required = true)
            @RequestParam String term) {
        
        if (productSearchService == null) {
            log.warn("ProductSearchService not available, returning empty results");
            return ResponseEntity.ok(List.of());
        }
        
        return ResponseEntity.ok(productSearchService.getProductSuggestions(term));
    }

    @PutMapping("/{id}/stock")
    @Operation(summary = "Update product stock",
               description = "Update the stock quantity of a product")
    public ResponseEntity<Void> updateStock(
            @Parameter(description = "Product ID", required = true)
            @PathVariable String id,
            
            @Parameter(description = "Quantity to add (positive) or remove (negative)", required = true)
            @RequestParam int quantity) {
        
        if (productSearchService == null) {
            log.warn("ProductSearchService not available, skipping stock update");
            return ResponseEntity.ok().build();
        }
        
        productSearchService.updateStock(id, quantity);
        return ResponseEntity.ok().build();
    }
}
