package udpm.hn.server.infrastructure.processing.search.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import udpm.hn.server.infrastructure.processing.search.model.ProductDocument;

import java.util.List;

public interface ProductSearchRepository extends ElasticsearchRepository<ProductDocument, String> {
    
    List<ProductDocument> findByName(String name);
    
    List<ProductDocument> findByCategory(String category);
    
    List<ProductDocument> findByPriceBetween(Double minPrice, Double maxPrice);
    
    List<ProductDocument> findByActiveTrue();
    
    List<ProductDocument> findByStockQuantityGreaterThan(Integer quantity);
    
    List<ProductDocument> findByNameContainingOrDescriptionContaining(String name, String description);
}
