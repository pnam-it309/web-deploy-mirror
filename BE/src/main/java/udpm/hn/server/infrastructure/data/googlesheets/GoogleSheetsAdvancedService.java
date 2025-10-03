package udpm.hn.server.infrastructure.data.googlesheets;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import udpm.hn.server.infrastructure.data.excel.ExcelProcessingService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(
    name = "google.sheets.enabled",
    havingValue = "true",
    matchIfMissing = false
)
public class GoogleSheetsAdvancedService {

    private final Sheets sheetsService;
    private final ExcelProcessingService excelProcessingService;
    
    private static final int BATCH_UPDATE_SIZE = 5000;
    
    /**
     * Import Excel file to Google Sheets
     */
    public void importExcelToSheets(MultipartFile file, String spreadsheetId, String sheetName) throws IOException {
        // Read Excel data
        List<Map<String, String>> excelData = excelProcessingService.readExcel(file);
        
        if (excelData.isEmpty()) {
            throw new IllegalArgumentException("Excel file is empty");
        }
        
        // Prepare data for Google Sheets
        List<List<Object>> data = new ArrayList<>();
        
        // Add headers
        data.add(new ArrayList<>(excelData.get(0).keySet()));
        
        // Add rows
        excelData.forEach(row -> {
            data.add(new ArrayList<>(row.values()));
        });
        
        // Write to Google Sheets
        updateSheet(spreadsheetId, sheetName, data);
    }
    
    /**
     * Export Google Sheet to Excel
     */
    public byte[] exportSheetToExcel(String spreadsheetId, String range) throws IOException {
        // Get data from Google Sheets
        List<List<Object>> sheetData = getSheetData(spreadsheetId, range);
        
        if (sheetData.isEmpty()) {
            throw new IllegalArgumentException("No data found in the specified range");
        }
        
        // Convert to Excel format
        List<Map<String, Object>> data = new ArrayList<>();
        List<String> headers = sheetData.get(0).stream()
                .map(Object::toString)
                .collect(Collectors.toList());
        
        for (int i = 1; i < sheetData.size(); i++) {
            Map<String, Object> rowData = new LinkedHashMap<>();
            for (int j = 0; j < headers.size(); j++) {
                rowData.put(headers.get(j), 
                    j < sheetData.get(i).size() ? sheetData.get(i).get(j) : "");
            }
            data.add(rowData);
        }
        
        return excelProcessingService.writeToExcel(data, headers);
    }
    
    /**
     * Get sheet data with caching
     */
    @Cacheable(value = "sheetData", key = "#spreadsheetId + ':' + #range")
    public List<List<Object>> getSheetData(String spreadsheetId, String range) throws IOException {
        ValueRange response = sheetsService.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        return response.getValues() != null ? response.getValues() : Collections.emptyList();
    }
    
    /**
     * Update sheet with batch processing for large datasets
     */
    public void updateSheet(String spreadsheetId, String range, List<List<Object>> data) throws IOException {
        if (data == null || data.isEmpty()) {
            return;
        }
        
        // Process in batches if data is large
        if (data.size() > BATCH_UPDATE_SIZE) {
            int totalBatches = (int) Math.ceil((double) data.size() / BATCH_UPDATE_SIZE);
            
            for (int i = 0; i < totalBatches; i++) {
                int fromIndex = i * BATCH_UPDATE_SIZE;
                int toIndex = Math.min((i + 1) * BATCH_UPDATE_SIZE, data.size());
                
                List<List<Object>> batch = data.subList(fromIndex, toIndex);
                updateBatch(spreadsheetId, range, batch, i == 0);
                
                // Add a small delay between batches to avoid rate limiting
                if (i < totalBatches - 1) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        } else {
            updateBatch(spreadsheetId, range, data, true);
        }
    }
    
    private void updateBatch(String spreadsheetId, String range, List<List<Object>> batch, boolean clearSheet) 
            throws IOException {
        
        String rangeToUpdate = clearSheet ? range : getNextRange(spreadsheetId, range);
        
        ValueRange body = new ValueRange()
                .setValues(batch);
        
        if (clearSheet) {
            // Clear existing data if this is the first batch
            clearSheet(spreadsheetId, range);
        }
        
        sheetsService.spreadsheets().values()
                .update(spreadsheetId, rangeToUpdate, body)
                .setValueInputOption("USER_ENTERED")
                .execute();
    }
    
    private String getNextRange(String spreadsheetId, String baseRange) throws IOException {
        // Get the last row with data
        ValueRange response = sheetsService.spreadsheets().values()
                .get(spreadsheetId, baseRange)
                .execute();
        
        int lastRow = response.getValues() != null ? response.getValues().size() : 0;
        return baseRange.replaceAll("\\d+$", "") + (lastRow + 1);
    }
    
    private void clearSheet(String spreadsheetId, String range) throws IOException {
        ClearValuesRequest requestBody = new ClearValuesRequest();
        sheetsService.spreadsheets().values()
                .clear(spreadsheetId, range, requestBody)
                .execute();
    }
    
    /**
     * Get all sheet names in a spreadsheet
     */
    public List<String> getSheetNames(String spreadsheetId) throws IOException {
        List<String> sheetNames = new ArrayList<>();
        Spreadsheet spreadsheet = sheetsService.spreadsheets().get(spreadsheetId).execute();
        
        for (Sheet sheet : spreadsheet.getSheets()) {
            sheetNames.add(sheet.getProperties().getTitle());
        }
        
        return sheetNames;
    }
    
    /**
     * Create multiple sheets in one batch
     */
    public void createSheets(String spreadsheetId, List<String> sheetNames) throws IOException {
        List<Request> requests = new ArrayList<>();
        
        for (String sheetName : sheetNames) {
            AddSheetRequest addSheetRequest = new AddSheetRequest()
                    .setProperties(new SheetProperties()
                            .setTitle(sheetName));
            
            requests.add(new Request().setAddSheet(addSheetRequest));
        }
        
        BatchUpdateSpreadsheetRequest batchUpdateRequest = new BatchUpdateSpreadsheetRequest()
                .setRequests(requests);
        
        sheetsService.spreadsheets()
                .batchUpdate(spreadsheetId, batchUpdateRequest)
                .execute();
    }
}
