package udpm.hn.server.core.admin.import_data.dto.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public abstract class BaseImportResponse {
    private int totalRecords = 0;
    private int successCount = 0;
    private int errorCount = 0;
    private List<ImportError> errors = new ArrayList<>();

    @Data
    public static class ImportError {
        private int rowNumber;
        private String field;
        private String message;
        private String value;

        public ImportError(int rowNumber, String field, String message, String value) {
            this.rowNumber = rowNumber;
            this.field = field;
            this.message = message;
            this.value = value;
        }
    }

    public void addError(int rowNumber, String field, String message, String value) {
        errors.add(new ImportError(rowNumber, field, message, value));
        errorCount++;
        totalRecords++;
    }

    protected void incrementSuccess() {
        successCount++;
        totalRecords++;
    }
}
