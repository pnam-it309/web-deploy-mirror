// ProductRowMapper.java
package udpm.hn.server.infrastructure.core.job.products.commonio;

import org.springframework.batch.item.excel.RowMapper;
import org.springframework.batch.item.excel.support.rowset.RowSet;
import udpm.hn.server.infrastructure.core.job.products.model.request.ProductExcelRequest;

import java.math.BigDecimal;

public class ProductRowMapper implements RowMapper<ProductExcelRequest> {


    @Override
    public ProductExcelRequest mapRow(RowSet rs) throws Exception {
        ProductExcelRequest request = new ProductExcelRequest();
        
        // Skip header row if needed
        if (rs.getCurrentRowIndex() == 0) {
            return null;
        }

        // Get cell values using RowSet's getColumnValue
        String[] rowData = rs.getCurrentRow();
        
        if (rowData.length < 9) {
            throw new IllegalStateException("Not enough columns in the input file. Expected at least 9 columns.");
        }

        try {
            request.setOrderNumber(Integer.parseInt(rowData[0].trim()));
            request.setSku(rowData[1]);
            request.setName(rowData[2]);
            request.setSlug(rowData[3]);
            request.setShortDescription(rowData[4]);
            request.setPrice(new BigDecimal(rowData[5].trim()));
            request.setStockQuantity(Integer.parseInt(rowData[6].trim()));
            request.setBrandName(rowData[7]);
            request.setCategoryName(rowData[8]);
        } catch (NumberFormatException e) {
            throw new IllegalStateException("Error parsing numeric value in row: " + String.join(", ", rowData), e);
        }

        return request;
    }
}