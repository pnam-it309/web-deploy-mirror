package udpm.hn.server.infrastructure.processing.batch.config;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;
import udpm.hn.server.entity.Brand;
import udpm.hn.server.entity.Category;
import udpm.hn.server.entity.Product;
import udpm.hn.server.infrastructure.core.constant.EntityStatus;
import udpm.hn.server.infrastructure.processing.batch.processor.ProductItemProcessor;

@Configuration
@RequiredArgsConstructor
public class ProductImportBatchConfig {

    private static final String JOB_NAME = "productImportJob";
    private static final String STEP_NAME = "productImportStep";
    
    private final EntityManagerFactory entityManagerFactory;

    @Bean(name = "productImportReader")
    public FlatFileItemReader<Product> productItemReader() {
        return new FlatFileItemReaderBuilder<Product>()
                .name("productItemReader")
                .resource(new ClassPathResource("data/products.csv"))
                .delimited()
                .names(new String[]{"sku", "name", "price", "stockQuantity", "brandId", "categoryId"})
                .fieldSetMapper(new BeanWrapperFieldSetMapper<Product>() {{
                    setTargetType(Product.class);
                }})
                .build();
    }

    @Bean(name = "productImportProcessor")
    public ProductItemProcessor productItemProcessor() {
        return new ProductItemProcessor();
    }

    @Bean(name = "productImportWriter")
    public JpaItemWriter<Product> productItemWriter() {
        return new JpaItemWriterBuilder<Product>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }

    @Bean(name = "productImportStep")
    public Step productImportStep(JobRepository jobRepository, 
                                PlatformTransactionManager transactionManager,
                                FlatFileItemReader<Product> productImportReader,
                                ProductItemProcessor productImportProcessor,
                                JpaItemWriter<Product> productImportWriter) {
        return new StepBuilder(STEP_NAME, jobRepository)
                .<Product, Product>chunk(100, transactionManager)
                .reader(productImportReader)
                .processor(productImportProcessor)
                .writer(productImportWriter)
                .build();
    }

    @Bean(name = "productImportJob")
    public Job productImportJob(JobRepository jobRepository, 
                              Step productImportStep) {
        return new JobBuilder(JOB_NAME, jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(productImportStep)
                .build();
    }
}
