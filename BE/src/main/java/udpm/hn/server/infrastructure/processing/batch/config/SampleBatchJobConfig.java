package udpm.hn.server.infrastructure.processing.batch.config;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import udpm.hn.server.infrastructure.integration.websocket.service.WebSocketService;
//import udpm.hn.server.entity.SyncHistory;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class SampleBatchJobConfig {
    
    private final WebSocketService webSocketService;

    @Bean
    public Job sampleJob(JobRepository jobRepository, Step sampleStep) {
        return new JobBuilder("sampleJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(new JobExecutionListener() {
                    @Override
                    public void beforeJob(JobExecution jobExecution) {
                        String jobId = jobExecution.getJobId().toString();
                        Map<String, Object> data = new HashMap<>();
                        data.put("jobId", jobId);
                        data.put("startTime", LocalDateTime.now());
                        webSocketService.sendBatchProgress(jobId, "Batch job started", data);
                    }

                    @Override
                    public void afterJob(JobExecution jobExecution) {
                        String jobId = jobExecution.getJobId().toString();
                        Map<String, Object> data = new HashMap<>();
                        data.put("jobId", jobId);
                        data.put("endTime", LocalDateTime.now());
                        data.put("status", jobExecution.getStatus().toString());
                        data.put("exitStatus", jobExecution.getExitStatus().getExitCode());
                        
                        if (jobExecution.getStatus().isUnsuccessful()) {
                            webSocketService.sendBatchError(jobId, "Batch job failed", data);
                        } else {
                            webSocketService.sendBatchComplete(jobId, "Batch job completed", data);
                        }
                    }
                })
                .flow(sampleStep)
                .end()
                .build();
    }

//    @Bean
//    public Step sampleStep(JobRepository jobRepository,
//                          PlatformTransactionManager transactionManager,
//                          ItemReader<SyncHistory> reader,
//                          ItemProcessor<SyncHistory, SyncHistory> processor,
//                          ItemWriter<SyncHistory> writer) {
//        return new StepBuilder("sampleStep", jobRepository)
//                .<SyncHistory, SyncHistory>chunk(10, transactionManager)
//                .reader(reader)
//                .processor(processor)
//                .writer(writer)
//                .build();
//    }
//
//    @Bean
//    public ItemProcessor<SyncHistory, SyncHistory> processor() {
//        return syncHistory -> {
//            // Process the item (e.g., transform data, apply business logic)
//            syncHistory.setStatus(EntityStatus.ACTIVE);
//
//            // Send progress update
//            Map<String, Object> progressData = new HashMap<>();
//            progressData.put("itemId", syncHistory.getId());
//            progressData.put("status", "PROCESSING");
//            webSocketService.sendBatchProgress("current-job", "Processing item: " + syncHistory.getId(), progressData);
//
//            return syncHistory;
//        };
//    }
//
//    @Bean
//    public ItemWriter<SyncHistory> writer(EntityManagerFactory entityManagerFactory) {
//        JpaItemWriter<SyncHistory> writer = new JpaItemWriter<>();
//        writer.setEntityManagerFactory(entityManagerFactory);
//        return writer;
//    }
//
//    private SyncHistory createSyncHistory(String resourceType) {
//        SyncHistory syncHistory = new SyncHistory();
//        syncHistory.setResourceType(resourceType);
//        syncHistory.setStatus(EntityStatus.ACTIVE);
//        return syncHistory;
//    }
}
