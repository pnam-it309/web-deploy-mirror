package udpm.hn.server.infrastructure.data.cloudinary;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class CloudinaryUploadImages {

    /**
     * Simplified implementation - Cloudinary upload disabled for now
     */
    public String uploadImage(byte[] imageBytes) {
        log.info("Cloudinary upload disabled - returning placeholder URL");
        return "https://placeholder.com/image.jpg";
    }

    /**
     * Simplified implementation - Cloudinary delete disabled for now
     */
    public String deleteImage(String publicId) {
        log.info("Cloudinary delete disabled - would delete image: {}", publicId);
        return "Image deleted successfully";
    }
}