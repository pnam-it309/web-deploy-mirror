// ProductProcessor.java
package udpm.hn.server.infrastructure.core.job.products.commonio;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import udpm.hn.server.entity.Brand;
import udpm.hn.server.entity.Category;
import udpm.hn.server.entity.Product;

import udpm.hn.server.infrastructure.core.job.products.commonio.CsvHistoryWriter;
import udpm.hn.server.infrastructure.core.job.products.model.request.ProductExcelRequest;
import udpm.hn.server.infrastructure.core.job.products.repository.BrandRowRepository;
import udpm.hn.server.infrastructure.core.job.products.repository.CategoryRowRepository;
import udpm.hn.server.infrastructure.core.job.products.repository.ProductExcelRepository;


@Slf4j
@Component
public class ProductProcessor implements ItemProcessor<ProductExcelRequest, Product> {

    @Setter(onMethod_ = @Autowired)
    private ProductExcelRepository productExcelRepository;

    @Setter(onMethod_ = @Autowired)
    private BrandRowRepository brandrowRepository;

    @Setter(onMethod_ = @Autowired)
    private CategoryRowRepository categoryrowRepository;

    @Setter(onMethod_ = @Autowired)
    private CsvHistoryWriter csvHistoryWriter;

    @Override
    public Product process(ProductExcelRequest item) throws Exception {
        // Validate required fields
        if (item.getSku() == null || item.getSku().trim().isEmpty()) {
            log.error("SKU is required for product");
            return null;
        }

        // Check if product with same SKU already exists
        if (productExcelRepository.existsBySku(item.getSku())) {
            log.warn("Product with SKU {} already exists", item.getSku());
            return null;
        }

        // Find or create brand
        Brand brand = brandrowRepository.findByName(item.getBrandName())
                .orElseGet(() -> {
                    Brand newBrand = new Brand();
                    newBrand.setName(item.getBrandName());
                    return brandrowRepository.save(newBrand);
                });

        // Find or create category
        Category category = categoryrowRepository.findByName(item.getCategoryName())
                .orElseGet(() -> {
                    Category newCategory = new Category();
                    newCategory.setName(item.getCategoryName());
                    return categoryrowRepository.save(newCategory);
                });

        // Create and return new product
        Product product = new Product();
        product.setSku(item.getSku());
        product.setName(item.getName());
        product.setSlug(generateSlug(item.getName()));
        product.setShortDescription(item.getShortDescription());
        product.setPrice(item.getPrice());
        product.setStockQuantity(item.getStockQuantity() != null ? item.getStockQuantity() : 0);
        product.setBrand(brand);
        product.setCategory(category);

        return product;
    }

    private String generateSlug(String name) {
        if (name == null) return null;
        return name.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-");
    }
}