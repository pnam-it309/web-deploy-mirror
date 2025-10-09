package udpm.hn.server.infrastructure.core.log;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class LogService {
    public List<LogEntry> readCSV(String filePath) throws FileNotFoundException {
        List<LogEntry> logEntries = new ArrayList<>();

        try (FileReader reader = new FileReader(filePath)) {
            CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                    .setHeader("ID", "Student", "Message")
                    .build();
            try (CSVParser csvParser = new CSVParser(reader, csvFormat)) {
                for (CSVRecord csvRecord : csvParser) {
                    LogEntry logEntry = new LogEntry();
                    logEntry.setId(csvRecord.get("ID"));
                    logEntry.setMessage(csvRecord.get("Message"));
                    logEntries.add(logEntry);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return logEntries;
    }
}
