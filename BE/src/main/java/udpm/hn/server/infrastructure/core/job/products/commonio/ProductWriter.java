// ProductWriter.java
package udpm.hn.server.infrastructure.core.job.products.commonio;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import udpm.hn.server.entity.Product;
import udpm.hn.server.infrastructure.core.job.products.repository.ProductExcelRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductWriter implements ItemWriter<Product> {

    private final ProductExcelRepository productExcelRepository;

    @Override
    public void write(Chunk<? extends Product> chunk) throws Exception {
        if (chunk != null) {
            productExcelRepository.saveAll(chunk.getItems());
            log.info("Saved {} products", chunk.getItems().size());
        }
    }
}