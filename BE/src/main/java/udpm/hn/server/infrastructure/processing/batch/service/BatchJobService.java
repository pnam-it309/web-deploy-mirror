package udpm.hn.server.infrastructure.processing.batch.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BatchJobService {

    private final JobLauncher jobLauncher;
    private final JobExplorer jobExplorer;
    private final Map<String, Job> jobs;

    /**
     * Run a batch job by name
     */
    public Long runJob(String jobName) throws JobExecutionException {
        Job job = jobs.get(jobName);
        if (job == null) {
            throw new IllegalArgumentException("No job found with name: " + jobName);
        }

        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("startAt", System.currentTimeMillis())
                .toJobParameters();

        JobExecution jobExecution = jobLauncher.run(job, jobParameters);
        log.info("Started job {} with execution id: {}", jobName, jobExecution.getId());
        return jobExecution.getId();
    }

    /**
     * Get all job executions for a specific job name
     */
    @Transactional(readOnly = true)
    public List<JobExecution> getJobExecutions(String jobName) {
        List<JobExecution> executions = new ArrayList<>();
        jobExplorer.getJobInstances(jobName, 0, Integer.MAX_VALUE)
                .forEach(instance -> 
                    executions.addAll(jobExplorer.getJobExecutions(instance)));
        return executions;
    }

    /**
     * Get job execution by id
     */
    @Transactional(readOnly = true)
    public JobExecution getJobExecution(Long executionId) {
        return jobExplorer.getJobExecution(executionId);
    }

    /**
     * Get all available job names
     */
    @Transactional(readOnly = true)
    public Set<String> getJobNames() {
        return new HashSet<>(jobs.keySet());
    }

    /**
     * Get job execution status
     */
    @Transactional(readOnly = true)
    public BatchStatus getJobStatus(Long executionId) {
        JobExecution jobExecution = jobExplorer.getJobExecution(executionId);
        return jobExecution != null ? jobExecution.getStatus() : null;
    }
}
