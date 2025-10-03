package udpm.hn.server.infrastructure.data.storage;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    String uploadFile(MultipartFile file, String folder);

    void deleteFile(String fileUrl);

    String getFileUrl(String filePath);

    byte[] downloadFile(String fileUrl);
}
