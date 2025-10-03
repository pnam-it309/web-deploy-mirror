package udpm.hn.server.core.admin.import_data.config;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides field mapping configurations for different import types
 */
@Component
public class ImportConfigProvider {
    
    private final Map<String, ImportFieldMapping> configs = new HashMap<>();
    
    public ImportConfigProvider() {
        // Initialize default configurations
        initializeProductMappings();
        initializeCategoryMappings();
        initializeBrandMappings();
    }
    
    private void initializeProductMappings() {
        ImportFieldMapping mapping = ImportFieldMapping.builder()
            .map(0, "sku").required("sku")
            .map(1, "name").required("name")
            .map(2, "description")
            .map(3, "price").required("price")
            .map(4, "quantity")
            .map(5, "category")
            .map(6, "brand")
            .withDefault("quantity", 0)
            .withValidator("price", (fieldName, value) -> {
                try {
                    double price = Double.parseDouble(value);
                    if (price < 0) {
                        return ImportFieldMapping.ValidationResult.invalid("Price cannot be negative");
                    }
                    return ImportFieldMapping.ValidationResult.valid();
                } catch (NumberFormatException e) {
                    return ImportFieldMapping.ValidationResult.invalid("Invalid price format");
                }
            })
            .build();
            
        configs.put("product", mapping);
    }
    
    private void initializeCategoryMappings() {
        ImportFieldMapping mapping = ImportFieldMapping.builder()
            .map(0, "code").required("code")
            .map(1, "name").required("name")
            .map(2, "description")
            .map(3, "parentCategory")
            .build();
            
        configs.put("category", mapping);
    }
    
    private void initializeBrandMappings() {
        ImportFieldMapping mapping = ImportFieldMapping.builder()
            .map(0, "code").required("code")
            .map(1, "name").required("name")
            .map(2, "description")
            .map(3, "website")
            .build();
            
        configs.put("brand", mapping);
    }
    
    /**
     * Get field mapping configuration for a specific import type
     * @param importType The type of import (e.g., "product", "category", "brand")
     * @return The field mapping configuration
     * @throws IllegalArgumentException if the import type is not supported
     */
    public ImportFieldMapping getMapping(String importType) {
        ImportFieldMapping mapping = configs.get(importType.toLowerCase());
        if (mapping == null) {
            throw new IllegalArgumentException("Unsupported import type: " + importType);
        }
        return mapping;
    }
    
    /**
     * Register a custom field mapping configuration
     * @param importType The type of import
     * @param mapping The field mapping configuration
     */
    public void registerMapping(String importType, ImportFieldMapping mapping) {
        configs.put(importType.toLowerCase(), mapping);
    }
}
