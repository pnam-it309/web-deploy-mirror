package udpm.hn.server.infrastructure.search.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import udpm.hn.server.entity.App;
import udpm.hn.server.infrastructure.search.service.MeilisearchService;

/**
 * Event Listener for automatic Meilisearch synchronization
 * 
 * Khi có product được tạo/sửa/xóa → tự động sync với Meilisearch
 * để đảm bảo search index luôn up-to-date
 * 
 * Using @TransactionalEventListener để chỉ sync sau khi DB transaction commit
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class ProductSearchSyncListener {

    private final MeilisearchService meilisearchService;

    /**
     * Listen for product creation events
     */
    @TransactionalEventListener
    @Async
    public void onProductCreated(ProductCreatedEvent event) {
        try {
            log.info("Syncing new product to Meilisearch: {}", event.product().getName());
            meilisearchService.indexProduct(event.product());
        } catch (Exception e) {
            log.error("Failed to sync new product to Meilisearch", e);
        }
    }

    /**
     * Listen for product update events
     */
    @TransactionalEventListener
    @Async
    public void onProductUpdated(ProductUpdatedEvent event) {
        try {
            log.info("Updating product in Meilisearch: {}", event.product().getName());
            meilisearchService.indexProduct(event.product());
        } catch (Exception e) {
            log.error("Failed to update product in Meilisearch", e);
        }
    }

    /**
     * Listen for product deletion events
     */
    @TransactionalEventListener
    @Async
    public void onProductDeleted(ProductDeletedEvent event) {
        try {
            log.info("Removing product from Meilisearch: {}", event.productId());
            meilisearchService.deleteProduct(event.productId());
        } catch (Exception e) {
            log.error("Failed to remove product from Meilisearch", e);
        }
    }

    // Event classes (simple POJOs)
    
    public record ProductCreatedEvent(App product) {}
    
    public record ProductUpdatedEvent(App product) {}
    
    public record ProductDeletedEvent(String productId) {}
}
