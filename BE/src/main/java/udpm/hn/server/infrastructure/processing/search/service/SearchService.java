package udpm.hn.server.infrastructure.processing.search.service;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import udpm.hn.server.infrastructure.processing.search.model.BaseSearchDocument;

import java.util.List;
import java.util.Optional;

/**
 * Generic search service interface for Elasticsearch operations.
 *
 * @param <T> The document type that extends BaseSearchDocument
 */
public interface SearchService<T extends BaseSearchDocument> {

    /**
     * Index a single document
     */
    T index(T document);

    /**
     * Index multiple documents in bulk
     */
    Iterable<T> bulkIndex(List<T> documents);

    /**
     * Find a document by ID
     */
    Optional<T> findById(String id);

    /**
     * Delete a document by ID
     */
    void deleteById(String id);

    /**
     * Search documents with a query builder
     */
    Page<T> search(QueryBuilder query, Pageable pageable);

    /**
     * Search with aggregations
     */
    SearchResponse searchWithAggregation(QueryBuilder queryBuilder, AggregationBuilder aggregationBuilder, String... fields);

    /**
     * Search with sorting
     */
    Page<T> searchWithSort(QueryBuilder query, Pageable pageable, SortBuilder<?> sortBuilder);

    /**
     * Get all documents (use with caution for large indices)
     */
    Iterable<T> findAll();

    /**
     * Get the count of documents matching a query
     */
    long count(QueryBuilder query);

    /**
     * Check if a document exists by ID
     */
    boolean exists(String id);

    /**
     * Refresh the index to make all operations available for search
     */
    void refreshIndex();

    /**
     * Delete the entire index (use with caution)
     */
    boolean deleteIndex();

    /**
     * Reindex all documents from the database
     */
    void reindexAll();
}
