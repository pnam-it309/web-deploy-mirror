package udpm.hn.server.infrastructure.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Cấu hình Redis Cache cho ứng dụng
 * - Kích hoạt caching với @EnableCaching
 * - Cấu hình cache manager với TTL khác nhau cho từng cache
 * - Xử lý lỗi cache gracefully
 * - Can be disabled by setting: spring.data.redis.enabled=false
 */
@Slf4j
@Configuration
@EnableCaching
@ConditionalOnProperty(name = "spring.data.redis.enabled", havingValue = "true", matchIfMissing = false)
public class CacheConfig implements CachingConfigurer {

    /**
     * Cấu hình Cache Manager với các cache có TTL khác nhau
     */
    @Bean
    @Override
    public CacheManager cacheManager() {
        // Tạo ObjectMapper với type info để serialize/deserialize đúng
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.activateDefaultTyping(
                BasicPolymorphicTypeValidator.builder()
                        .allowIfBaseType(Object.class)
                        .build(),
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY);

        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);

        // Cấu hình mặc định cho cache
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(1)) // TTL mặc định: 1 giờ
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer))
                .disableCachingNullValues();

        // Cấu hình TTL riêng cho từng cache
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();

        // Apps cache - 30 phút
        cacheConfigurations.put("apps", defaultConfig.entryTtl(Duration.ofMinutes(30)));
        cacheConfigurations.put("allApps", defaultConfig.entryTtl(Duration.ofMinutes(15)));

        // Customer apps cache - 1 giờ
        cacheConfigurations.put("customerApps", defaultConfig.entryTtl(Duration.ofHours(1)));

        // Domains cache - 2 giờ (ít thay đổi)
        cacheConfigurations.put("domains", defaultConfig.entryTtl(Duration.ofHours(2)));
        cacheConfigurations.put("allDomains", defaultConfig.entryTtl(Duration.ofHours(2)));

        // Features cache - 1 giờ
        cacheConfigurations.put("features", defaultConfig.entryTtl(Duration.ofHours(1)));

        // Technologies cache - 2 giờ
        cacheConfigurations.put("technologies", defaultConfig.entryTtl(Duration.ofHours(2)));

        // Dashboard cache - 5 phút (cần fresh data)
        cacheConfigurations.put("dashboard", defaultConfig.entryTtl(Duration.ofMinutes(5)));

        // User search cache - 10 phút
        cacheConfigurations.put("userSearch", defaultConfig.entryTtl(Duration.ofMinutes(10)));

        log.info("Redis Cache Manager initialized with {} cache configurations", cacheConfigurations.size());

        return RedisCacheManager.builder(getRedisConnectionFactory())
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(cacheConfigurations)
                .transactionAware()
                .build();
    }

    /**
     * Custom key generator cho cache
     */
    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(target.getClass().getSimpleName());
            sb.append(".");
            sb.append(method.getName());
            sb.append(":");
            for (Object param : params) {
                if (param != null) {
                    sb.append(param.toString());
                    sb.append(",");
                }
            }
            String key = sb.toString();
            log.debug("Generated cache key: {}", key);
            return key;
        };
    }

    /**
     * Xử lý lỗi cache - không làm crash ứng dụng khi Redis down
     */
    @Bean
    @Override
    public CacheErrorHandler errorHandler() {
        return new CacheErrorHandler() {
            @Override
            public void handleCacheGetError(RuntimeException exception, org.springframework.cache.Cache cache,
                    Object key) {
                log.error("Cache GET error - cache: {}, key: {}, error: {}",
                        cache.getName(), key, exception.getMessage());
            }

            @Override
            public void handleCachePutError(RuntimeException exception, org.springframework.cache.Cache cache,
                    Object key, Object value) {
                log.error("Cache PUT error - cache: {}, key: {}, error: {}",
                        cache.getName(), key, exception.getMessage());
            }

            @Override
            public void handleCacheEvictError(RuntimeException exception, org.springframework.cache.Cache cache,
                    Object key) {
                log.error("Cache EVICT error - cache: {}, key: {}, error: {}",
                        cache.getName(), key, exception.getMessage());
            }

            @Override
            public void handleCacheClearError(RuntimeException exception, org.springframework.cache.Cache cache) {
                log.error("Cache CLEAR error - cache: {}, error: {}",
                        cache.getName(), exception.getMessage());
            }
        };
    }

    /**
     * Inject RedisConnectionFactory
     */
    private RedisConnectionFactory redisConnectionFactory;

    public CacheConfig(RedisConnectionFactory redisConnectionFactory) {
        this.redisConnectionFactory = redisConnectionFactory;
    }

    private RedisConnectionFactory getRedisConnectionFactory() {
        return this.redisConnectionFactory;
    }
}
