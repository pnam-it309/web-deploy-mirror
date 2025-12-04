package udpm.hn.server.infrastructure.processing.search.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchClientAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableAutoConfiguration(exclude = {
        ElasticsearchClientAutoConfiguration.class,
        ElasticsearchRepositoriesAutoConfiguration.class
})
@EnableElasticsearchRepositories(basePackages = "none") // Disable Elasticsearch repositories
public class ElasticsearchConfig {
    // This class disables Elasticsearch auto-configuration
}
