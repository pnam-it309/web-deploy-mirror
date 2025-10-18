package udpm.hn.server.core.admin.import_data.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import udpm.hn.server.core.admin.import_data.dto.request.ProductImportRequest;
import udpm.hn.server.core.admin.import_data.dto.response.ProductImportResponse;
import udpm.hn.server.core.admin.import_data.service.ImportService;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.infrastructure.core.constant.MappingConstants;
import udpm.hn.server.utils.Helper;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping(MappingConstants.API_ADMIN_IMPORT)
public class ImportDataController {

    private final ImportService<ProductImportRequest, ProductImportResponse> importService;

    @PostMapping("/products/excel")
    public ResponseEntity<?> importProductsFromExcel(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "hasHeader", defaultValue = "true") boolean hasHeader,
            @RequestParam(value = "headerRow", defaultValue = "0") int headerRow,
            @RequestParam(value = "dataStartRow", defaultValue = "1") int dataStartRow) {

        ProductImportRequest request = new ProductImportRequest();
        request.setFile(file);
        request.setHasHeader(hasHeader);
        request.setHeaderRow(headerRow);
        request.setDataStartRow(dataStartRow);

        try {
            return Helper.createResponseEntity(
                    ResponseObject.successForward(importService.importFromExcel(request), "Import successful")
            );
        } catch (IOException e) {
            return Helper.createResponseEntity(
                    ResponseObject.errorForward("Failed to import from Excel: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR)
            );
        }
    }

    @PostMapping("/products/google-sheet")
    public ResponseEntity<?> importProductsFromGoogleSheet(
            @RequestParam("sheetUrl") String sheetUrl,
            @RequestParam(value = "sheetName", required = false) String sheetName,
            @RequestParam(value = "hasHeader", defaultValue = "true") boolean hasHeader,
            @RequestParam(value = "headerRow", defaultValue = "0") int headerRow,
            @RequestParam(value = "dataStartRow", defaultValue = "1") int dataStartRow) {

        ProductImportRequest request = new ProductImportRequest();
        request.setSheetUrl(sheetUrl);
        request.setSheetName(sheetName);
        request.setHasHeader(hasHeader);
        request.setHeaderRow(headerRow);
        request.setDataStartRow(dataStartRow);

        try {
            return Helper.createResponseEntity(
                    ResponseObject.successForward(importService.importFromGoogleSheet(request), "Import successful")
            );
        } catch (IOException e) {
            return Helper.createResponseEntity(
                    ResponseObject.errorForward("Failed to import from Google Sheet: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR)
            );
        }
    }
}
