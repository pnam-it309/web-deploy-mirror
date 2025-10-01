package udpm.hn.server.infrastructure.core.log;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class LogService {

    /**
     * Simplified implementation - CSV reading disabled for now
     */
    public List<LogEntry> readCSV(String filePath) {
        log.info("CSV reading disabled - returning empty list for file: {}", filePath);
        return new ArrayList<>();
    }

    /**
     * Simplified implementation - log writing disabled for now
     */
    public void writeLog(String message) {
        log.info("Log writing disabled - would write: {}", message);
    }
}
