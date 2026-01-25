package udpm.hn.server.infrastructure.search.service;

import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.SearchRequest;
import com.meilisearch.sdk.exceptions.MeilisearchException;
import com.meilisearch.sdk.model.SearchResult;
import com.meilisearch.sdk.model.Settings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import udpm.hn.server.entity.App;
import udpm.hn.server.infrastructure.search.model.ProductSearchDocument;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Meilisearch Service for Product Search
 * 
 * Provides:
 * - Index products to Meilisearch
 * - Typo-tolerant search
 * - Faceted filtering (by domain, tech)
 * - Instant search results
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MeilisearchService {

    private final Client meilisearchClient;
    private static final String INDEX_NAME = "products";

    /**
     * Initialize index với settings khi application startup
     */
    @PostConstruct
    public void initializeIndex() {
        try {
            Index index = meilisearchClient.index(INDEX_NAME);
            
            // Configure searchable attributes (theo thứ tự ưu tiên)
            Settings settings = new Settings();
            settings.setSearchableAttributes(new String[]{
                "name",           // Highest priority
                "summary",
                "description",
                "domain_name",
                "technology_names",
                "feature_names"
            });
            
            // Filterable attributes cho faceted search
            settings.setFilterableAttributes(new String[]{
                "domain_id",
                "technology_ids",
                "status",
                "is_featured",
                "published_at"
            });
            
            // Sortable attributes
            settings.setSortableAttributes(new String[]{
                "view_count",
                "published_at"
            });
            
            // Displayed attributes (trả về trong kết quả)
            settings.setDisplayedAttributes(new String[]{
                "id", "name", "slug", "summary", "thumbnail_url", 
                "domain_name", "technology_names", "view_count", "is_featured"
            });
            
            index.updateSettings(settings);
            
            log.info("✅ Meilisearch index '{}' initialized successfully", INDEX_NAME);
            
        } catch (Exception e) {
            log.error("❌ Failed to initialize Meilisearch index: {}", e.getMessage());
        }
    }

    /**
     * Index a single product
     */
    public void indexProduct(App app) {
        try {
            ProductSearchDocument doc = convertToSearchDocument(app);
            Index index = meilisearchClient.index(INDEX_NAME);
            index.addDocuments("[" + serializeDocument(doc) + "]");
            
            log.debug("Indexed product: {} (ID: {})", app.getName(), app.getId());
        } catch (Exception e) {
            log.error("Failed to index product {}: {}", app.getId(), e.getMessage());
        }
    }

    /**
     * Index multiple products (bulk)
     */
    public void indexProducts(List<App> apps) {
        try {
            List<ProductSearchDocument> docs = apps.stream()
                .map(this::convertToSearchDocument)
                .collect(Collectors.toList());
            
            Index index = meilisearchClient.index(INDEX_NAME);
            
            // Meilisearch accepts JSON array
            String jsonDocuments = docs.stream()
                .map(this::serializeDocument)
                .collect(Collectors.joining(",", "[", "]"));
            
            index.addDocuments(jsonDocuments);
            
            log.info("✅ Indexed {} products to Meilisearch", apps.size());
        } catch (Exception e) {
            log.error("❌ Failed to bulk index products: {}", e.getMessage());
        }
    }

    /**
     * Delete product from index
     */
    public void deleteProduct(String productId) {
        try {
            Index index = meilisearchClient.index(INDEX_NAME);
            index.deleteDocument(productId);
            log.debug("Deleted product from index: {}", productId);
        } catch (Exception e) {
            log.error("Failed to delete product {}: {}", productId, e.getMessage());
        }
    }

    /**
     * Search products với query string
     * 
     * @param query Search query (có thể có typo)
     * @param filters Optional filters (e.g., "domain_id = 'xxx'")
     * @param limit Number of results
     * @return Search results
     */
    public SearchResult search(String query, String filters, int limit) {
        try {
            Index index = meilisearchClient.index(INDEX_NAME);
            
            SearchRequest searchRequest = SearchRequest.builder()
                .q(query)
                .filter(filters != null ? new String[]{filters} : null)
                .limit(limit)
                .attributesToHighlight(new String[]{"name", "summary", "description"})
                .build();
            
            SearchResult result = (SearchResult) index.search(searchRequest);
            
            log.debug("Search '{}' returned {} results", query, result.getHits().size());
            return result;
            
        } catch (MeilisearchException e) {
            log.error("Search failed: {}", e.getMessage());
            return new SearchResult(); // Return empty result
        }
    }

    /**
     * Clear entire index (use with caution)
     */
    public void clearIndex() {
        try {
            Index index = meilisearchClient.index(INDEX_NAME);
            index.deleteAllDocuments();
            log.warn("⚠️ All documents deleted from index '{}'", INDEX_NAME);
        } catch (Exception e) {
            log.error("Failed to clear index: {}", e.getMessage());
        }
    }

    /**
     * Convert App entity to ProductSearchDocument
     */
    private ProductSearchDocument convertToSearchDocument(App app) {
        return ProductSearchDocument.builder()
            .id(app.getId())
            .name(app.getName())
            .slug(app.getSlug())
            .summary(app.getSummary())
            .description(app.getDescription())
            .domainName(app.getDomain() != null ? app.getDomain().getName() : null)
            .domainId(app.getDomain() != null ? app.getDomain().getId() : null)
            .technologyNames(
                app.getTechnologies() != null 
                    ? app.getTechnologies().stream().map(t -> t.getName()).collect(Collectors.toList())
                    : new ArrayList<>()
            )
            .technologyIds(
                app.getTechnologies() != null 
                    ? app.getTechnologies().stream().map(t -> t.getId()).collect(Collectors.toList())
                    : new ArrayList<>()
            )
            .featureNames(
                app.getFeatures() != null 
                    ? app.getFeatures().stream().map(f -> f.getTitle()).collect(Collectors.toList())
                    : new ArrayList<>()
            )
            .viewCount(app.getViewCount() != null ? app.getViewCount() : 0)
            .isFeatured(app.getIsFeatured() != null ? app.getIsFeatured() : false)
            .status(app.getStatus() != null ? app.getStatus().name() : null)
            .publishedAt(app.getCreatedAt() != null ? app.getCreatedAt() : null)
            .thumbnailUrl(app.getThumbnail())
            .demoUrl(app.getLiveDemo())
            .sourceUrl(app.getSourceCode())
            .build();
    }

    /**
     * Serialize ProductSearchDocument to JSON manually
     * (Simple approach without Jackson complexity)
     */
    private String serializeDocument(ProductSearchDocument doc) {
        return String.format("""
            {
                "id": "%s",
                "name": "%s",
                "slug": "%s",
                "summary": "%s",
                "description": "%s",
                "domain_name": "%s",
                "domain_id": "%s",
                "technology_names": %s,
                "technology_ids": %s,
                "feature_names": %s,
                "view_count": %d,
                "is_featured": %b,
                "status": "%s",
                "published_at": %d,
                "thumbnail_url": "%s",
                "demo_url": "%s",
                "source_url": "%s"
            }
            """,
            escapeJson(doc.getId()),
            escapeJson(doc.getName()),
            escapeJson(doc.getSlug()),
            escapeJson(doc.getSummary()),
            escapeJson(doc.getDescription()),
            escapeJson(doc.getDomainName()),
            escapeJson(doc.getDomainId()),
            toJsonArray(doc.getTechnologyNames()),
            toJsonArray(doc.getTechnologyIds()),
            toJsonArray(doc.getFeatureNames()),
            doc.getViewCount(),
            doc.getIsFeatured(),
            escapeJson(doc.getStatus()),
            doc.getPublishedAt(),
            escapeJson(doc.getThumbnailUrl()),
            escapeJson(doc.getDemoUrl()),
            escapeJson(doc.getSourceUrl())
        );
    }

    private String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r");
    }

    private String toJsonArray(List<String> list) {
        if (list == null || list.isEmpty()) return "[]";
        return list.stream()
            .map(s -> "\"" + escapeJson(s) + "\"")
            .collect(Collectors.joining(",", "[", "]"));
    }
}
