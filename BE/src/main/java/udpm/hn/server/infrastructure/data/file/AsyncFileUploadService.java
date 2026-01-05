package udpm.hn.server.infrastructure.data.file;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Async File Upload Service
 * - Xử lý upload file bất đồng bộ
 * - Sử dụng CompletableFuture để parallel processing
 * - Thread pool riêng cho file upload
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AsyncFileUploadService {

    private final FileProcessingService fileProcessingService;

    /**
     * Upload single file asynchronously
     */
    @Async("fileUploadExecutor")
    public CompletableFuture<String> uploadFileAsync(MultipartFile file, String... subDirectories) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                log.info("Starting async upload for file: {}", file.getOriginalFilename());
                String filePath = fileProcessingService.storeFile(file, subDirectories);
                log.info("Completed async upload for file: {} -> {}", file.getOriginalFilename(), filePath);
                return filePath;
            } catch (Exception e) {
                log.error("Error uploading file: {}", file.getOriginalFilename(), e);
                throw new RuntimeException("Failed to upload file: " + file.getOriginalFilename(), e);
            }
        });
    }

    /**
     * Upload multiple files in parallel
     */
    @Async("fileUploadExecutor")
    public CompletableFuture<List<String>> uploadFilesAsync(List<MultipartFile> files, String... subDirectories) {
        log.info("Starting parallel upload for {} files", files.size());

        List<CompletableFuture<String>> uploadFutures = files.stream()
                .map(file -> uploadFileAsync(file, subDirectories))
                .collect(Collectors.toList());

        return CompletableFuture.allOf(uploadFutures.toArray(new CompletableFuture[0]))
                .thenApply(v -> uploadFutures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList()))
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Error in parallel file upload", ex);
                    } else {
                        log.info("Completed parallel upload for {} files", result.size());
                    }
                });
    }

    /**
     * Upload files with validation
     */
    @Async("fileUploadExecutor")
    public CompletableFuture<List<String>> uploadFilesWithValidation(
            List<MultipartFile> files,
            long maxSizeBytes,
            List<String> allowedExtensions,
            String... subDirectories) {

        return CompletableFuture.supplyAsync(() -> {
            // Validate files first
            for (MultipartFile file : files) {
                if (file.getSize() > maxSizeBytes) {
                    throw new RuntimeException(String.format(
                            "File %s exceeds maximum size of %d bytes",
                            file.getOriginalFilename(), maxSizeBytes));
                }

                String extension = fileProcessingService.getFileExtension(file.getOriginalFilename());
                if (!allowedExtensions.contains(extension.toLowerCase())) {
                    throw new RuntimeException(String.format(
                            "File %s has invalid extension. Allowed: %s",
                            file.getOriginalFilename(), String.join(", ", allowedExtensions)));
                }
            }

            // Upload in parallel after validation
            return uploadFilesAsync(files, subDirectories).join();
        });
    }

    /**
     * Delete file asynchronously
     */
    @Async("fileUploadExecutor")
    public CompletableFuture<Void> deleteFileAsync(String fileName) {
        return CompletableFuture.runAsync(() -> {
            try {
                log.info("Starting async delete for file: {}", fileName);
                fileProcessingService.deleteFile(fileName);
                log.info("Completed async delete for file: {}", fileName);
            } catch (Exception e) {
                log.error("Error deleting file: {}", fileName, e);
                throw new RuntimeException("Failed to delete file: " + fileName, e);
            }
        });
    }

    /**
     * Delete multiple files in parallel
     */
    @Async("fileUploadExecutor")
    public CompletableFuture<Void> deleteFilesAsync(List<String> fileNames) {
        log.info("Starting parallel delete for {} files", fileNames.size());

        List<CompletableFuture<Void>> deleteFutures = fileNames.stream()
                .map(this::deleteFileAsync)
                .collect(Collectors.toList());

        return CompletableFuture.allOf(deleteFutures.toArray(new CompletableFuture[0]))
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Error in parallel file deletion", ex);
                    } else {
                        log.info("Completed parallel deletion for {} files", fileNames.size());
                    }
                });
    }
}
