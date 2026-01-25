package udpm.hn.server.core.admin.preview.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import udpm.hn.server.core.admin.preview.service.PreviewService;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import udpm.hn.server.utils.Helper;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(MappingConstants.API_ADMIN_PREVIEW)
@RequiredArgsConstructor
public class AdminPreviewController {

    private final PreviewService previewService;

    @PostMapping("/{appId}/generate-token")
    public ResponseEntity<?> generatePreviewToken(@PathVariable String appId) {
        try {
            String token = previewService.generatePreviewToken(appId);

            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("previewUrl", "/preview/" + token);

            return Helper.createResponseEntity(ResponseObject.successForward(response, "Preview token generated"));
        } catch (Exception e) {
            return Helper.createResponseEntity(
                    ResponseObject.errorForward(e.getMessage(), org.springframework.http.HttpStatus.BAD_REQUEST));
        }
    }
}
