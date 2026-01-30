package udpm.hn.server.core.admin.export.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import udpm.hn.server.core.admin.app.repository.AppManageRepository;
import udpm.hn.server.entity.App;
import udpm.hn.server.entity.Domain;
import udpm.hn.server.entity.Feature;
import udpm.hn.server.entity.Technology;
import udpm.hn.server.infrastructure.constant.ApprovalStatus;
import udpm.hn.server.repository.DomainRepository;
import udpm.hn.server.repository.FeatureRepository;
import udpm.hn.server.repository.TechnologyRepository;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExcelExportImportService {

    private final AppManageRepository appRepository;
    private final DomainRepository domainRepository;
    private final TechnologyRepository technologyRepository;
    private final FeatureRepository featureRepository;

    // ==========================================
    // APPS
    // ==========================================

    @Transactional(readOnly = true)
    public byte[] exportAppsToExcel() throws Exception {
        List<App> apps = appRepository.findAll();
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Apps");
            createHeader(workbook, sheet, new String[]{
                "ID", "Name", "SKU", "Short Description", "Domain", "Status",
                "Featured", "View Count", "Meta Title", "Meta Description", "Meta Keywords"
            });

            int rowNum = 1;
            for (App app : apps) {
                if (app.getDeletedAt() != null) continue;
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
            autoSizeColumns(sheet, 11);
            return write(workbook);
        }
    }

    public byte[] downloadTemplate() throws Exception {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Template");
            createTemplateHeader(workbook, sheet, new String[]{
                "ID (Auto)", "Name (Required)", "SKU", "Short Description", "Domain (Required)",
                "Status", "Featured", "View Count", "Meta Title", "Meta Description", "Meta Keywords"
            });
            // Sample
            Row sampleRow = sheet.createRow(1);
            sampleRow.createCell(1).setCellValue("Sample App Name");
            sampleRow.createCell(4).setCellValue("e-commerce");
            
            return write(workbook);
        }
    }

    public Map<String, Object> importAppsFromExcel(MultipartFile file) throws Exception {
        List<String> errors = new ArrayList<>();
        List<String> imported = new ArrayList<>();
        try (InputStream is = file.getInputStream(); Workbook workbook = WorkbookFactory.create(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            if (rowIterator.hasNext()) rowIterator.next(); // skip header

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                try {
                    String name = getCellStringValue(row.getCell(1));
                    if (isEmpty(name)) {
                         // Skip empty rows
                         continue;
                    }

                    String domainName = getCellStringValue(row.getCell(4));
                    Domain domain = null;
                    if (!isEmpty(domainName)) {
                        domain = domainRepository.findAll().stream()
                                .filter(d -> d.getName().equalsIgnoreCase(domainName))
                                .findFirst().orElse(null);
                    }
                    if (domain == null) {
                        errors.add("Row " + (row.getRowNum() + 1) + ": Domain '" + domainName + "' not found");
                        continue;
                    }

                    App app = new App();
                    app.setName(name);
                    app.setSku(getCellStringValue(row.getCell(2)));
                    app.setShortDescription(getCellStringValue(row.getCell(3)));
                    app.setDomain(domain);
                    app.setApprovalStatus(ApprovalStatus.PENDING); // Default
                    app.setIsFeatured(false);
                    app.setViewCount(0L);
                    app.setMetaTitle(getCellStringValue(row.getCell(8)));
                    app.setMetaDescription(getCellStringValue(row.getCell(9)));
                    app.setMetaKeywords(getCellStringValue(row.getCell(10)));
                    appRepository.save(app);
                    imported.add(name);
                } catch (Exception e) {
                    errors.add("Row " + (row.getRowNum() + 1) + ": " + e.getMessage());
                }
            }
        }
        return responseMap(imported, errors);
    }

    // ==========================================
    // DOMAINS
    // ==========================================

    @Transactional(readOnly = true)
    public byte[] exportDomainsToExcel() throws Exception {
        List<Domain> domains = domainRepository.findAll();
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Domains");
            createHeader(workbook, sheet, new String[]{ "ID", "Name", "Slug", "Description", "Parent Domain", "Sort Order" });
            
            int rowNum = 1;
            for (Domain d : domains) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(d.getId());
                row.createCell(1).setCellValue(d.getName());
                row.createCell(2).setCellValue(d.getSlug());
                row.createCell(3).setCellValue(d.getDescription());
                row.createCell(4).setCellValue(d.getParent() != null ? d.getParent().getName() : "");
                row.createCell(5).setCellValue(d.getSortOrder() != null ? d.getSortOrder() : 0);
            }
            autoSizeColumns(sheet, 6);
            return write(workbook);
        }
    }

    public byte[] downloadDomainTemplate() throws Exception {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Template");
            createTemplateHeader(workbook, sheet, new String[]{ "Name (Required)", "Slug", "Description", "Parent Domain Name", "Sort Order" });
            
            Row row = sheet.createRow(1);
            row.createCell(0).setCellValue("Technology & Devices");
            row.createCell(2).setCellValue("Sample Description");
            return write(workbook);
        }
    }

    public Map<String, Object> importDomainsFromExcel(MultipartFile file) throws Exception {
        List<String> errors = new ArrayList<>();
        List<String> imported = new ArrayList<>();
        try (InputStream is = file.getInputStream(); Workbook workbook = WorkbookFactory.create(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            if (rowIterator.hasNext()) rowIterator.next();

            List<Domain> allDomains = domainRepository.findAll(); 

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                try {
                    String name = getCellStringValue(row.getCell(0));
                    if (isEmpty(name)) continue;

                    String parentName = getCellStringValue(row.getCell(3));
                    Domain parent = null;
                    if(!isEmpty(parentName)) {
                        parent = allDomains.stream().filter(d -> d.getName().equalsIgnoreCase(parentName)).findFirst().orElse(null);
                        // Warning: if parent not found, we ignore or error? Let's ignore for now or log error?
                        // If strict: error.
                    }

                    Domain d = new Domain();
                    d.setName(name);
                    d.setSlug(getCellStringValue(row.getCell(1)));
                    d.setDescription(getCellStringValue(row.getCell(2)));
                    d.setParent(parent);
                    
                    String sortOrderStr = getCellStringValue(row.getCell(4));
                    try { d.setSortOrder(sortOrderStr != null ? Integer.parseInt(sortOrderStr) : 0); } catch(Exception e) { d.setSortOrder(0); }

                    domainRepository.save(d);
                    allDomains.add(d); // Add to local list for subsequent parents
                    imported.add(name);
                } catch (Exception e) {
                   errors.add("Row " + (row.getRowNum() + 1) + ": " + e.getMessage());
                }
            }
        }
        return responseMap(imported, errors);
    }

    // ==========================================
    // TECHNOLOGIES
    // ==========================================

    @Transactional(readOnly = true)
    public byte[] exportTechnologiesToExcel() throws Exception {
        List<Technology> techs = technologyRepository.findAll();
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Technologies");
            createHeader(workbook, sheet, new String[]{ "ID", "Name", "Slug", "Description", "Icon" });
            
            int rowNum = 1;
            for (Technology t : techs) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(t.getId());
                row.createCell(1).setCellValue(t.getName());
                row.createCell(2).setCellValue(t.getSlug());
                row.createCell(3).setCellValue(t.getDescription());
                row.createCell(4).setCellValue(t.getIcon());
            }
            autoSizeColumns(sheet, 5);
            return write(workbook);
        }
    }

    public byte[] downloadTechnologyTemplate() throws Exception {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Template");
            createTemplateHeader(workbook, sheet, new String[]{ "Name (Required)", "Slug", "Description", "Icon" });
            
            Row row = sheet.createRow(1);
            row.createCell(0).setCellValue("Java");
            row.createCell(2).setCellValue("Backend Language");
            return write(workbook);
        }
    }

    public Map<String, Object> importTechnologiesFromExcel(MultipartFile file) throws Exception {
        List<String> errors = new ArrayList<>();
        List<String> imported = new ArrayList<>();
        try (InputStream is = file.getInputStream(); Workbook workbook = WorkbookFactory.create(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> iter = sheet.iterator();
            if (iter.hasNext()) iter.next();

            while (iter.hasNext()) {
                Row row = iter.next();
                try {
                    String name = getCellStringValue(row.getCell(0));
                    if (isEmpty(name)) continue;

                    Technology t = new Technology();
                    t.setName(name);
                    t.setSlug(getCellStringValue(row.getCell(1)));
                    t.setDescription(getCellStringValue(row.getCell(2)));
                    t.setIcon(getCellStringValue(row.getCell(3)));
                    technologyRepository.save(t);
                    imported.add(name);
                } catch(Exception e) {
                     errors.add("Row " + (row.getRowNum() + 1) + ": " + e.getMessage());
                }
            }
        }
        return responseMap(imported, errors);
    }

    // ==========================================
    // FEATURES
    // ==========================================

    @Transactional(readOnly = true)
    public byte[] exportFeaturesToExcel() throws Exception {
        List<Feature> features = featureRepository.findAll();
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Features");
            createHeader(workbook, sheet, new String[]{ "ID", "Name", "App Name", "Description", "Sort Order" });
            
            int rowNum = 1;
            for (Feature f : features) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(f.getId());
                row.createCell(1).setCellValue(f.getName());
                row.createCell(2).setCellValue(f.getApp() != null ? f.getApp().getName() : "");
                row.createCell(3).setCellValue(f.getDescription());
                row.createCell(4).setCellValue(f.getSortOrder() != null ? f.getSortOrder() : 0);
            }
            autoSizeColumns(sheet, 5);
            return write(workbook);
        }
    }

    public byte[] downloadFeatureTemplate() throws Exception {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
             Sheet sheet = workbook.createSheet("Template");
             createTemplateHeader(workbook, sheet, new String[]{ "Name (Required)", "App SKU (Required)", "Description", "Sort Order" });
             
             Row row = sheet.createRow(1);
             row.createCell(0).setCellValue("Login Feature");
             row.createCell(1).setCellValue("APP-001");
             row.createCell(2).setCellValue("Authentication module");
             return write(workbook);
        }
    }

    public Map<String, Object> importFeaturesFromExcel(MultipartFile file) throws Exception {
         List<String> errors = new ArrayList<>();
         List<String> imported = new ArrayList<>();
         try (InputStream is = file.getInputStream(); Workbook workbook = WorkbookFactory.create(is)) {
             Sheet sheet = workbook.getSheetAt(0);
             Iterator<Row> iter = sheet.iterator();
             if (iter.hasNext()) iter.next();

             while (iter.hasNext()) {
                 Row row = iter.next();
                 try {
                     String name = getCellStringValue(row.getCell(0));
                     if (isEmpty(name)) continue;

                     String appSku = getCellStringValue(row.getCell(1));
                     if (isEmpty(appSku)) {
                          errors.add("Row " + (row.getRowNum()+1) + ": App SKU required");
                          continue;
                     }
                     
                     // Find app by SKU or Name ? SKU is better.
                     App app = appRepository.findAll().stream()
                        .filter(a -> appSku.equalsIgnoreCase(a.getSku()))
                        .findFirst().orElse(null);
                     
                     if (app == null) {
                         errors.add("Row " + (row.getRowNum() + 1) + ": App with SKU '" + appSku + "' not found");
                         continue;
                     }

                     Feature f = new Feature();
                     f.setName(name);
                     f.setApp(app);
                     f.setDescription(getCellStringValue(row.getCell(2)));
                     String so = getCellStringValue(row.getCell(3));
                     try { f.setSortOrder(so != null ? Integer.parseInt(so) : 0); } catch(Exception e) { f.setSortOrder(0); }
                     
                     featureRepository.save(f);
                     imported.add(name);
                 } catch(Exception e) {
                      errors.add("Row " + (row.getRowNum() + 1) + ": " + e.getMessage());
                 }
             }
         }
         return responseMap(imported, errors);
    }


    // ==========================================
    // HELPERS
    // ==========================================

    private void createHeader(Workbook wb, Sheet sheet, String[] headers) {
        Row headerRow = sheet.createRow(0);
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(style);
        }
    }

    private void createTemplateHeader(Workbook wb, Sheet sheet, String[] headers) {
        Row headerRow = sheet.createRow(0);
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.ROYAL_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(style);
            sheet.setColumnWidth(i, 20 * 256);
        }
    }

    private void autoSizeColumns(Sheet sheet, int count) {
        for (int i = 0; i < count; i++) sheet.autoSizeColumn(i);
    }

    private byte[] write(Workbook wb) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        wb.write(out);
        return out.toByteArray();
    }

    private boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }

    private Map<String, Object> responseMap(List<String> imported, List<String> errors) {
        return Map.of("importedCount", imported.size(), "imported", imported, "errorCount", errors.size(), "errors", errors);
    }

    private String getCellStringValue(Cell cell) {
        if (cell == null) return null;
        switch (cell.getCellType()) {
            case STRING: return cell.getStringCellValue();
            case NUMERIC: return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
            default: return null;
        }
    }
}
