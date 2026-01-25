package udpm.hn.server.infrastructure.media;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MediaValidationService {

    private final Tika tika = new Tika();

    @Value("${media.validation.max-image-size-mb:10}")
    private int maxImageSizeMB;

    @Value("${media.validation.max-video-size-mb:100}")
    private int maxVideoSizeMB;

    @Value("${media.validation.allowed-image-types:jpg,jpeg,png,gif,webp}")
    private String allowedImageTypes;

    @Value("${media.validation.allowed-video-types:mp4,webm,avi,mov}")
    private String allowedVideoTypes;

    @Value("${media.validation.virus-scan-enabled:false}")
    private boolean virusScanEnabled;

    @Value("${media.validation.virus-scan-api:}")
    private String virusScanApi;

    @Value("${media.watermark.enabled:false}")
    private boolean watermarkEnabled;

    @Value("${media.watermark.text:FPL UDPM Catalog}")
    private String watermarkText;

    @Value("${media.watermark.opacity:0.3}")
    private float watermarkOpacity;

    @Value("${media.watermark.position:bottom-right}")
    private String watermarkPosition;

    /**
     * Validate uploaded file
     */
    public ValidationResult validate(MultipartFile file) {
        ValidationResult result = new ValidationResult();

        if (file == null || file.isEmpty()) {
            result.setValid(false);
            result.setError("File is empty");
            return result;
        }

        // 1. Detect MIME type
        String mimeType;
        try {
            mimeType = tika.detect(file.getBytes());
            result.setMimeType(mimeType);
        } catch (IOException e) {
            log.error("Failed to detect MIME type", e);
            result.setValid(false);
            result.setError("Failed to read file");
            return result;
        }

        // 2. Validate file type
        boolean isImage = mimeType.startsWith("image/");
        boolean isVideo = mimeType.startsWith("video/");
        result.setImage(isImage);
        result.setVideo(isVideo);

        if (!isImage && !isVideo) {
            result.setValid(false);
            result.setError("File must be an image or video");
            return result;
        }

        // 3. Validate file extension against allowed types
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            result.setValid(false);
            result.setError("Invalid filename");
            return result;
        }

        String extension = getFileExtension(originalFilename).toLowerCase();
        result.setExtension(extension);

        if (isImage) {
            List<String> allowed = Arrays.asList(allowedImageTypes.split(","));
            if (!allowed.contains(extension)) {
                result.setValid(false);
                result.setError("Image type not allowed. Allowed types: " + allowedImageTypes);
                return result;
            }
        } else {
            List<String> allowed = Arrays.asList(allowedVideoTypes.split(","));
            if (!allowed.contains(extension)) {
                result.setValid(false);
                result.setError("Video type not allowed. Allowed types: " + allowedVideoTypes);
                return result;
            }
        }

        // 4. Validate file size
        long fileSizeBytes = file.getSize();
        long fileSizeMB = fileSizeBytes / (1024 * 1024);
        result.setFileSizeMB(fileSizeMB);

        if (isImage && fileSizeMB > maxImageSizeMB) {
            result.setValid(false);
            result.setError("Image size exceeds " + maxImageSizeMB + "MB limit");
            return result;
        }

        if (isVideo && fileSizeMB > maxVideoSizeMB) {
            result.setValid(false);
            result.setError("Video size exceeds " + maxVideoSizeMB + "MB limit");
            return result;
        }

        // 5. Virus scan (if enabled)
        if (virusScanEnabled) {
            boolean virusFree = scanForVirus(file);
            if (!virusFree) {
                result.setValid(false);
                result.setError("File failed virus scan");
                return result;
            }
        }

        result.setValid(true);
        return result;
    }

    /**
     * Apply watermark to image
     */
    public byte[] applyWatermark(byte[] imageBytes) throws IOException {
        if (!watermarkEnabled) {
            return imageBytes;
        }

        ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
        BufferedImage original = ImageIO.read(bais);

        if (original == null) {
            log.warn("Failed to read image for watermarking");
            return imageBytes;
        }

        Graphics2D g2d = original.createGraphics();

        // Set rendering hints for better quality
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Set font and color
        int fontSize = original.getWidth() / 30;
        Font font = new Font("Arial", Font.BOLD, fontSize);
        g2d.setFont(font);

        // Set transparency
        AlphaComposite alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, watermarkOpacity);
        g2d.setComposite(alphaChannel);
        g2d.setColor(Color.WHITE);

        // Calculate position
        FontMetrics fontMetrics = g2d.getFontMetrics();
        int textWidth = fontMetrics.stringWidth(watermarkText);
        int textHeight = fontMetrics.getHeight();

        int x, y;
        switch (watermarkPosition.toLowerCase()) {
            case "top-left":
                x = 10;
                y = textHeight + 10;
                break;
            case "top-right":
                x = original.getWidth() - textWidth - 10;
                y = textHeight + 10;
                break;
            case "bottom-left":
                x = 10;
                y = original.getHeight() - 10;
                break;
            case "bottom-right":
            default:
                x = original.getWidth() - textWidth - 10;
                y = original.getHeight() - 10;
                break;
            case "center":
                x = (original.getWidth() - textWidth) / 2;
                y = (original.getHeight() - textHeight) / 2 + fontMetrics.getAscent();
                break;
        }

        // Draw watermark
        g2d.drawString(watermarkText, x, y);
        g2d.dispose();

        // Convert back to bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(original, "png", baos);
        return baos.toByteArray();
    }

    /**
     * Scan file for viruses using external API
     */
    /**
     * Scan file for viruses using external API
     */
    private boolean scanForVirus(MultipartFile file) {
        if (virusScanApi == null || virusScanApi.isEmpty()) {
            log.warn("Virus scan API not configured, skipping scan");
            return true; // Assume safe if no API configured
        }

        try {
            okhttp3.OkHttpClient client = new okhttp3.OkHttpClient();

            okhttp3.RequestBody requestBody = new okhttp3.MultipartBody.Builder()
                    .setType(okhttp3.MultipartBody.FORM)
                    .addFormDataPart("file", file.getOriginalFilename(),
                            okhttp3.RequestBody.create(file.getBytes(), okhttp3.MediaType.parse(file.getContentType())))
                    .build();

            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url(virusScanApi)
                    .post(requestBody)
                    .build();

            try (okhttp3.Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    log.error("Virus scan API returned error: {}", response.code());
                    // Fail-safe: allow upload if scan service is down, or configure to fail
                    return true;
                }

                String responseBody = response.body() != null ? response.body().string() : "";
                // Simple check for "true" or "clean" or JSON { "safe": true }
                // Adjust logic based on actual API contract
                boolean isSafe = responseBody.toLowerCase().contains("true")
                        || responseBody.toLowerCase().contains("clean")
                        || responseBody.toLowerCase().contains("safe");

                log.info("Virus scan result for {}: {}", file.getOriginalFilename(), isSafe ? "CLEAN" : "INFECTED");
                return isSafe;
            }
        } catch (Exception e) {
            log.error("Virus scan failed", e);
            // Fail-safe: allow upload even if scan fails
            return true;
        }
    }

    private String getFileExtension(String filename) {
        int lastDot = filename.lastIndexOf('.');
        if (lastDot == -1) {
            return "";
        }
        return filename.substring(lastDot + 1);
    }

    /**
     * Validation result class
     */
    public static class ValidationResult {
        private boolean valid;
        private String error;
        private String mimeType;
        private String extension;
        private boolean isImage;
        private boolean isVideo;
        private long fileSizeMB;

        // Getters and setters
        public boolean isValid() {
            return valid;
        }

        public void setValid(boolean valid) {
            this.valid = valid;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public String getMimeType() {
            return mimeType;
        }

        public void setMimeType(String mimeType) {
            this.mimeType = mimeType;
        }

        public String getExtension() {
            return extension;
        }

        public void setExtension(String extension) {
            this.extension = extension;
        }

        public boolean isImage() {
            return isImage;
        }

        public void setImage(boolean image) {
            isImage = image;
        }

        public boolean isVideo() {
            return isVideo;
        }

        public void setVideo(boolean video) {
            isVideo = video;
        }

        public long getFileSizeMB() {
            return fileSizeMB;
        }

        public void setFileSizeMB(long fileSizeMB) {
            this.fileSizeMB = fileSizeMB;
        }
    }
}
