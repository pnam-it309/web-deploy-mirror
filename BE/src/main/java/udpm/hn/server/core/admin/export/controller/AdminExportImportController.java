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

    @GetMapping("/apps/excel")
    public ResponseEntity<byte[]> exportAppsToExcel() {
        try {
            byte[] excelBytes = exportImportService.exportAppsToExcel();

            String fileName = "apps_export_" +
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) +
                    ".xlsx";

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                    .contentType(MediaType
                            .parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(excelBytes);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/apps/excel")
    public ResponseEntity<?> importAppsFromExcel(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return Helper.createResponseEntity(ResponseObject.<Object>errorForward("File is empty"));
            }

            String contentType = file.getContentType();
            if (contentType == null ||
                    (!contentType.contains("spreadsheetml") && !contentType.contains("excel"))) {
                return Helper.createResponseEntity(
                        ResponseObject.<Object>errorForward("Invalid file type. Please upload an Excel file"));
            }

            var result = exportImportService.importAppsFromExcel(file);
            return Helper.createResponseEntity(ResponseObject.successForward(result, "Import completed"));

        } catch (Exception e) {
            return Helper.createResponseEntity(ResponseObject.<Object>errorForward("Import failed: " + e.getMessage()));
        }
    }
}
