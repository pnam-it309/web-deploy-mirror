package udpm.hn.server.core.admin.import_data.service.Impl;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import udpm.hn.server.core.admin.import_data.dto.request.BaseImportRequest;
import udpm.hn.server.core.admin.import_data.dto.response.BaseImportResponse;
import udpm.hn.server.core.admin.import_data.service.ImportService;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

public abstract class BaseImportServiceImpl<T extends BaseImportRequest, R extends BaseImportResponse> 
        implements ImportService<T, R> {

    @Override
    public R importFromExcel(T request) throws IOException {
        if (request.getFile() == null || request.getFile().isEmpty()) {
            throw new IllegalArgumentException("File is required");
        }

        try (InputStream inputStream = request.getFile().getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {
            
            Sheet sheet = getSheet(workbook, request.getSheetName());
            return processSheet(sheet, request);
        }
    }

    @Override
    public R importFromGoogleSheet(T request) throws IOException {
        // Implementation for Google Sheets will be added later
        throw new UnsupportedOperationException("Google Sheets import is not implemented yet");
    }

    protected Sheet getSheet(Workbook workbook, String sheetName) {
        return sheetName != null ? 
                workbook.getSheet(sheetName) : 
                workbook.getSheetAt(0);
    }

    protected abstract R processSheet(Sheet sheet, T request);

    protected String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf((long) cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    protected void skipHeaderRows(Sheet sheet, int headerRow) {
        Iterator<Row> rowIterator = sheet.iterator();
        int currentRow = 0;
        while (rowIterator.hasNext() && currentRow < headerRow) {
            rowIterator.next();
            currentRow++;
        }
    }
}
