package udpm.hn.server.core.admin.import_data.service.impl;

import org.apache.poi.ss.usermodel.*;
import udpm.hn.server.core.admin.import_data.config.ImportConfigProvider;
import udpm.hn.server.core.admin.import_data.config.ImportFieldMapping;
import udpm.hn.server.core.admin.import_data.dto.response.BaseImportResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * Processes import data using the configured field mappings
 */
public class ImportDataProcessor {

    private final ImportConfigProvider configProvider;
    private final ImportFieldMapping fieldMapping;
    private final String importType;
    private final BaseImportResponse response;

    public ImportDataProcessor(ImportConfigProvider configProvider, String importType, BaseImportResponse response) {
        this.configProvider = configProvider;
        this.importType = importType;
        this.fieldMapping = configProvider.getMapping(importType);
        this.response = response;
    }

    /**
     * Process a single row from the import file
     * @param row The row to process
     * @param rowNum The row number (1-based)
     * @return A map of field names to values, or null if the row is invalid
     */
    public Map<String, Object> processRow(Row row, int rowNum) {
        Map<String, Object> rowData = new HashMap<>();
        boolean hasErrors = false;

        // Process each cell in the row
        for (int i = 0; i < row.getLastCellNum(); i++) {
            String fieldName = fieldMapping.getFieldName(i);
            if (fieldName == null) {
                continue; // Skip unmapped columns
            }

            Cell cell = row.getCell(i, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            String cellValue = getCellValueAsString(cell);
            
            // Apply default value if cell is empty
            if ((cellValue == null || cellValue.trim().isEmpty()) && fieldMapping.getDefaultValue(fieldName) != null) {
                cellValue = String.valueOf(fieldMapping.getDefaultValue(fieldName));
            }

            // Validate required fields
            if (fieldMapping.isFieldRequired(fieldName) && (cellValue == null || cellValue.trim().isEmpty())) {
                response.addError(rowNum, fieldName, "Required field is missing", "");
                hasErrors = true;
                continue;
            }

            // Apply field validator if exists
            ImportFieldMapping.FieldValidator validator = fieldMapping.getValidator(fieldName);
            if (validator != null && cellValue != null && !cellValue.trim().isEmpty()) {
                ImportFieldMapping.ValidationResult result = validator.validate(fieldName, cellValue);
                if (!result.isValid()) {
                    response.addError(rowNum, fieldName, result.getErrorMessage(), cellValue);
                    hasErrors = true;
                    continue;
                }
            }

            // Add the field to the row data
            rowData.put(fieldName, cellValue);
        }

        return hasErrors ? null : rowData;
    }

    /**
     * Convert a cell value to a string
     */
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return null;
        }
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    // Check if it's an integer value
                    double numValue = cell.getNumericCellValue();
                    if (numValue == (long) numValue) {
                        return String.valueOf((long) numValue);
                    }
                    return String.valueOf(numValue);
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }
}
