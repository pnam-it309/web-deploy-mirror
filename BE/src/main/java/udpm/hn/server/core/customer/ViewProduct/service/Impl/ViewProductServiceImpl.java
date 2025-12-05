package udpm.hn.server.core.customer.ViewProduct.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import udpm.hn.server.core.customer.ViewProduct.service.ViewProductService;
import udpm.hn.server.entity.Product;
import udpm.hn.server.repository.ProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ViewProductServiceImpl implements ViewProductService {
    private final ProductRepository productRepository;

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }
}
