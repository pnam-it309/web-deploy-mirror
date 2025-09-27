package udpm.hn.server.infrastructure.integration.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import udpm.hn.server.infrastructure.data.googlesheets.GoogleSheetsAdvancedService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/sheets")
@RequiredArgsConstructor
@Tag(name = "Google Sheets API", description = "APIs for Google Sheets and Excel integration")
public class GoogleSheetsController {

    private final GoogleSheetsAdvancedService sheetsService;

    @Operation(
        summary = "Import Excel to Google Sheets",
        description = "Upload an Excel file and import its data into a Google Sheet"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Excel imported successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(
        value = "/import/excel",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> importExcelToSheets(
            @Parameter(description = "Excel file to import")
            @RequestParam("file") MultipartFile file,
            
            @Parameter(description = "Google Spreadsheet ID")
            @RequestParam String spreadsheetId,
            
            @Parameter(description = "Target sheet name (default: Sheet1)")
            @RequestParam(required = false, defaultValue = "Sheet1") String sheetName) throws IOException {
        
        sheetsService.importExcelToSheets(file, spreadsheetId, sheetName);
        return ResponseEntity.ok().body("Excel imported to Google Sheets successfully");
    }

    @Operation(
        summary = "Export Google Sheet to Excel",
        description = "Export data from a Google Sheet to an Excel file"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Excel file generated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(
        value = "/export/excel",
        produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    public ResponseEntity<byte[]> exportSheetToExcel(
            @Parameter(description = "Google Spreadsheet ID", required = true)
            @RequestParam String spreadsheetId,
            
            @Parameter(description = "Range to export (e.g., 'Sheet1!A1:Z100')")
            @RequestParam(required = false, defaultValue = "Sheet1") String range) throws IOException {
        
        byte[] excelData = sheetsService.exportSheetToExcel(spreadsheetId, range);
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=exported_sheet.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excelData);
    }

    @Operation(
        summary = "Get sheet names",
        description = "Get a list of all sheet names in a Google Spreadsheet"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved sheet names"),
        @ApiResponse(responseCode = "400", description = "Invalid spreadsheet ID"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(
        value = "/{spreadsheetId}/sheets",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<String>> getSheetNames(
            @Parameter(description = "Google Spreadsheet ID", required = true)
            @PathVariable String spreadsheetId) throws IOException {
        List<String> sheetNames = sheetsService.getSheetNames(spreadsheetId);
        return ResponseEntity.ok(sheetNames);
    }

    @Operation(
        summary = "Create multiple sheets",
        description = "Create multiple sheets in a Google Spreadsheet"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Sheets created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(
        value = "/{spreadsheetId}/sheets",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> createSheets(
            @Parameter(description = "Google Spreadsheet ID", required = true)
            @PathVariable String spreadsheetId,
            
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "List of sheet names to create",
                required = true,
                content = @Content(schema = @Schema(implementation = List.class))
            )
            @RequestBody List<String> sheetNames) throws IOException {
        
        sheetsService.createSheets(spreadsheetId, sheetNames);
        return ResponseEntity.ok().body("Sheets created successfully");
    }
}
