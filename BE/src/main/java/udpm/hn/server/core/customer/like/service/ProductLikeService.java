package udpm.hn.server.core.customer.like.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import udpm.hn.server.entity.App;
import udpm.hn.server.entity.Customer;
import udpm.hn.server.entity.ProductLike;
import udpm.hn.server.repository.AppRepository;
import udpm.hn.server.repository.CustomerRepository;
import udpm.hn.server.repository.ProductLikeRepository;

@Service
@RequiredArgsConstructor
public class ProductLikeService {

    private final ProductLikeRepository productLikeRepository;
    private final AppRepository appRepository;
    private final CustomerRepository customerRepository;

    @Transactional
    public void toggleLike(String appId, String customerId) {
        var existingLike = productLikeRepository.findByAppIdAndCustomerId(appId, customerId);

        if (existingLike.isPresent()) {
            // Unlike
            productLikeRepository.delete(existingLike.get());
        } else {
            // Like
            App app = appRepository.findById(appId)
                    .orElseThrow(() -> new RuntimeException("App not found"));
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new RuntimeException("Customer not found"));

            ProductLike like = new ProductLike();
            like.setApp(app);
            like.setCustomer(customer);
            productLikeRepository.save(like);
        }
    }

    public Long getLikeCount(String appId) {
        return productLikeRepository.countByAppId(appId);
    }

    public boolean isLikedByCustomer(String appId, String customerId) {
        return productLikeRepository.existsByAppIdAndCustomerId(appId, customerId);
    }
}
