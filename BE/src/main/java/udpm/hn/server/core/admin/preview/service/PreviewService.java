package udpm.hn.server.core.admin.preview.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import udpm.hn.server.repository.AppRepository;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class PreviewService {

    private final AppRepository appRepository;

    // In-memory store for preview tokens (valid for 24 hours)
    // In production, use Redis or database
    private final Map<String, PreviewToken> previewTokens = new ConcurrentHashMap<>();

    private static class PreviewToken {
        String appId;
        long expiresAt;

        PreviewToken(String appId, long expiresAt) {
            this.appId = appId;
            this.expiresAt = expiresAt;
        }

        boolean isValid() {
            return System.currentTimeMillis() < expiresAt;
        }
    }

    public String generatePreviewToken(String appId) {
        if (!appRepository.existsById(appId)) {
            throw new RuntimeException("App not found");
        }

        // Generate secure random token
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);

        // Store token with 24-hour expiration
        long expiresAt = System.currentTimeMillis() + (24 * 60 * 60 * 1000);
        previewTokens.put(token, new PreviewToken(appId, expiresAt));

        // Cleanup expired tokens
        cleanupExpiredTokens();

        return token;
    }

    public String validatePreviewToken(String token) {
        PreviewToken previewToken = previewTokens.get(token);

        if (previewToken == null || !previewToken.isValid()) {
            return null; // Invalid or expired
        }

        return previewToken.appId;
    }

    private void cleanupExpiredTokens() {
        previewTokens.entrySet().removeIf(entry -> !entry.getValue().isValid());
    }
}
