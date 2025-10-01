package udpm.hn.server.infrastructure.processing.search.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import udpm.hn.server.infrastructure.processing.search.model.BaseSearchDocument;

import java.util.List;
import java.util.Optional;

/**
 * Generic search service interface for simplified operations.
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
     * Search documents with a query
     */
    Page<T> search(Object query, Pageable pageable);

    /**
     * Search with aggregations
     */
    Object searchWithAggregation(Object queryBuilder, Object aggregationBuilder, String... fields);

    /**
     * Search with sorting
     */
    Page<T> searchWithSort(Object query, Pageable pageable, Object sortBuilder);

    /**
     * Get all documents
     */
    Iterable<T> findAll();

    /**
     * Get the count of documents matching a query
     */
    long count(Object query);

    /**
     * Check if a document exists by ID
     */
    boolean exists(String id);

    /**
     * Refresh the index
     */
    void refreshIndex();

    /**
     * Delete the entire index
     */
    boolean deleteIndex();

    /**
     * Reindex all documents from the database
     */
    void reindexAll();
}
