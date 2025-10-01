package udpm.hn.server.core.admin.import_data.config;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration for mapping between Excel/Sheets columns and entity fields
 */
@Data
public class ImportFieldMapping {
    // Map of column index to field name
    private final Map<Integer, String> columnToFieldMap = new HashMap<>();
    
    // Map of field name to column index (reverse lookup)
    private final Map<String, Integer> fieldToColumnMap = new HashMap<>();
    
    // Default value for each field
    private final Map<String, Object> fieldDefaults = new HashMap<>();
    
    // Whether each field is required
    private final Map<String, Boolean> requiredFields = new HashMap<>();
    
    // Field validators
    private final Map<String, FieldValidator> fieldValidators = new HashMap<>();
    
    public static class Builder {
        private final ImportFieldMapping mapping = new ImportFieldMapping();
        
        public Builder map(int columnIndex, String fieldName) {
            mapping.columnToFieldMap.put(columnIndex, fieldName);
            mapping.fieldToColumnMap.put(fieldName, columnIndex);
            return this;
        }
        
        public Builder withDefault(String fieldName, Object defaultValue) {
            mapping.fieldDefaults.put(fieldName, defaultValue);
            return this;
        }
        
        public Builder required(String... fieldNames) {
            for (String fieldName : fieldNames) {
                mapping.requiredFields.put(fieldName, true);
            }
            return this;
        }
        
        public Builder withValidator(String fieldName, FieldValidator validator) {
            mapping.fieldValidators.put(fieldName, validator);
            return this;
        }
        
        public ImportFieldMapping build() {
            return mapping;
        }
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public String getFieldName(int columnIndex) {
        return columnToFieldMap.get(columnIndex);
    }
    
    public Integer getColumnIndex(String fieldName) {
        return fieldToColumnMap.get(fieldName);
    }
    
    public boolean isFieldRequired(String fieldName) {
        return requiredFields.getOrDefault(fieldName, false);
    }
    
    public Object getDefaultValue(String fieldName) {
        return fieldDefaults.get(fieldName);
    }
    
    public FieldValidator getValidator(String fieldName) {
        return fieldValidators.get(fieldName);
    }
    
    public interface FieldValidator {
        ValidationResult validate(String fieldName, String value);
    }
    
    @Data
    public static class ValidationResult {
        private final boolean valid;
        private final String errorMessage;
        
        public static ValidationResult valid() {
            return new ValidationResult(true, null);
        }
        
        public static ValidationResult invalid(String errorMessage) {
            return new ValidationResult(false, errorMessage);
        }
    }
}
