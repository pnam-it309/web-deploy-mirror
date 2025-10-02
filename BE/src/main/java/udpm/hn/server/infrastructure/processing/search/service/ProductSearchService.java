package udpm.hn.server.infrastructure.processing.search.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import udpm.hn.server.infrastructure.processing.search.model.ProductDocument;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class ProductSearchService {
    
    public ProductSearchService() {
        log.warn("ProductSearchService is running in mock mode. All search functionality will return empty results.");
    }
    
    /**
     * Mock implementation of product search
     */
    public Page<ProductDocument> searchProducts(String query, int page, int size) {
        log.warn("Search functionality is disabled. Returning empty results for query: " + query);
        return new PageImpl<>(Collections.emptyList(), PageRequest.of(page, size), 0);
    }
    
    /**
     * Mock implementation of product filtering
     */
    public Page<ProductDocument> filterProducts(String category, Double minPrice, Double maxPrice, int page, int size) {
        log.warn("Filter functionality is disabled. Returning empty results");
        return new PageImpl<>(Collections.emptyList(), PageRequest.of(page, size), 0);
    }
    
    /**
     * Mock implementation of in-stock products search
     */
    public Page<ProductDocument> findInStockProducts(int page, int size) {
        log.warn("In-stock search is disabled. Returning empty results");
        return new PageImpl<>(Collections.emptyList(), PageRequest.of(page, size), 0);
    }
    
    /**
     * Mock implementation of product suggestions
     */
    public List<String> getProductSuggestions(String term) {
        log.warn("Product suggestions are disabled. Returning empty results");
        return Collections.emptyList();
    }
    
    /**
     * Mock implementation of stock update
     */
    public void updateStock(String productId, int quantityChange) {
        log.warn("Stock update is disabled. No changes made for product: " + productId);
    }
}