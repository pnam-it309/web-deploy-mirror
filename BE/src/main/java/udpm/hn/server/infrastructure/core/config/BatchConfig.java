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
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;

/**
 * Cấu hình Spring Batch cho ứng dụng
 * - Kích hoạt xử lý batch với @EnableBatchProcessing
 * - Cấu hình JobRepository và JobLauncher
 * - Quản lý transaction và kết nối cơ sở dữ liệu
 */

@Configuration
@EnableBatchProcessing
@EnableTransactionManagement
public class BatchConfig {

    private final DataSource dataSource;
    private final LocalContainerEntityManagerFactoryBean entityManagerFactory;

    public BatchConfig(DataSource dataSource, LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        this.dataSource = dataSource;
        this.entityManagerFactory = entityManagerFactory;
    }

    /**
     * Tạo bean JobRepository để lưu trữ thông tin về các job, step và execution
     * @return Đối tượng JobRepository đã được cấu hình
     * @throws Exception Nếu có lỗi khi khởi tạo JobRepository
     */
    @Bean
    public JobRepository jobRepository(PlatformTransactionManager transactionManager) throws Exception {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        factory.setDataSource(dataSource);
        factory.setTransactionManager(transactionManager);
        factory.setIsolationLevelForCreate("ISOLATION_DEFAULT");
        factory.setTablePrefix("BATCH_");
        factory.afterPropertiesSet();
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
    public JobLauncher jobLauncher(JobRepository jobRepository) throws Exception {
        TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor("batch-"));
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }
    
    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory.getObject());
        return transactionManager;
    }
}
