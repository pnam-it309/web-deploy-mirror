package udpm.hn.server.infrastructure.data.storage;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Slf4j
@Service
@Primary
@RequiredArgsConstructor
@ConditionalOnBean(AmazonS3.class)
public class CloudStorageService implements StorageService {

    private final AmazonS3 s3Client;

    @Value("${aws.s3.bucket-name:}")
    private String bucketName;

    @Value("${aws.s3.region:ap-southeast-1}")
    private String region;

    public String uploadFile(MultipartFile file, String folder) {
        if (StringUtils.isEmpty(bucketName)) {
            throw new IllegalStateException("S3 bucket name is not configured");
        }
        
        try {
            String fileName = generateFileName(file.getOriginalFilename());
            String filePath = folder != null ? folder + "/" + fileName : fileName;
            
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());
            
            try (InputStream inputStream = file.getInputStream()) {
                s3Client.putObject(new PutObjectRequest(bucketName, filePath, inputStream, metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            }
            
            return getFileUrl(filePath);
        } catch (IOException e) {
            log.error("Error uploading file to S3: {}", e.getMessage());
            throw new StorageException("Failed to upload file", e);
        }
    }

    @Async
    public void deleteFile(String fileUrl) {
        if (StringUtils.isEmpty(bucketName)) {
            log.warn("S3 bucket name is not configured, cannot delete file: {}", fileUrl);
            return;
        }
        
        try {
            String fileName = extractFileNameFromUrl(fileUrl);
            s3Client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
        } catch (Exception e) {
            log.error("Error deleting file from S3: {}", e.getMessage());
            throw new StorageException("Failed to delete file", e);
        }
    }

    public String getFileUrl(String filePath) {
        if (StringUtils.isEmpty(bucketName)) {
            throw new IllegalStateException("S3 bucket name is not configured");
        }
        return String.format("https://%s.s3.%s.amazonaws.com/%s", 
            bucketName, region, filePath);
    }

    public byte[] downloadFile(String fileUrl) {
        if (StringUtils.isEmpty(bucketName)) {
            throw new IllegalStateException("S3 bucket name is not configured");
        }
        
        try {
            String fileName = extractFileNameFromUrl(fileUrl);
            S3Object s3Object = s3Client.getObject(new GetObjectRequest(bucketName, fileName));
            return s3Object.getObjectContent().readAllBytes();
        } catch (Exception e) {
            log.error("Error downloading file from S3: {}", e.getMessage());
            throw new StorageException("Failed to download file", e);
        }
    }

    private String generateFileName(String originalFileName) {
        return UUID.randomUUID().toString() + "_" + originalFileName;
    }
    
    private String extractFileNameFromUrl(String fileUrl) {
        // Handle both full URL and just the key
        if (fileUrl.contains("amazonaws.com/")) {
            return fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        }
        return fileUrl;
    }
}




