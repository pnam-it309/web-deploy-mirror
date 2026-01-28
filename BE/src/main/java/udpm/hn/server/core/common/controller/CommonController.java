package udpm.hn.server.core.common.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import udpm.hn.server.core.common.service.MediaService;
import udpm.hn.server.infrastructure.constant.MappingConstants;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping(MappingConstants.API_COMMON)
@RequiredArgsConstructor
public class CommonController {

    private final MediaService mediaService;

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String url = mediaService.uploadFile(file);
            return ResponseEntity.ok(Collections.singletonMap("url", url));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", e.getMessage()));
        }
    }
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("Server is awake!");
    }

}
