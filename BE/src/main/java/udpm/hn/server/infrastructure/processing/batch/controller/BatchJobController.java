package udpm.hn.server.infrastructure.processing.batch.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import udpm.hn.server.infrastructure.processing.batch.service.BatchJobService;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/batch")
@RequiredArgsConstructor
public class BatchJobController {

    private final BatchJobService batchJobService;

    @PostMapping("/jobs/{jobName}")
    public ResponseEntity<?> runJob(@PathVariable String jobName) {
        try {
            Long executionId = batchJobService.runJob(jobName);
            return ResponseEntity.ok(Map.of(
                "message", "Job started successfully",
                "executionId", executionId
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Failed to start job: " + e.getMessage()));
        }
    }

    @GetMapping("/jobs")
    public ResponseEntity<Set<String>> listJobs() {
        return ResponseEntity.ok(batchJobService.getJobNames());
    }

    @GetMapping("/jobs/{jobName}/executions")
    public ResponseEntity<List<JobExecution>> listJobExecutions(@PathVariable String jobName) {
        return ResponseEntity.ok(batchJobService.getJobExecutions(jobName));
    }

    @GetMapping("/executions/{executionId}")
    public ResponseEntity<JobExecution> getJobExecution(@PathVariable Long executionId) {
        JobExecution execution = batchJobService.getJobExecution(executionId);
        if (execution == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(execution);
    }

    @GetMapping("/executions/{executionId}/status")
    public ResponseEntity<BatchStatus> getJobStatus(@PathVariable Long executionId) {
        BatchStatus status = batchJobService.getJobStatus(executionId);
        if (status == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(status);
    }
}
