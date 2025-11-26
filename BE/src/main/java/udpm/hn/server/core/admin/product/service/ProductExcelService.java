package udpm.hn.server.core.admin.product.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import udpm.hn.server.core.admin.category.repository.CategoryManageRepository;
import udpm.hn.server.core.admin.brand.repository.BrandManageRepository;
import udpm.hn.server.core.admin.product.dto.request.ProductExcelRow;
import udpm.hn.server.core.admin.product.dto.response.ProductImportResponse;
import udpm.hn.server.core.admin.product.repository.ProductManageRepository;
import udpm.hn.server.entity.Brand;
import udpm.hn.server.entity.Category;
import udpm.hn.server.entity.Product;
import udpm.hn.server.infrastructure.core.constant.EntityStatus;
import udpm.hn.server.infrastructure.core.constant.EntityUnit;
import udpm.hn.server.utils.SlugUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductExcelService {

    private final ProductManageRepository productRepository;
    private final BrandManageRepository brandRepository;
    private final CategoryManageRepository categoryRepository;

    @Transactional
    public ProductImportResponse importFromExcel(MultipartFile file) {
        List<Product> validProducts = new ArrayList<>();
        List<ProductExcelRow> errorRows = new ArrayList<>();
        int successCount = 0;

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            // 1. Tải Cache (Map) để validate nhanh
            // Map<BrandCode, Brand> (Chuyển về lower case để so sánh không phân biệt hoa thường)
            Map<String, Brand> brandMap = brandRepository.findAll().stream()
                    .collect(Collectors.toMap(b -> b.getCode().toLowerCase(), b -> b, (k1, k2) -> k1));

            // Map<CategoryName, Category> (Dùng Tên danh mục để map)
            Map<String, Category> categoryMap = categoryRepository.findAll().stream()
                    .collect(Collectors.toMap(c -> c.getName().toLowerCase(), c -> c, (k1, k2) -> k1));

            // 2. Duyệt từng dòng
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Bỏ qua header

                ProductExcelRow rowData = parseRow(row);

                // Bỏ qua dòng trống
                if (isEmptyRow(rowData)) continue;

                // 3. Validate
                List<String> errors = validateRow(rowData, brandMap, categoryMap);

                if (errors.isEmpty()) {
                    // Dòng Hợp lệ -> Convert sang Entity
                    Product product = createProductFromRow(rowData, brandMap, categoryMap);
                    validProducts.add(product);
                    successCount++;
                } else {
                    // Dòng Lỗi -> Thêm vào danh sách báo cáo
                    rowData.setErrorMessage(String.join(", ", errors));
                    rowData.setValid(false);
                    errorRows.add(rowData);
                }
            }

            // 4. Lưu tất cả dòng hợp lệ
            if (!validProducts.isEmpty()) {
                productRepository.saveAll(validProducts);
            }

        } catch (IOException e) {
            log.error("Lỗi đọc file Excel: ", e);
            throw new RuntimeException("Lỗi đọc file Excel: " + e.getMessage());
        }

        return new ProductImportResponse(successCount + errorRows.size(), successCount, errorRows.size(), errorRows);
    }

    private ProductExcelRow parseRow(Row row) {
        ProductExcelRow data = new ProductExcelRow();
        data.setRowIndex(row.getRowNum() + 1); // Excel bắt đầu từ 1
        data.setName(getCellValue(row, 0));
        data.setSku(getCellValue(row, 1));
        data.setPrice(getCellValue(row, 2));
        data.setQuantity(getCellValue(row, 3));
        data.setBrandCode(getCellValue(row, 4));    // Cột Brand Code
        data.setCategoryCode(getCellValue(row, 5)); // Cột Category Name
        data.setUnit(getCellValue(row, 6));
        data.setDescription(getCellValue(row, 7));
        return data;
    }

    private List<String> validateRow(ProductExcelRow row, Map<String, Brand> brandMap, Map<String, Category> categoryMap) {
        List<String> errors = new ArrayList<>();

        if (row.getName().isEmpty()) errors.add("Tên SP trống");
        if (row.getSku().isEmpty()) errors.add("SKU trống");
        else if (productRepository.existsBySku(row.getSku())) errors.add("SKU đã tồn tại"); // Check trùng SKU

        // Validate Giá
        try {
            if (new BigDecimal(row.getPrice()).compareTo(BigDecimal.ZERO) < 0) errors.add("Giá phải >= 0");
        } catch (Exception e) {
            errors.add("Giá không đúng định dạng số");
        }

        // Validate Số lượng
        try {
            if (Integer.parseInt(row.getQuantity()) < 0) errors.add("Số lượng phải >= 0");
        } catch (Exception e) {
            errors.add("Số lượng phải là số nguyên");
        }

        // Validate Brand (Theo Mã)
        if (row.getBrandCode().isEmpty()) errors.add("Mã thương hiệu trống");
        else if (!brandMap.containsKey(row.getBrandCode().toLowerCase())) errors.add("Mã thương hiệu không tồn tại");

        // Validate Category (Theo Tên)
        if (row.getCategoryCode().isEmpty()) errors.add("Tên danh mục trống");
        else if (!categoryMap.containsKey(row.getCategoryCode().toLowerCase())) errors.add("Danh mục không tồn tại");

        // Validate Unit (Enum)
        try {
            // Chuyển TitleCase (ví dụ: "Cai") để khớp Enum
            // Hoặc chấp nhận user nhập "Cái" rồi mình map tay, ở đây giả sử user nhập đúng code Enum hoặc tên
            // Ta sẽ hỗ trợ tìm kiếm case-insensitive
            boolean foundUnit = false;
            for (EntityUnit u : EntityUnit.values()) {
                if (u.name().equalsIgnoreCase(row.getUnit())) {
                    foundUnit = true;
                    break;
                }
            }
            if (!foundUnit && !row.getUnit().isEmpty()) errors.add("Đơn vị không hợp lệ");
        } catch (Exception e) {
            errors.add("Lỗi đơn vị");
        }

        return errors;
    }

    private Product createProductFromRow(ProductExcelRow row, Map<String, Brand> brandMap, Map<String, Category> categoryMap) {
        Product p = new Product();
        p.setName(row.getName());
        p.setSku(row.getSku());
        p.setSlug(SlugUtils.toSlug(row.getName())); // Tự động tạo Slug
        p.setPrice(new BigDecimal(row.getPrice()));
        p.setStockQuantity(Integer.parseInt(row.getQuantity()));
        p.setShortDescription(row.getDescription());
        p.setStatus(EntityStatus.ACTIVE); // Mặc định Active

        // Map quan hệ
        p.setBrand(brandMap.get(row.getBrandCode().toLowerCase()));
        p.setCategory(categoryMap.get(row.getCategoryCode().toLowerCase()));

        // Map Unit (Tìm enum khớp nhất)
        for (EntityUnit u : EntityUnit.values()) {
            if (u.name().equalsIgnoreCase(row.getUnit())) {
                p.setUnit(u);
                break;
            }
        }
        // Default unit nếu null? Có thể set CAI hoặc để null
        if (p.getUnit() == null) p.setUnit(EntityUnit.Cai);

        return p;
    }

    // Helper: Lấy giá trị ô an toàn
    private String getCellValue(Row row, int index) {
        Cell cell = row.getCell(index);
        if (cell == null) return "";

        switch (cell.getCellType()) {
            case STRING: return cell.getStringCellValue().trim();
            case NUMERIC:
                // Xử lý số nguyên để không bị hiện .0 (ví dụ: 10.0 -> 10)
                if (DateUtil.isCellDateFormatted(cell)) return cell.toString();
                double val = cell.getNumericCellValue();
                if (val == (long) val) return String.format("%d", (long) val);
                else return String.valueOf(val);
            case BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
            default: return "";
        }
    }

    private boolean isEmptyRow(ProductExcelRow row) {
        return row.getName().isEmpty() && row.getSku().isEmpty();
    }
}