package udpm.hn.server.infrastructure.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.SimpleCacheErrorHandler;
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
 * Redis Cache Configuration
 * Configures Spring Cache abstraction with Redis as the caching provider
 * 
 * Features:
 * - Multiple cache configurations with different TTLs
 * - JSON serialization for human-readable cache values
 * - Graceful error handling (cache failures don't break app)
 * - Optimized for performance and reliability
 */
@Slf4j
@Configuration
@EnableCaching
public class CacheConfig implements CachingConfigurer {

    /**
     * Main Cache Manager Bean
     * Configures different caches with specific TTL policies
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        log.info("Initializing Redis Cache Manager with custom configurations");

        // Default cache configuration (10 minutes TTL)
        RedisCacheConfiguration defaultConfig = createCacheConfiguration(Duration.ofMinutes(10));

        // Custom cache configurations for different data types
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();

        // ==========================================
        // STATIC/RARELY CHANGED DATA (Long TTL)
        // ==========================================

        // Domains - rarely change, cache for 1 hour
        cacheConfigurations.put("domains",
                createCacheConfiguration(Duration.ofHours(1)));

        // Technologies - rarely change, cache for 6 hours
        cacheConfigurations.put("technologies",
                createCacheConfiguration(Duration.ofHours(6)));

        // Roles & Permissions - change very rarely, cache for 2 hours
        cacheConfigurations.put("roles",
                createCacheConfiguration(Duration.ofHours(2)));

        cacheConfigurations.put("permissions",
                createCacheConfiguration(Duration.ofHours(2)));

        // ==========================================
        // FREQUENTLY ACCESSED DATA (Medium TTL)
        // ==========================================

        // App listings - updated frequently, cache for 15 minutes
        cacheConfigurations.put("apps",
                createCacheConfiguration(Duration.ofMinutes(15)));

        // Featured apps - homepage data, cache for 30 minutes
        cacheConfigurations.put("featured-apps",
                createCacheConfiguration(Duration.ofMinutes(30)));

        // App details - cache for 20 minutes
        cacheConfigurations.put("app-details",
                createCacheConfiguration(Duration.ofMinutes(20)));

        // ==========================================
        // DYNAMIC DATA (Short TTL)
        // ==========================================

        // Dashboard statistics - recalculated frequently, 5 minutes
        cacheConfigurations.put("dashboard:stats",
                createCacheConfiguration(Duration.ofMinutes(5)));

        // Search results - user-specific, 3 minutes
        cacheConfigurations.put("search:results",
                createCacheConfiguration(Duration.ofMinutes(3)));

        // Reviews count/rating - updated often, 10 minutes
        cacheConfigurations.put("reviews:stats",
                createCacheConfiguration(Duration.ofMinutes(10)));

        // ==========================================
        // SESSION-LIKE DATA (Very Short TTL)
        // ==========================================

        // Trending data - real-time, 2 minutes
        cacheConfigurations.put("trending",
                createCacheConfiguration(Duration.ofMinutes(2)));

        // Analytics - near real-time, 1 minute
        cacheConfigurations.put("analytics",
                createCacheConfiguration(Duration.ofMinutes(1)));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(cacheConfigurations)
                .transactionAware() // Make cache operations transactional
                .build();
    }

    /**
     * Create cache configuration with custom TTL and serialization
     */
    private RedisCacheConfiguration createCacheConfiguration(Duration ttl) {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(ttl)
                .disableCachingNullValues() // Don't cache null values
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(
                                new StringRedisSerializer()))
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(
                                jsonRedisSerializer()))
                .prefixCacheNameWith("fpl-catalog::"); // Add prefix to all keys
    }

    /**
     * JSON Serializer for Redis values
     * Allows storing objects in human-readable JSON format
     */
    private GenericJackson2JsonRedisSerializer jsonRedisSerializer() {
        ObjectMapper objectMapper = new ObjectMapper();

        // Register Java 8 time module
        objectMapper.registerModule(new JavaTimeModule());

        // Enable polymorphic type handling for inheritance
        objectMapper.activateDefaultTyping(
                BasicPolymorphicTypeValidator.builder()
                        .allowIfBaseType(Object.class)
                        .build(),
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY);

        return new GenericJackson2JsonRedisSerializer(objectMapper);
    }

    /**
     * Cache Error Handler
     * Ensures cache failures don't crash the application
     * Logs errors and continues with database queries
     */
    @Bean
    @Override
    public CacheErrorHandler errorHandler() {
        return new SimpleCacheErrorHandler() {
            @Override
            public void handleCacheGetError(RuntimeException exception,
                    org.springframework.cache.Cache cache,
                    Object key) {
                log.warn("Cache GET failed for cache={}, key={}, error={}",
                        cache.getName(), key, exception.getMessage());
                // Don't throw - continue without cache
            }

            @Override
            public void handleCachePutError(RuntimeException exception,
                    org.springframework.cache.Cache cache,
                    Object key,
                    Object value) {
                log.warn("Cache PUT failed for cache={}, key={}, error={}",
                        cache.getName(), key, exception.getMessage());
                // Don't throw - operation continues
            }

            @Override
            public void handleCacheEvictError(RuntimeException exception,
                    org.springframework.cache.Cache cache,
                    Object key) {
                log.warn("Cache EVICT failed for cache={}, key={}, error={}",
                        cache.getName(), key, exception.getMessage());
                // Don't throw - eviction failure is not critical
            }

            @Override
            public void handleCacheClearError(RuntimeException exception,
                    org.springframework.cache.Cache cache) {
                log.error("Cache CLEAR failed for cache={}, error={}",
                        cache.getName(), exception.getMessage());
                // Log as error but don't crash
            }
        };
    }
}
