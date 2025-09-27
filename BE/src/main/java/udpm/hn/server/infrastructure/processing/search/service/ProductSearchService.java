package udpm.hn.server.infrastructure.processing.search.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;
import udpm.hn.server.infrastructure.processing.search.model.ProductDocument;
import udpm.hn.server.infrastructure.processing.search.repository.ProductSearchRepository;
import udpm.hn.server.infrastructure.processing.search.ElasticsearchSearchService;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductSearchService extends ElasticsearchSearchService<ProductDocument> {

    private final ProductSearchRepository productSearchRepository;

    public ProductSearchService(ElasticsearchOperations elasticsearchOperations, 
                              ProductSearchRepository productSearchRepository) {
        super(elasticsearchOperations, ProductDocument.class);
        this.productSearchRepository = productSearchRepository;
    }

    /**
     * Search products by name or description with pagination
     */
    public Page<ProductDocument> searchProducts(String query, int page, int size) {
        try {
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                    .should(QueryBuilders.matchQuery("name", query).boost(2.0f))
                    .should(QueryBuilders.matchQuery("description", query));

            Query searchQuery = new NativeSearchQueryBuilder()
                    .withQuery(boolQuery)
                    .withPageable(PageRequest.of(page, size))
                    .withSort(SortBuilders.scoreSort())
                    .build();

            SearchHits<ProductDocument> searchHits = getElasticsearchOperations()
                    .search(searchQuery, ProductDocument.class, getIndexCoordinates());

            return new org.springframework.data.elasticsearch.core.SearchPageImpl<>(
                    searchHits,
                    searchQuery.getPageable(),
                    searchHits.getTotalHits()
            );
        } catch (Exception e) {
            log.error("Error searching products: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to search products", e);
        }
    }

    /**
     * Filter products by category and price range
     */
    public Page<ProductDocument> filterProducts(String category, Double minPrice, Double maxPrice, int page, int size) {
        try {
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

            if (category != null && !category.isEmpty()) {
                boolQuery.must(QueryBuilders.termQuery("category", category));
            }

            if (minPrice != null || maxPrice != null) {
                boolQuery.must(QueryBuilders.rangeQuery("price")
                        .gte(minPrice != null ? minPrice : 0)
                        .lte(maxPrice != null ? maxPrice : Double.MAX_VALUE));
            }

            Query searchQuery = new NativeSearchQueryBuilder()
                    .withQuery(boolQuery)
                    .withPageable(PageRequest.of(page, size))
                    .withSort(SortBuilders.fieldSort("price").order(SortOrder.ASC))
                    .build();

            SearchHits<ProductDocument> searchHits = getElasticsearchOperations()
                    .search(searchQuery, ProductDocument.class, getIndexCoordinates());

            return new org.springframework.data.elasticsearch.core.SearchPageImpl<>(
                    searchHits,
                    searchQuery.getPageable(),
                    searchHits.getTotalHits()
            );
        } catch (Exception e) {
            log.error("Error filtering products: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to filter products", e);
        }
    }

    /**
     * Find products in stock with pagination
     */
    public Page<ProductDocument> findInStockProducts(int page, int size) {
        try {
            return productSearchRepository.findByStockQuantityGreaterThan(0, PageRequest.of(page, size));
        } catch (Exception e) {
            log.error("Error finding in-stock products: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to find in-stock products", e);
        }
    }

    /**
     * Get product suggestions based on search term
     */
    public List<String> getProductSuggestions(String term) {
        if (term == null || term.trim().isEmpty()) {
            return List.of();
        }

        try {
            Query searchQuery = new NativeSearchQueryBuilder()
                    .withQuery(QueryBuilders.matchQuery("name", term))
                    .withFields("name")
                    .withPageable(PageRequest.of(0, 5))
                    .build();

            SearchHits<ProductDocument> searchHits = getElasticsearchOperations()
                    .search(searchQuery, ProductDocument.class, getIndexCoordinates());

            return searchHits.getSearchHits().stream()
                    .map(hit -> hit.getContent().getName())
                    .distinct()
                    .toList();
        } catch (Exception e) {
            log.error("Error getting product suggestions: {}", e.getMessage(), e);
            return List.of();
        }
    }

    /**
     * Update product stock quantity
     */
    public void updateStock(String productId, int quantityChange) {
        if (productId == null || productId.trim().isEmpty()) {
            throw new IllegalArgumentException("Product ID cannot be null or empty");
        }

        try {
            productSearchRepository.findById(productId).ifPresent(product -> {
                int newQuantity = product.getStockQuantity() + quantityChange;
                if (newQuantity >= 0) {
                    product.setStockQuantity(newQuantity);
                    productSearchRepository.save(product);
                    log.debug("Updated stock for product {}: {}", productId, newQuantity);
                } else {
                    log.warn("Insufficient stock for product {}: attempted to reduce by {} when only {} available",
                            productId, quantityChange, product.getStockQuantity());
                    throw new IllegalStateException("Insufficient stock");
                }
            });
        } catch (Exception e) {
            log.error("Error updating stock for product {}: {}", productId, e.getMessage(), e);
            throw new RuntimeException("Failed to update stock", e);
        }
    }
}