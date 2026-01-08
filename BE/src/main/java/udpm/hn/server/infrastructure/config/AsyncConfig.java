package udpm.hn.server.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Cấu hình Async cho ứng dụng
 * - Kích hoạt xử lý bất đồng bộ với @Async
 * - Cung cấp ThreadPoolTaskExecutor tùy chỉnh để quản lý thread pool
 * - Hỗ trợ multi-thread và tránh blocking IO
 */
@Configuration
@EnableAsync
public class AsyncConfig {

    /**
     * Bean TaskExecutor tùy chỉnh
     * - CorePoolSize: Số thread tối thiểu luôn sẵn sàng
     * - MaxPoolSize: Số thread tối đa khi queue đầy
     * - QueueCapacity: Số task chờ trong hàng đợi trước khi tạo thread mới
     * - ThreadNamePrefix: Tiền tố để dễ debug trong logs
     */
    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5); // Thread pool cơ bản
        executor.setMaxPoolSize(10); // Thread pool tối đa
        executor.setQueueCapacity(100); // Hàng đợi task
        executor.setThreadNamePrefix("Async-");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        executor.initialize();
        return executor;
    }

    /**
     * Bean Executor riêng cho email (nếu cần tách biệt)
     */
    @Bean(name = "emailExecutor")
    public Executor emailExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(50);
        executor.setThreadNamePrefix("Email-");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(30);
        executor.initialize();
        return executor;
    }
}
