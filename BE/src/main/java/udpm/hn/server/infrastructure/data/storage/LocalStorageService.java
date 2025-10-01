package udpm.hn.server.infrastructure.data.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Slf4j
@Service
@ConditionalOnBean(name = "s3Client")
class LocalStorageService implements StorageService {

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    @Override
    public String uploadFile(MultipartFile file, String folder) {
        try {
            String fileName = generateFileName(file.getOriginalFilename());
            String filePath = folder != null ? folder + "/" + fileName : fileName;
            Path targetLocation = Paths.get(uploadDir).resolve(filePath);

            Files.createDirectories(targetLocation.getParent());
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);
            }

            return targetLocation.toString();
        } catch (IOException e) {
            log.error("Error uploading file: {}", e.getMessage());
            throw new StorageException("Failed to upload file", e);
        }
    }

    @Override
    public void deleteFile(String fileUrl) {
        try {
            Path filePath = Paths.get(fileUrl);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            log.error("Error deleting file: {}", e.getMessage());
            throw new StorageException("Failed to delete file", e);
        }
    }

    @Override
    public String getFileUrl(String filePath) {
        return Paths.get(uploadDir).resolve(filePath).toString();
    }

    @Override
    public byte[] downloadFile(String fileUrl) {
        try {
            return Files.readAllBytes(Paths.get(fileUrl));
        } catch (IOException e) {
            log.error("Error reading file: {}", e.getMessage());
            throw new StorageException("Failed to read file", e);
        }
    }

    private String generateFileName(String originalFileName) {
        return UUID.randomUUID().toString() + "_" + originalFileName;
    }
}
