// FileUploadServiceImpl.java
package udpm.hn.server.infrastructure.core.job.products.upload.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import udpm.hn.server.infrastructure.core.job.products.upload.FileUploadService;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileUploadServiceImpl implements FileUploadService {

    private static final String UPLOAD_DIR = "uploads/";

    @Override
    public void uploadFile(MultipartFile file) throws Exception {
        try {
            // Create upload directory if not exists
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Save the file
            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(filename);
            file.transferTo(filePath);

            log.info("File uploaded successfully: {}", filePath);
        } catch (Exception e) {
            log.error("Error uploading file", e);
            throw new Exception("Could not upload file: " + e.getMessage());
        }
    }

    @Override
    public void save(InputStream file) throws IOException, InvalidFormatException {

    }
}