package udpm.hn.server.infrastructure.core.config;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * Cấu hình Spring Batch cho ứng dụng
 * - Kích hoạt xử lý batch với @EnableBatchProcessing
 * - Cấu hình JobRepository và JobLauncher
 * - Quản lý transaction và kết nối cơ sở dữ liệu
 */

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    // Nguồn dữ liệu để lưu trữ thông tin batch
    private final DataSource dataSource;
    
    // Quản lý transaction cho các job batch
    private final PlatformTransactionManager transactionManager;

    /**
     * Khởi tạo BatchConfig với các dependency cần thiết
     * @param dataSource Nguồn dữ liệu để lưu trữ thông tin batch
     * @param transactionManager Quản lý transaction cho các job
     */
    public BatchConfig(DataSource dataSource, PlatformTransactionManager transactionManager) {
        this.dataSource = dataSource;
        this.transactionManager = transactionManager;
    }

    /**
     * Tạo bean JobRepository để lưu trữ thông tin về các job, step và execution
     * @return Đối tượng JobRepository đã được cấu hình
     * @throws Exception Nếu có lỗi khi khởi tạo JobRepository
     */
    @Bean
    public JobRepository jobRepository() throws Exception {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        factory.setDataSource(dataSource);  // Thiết lập nguồn dữ liệu
        factory.setTransactionManager(transactionManager);  // Thiết lập transaction manager
        factory.setIsolationLevelForCreate("ISOLATION_DEFAULT");  // Mức cô lập mặc định
        factory.setTablePrefix("BATCH_");  // Tiền tố cho các bảng batch
        factory.afterPropertiesSet();  // Xác thực và khởi tạo
        return factory.getObject();
    }

    /**
     * Tạo bean JobLauncher để khởi chạy các job batch
     * Sử dụng SimpleAsyncTaskExecutor để chạy các job bất đồng bộ
     * 
     * @return Đối tượng JobLauncher đã được cấu hình
     * @throws Exception Nếu có lỗi khi khởi tạo JobLauncher
     */
    @Bean
    public JobLauncher jobLauncher() throws Exception {
        TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
        jobLauncher.setJobRepository(jobRepository());  // Thiết lập job repository
        jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());  // Sử dụng executor bất đồng bộ
        jobLauncher.afterPropertiesSet();  // Xác thực và khởi tạo
        return jobLauncher;
    }
}
