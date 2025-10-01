package udpm.hn.server.core.admin.import_data.service;

import udpm.hn.server.core.admin.import_data.dto.request.BaseImportRequest;
import udpm.hn.server.core.admin.import_data.dto.response.BaseImportResponse;

import java.io.IOException;

/**
 * Service interface for importing data from various sources like Excel and Google Sheets.
 *
 * @param <T> The type of the request DTO that extends BaseImportRequest
 * @param <R> The type of the response DTO that extends BaseImportResponse
 */
public interface ImportService<T extends BaseImportRequest, R extends BaseImportResponse> {

    /**
     * Import data from an Excel file
     *
     * @param request The import request containing the file and options
     * @return The import response with results and any errors
     * @throws IOException If there is an error reading the file
     */
    R importFromExcel(T request) throws IOException;

    /**
     * Import data from a Google Sheet
     *
     * @param request The import request containing the sheet URL and options
     * @return The import response with results and any errors
     * @throws IOException If there is an error accessing the Google Sheet
     */
    R importFromGoogleSheet(T request) throws IOException;
}
