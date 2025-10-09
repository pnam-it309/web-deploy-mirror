package udpm.hn.server.infrastructure.processing.batch.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import udpm.hn.server.entity.Brand;
import udpm.hn.server.entity.Category;
import udpm.hn.server.entity.Product;
import udpm.hn.server.infrastructure.core.constant.EntityStatus;

@Component
public class ProductItemProcessor implements ItemProcessor<Product, Product> {

    @Override
    public Product process(final Product product) throws Exception {
        // Set default status if not set
        if (product.getStatus() == null) {
            product.setStatus(EntityStatus.ACTIVE);
        }

        // Ensure brand is managed by setting up the relationship
        if (product.getBrand() != null && product.getBrand().getId() != null) {
            Brand brand = new Brand();
            brand.setId(product.getBrand().getId());
            product.setBrand(brand);
        }

        // Ensure category is managed by setting up the relationship
        if (product.getCategory() != null && product.getCategory().getId() != null) {
            Category category = new Category();
            category.setId(product.getCategory().getId());
            product.setCategory(category);
        }

        return product;
    }
}
