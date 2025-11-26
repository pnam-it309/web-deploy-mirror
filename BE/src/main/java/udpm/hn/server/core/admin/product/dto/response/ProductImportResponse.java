package udpm.hn.server.core.admin.product.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import udpm.hn.server.core.admin.product.dto.request.ProductExcelRow;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductImportResponse {
    private int totalRows;
    private int successCount;
    private int errorCount;

    // Danh sách các dòng bị lỗi để Frontend hiển thị lại cho user sửa
    private List<ProductExcelRow> errorRows;
}