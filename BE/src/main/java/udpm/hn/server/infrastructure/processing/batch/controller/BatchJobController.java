package udpm.hn.server.infrastructure.processing.batch.controller;

import org.springframework.batch.core.BatchStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import udpm.hn.server.infrastructure.processing.batch.service.BatchJobService;

@RestController
@RequestMapping("/api/batch")
public class BatchJobController {

    private final BatchJobService batchJobService;

    public BatchJobController(BatchJobService batchJobService) {
        this.batchJobService = batchJobService;
    }

    @PostMapping("/run-sample-job")
    public ResponseEntity<String> runSampleJob() {
        try {
            BatchStatus status = batchJobService.runSampleJob();
            return ResponseEntity.ok("Batch job completed with status: " + status);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error running batch job: " + e.getMessage());
        }
    }
}
