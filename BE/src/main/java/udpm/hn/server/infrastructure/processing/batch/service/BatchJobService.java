package udpm.hn.server.infrastructure.processing.batch.service;

import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class BatchJobService {

    private final JobLauncher jobLauncher;
    private final Job sampleJob;

    @Autowired
    public BatchJobService(JobLauncher jobLauncher, 
                          @Qualifier("sampleJob") Job sampleJob) {
        this.jobLauncher = jobLauncher;
        this.sampleJob = sampleJob;
    }

    public BatchStatus runSampleJob() throws JobParametersInvalidException, 
                                           JobExecutionAlreadyRunningException, 
                                           JobRestartException, 
                                           JobInstanceAlreadyCompleteException {
        
        Map<String, JobParameter<?>> maps = new HashMap<>();
        maps.put("time", new JobParameter<>(System.currentTimeMillis(), Long.class));
        JobParameters parameters = new JobParameters(maps);
        
        JobExecution jobExecution = jobLauncher.run(sampleJob, parameters);
        
        System.out.println("JobExecution: " + jobExecution.getStatus());
        
        while (jobExecution.isRunning()) {
            System.out.println("...");
        }
        
        return jobExecution.getStatus();
    }
}
