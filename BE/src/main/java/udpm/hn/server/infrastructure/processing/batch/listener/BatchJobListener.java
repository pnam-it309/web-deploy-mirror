package udpm.hn.server.infrastructure.processing.batch.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;
import udpm.hn.server.infrastructure.integration.websocket.service.WebSocketService;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Lớp lắng nghe sự kiện của batch job
 * - Ghi log chi tiết quá trình thực thi job
 * - Gửi thông báo trạng thái qua WebSocket
 * - Theo dõi thời gian thực thi và xử lý lỗi
 */

/**
 * Lớp lắng nghe sự kiện của batch job
 * - Xử lý các sự kiện trước và sau khi job chạy
 * - Gửi thông báo qua WebSocket về tiến trình xử lý
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class BatchJobListener implements JobExecutionListener {

    // Dịch vụ gửi thông báo qua WebSocket
    private final WebSocketService webSocketService;
    
    // Thời điểm bắt đầu chạy job
    private Instant startTime;

    /**
     * Xử lý trước khi job bắt đầu chạy
     * - Ghi log thông tin bắt đầu job
     * - Gửi thông báo qua WebSocket về việc bắt đầu job
     * - Lưu thời điểm bắt đầu để tính toán thời gian thực thi
     * 
     * @param jobExecution Đối tượng chứa thông tin về job đang thực thi
     */
    @Override
    public void beforeJob(JobExecution jobExecution) {
        // Lưu thời điểm bắt đầu
        startTime = Instant.now();
        String jobId = jobExecution.getJobId().toString();
        log.info("Bắt đầu job: {} với ID: {}", jobExecution.getJobInstance().getJobName(), jobId);
        
        webSocketService.sendBatchProgress(jobId, "Job started: " + jobExecution.getJobInstance().getJobName(), 
            new HashMap<String, Object>() {{
                put("jobName", jobExecution.getJobInstance().getJobName());
                put("startTime", startTime.toString());
                put("status", "STARTED");
            }}
        );
    }

    /**
     * Xử lý sau khi job kết thúc
     * - Tính toán thời gian thực thi
     * - Thu thập thông tin kết quả thực thi
     * - Gửi thông báo kết quả qua WebSocket
     * - Xử lý lỗi nếu có
     * 
     * @param jobExecution Đối tượng chứa thông tin về job đã thực thi
     */
    @Override
    public void afterJob(JobExecution jobExecution) {
        // Tính toán thời gian thực thi
        Instant endTime = Instant.now();
        Duration duration = Duration.between(startTime, endTime);
        String jobId = jobExecution.getJobId().toString();
        
        // Tạo đối tượng chứa thông tin cơ bản của job
        Map<String, Object> jobData = new HashMap<>();
        // Thêm thông tin cơ bản của job
        jobData.put("jobName", jobExecution.getJobInstance().getJobName());  // Tên job
        jobData.put("jobId", jobId);  // ID của job
        jobData.put("status", jobExecution.getStatus().name());  // Trạng thái job
        jobData.put("startTime", startTime.toString());  // Thời điểm bắt đầu
        jobData.put("endTime", endTime.toString());      // Thời điểm kết thúc
        jobData.put("duration", formatDuration(duration));  // Thời gian thực thi (đã định dạng)
        
        // Thêm dữ liệu từ execution context
        if (jobExecution.getExecutionContext() != null) {
            for (Map.Entry<String, Object> entry : jobExecution.getExecutionContext().entrySet()) {
                // Đảm bảo giá trị không null
                String value = (entry.getValue() != null) ? entry.getValue().toString() : "";
                jobData.put(entry.getKey(), value);
            }
        }

        // Xử lý khi job hoàn thành thành công
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("Job hoàn thành thành công sau {}", formatDuration(duration));
            webSocketService.sendBatchComplete(jobId, "Job đã hoàn thành thành công", jobData);
        } else if (jobExecution.getStatus() == BatchStatus.FAILED) {
            log.error("Job thất bại sau {}", formatDuration(duration));
            
            // Tạo thông báo lỗi từ tất cả các ngoại lệ
            StringBuilder errorBuilder = new StringBuilder();
            for (Throwable t : jobExecution.getAllFailureExceptions()) {
                if (errorBuilder.length() > 0) {
                    errorBuilder.append("\n");
                }
                // Thêm thông tin chi tiết về lỗi
                errorBuilder.append(t.getClass().getSimpleName())
                          .append(": ")
                          .append(t.getMessage());
                // Thêm thông tin stack trace nếu cần
                if (t.getCause() != null) {
                    errorBuilder.append("\nNguyên nhân: ").append(t.getCause().getMessage());
                }
            }
            jobData.put("error", errorBuilder.toString());
            
            // Gửi thông báo lỗi qua WebSocket
            webSocketService.sendBatchError(jobId, "Job đã thất bại", jobData);
        }
    }

    /**
     * Định dạng thời gian thực thi thành chuỗi dễ đọc
     * - Định dạng: HH:MM:SS hoặc X giây (nếu dưới 1 phút)
     * - Hỗ trợ hiển thị thời gian dài
     * 
     * @param duration Khoảng thời gian cần định dạng
     * @return Chuỗi thời gian đã được định dạng (ví dụ: "02:30:45" hoặc "45 giây")
     */
    private String formatDuration(Duration duration) {
        return String.format("%d phút %d giây", 
            duration.toMinutes(), 
            duration.getSeconds() % 60);
    }
}
