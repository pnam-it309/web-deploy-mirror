// ExcelProductController.java
package udpm.hn.server.infrastructure.core.job.products.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.infrastructure.core.job.products.upload.FileUploadService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/api/products/import")
@RequiredArgsConstructor
public class ExcelProductController {

    private static final String UPLOAD_DIR = "uploads/";
    private final FileUploadService fileUploadService;
    private final JobLauncher jobLauncher;
    private final Job importProductJob;

    @PostMapping
    public ResponseEntity<ResponseObject<?>> importProducts(@RequestParam("file") MultipartFile file) {
        try {
            // Validate file
            if (file.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ResponseObject<>(false, "File is empty", null));
            }

            // Create upload directory if not exists
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Save the file
            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(filename);
            file.transferTo(filePath);

            // Launch the job
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("input.file.name", filePath.toString())
                    .addDate("date", new Date())
                    .toJobParameters();

            jobLauncher.run(importProductJob, jobParameters);

            return ResponseEntity.ok(new ResponseObject<>(true, "Product import started successfully", null));
        } catch (Exception e) {
            log.error("Error importing products", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseObject<>(false, "Error importing products: " + e.getMessage(), null));
        }
    }
}