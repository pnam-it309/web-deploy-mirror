package udpm.hn.server.core.admin.export.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import udpm.hn.server.core.admin.app.repository.AppManageRepository;
import udpm.hn.server.entity.App;
import udpm.hn.server.entity.Domain;
import udpm.hn.server.infrastructure.constant.ApprovalStatus;
import udpm.hn.server.repository.DomainRepository;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExcelExportImportService {

    private final AppManageRepository appRepository;
    private final DomainRepository domainRepository;

    public byte[] exportAppsToExcel() throws Exception {
        List<App> apps = appRepository.findAll();

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Apps");

            // Header row
            Row headerRow = sheet.createRow(0);
            String[] headers = { "ID", "Name", "SKU", "Short Description", "Domain", "Status",
                    "Featured", "View Count", "Meta Title", "Meta Description", "Meta Keywords" };

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Data rows
            int rowNum = 1;
            for (App app : apps) {
                if (app.getDeletedAt() != null)
                    continue; // Skip deleted

                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(app.getId());
                row.createCell(1).setCellValue(app.getName());
                row.createCell(2).setCellValue(app.getSku() != null ? app.getSku() : "");
                row.createCell(3).setCellValue(app.getShortDescription() != null ? app.getShortDescription() : "");
                row.createCell(4).setCellValue(app.getDomain() != null ? app.getDomain().getName() : "");
                row.createCell(5).setCellValue(app.getApprovalStatus() != null ? app.getApprovalStatus().name() : "");
                row.createCell(6).setCellValue(Boolean.TRUE.equals(app.getIsFeatured()) ? "Yes" : "No");
                row.createCell(7).setCellValue(app.getViewCount() != null ? app.getViewCount() : 0);
                row.createCell(8).setCellValue(app.getMetaTitle() != null ? app.getMetaTitle() : "");
                row.createCell(9).setCellValue(app.getMetaDescription() != null ? app.getMetaDescription() : "");
                row.createCell(10).setCellValue(app.getMetaKeywords() != null ? app.getMetaKeywords() : "");
            }

            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();
        }
    }

    public Map<String, Object> importAppsFromExcel(MultipartFile file) throws Exception {
        List<String> errors = new ArrayList<>();
        List<String> imported = new ArrayList<>();

        try (InputStream is = file.getInputStream();
                Workbook workbook = WorkbookFactory.create(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            // Skip header
            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                try {
                    String name = getCellStringValue(row.getCell(1));
                    if (name == null || name.isEmpty()) {
                        errors.add("Row " + row.getRowNum() + ": Name is required");
                        continue;
                    }

                    String domainName = getCellStringValue(row.getCell(4));
                    Domain domain = null;
                    if (domainName != null && !domainName.isEmpty()) {
                        domain = domainRepository.findAll().stream()
                                .filter(d -> d.getName().equalsIgnoreCase(domainName))
                                .findFirst()
                                .orElse(null);
                    }

                    if (domain == null) {
                        errors.add("Row " + row.getRowNum() + ": Domain '" + domainName + "' not found");
                        continue;
                    }

                    App app = new App();
                    app.setName(name);
                    app.setSku(getCellStringValue(row.getCell(2)));
                    app.setShortDescription(getCellStringValue(row.getCell(3)));
                    app.setDomain(domain);
                    app.setApprovalStatus(ApprovalStatus.PENDING);
                    app.setIsFeatured(false);
                    app.setViewCount(0L);
                    app.setMetaTitle(getCellStringValue(row.getCell(8)));
                    app.setMetaDescription(getCellStringValue(row.getCell(9)));
                    app.setMetaKeywords(getCellStringValue(row.getCell(10)));

                    appRepository.save(app);
                    imported.add(name);

                } catch (Exception e) {
                    errors.add("Row " + row.getRowNum() + ": " + e.getMessage());
                }
            }
        }

        return Map.of(
                "importedCount", imported.size(),
                "imported", imported,
                "errorCount", errors.size(),
                "errors", errors);
    }

    private String getCellStringValue(Cell cell) {
        if (cell == null)
            return null;
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return null;
        }
    }
}
