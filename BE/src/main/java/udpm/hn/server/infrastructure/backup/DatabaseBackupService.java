package udpm.hn.server.infrastructure.backup;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.zip.GZIPOutputStream;
import com.amazonaws.services.s3.AmazonS3;

@Service
@RequiredArgsConstructor
@Slf4j
public class DatabaseBackupService {

    @Value("${spring.datasource.username:root}")
    private String dbUsername;

    @Value("${spring.datasource.password:}")
    private String dbPassword;

    @Value("${spring.datasource.url:}")
    private String dbUrl;

    @Value("${backup.s3-bucket:fpl-catalog-backups}")
    private String s3Bucket;

    @Value("${backup.retention-days:30}")
    private int retentionDays;

    private final AmazonS3 s3Client;

    private static final String BACKUP_DIR = "backups/";

    /**
     * Scheduled backup - runs daily at 2 AM
     */
    @Scheduled(cron = "${backup.schedule:0 0 2 * * ?}")
    public void createScheduledBackup() {
        log.info("Starting scheduled database backup");
        createBackup();
    }

    /**
     * Create database backup
     */
    public String createBackup() {
        try {
            // Create backup directory if not exists
            Files.createDirectories(Paths.get(BACKUP_DIR));

            // Extract database name from URL
            String dbName = extractDatabaseName(dbUrl);
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String backupFileName = String.format("backup_%s_%s.sql", dbName, timestamp);
            String backupFilePath = BACKUP_DIR + backupFileName;

            // Execute mysqldump command
            String command = String.format(
                    "mysqldump -u%s -p%s %s --result-file=%s",
                    dbUsername,
                    dbPassword,
                    dbName,
                    backupFilePath);

            Process process = Runtime.getRuntime().exec(command);
            int exitCode = process.waitFor();

            if (exitCode != 0) {
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String errorLine;
                while ((errorLine = errorReader.readLine()) != null) {
                    log.error("mysqldump error: {}", errorLine);
                }
                throw new RuntimeException("mysqldump failed with exit code: " + exitCode);
            }

            // Compress backup file
            String gzipFilePath = compressBackup(backupFilePath);

            // Upload to S3
            uploadToS3(gzipFilePath);

            // Delete uncompressed backup
            Files.deleteIfExists(Paths.get(backupFilePath));

            // Clean old backups
            cleanOldBackups();

            log.info("Backup created successfully: {}", gzipFilePath);
            return gzipFilePath;

        } catch (Exception e) {
            log.error("Failed to create backup", e);
            throw new RuntimeException("Backup failed", e);
        }
    }

    /**
     * Compress backup file using gzip
     */
    private String compressBackup(String sourceFile) throws IOException {
        String gzipFile = sourceFile + ".gz";

        try (FileInputStream fis = new FileInputStream(sourceFile);
                FileOutputStream fos = new FileOutputStream(gzipFile);
                GZIPOutputStream gzipOS = new GZIPOutputStream(fos)) {

            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) != -1) {
                gzipOS.write(buffer, 0, len);
            }
        }

        return gzipFile;
    }

    /**
     * Delete backups older than retention period
     */
    private void cleanOldBackups() throws IOException {
        Path backupDir = Paths.get(BACKUP_DIR);
        long cutoffTime = System.currentTimeMillis() - (retentionDays * 24L * 60 * 60 * 1000);

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(backupDir, "*.gz")) {
            for (Path entry : stream) {
                long lastModified = Files.getLastModifiedTime(entry).toMillis();
                if (lastModified < cutoffTime) {
                    Files.delete(entry);
                    log.info("Deleted old backup: {}", entry.getFileName());
                }
            }
        }
    }

    /**
     * Extract database name from JDBC URL
     */
    private String extractDatabaseName(String jdbcUrl) {
        // jdbc:mysql://localhost:3306/catalog_web?params...
        String[] parts = jdbcUrl.split("/");
        if (parts.length < 4) {
            return "catalog_web"; // default
        }
        String dbPart = parts[3];
        return dbPart.split("\\?")[0]; // Remove query parameters
    }

    /**
     * Upload backup to S3
     */
    private void uploadToS3(String filePath) {
        if (s3Client == null) {
            log.warn("S3 client not configured - skipping S3 upload");
            return;
        }

        try {
            File file = new File(filePath);
            String fileName = file.getName();

            log.info("Uploading backup to S3 bucket: {}", s3Bucket);
            s3Client.putObject(s3Bucket, fileName, file);
            log.info("S3 upload completed: {}", fileName);

        } catch (Exception e) {
            log.error("Failed to upload backup to S3", e);
            // Don't throw exception to allow local backup to succeed
        }
    }

    /**
     * Restore from backup
     * Returns a presigned URL to download the backup file
     */
    public String restoreBackup(String backupFileName) {
        if (s3Client == null) {
            throw new UnsupportedOperationException("S3 client not configured");
        }

        try {
            log.info("Generating download URL for backup: {}", backupFileName);

            java.util.Date expiration = new java.util.Date();
            long expTimeMillis = expiration.getTime();
            expTimeMillis += 1000 * 60 * 60; // 1 hour
            expiration.setTime(expTimeMillis);

            java.net.URL url = s3Client.generatePresignedUrl(s3Bucket, backupFileName, expiration);
            return url.toString();

        } catch (Exception e) {
            log.error("Failed to generate restore URL", e);
            throw new RuntimeException("Failed to retrieve backup from S3", e);
        }
    }
}
