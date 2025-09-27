package udpm.hn.server.core.admin.import_data.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import udpm.hn.server.core.admin.import_data.service.ImportService;
import udpm.hn.server.infrastructure.core.constant.MappingConstants;

@RestController
@RequiredArgsConstructor
@RequestMapping(MappingConstants.API_ADMIN_IMPORT)
public class ImportDataController {
    private final ImportService importService;
}
