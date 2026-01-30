package udpm.hn.server.core.admin.export.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import udpm.hn.server.core.admin.export.service.ExcelExportImportService;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import udpm.hn.server.utils.Helper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping(MappingConstants.API_ADMIN_EXPORT)
@RequiredArgsConstructor
public class AdminExportImportController {

    private final ExcelExportImportService exportImportService;

    // ================== APPS ==================

    @GetMapping("/apps/excel")
    public ResponseEntity<byte[]> exportAppsToExcel() {
        return createExportResponse(exportImportService::exportAppsToExcel, "apps_export");
    }

    @PostMapping("/apps/excel")
    public ResponseEntity<?> importAppsFromExcel(@RequestParam("file") MultipartFile file) {
        return createImportResponse(file, () -> exportImportService.importAppsFromExcel(file));
    }

    @GetMapping("/apps/template")
    public ResponseEntity<byte[]> downloadAppTemplate() {
        return createExportResponse(exportImportService::downloadTemplate, "apps_import_template", true);
    }

    // ================== DOMAINS ==================

    @GetMapping("/domains/excel")
    public ResponseEntity<byte[]> exportDomainsToExcel() {
        return createExportResponse(exportImportService::exportDomainsToExcel, "domains_export");
    }

    @PostMapping("/domains/excel")
    public ResponseEntity<?> importDomainsFromExcel(@RequestParam("file") MultipartFile file) {
        return createImportResponse(file, () -> exportImportService.importDomainsFromExcel(file));
    }

    @GetMapping("/domains/template")
    public ResponseEntity<byte[]> downloadDomainTemplate() {
         return createExportResponse(exportImportService::downloadDomainTemplate, "domains_import_template", true);
    }

    // ================== TECHNOLOGIES ==================

    @GetMapping("/technologies/excel")
    public ResponseEntity<byte[]> exportTechnologiesToExcel() {
        return createExportResponse(exportImportService::exportTechnologiesToExcel, "technologies_export");
    }

    @PostMapping("/technologies/excel")
    public ResponseEntity<?> importTechnologiesFromExcel(@RequestParam("file") MultipartFile file) {
        return createImportResponse(file, () -> exportImportService.importTechnologiesFromExcel(file));
    }

    @GetMapping("/technologies/template")
    public ResponseEntity<byte[]> downloadTechnologyTemplate() {
        return createExportResponse(exportImportService::downloadTechnologyTemplate, "technologies_import_template", true);
    }

    // ================== FEATURES ==================

    @GetMapping("/features/excel")
    public ResponseEntity<byte[]> exportFeaturesToExcel() {
        return createExportResponse(exportImportService::exportFeaturesToExcel, "features_export");
    }

    @PostMapping("/features/excel")
    public ResponseEntity<?> importFeaturesFromExcel(@RequestParam("file") MultipartFile file) {
        return createImportResponse(file, () -> exportImportService.importFeaturesFromExcel(file));
    }

    @GetMapping("/features/template")
    public ResponseEntity<byte[]> downloadFeatureTemplate() {
        return createExportResponse(exportImportService::downloadFeatureTemplate, "features_import_template", true);
    }

    // ================== HELPERS ==================

    @FunctionalInterface
    interface ByteSupplier {
        byte[] get() throws Exception;
    }

    @FunctionalInterface
    interface MapSupplier {
        Object get() throws Exception;
    }

    private ResponseEntity<byte[]> createExportResponse(ByteSupplier supplier, String baseName) {
        return createExportResponse(supplier, baseName, false);
    }

    private ResponseEntity<byte[]> createExportResponse(ByteSupplier supplier, String baseName, boolean isTemplate) {
        try {
            byte[] excelBytes = supplier.get();
            String fileName = baseName + (isTemplate ? "" : "_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))) + ".xlsx";

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(excelBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE)
                    .body(("Export failed: " + e.getMessage()).getBytes());
        }
    }

    private ResponseEntity<?> createImportResponse(MultipartFile file, MapSupplier supplier) {
        try {
            if (file.isEmpty()) return Helper.createResponseEntity(ResponseObject.<Object>errorForward("File is empty"));
            String contentType = file.getContentType();
            if (contentType == null || (!contentType.contains("spreadsheetml") && !contentType.contains("excel"))) {
                return Helper.createResponseEntity(ResponseObject.<Object>errorForward("Invalid file type. Please upload an Excel file"));
            }
            return Helper.createResponseEntity(ResponseObject.successForward(supplier.get(), "Import completed"));
        } catch (Exception e) {
            e.printStackTrace();
            return Helper.createResponseEntity(ResponseObject.<Object>errorForward("Import failed: " + e.getMessage()));
        }
    }
}
