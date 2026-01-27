package udpm.hn.server.core.customer.like.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ProductLikeService {

    private final ProductLikeRepository productLikeRepository;
    private final AppRepository appRepository;
    private final CustomerRepository customerRepository;

    @Transactional
    public boolean toggleLike(String appId, String customerId) {
        log.info("toggleLike called for App: {}, Customer: {}", appId, customerId);
        var existingLike = productLikeRepository.findByAppIdAndCustomerId(appId, customerId);

        if (existingLike.isPresent()) {
            log.info("Like exists. Deleting...");
            productLikeRepository.delete(existingLike.get());
            return false;
        } else {
            log.info("Like does not exist. Creating...");
            App app = appRepository.findById(appId)
                    .orElseThrow(() -> new RuntimeException("App not found"));
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new RuntimeException("Customer not found"));

            ProductLike like = new ProductLike();
            like.setApp(app);
            like.setCustomer(customer);
            productLikeRepository.save(like);
            log.info("Like saved.");
            return true;
        }
    }

    public Long getLikeCount(String appId) {
        return productLikeRepository.countByAppId(appId);
    }

    public boolean isLikedByCustomer(String appId, String customerId) {
        boolean exists = productLikeRepository.existsByAppIdAndCustomerId(appId, customerId);
        log.info("isLikedByCustomer: App: {}, Customer: {}, Exists: {}", appId, customerId, exists);
        return exists;
    }

    @Transactional
    public boolean toggleLikeByEmail(String appId, String email) {
        log.info("toggleLikeByEmail: {}", email);
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        log.info("Resolved Customer ID: {}", customer.getId());
        return toggleLike(appId, customer.getId());
    }

    public boolean isLikedByEmail(String appId, String email) {
        log.info("isLikedByEmail: {}", email);
        return customerRepository.findByEmail(email)
                .map(customer -> {
                    log.info("Resolved Customer ID: {}", customer.getId());
                    return isLikedByCustomer(appId, customer.getId());
                })
                .orElse(false);
    }

    public java.util.List<udpm.hn.server.core.customer.app.model.response.CustomerAppResponse> getLikedAppsByEmail(String email) {
        log.info("getLikedAppsByEmail: {}", email);
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        return productLikeRepository.findAllByCustomerId(customer.getId()).stream()
                .map(like -> {
                    App app = like.getApp();
                    udpm.hn.server.core.customer.app.model.response.CustomerAppResponse response = new udpm.hn.server.core.customer.app.model.response.CustomerAppResponse();
                    response.setId(app.getId());
                    response.setName(app.getName());
                    response.setThumbnail(app.getThumbnail());
                    if (app.getDomain() != null) {
                        response.setDomainName(app.getDomain().getName());
                    }
                    // Map other fields if necessary
                    return response;
                })
                .collect(java.util.stream.Collectors.toList());
    }
}
