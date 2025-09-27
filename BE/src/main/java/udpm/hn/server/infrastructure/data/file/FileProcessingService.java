package udpm.hn.server.infrastructure.data.file;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class FileProcessingService {

    private final Path fileStorageLocation;

    public FileProcessingService(@Value("${file.upload-dir:uploads}") String uploadDir) {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create upload directory", ex);
        }
    }

    public String storeFile(MultipartFile file, String... subDirectories) {
        // Normalize file name
        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        
        try {
            // Check if the file's name contains invalid characters
            if (originalFileName.contains("..")) {
                throw new RuntimeException("Sorry! Filename contains invalid path sequence " + originalFileName);
            }

            // Generate a unique filename
            String fileExtension = FilenameUtils.getExtension(originalFileName);
            String fileName = String.format("%s.%s", UUID.randomUUID(), fileExtension);
            
            // Create target directory if subdirectories are provided
            Path targetLocation = this.fileStorageLocation;
            if (subDirectories != null && subDirectories.length > 0) {
                targetLocation = Paths.get(this.fileStorageLocation.toString(), subDirectories);
                Files.createDirectories(targetLocation);
            }
            
            // Copy file to the target location
            Path targetPath = targetLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            
            // Return the path relative to the storage location
            return this.fileStorageLocation.relativize(targetPath).toString();
            
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + originalFileName + ". Please try again!", ex);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException("File not found " + fileName, ex);
        }
    }

    public void deleteFile(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Files.deleteIfExists(filePath);
        } catch (IOException ex) {
            throw new RuntimeException("Could not delete file " + fileName, ex);
        }
    }

    public List<String> listFiles(String directory) throws IOException {
        try (Stream<Path> walk = Files.walk(this.fileStorageLocation.resolve(directory), 1)) {
            return walk
                    .filter(Files::isRegularFile)
                    .map(path -> this.fileStorageLocation.relativize(path).toString())
                    .collect(Collectors.toList());
        }
    }

    public void createDirectory(String directoryName) throws IOException {
        Path dirPath = this.fileStorageLocation.resolve(directoryName);
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }
    }

    public String getFileExtension(String fileName) {
        return FilenameUtils.getExtension(fileName);
    }

    public long getFileSize(String fileName) throws IOException {
        Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
        return Files.size(filePath);
    }
}
