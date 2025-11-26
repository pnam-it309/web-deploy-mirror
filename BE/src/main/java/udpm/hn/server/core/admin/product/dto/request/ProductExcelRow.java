package udpm.hn.server.core.admin.product.dto.request;

import lombok.Data;

/**
 * Class này đại diện cho 1 dòng trong file Excel.
 * Tất cả các trường đều để String để tránh lỗi crash khi đọc (ví dụ giá nhập chữ).
 * Chúng ta sẽ validate và parse sau.
 */
@Data
public class ProductExcelRow {
    private int rowIndex;        // Số thứ tự dòng trong Excel (để báo lỗi: "Lỗi tại dòng 5")

    private String name;         // Tên sản phẩm
    private String sku;          // Mã SKU
    private String price;        // Giá (String để check valid số)
    private String quantity;     // Số lượng
    private String description;  // Mô tả ngắn

    // QUAN TRỌNG: Người dùng nhập MÃ (Code), không nhập ID UUID
    private String brandCode;
    private String categoryCode;

    private String unit;         // Đơn vị (Cái, Hộp...)

    // Trường dùng để trả về kết quả validate cho Frontend
    private String errorMessage;
    private boolean isValid;
}