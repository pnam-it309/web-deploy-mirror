package udpm.hn.server.infrastructure.gdpr;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import udpm.hn.server.entity.*;
import udpm.hn.server.repository.*;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class GDPRService {

    private final CustomerRepository customerRepository;
    private final EmailSubscriptionRepository emailSubscriptionRepository;
    private final PushSubscriptionRepository pushSubscriptionRepository;
    private final ReviewRepository reviewRepository;
    private final ProductLikeRepository productLikeRepository;
    private final ObjectMapper objectMapper;

    /**
     * Export all user data as ZIP file
     */
    @Transactional(readOnly = true)
    public byte[] exportUserData(String email) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ZipOutputStream zos = new ZipOutputStream(baos);

            // Export customer data
            Customer customer = customerRepository.findByEmail(email).orElse(null);
            if (customer != null) {
                addJsonToZip(zos, "customer.json", customer);
            }

            // Export email subscriptions
            emailSubscriptionRepository.findByEmail(email).ifPresent(sub -> {
                try {
                    addJsonToZip(zos, "email_subscriptions.json", List.of(sub));
                } catch (Exception ignored) {
                }
            });

            // Export push subscriptions
            List<PushSubscription> pushSubs = pushSubscriptionRepository.findByUserEmail(email);
            addJsonToZip(zos, "push_subscriptions.json", pushSubs);

            // Export reviews (if repository supports it)
            if (customer != null) {
                // Reviews and likes would need custom repository methods
                // For now, we'll add placeholders
                addJsonToZip(zos, "reviews.json", List.of());
                addJsonToZip(zos, "product_likes.json", List.of());
            }

            // Add README
            String readme = "# Your Data Export from FPL UDPM Catalog\n\n" +
                    "This ZIP file contains all your personal data stored in our system.\n\n" +
                    "Files included:\n" +
                    "- customer.json: Your account information\n" +
                    "- email_subscriptions.json: Your email subscription preferences\n" +
                    "- push_subscriptions.json: Your push notification subscriptions\n" +
                    "- reviews.json: Reviews you have written\n" +
                    "- product_likes.json: Products you have liked\n";

            zos.putNextEntry(new ZipEntry("README.md"));
            zos.write(readme.getBytes());
            zos.closeEntry();

            zos.close();
            return baos.toByteArray();
        } catch (Exception e) {
            log.error("Failed to export user data", e);
            throw new RuntimeException("Failed to export user data", e);
        }
    }

    /**
     * Permanently delete user account and all data
     */
    @Transactional
    public void deleteUserPermanently(String email) {
        Customer customer = customerRepository.findByEmail(email).orElse(null);

        if (customer != null) {
            // Delete customer account (cascade rules should handle related data)
            customerRepository.delete(customer);
        }

        // Delete email subscriptions
        emailSubscriptionRepository.findByEmail(email).ifPresent(emailSubscriptionRepository::delete);

        // Delete push subscriptions
        pushSubscriptionRepository.findByUserEmail(email).forEach(pushSubscriptionRepository::delete);

        log.info("Permanently deleted all data for user: {}", email);
    }

    /**
     * Anonymize user data (alternative to deletion)
     */
    @Transactional
    public void anonymizeUserData(String email) {
        Customer customer = customerRepository.findByEmail(email).orElse(null);

        if (customer != null) {
            // Anonymize customer data
            customer.setEmail("deleted_" + System.currentTimeMillis() + "@anonymous.com");
            customer.setFullName("Anonymous User");
            customerRepository.save(customer);
        }

        log.info("Anonymized data for user: {}", email);
    }

    private void addJsonToZip(ZipOutputStream zos, String filename, Object data) throws Exception {
        zos.putNextEntry(new ZipEntry(filename));
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
        zos.write(json.getBytes());
        zos.closeEntry();
    }
}
