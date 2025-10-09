// ProductJobConfig.java
package udpm.hn.server.infrastructure.core.job.products.jobconfg;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.excel.RowMapper;
import org.springframework.batch.item.excel.poi.PoiItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;
import udpm.hn.server.entity.Product;
import udpm.hn.server.infrastructure.core.job.products.commonio.ProductProcessor;
import udpm.hn.server.infrastructure.core.job.products.commonio.ProductRowMapper;
import udpm.hn.server.infrastructure.core.job.products.commonio.ProductWriter;
import udpm.hn.server.infrastructure.core.job.products.model.request.ProductExcelRequest;
import udpm.hn.server.infrastructure.core.job.products.repository.ProductExcelRepository;

@Slf4j
@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class ProductJobConfig {

    private final PlatformTransactionManager transactionManager;
    private final ProductExcelRepository productExcelRepository;

    @Bean
    public ItemReader<ProductExcelRequest> excelProductReader() {
        PoiItemReader<ProductExcelRequest> reader = new PoiItemReader<>();
        reader.setLinesToSkip(1); // Skip header
        reader.setRowMapper(excelRowMapper());
        return reader;
    }

    @Bean
    public RowMapper<ProductExcelRequest> excelRowMapper() {
        return new ProductRowMapper();
    }

    @Bean
    public ItemProcessor<ProductExcelRequest, Product> productExcelProcessor() {
        return new ProductProcessor();
    }

    @Bean
    public ItemWriter<Product> productExcelWriter() {
        return new ProductWriter(productExcelRepository);
    }

    @Bean
    public Step importProductStep(JobRepository jobRepository, ItemReader<ProductExcelRequest> reader,
                                  ItemProcessor<ProductExcelRequest, Product> processor,
                                  ItemWriter<Product> writer) {
        return new StepBuilder("importProductStep", jobRepository)
                .<ProductExcelRequest, Product>chunk(100, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Job importProductJob(JobRepository jobRepository, Step importProductStep) {
        return new JobBuilder("importProductJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(importProductStep)
                .end()
                .build();
    }
}