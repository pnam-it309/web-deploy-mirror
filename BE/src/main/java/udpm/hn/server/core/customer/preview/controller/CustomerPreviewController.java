package udpm.hn.server.core.customer.preview.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import udpm.hn.server.core.admin.preview.service.PreviewService;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.customer.app.service.CustomerAppService;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import udpm.hn.server.utils.Helper;

@RestController
@RequestMapping(MappingConstants.API_CUSTOMER_PREVIEW)
@RequiredArgsConstructor
public class CustomerPreviewController {

    private final PreviewService previewService;
    private final CustomerAppService customerAppService;

    @GetMapping("/{token}")
    public ResponseEntity<?> getPreviewByToken(@PathVariable String token) {
        String appId = previewService.validatePreviewToken(token);

        if (appId == null) {
            return Helper.createResponseEntity(ResponseObject.errorForward("Invalid or expired preview token",
                    org.springframework.http.HttpStatus.NOT_FOUND));
        }

        try {
            // Get app details without approval status check
            var appDetail = customerAppService.getDetail(appId);
            return Helper.createResponseEntity(ResponseObject.successForward(appDetail, "Preview loaded"));
        } catch (Exception e) {
            return Helper.createResponseEntity(ResponseObject.errorForward(e.getMessage(),
                    org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
}
