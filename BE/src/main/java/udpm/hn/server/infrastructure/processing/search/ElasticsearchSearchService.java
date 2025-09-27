package udpm.hn.server.infrastructure.processing.search;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;
import udpm.hn.server.infrastructure.processing.search.model.BaseSearchDocument;
import udpm.hn.server.infrastructure.processing.search.service.SearchService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ElasticsearchSearchService<T extends BaseSearchDocument> implements SearchService<T> {

    protected final ElasticsearchOperations elasticsearchOperations;
    protected final Class<T> documentClass;

    /**
     * Get the ElasticsearchOperations instance
     */
    public ElasticsearchOperations getElasticsearchOperations() {
        return elasticsearchOperations;
    }

    @Override
    public T index(T document) {
        try {
            if (document == null) {
                throw new IllegalArgumentException("Document cannot be null");
            }
            log.debug("Indexing document: {}", document);
            T saved = elasticsearchOperations.save(document, getIndexCoordinates(document));
            log.debug("Successfully indexed document with id: {}", saved.getId());
            return saved;
        } catch (Exception e) {
            log.error("Error indexing document: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to index document", e);
        }
    }

    @Override
    public Iterable<T> bulkIndex(List<T> documents) {
        if (documents == null || documents.isEmpty()) {
            return List.of();
        }

        try {
            log.debug("Bulk indexing {} documents", documents.size());
            Iterable<T> savedDocuments = elasticsearchOperations.save(documents, getIndexCoordinates(documents.get(0)));
            log.debug("Successfully bulk indexed {} documents", documents.size());
            return savedDocuments;
        } catch (Exception e) {
            log.error("Error during bulk indexing: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to perform bulk index operation", e);
        }
    }

    @Override
    public Optional<T> findById(String id) {
        if (id == null || id.trim().isEmpty()) {
            return Optional.empty();
        }

        try {
            log.debug("Searching for document with id: {}", id);
            T document = elasticsearchOperations.get(id, documentClass);
            if (document != null) {
                log.debug("Found document with id: {}", id);
            } else {
                log.debug("No document found with id: {}", id);
            }
            return Optional.ofNullable(document);
        } catch (Exception e) {
            log.error("Error finding document by id {}: {}", id, e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public void deleteById(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Document ID cannot be null or empty");
        }

        try {
            log.debug("Deleting document with id: {}", id);
            String deletedId = elasticsearchOperations.delete(id, documentClass);
            log.debug("Successfully deleted document with id: {}", deletedId);
        } catch (Exception e) {
            log.error("Error deleting document with id {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Failed to delete document", e);
        }
    }

    @Override
    public Page<T> search(QueryBuilder query, Pageable pageable) {
        Objects.requireNonNull(query, "Query cannot be null");
        Objects.requireNonNull(pageable, "Pageable cannot be null");

        try {
            log.debug("Executing search query: {} with pageable: {}", query, pageable);
            Query searchQuery = new NativeSearchQueryBuilder()
                    .withQuery(query)
                    .withPageable(pageable)
                    .build();

            SearchHits<T> searchHits = elasticsearchOperations.search(searchQuery, documentClass, getIndexCoordinates());

            List<T> content = searchHits.getSearchHits().stream()
                    .map(SearchHit::getContent)
                    .collect(Collectors.toList());

            log.debug("Found {} results for query", content.size());
            return new PageImpl<>(content, pageable, searchHits.getTotalHits());
        } catch (Exception e) {
            log.error("Error executing search query: {}", e.getMessage(), e);
            throw new RuntimeException("Search operation failed", e);
        }
    }

    @Override
    public SearchResponse searchWithAggregation(QueryBuilder queryBuilder, AggregationBuilder aggregationBuilder, String... fields) {
        Objects.requireNonNull(queryBuilder, "Query builder cannot be null");
        Objects.requireNonNull(aggregationBuilder, "Aggregation builder cannot be null");

        try {
            log.debug("Executing search with aggregation: {}", aggregationBuilder);
            NativeSearchQueryBuilder queryBuilderObj = new NativeSearchQueryBuilder()
                    .withQuery(queryBuilder)
                    .addAggregation(aggregationBuilder);

            if (fields != null && fields.length > 0) {
                queryBuilderObj.withFields(fields);
            }

            Query searchQuery = queryBuilderObj.build();
            SearchHits<T> searchHits = elasticsearchOperations.search(searchQuery, documentClass, getIndexCoordinates());

            return searchHits.getSearchResponse()
                    .orElseThrow(() -> new IllegalStateException("Search response is empty"));

        } catch (Exception e) {
            log.error("Error executing search with aggregation: {}", e.getMessage(), e);
            throw new RuntimeException("Search with aggregation failed", e);
        }
    }

    @Override
    public Page<T> searchWithSort(QueryBuilder query, Pageable pageable, SortBuilder<?> sortBuilder) {
        Objects.requireNonNull(query, "Query cannot be null");
        Objects.requireNonNull(pageable, "Pageable cannot be null");
        Objects.requireNonNull(sortBuilder, "Sort builder cannot be null");

        try {
            log.debug("Executing search with sort: {}", sortBuilder);
            Query searchQuery = new NativeSearchQueryBuilder()
                    .withQuery(query)
                    .withPageable(PageRequest.of(
                            pageable.getPageNumber(),
                            pageable.getPageSize(),
                            pageable.getSort()
                    ))
                    .withSort(sortBuilder)
                    .build();

            SearchHits<T> searchHits = elasticsearchOperations.search(searchQuery, documentClass, getIndexCoordinates());

            List<T> content = searchHits.getSearchHits().stream()
                    .map(SearchHit::getContent)
                    .collect(Collectors.toList());

            log.debug("Found {} results for sorted query", content.size());
            return new PageImpl<>(content, pageable, searchHits.getTotalHits());
        } catch (Exception e) {
            log.error("Error executing sorted search: {}", e.getMessage(), e);
            throw new RuntimeException("Sorted search operation failed", e);
        }
    }

    @Override
    public Iterable<T> findAll() {
        try {
            log.debug("Fetching all documents");
            // Use a scroll query for large result sets
            Query searchQuery = new NativeSearchQueryBuilder()
                    .withPageable(PageRequest.of(0, 1000)) // Process in batches of 1000
                    .build();

            SearchHits<T> searchHits = elasticsearchOperations.search(searchQuery, documentClass, getIndexCoordinates());

            List<T> results = searchHits.getSearchHits().stream()
                    .map(SearchHit::getContent)
                    .collect(Collectors.toList());

            log.debug("Found {} documents", results.size());
            return results;
        } catch (Exception e) {
            log.error("Error fetching all documents: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch all documents", e);
        }
    }

    @Override
    public long count(QueryBuilder query) {
        Objects.requireNonNull(query, "Query cannot be null");

        try {
            log.debug("Counting documents for query: {}", query);
            Query searchQuery = new NativeSearchQueryBuilder()
                    .withQuery(query)
                    .build();

            long count = elasticsearchOperations.count(searchQuery, documentClass, getIndexCoordinates());
            log.debug("Found {} documents matching the query", count);
            return count;
        } catch (Exception e) {
            log.error("Error counting documents: {}", e.getMessage(), e);
            throw new RuntimeException("Count operation failed", e);
        }
    }

    @Override
    public boolean exists(String id) {
        if (id == null || id.trim().isEmpty()) {
            return false;
        }

        try {
            log.debug("Checking if document exists with id: {}", id);
            boolean exists = elasticsearchOperations.exists(id, documentClass);
            log.debug("Document with id {} exists: {}", id, exists);
            return exists;
        } catch (Exception e) {
            log.error("Error checking if document exists with id {}: {}", id, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public void refreshIndex() {
        try {
            log.debug("Refreshing index for document class: {}", documentClass.getSimpleName());
            IndexOperations indexOps = elasticsearchOperations.indexOps(documentClass);
            boolean refreshed = indexOps.refresh();
            log.debug("Index refresh for {} {}", 
                    documentClass.getSimpleName(), 
                    refreshed ? "succeeded" : "failed");
        } catch (Exception e) {
            log.error("Error refreshing index: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to refresh index", e);
        }
    }

    @Override
    public boolean deleteIndex() {
        try {
            log.warn("Attempting to delete index for document class: {}", documentClass.getSimpleName());
            IndexOperations indexOps = elasticsearchOperations.indexOps(documentClass);

            if (indexOps.exists()) {
                boolean deleted = indexOps.delete();
                log.warn("Index for {} {}", 
                        documentClass.getSimpleName(), 
                        deleted ? "was deleted" : "could not be deleted");
                return deleted;
            }

            log.warn("Index for {} does not exist", documentClass.getSimpleName());
            return false;
        } catch (Exception e) {
            log.error("Error deleting index: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to delete index", e);
        }
    }

    @Override
    public void reindexAll() {
        // This method should be overridden in the concrete service implementation
        // to fetch data from the primary database and index it in Elasticsearch
        log.warn("reindexAll() is not implemented. Please override this method in the concrete service implementation.");
    }

    /**
     * Get the index coordinates for the document class
     */
    protected IndexCoordinates getIndexCoordinates() {
        try {
            T instance = documentClass.getDeclaredConstructor().newInstance();
            String indexName = instance.getIndexName();
            log.trace("Resolved index name: {}", indexName);
            return IndexCoordinates.of(indexName);
        } catch (Exception e) {
            String errorMsg = String.format("Failed to create index coordinates for class %s: %s", 
                    documentClass.getSimpleName(), e.getMessage());
            log.error(errorMsg, e);
            throw new RuntimeException(errorMsg, e);
        }
    }

    /**
     * Get the index coordinates for a specific document
     */
    protected IndexCoordinates getIndexCoordinates(T document) {
        if (document == null) {
            throw new IllegalArgumentException("Document cannot be null");
        }

        String indexName = document.getIndexName();
        if (indexName == null || indexName.trim().isEmpty()) {
            throw new IllegalStateException("Document index name cannot be null or empty");
        }

        log.trace("Resolved index name for document: {}", indexName);
        return IndexCoordinates.of(indexName);
    }
}