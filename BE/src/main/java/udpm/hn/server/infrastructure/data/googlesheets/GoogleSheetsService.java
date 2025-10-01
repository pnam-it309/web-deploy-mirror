package udpm.hn.server.infrastructure.data.googlesheets;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(
    name = "google.sheets.enabled",
    havingValue = "true",
    matchIfMissing = false
)
public class GoogleSheetsService {

    private final Sheets sheetsService;
    private static final String VALUE_INPUT_OPTION = "RAW";
    private static final String INSERT_DATA_OPTION = "INSERT_ROWS";

    public List<List<Object>> readSheet(String spreadsheetId, String range) throws IOException {
        ValueRange response = sheetsService.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        return response.getValues() != null ? response.getValues() : Collections.emptyList();
    }

    public void writeToSheet(String spreadsheetId, String range, List<List<Object>> values) throws IOException {
        ValueRange body = new ValueRange()
                .setValues(values);
        
        UpdateValuesResponse result = sheetsService.spreadsheets().values()
                .update(spreadsheetId, range, body)
                .setValueInputOption(VALUE_INPUT_OPTION)
                .execute();
        
        log.info("{} cells updated.", result.getUpdatedCells());
    }

    public void appendToSheet(String spreadsheetId, String range, List<List<Object>> values) throws IOException {
        ValueRange body = new ValueRange()
                .setValues(values);
        
        AppendValuesResponse result = sheetsService.spreadsheets().values()
                .append(spreadsheetId, range, body)
                .setValueInputOption(VALUE_INPUT_OPTION)
                .setInsertDataOption(INSERT_DATA_OPTION)
                .execute();
        
        log.info("{} cells appended.", result.getUpdates().getUpdatedCells());
    }

    public void createSheet(String spreadsheetId, String sheetTitle) throws IOException {
        Spreadsheet spreadsheet = sheetsService.spreadsheets().get(spreadsheetId).execute();
        
        // Check if sheet already exists
        boolean sheetExists = spreadsheet.getSheets().stream()
                .anyMatch(sheet -> sheet.getProperties().getTitle().equals(sheetTitle));
                
        if (!sheetExists) {
            AddSheetRequest addSheetRequest = new AddSheetRequest()
                    .setProperties(new SheetProperties().setTitle(sheetTitle));
            
            BatchUpdateSpreadsheetRequest batchUpdateRequest = new BatchUpdateSpreadsheetRequest()
                    .setRequests(Collections.singletonList(
                            new Request().setAddSheet(addSheetRequest)
                    ));
            
            sheetsService.spreadsheets()
                    .batchUpdate(spreadsheetId, batchUpdateRequest)
                    .execute();
            log.info("Created new sheet: {}", sheetTitle);
        }
    }
}
