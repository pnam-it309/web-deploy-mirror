// ProductTasklet.java
package udpm.hn.server.infrastructure.core.job.products.commonio;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import udpm.hn.server.entity.Product;
import udpm.hn.server.infrastructure.core.job.products.model.request.ProductExcelRequest;
import udpm.hn.server.infrastructure.core.job.products.repository.ProductExcelRepository;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@Transactional
public class ProductTasklet implements Tasklet {

    private final ProductExcelRepository productExcelRepository;
    private final ItemReader<ProductExcelRequest> reader;
    private final ItemProcessor<ProductExcelRequest, Product> processor;

    public ProductTasklet(ProductExcelRepository productExcelRepository,
                          ItemReader<ProductExcelRequest> reader,
                          @Qualifier("productExcelProcessor") ItemProcessor<ProductExcelRequest, Product> processor) {
        this.productExcelRepository = productExcelRepository;
        this.reader = reader;
        this.processor = processor;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        List<Product> products = new ArrayList<>();
        ProductExcelRequest item;

        try {
            while ((item = reader.read()) != null) {
                Product product = processor.process(item);
                if (product != null) {
                    products.add(product);
                }
            }

            // Save all valid products
            if (!products.isEmpty()) {
                productExcelRepository.saveAll(products);
                log.info("Successfully imported {} products", products.size());
            }

            return RepeatStatus.FINISHED;
        } catch (Exception e) {
            log.error("Error processing products", e);
            throw e;
        }
    }
}