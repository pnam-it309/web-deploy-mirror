package udpm.hn.server.infrastructure.data.excel;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class ExcelProcessingService {
    
    private static final int ROW_ACCESS_WINDOW = 100;
    private static final int BATCH_SIZE = 1000;
    
    /**
     * Read Excel file and return data as list of maps (column name -> value)
     */
    public List<Map<String, String>> readExcel(MultipartFile file) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();
            
            List<Map<String, String>> data = new ArrayList<>();
            List<String> headers = new ArrayList<>();
            
            // Read headers
            if (rows.hasNext()) {
                Row headerRow = rows.next();
                headerRow.forEach(cell -> headers.add(cell.getStringCellValue()));
            }
            
            // Read data rows
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                Map<String, String> rowData = new LinkedHashMap<>();
                
                for (int i = 0; i < headers.size(); i++) {
                    Cell cell = currentRow.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    rowData.put(headers.get(i), getCellValueAsString(cell));
                }
                
                data.add(rowData);
            }
            
            return data;
        }
    }
    
    /**
     * Process large Excel file with batch processing
     */
    public void processLargeExcel(MultipartFile file, ExcelRowProcessor processor) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();
            
            // Skip header if needed
            if (rows.hasNext()) {
                rows.next(); // Skip header
            }
            
            List<Map<String, String>> batch = new ArrayList<>(BATCH_SIZE);
            int rowNum = 0;
            
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                Map<String, String> rowData = new LinkedHashMap<>();
                
                // Process each cell in the row
                for (Cell cell : currentRow) {
                    rowData.put(
                        String.valueOf(cell.getAddress().getColumn() + 1), // 1-based column index
                        getCellValueAsString(cell)
                    );
                }
                
                batch.add(rowData);
                rowNum++;
                
                // Process batch when it reaches batch size
                if (batch.size() >= BATCH_SIZE) {
                    processor.processBatch(batch, rowNum - batch.size() + 1, rowNum);
                    batch.clear();
                }
            }
            
            // Process remaining rows
            if (!batch.isEmpty()) {
                processor.processBatch(batch, rowNum - batch.size() + 1, rowNum);
            }
        }
    }
    
    /**
     * Write data to Excel with streaming for large files
     */
    public byte[] writeToExcel(List<Map<String, Object>> data, List<String> headers) throws IOException {
        try (SXSSFWorkbook workbook = new SXSSFWorkbook(ROW_ACCESS_WINDOW)) {
            Sheet sheet = workbook.createSheet("Data");
            
            // Create header row
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers.get(i));
            }
            
            // Create data rows
            AtomicInteger rowNum = new AtomicInteger(1);
            data.forEach(rowData -> {
                Row row = sheet.createRow(rowNum.getAndIncrement());
                int cellNum = 0;
                
                for (String header : headers) {
                    Object value = rowData.get(header);
                    Cell cell = row.createCell(cellNum++);
                    
                    if (value != null) {
                        if (value instanceof Number) {
                            cell.setCellValue(((Number) value).doubleValue());
                        } else if (value instanceof Date) {
                            cell.setCellValue((Date) value);
                        } else if (value instanceof Boolean) {
                            cell.setCellValue((Boolean) value);
                        } else {
                            cell.setCellValue(value.toString());
                        }
                    }
                }
            });
            
            // Write to byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }
    
    /**
     * Convert Excel cell value to string
     */
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                }
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                return cell.toString().trim();
        }
    }
    
    /**
     * Interface for processing Excel rows in batches
     */
    @FunctionalInterface
    public interface ExcelRowProcessor {
        void processBatch(List<Map<String, String>> batch, int startRow, int endRow);
    }
}
