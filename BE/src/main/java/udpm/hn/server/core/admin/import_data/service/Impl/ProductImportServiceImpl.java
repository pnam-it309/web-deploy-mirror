package udpm.hn.server.core.admin.import_data.service.Impl;

import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import udpm.hn.server.core.admin.import_data.dto.request.ProductImportRequest;
import udpm.hn.server.core.admin.import_data.dto.response.ProductImportResponse;
import udpm.hn.server.core.admin.import_data.service.ImportService;

@Service
public class ProductImportServiceImpl extends BaseImportServiceImpl<ProductImportRequest, ProductImportResponse> {

    @Override
    protected ProductImportResponse processSheet(Sheet sheet, ProductImportRequest request) {
        ProductImportResponse response = new ProductImportResponse();
        
        // Skip header rows if needed
        if (request.isHasHeader()) {
            skipHeaderRows(sheet, request.getHeaderRow());
        }

        // Process each row
        for (Row row : sheet) {
            if (row.getRowNum() < request.getDataStartRow()) {
                continue; // Skip rows before data start row
            }

            try {
                // Process each cell in the row
                String sku = getCellValueAsString(row.getCell(0));
                String name = getCellValueAsString(row.getCell(1));
                String description = getCellValueAsString(row.getCell(2));
                String priceStr = getCellValueAsString(row.getCell(3));
                String quantityStr = getCellValueAsString(row.getCell(4));
                String category = getCellValueAsString(row.getCell(5));
                String brand = getCellValueAsString(row.getCell(6));

                // Validate required fields
                if (sku.isEmpty() || name.isEmpty()) {
                    response.addError(
                        row.getRowNum() + 1, 
                        sku.isEmpty() ? "SKU" : "Name", 
                        "Required field is missing", 
                        sku.isEmpty() ? "" : name
                    );
                    continue;
                }

                // Process and save the product
                // This is where you would typically call your service to save the product
                // For example: productService.saveOrUpdate(createProductFromRow(row));
                
                // For now, just add a success message
//                response.incrementSuccess();
                
            } catch (Exception e) {
                response.addError(
                    row.getRowNum() + 1,
                    "",
                    "Error processing row: " + e.getMessage(),
                    ""
                );
            }
        }

        return response;
    }

    // Helper method to create product from row data
    /*
    private Product createProductFromRow(Row row) {
        // Implement product creation logic here
        // This is just a placeholder
        return new Product();
    }
    */
}
