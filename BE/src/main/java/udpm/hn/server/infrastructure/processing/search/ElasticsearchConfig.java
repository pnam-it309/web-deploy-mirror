import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * Cấu hình Elasticsearch cho ứng dụng.
 * Hỗ trợ kết nối với Elasticsearch có hoặc không có xác thực.
 * Mặc định kết nối đến localhost:9200 nếu không cấu hình.
 */
@Configuration
@EnableElasticsearchRepositories(
    basePackages = "udpm.hn.server.infrastructure.processing.search.repository",
    considerNestedRepositories = true
)
public class ElasticsearchConfig {

    @Value("${spring.elasticsearch.uris:http://localhost:9200}")
    private String elasticsearchUris;

    @Value("${spring.elasticsearch.username:}")
    private String username;

    @Value("${spring.elasticsearch.password:}")
    private String password;

    @Bean(destroyMethod = "close")
    @Primary
    public RestClient restClient() {
        // Parse the URI to get host and port
        HttpHost host = HttpHost.create(elasticsearchUris);
        RestClientBuilder builder = RestClient.builder(host);

        // Add basic auth if credentials are provided
        if (username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
            final BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(
                AuthScope.ANY,
                new UsernamePasswordCredentials(username, password)
            );

            builder.setHttpClientConfigCallback((HttpAsyncClientBuilder httpClientBuilder) ->
                httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider)
            );
        }

        return builder.build();
    }

    @Bean
    public ElasticsearchTransport elasticsearchTransport(RestClient restClient) {
        return new RestClientTransport(restClient, new JacksonJsonpMapper());
    }

    @Bean
    public ElasticsearchClient elasticsearchClient(ElasticsearchTransport transport) {
        return new ElasticsearchClient(transport);
    }
}
