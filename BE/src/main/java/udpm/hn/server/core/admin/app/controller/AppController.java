package udpm.hn.server.core.admin.app.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import udpm.hn.server.core.admin.app.dto.request.*;
import udpm.hn.server.core.admin.app.dto.response.AppDetailResponse;
import udpm.hn.server.core.admin.app.dto.response.AppResponse;
import udpm.hn.server.core.admin.app.service.AppService;
import udpm.hn.server.infrastructure.constant.MappingConstants;

@RestController
@RequestMapping(MappingConstants.API_ADMIN_APP)
@RequiredArgsConstructor
public class AppController {

    private final AppService appService;

    @GetMapping
    public ResponseEntity<Page<AppResponse>> getAllApps(
            @ModelAttribute AppFilterRequest request,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(appService.getAllApps(request, pageable));
    }

    @PostMapping
    public ResponseEntity<AppResponse> createApp(@Valid @RequestBody AppCreateRequest request) {
        return new ResponseEntity<>(appService.createApp(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppResponse> getAppById(@PathVariable String id) {
        return ResponseEntity.ok(appService.getAppById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppResponse> updateApp(@PathVariable String id, @RequestBody AppUpdateRequest request) {
        return ResponseEntity.ok(appService.updateApp(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApp(@PathVariable String id) {
        appService.deleteApp(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<AppDetailResponse> getAppDetail(@PathVariable String id) {
        return ResponseEntity.ok(appService.getAppDetail(id));
    }

    @PutMapping("/{id}/details")
    public ResponseEntity<AppDetailResponse> updateAppDetail(@PathVariable String id, @RequestBody AppDetailUpdateRequest request) {
        return ResponseEntity.ok(appService.updateAppDetail(id, request));
    }
}