package udpm.hn.server.infrastructure.search.config;

import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Meilisearch Client Configuration
 * 
 * Meilisearch is an open-source, typo-tolerant search engine.
 * FREE alternative to Algolia with self-hosting option.
 * 
 * Features:
 * - Instant search results (<50ms)
 * - Typo tolerance out of the box
 * - Faceted search & filtering
 * - Highlighting & snippeting
 * 
 * @see <a href="https://www.meilisearch.com">Meilisearch Docs</a>
 */
@Configuration
@Slf4j
public class MeilisearchConfig {

    @Value("${meilisearch.host:http://localhost:7700}")
    private String host;

    @Value("${meilisearch.apiKey:changeme_master_key_for_production}")
    private String apiKey;

    @Bean
    public Client meilisearchClient() {
        log.info("Initializing Meilisearch client với host: {}", host);
        
        Config config = new Config(host, apiKey);
        Client client = new Client(config);
        
        try {
            // Test connection
            client.health();
            log.info("✅ Meilisearch connection successful");
        } catch (Exception e) {
            log.error("❌ Failed to connect to Meilisearch: {}", e.getMessage());
            log.warn("Search features sẽ không hoạt động. Kiểm tra docker-compose up meilisearch");
        }
        
        return client;
    }
}
