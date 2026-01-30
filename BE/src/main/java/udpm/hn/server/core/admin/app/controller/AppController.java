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
    public ResponseEntity<AppDetailResponse> updateAppDetail(@PathVariable String id,
            @RequestBody AppDetailUpdateRequest request) {
        return ResponseEntity.ok(appService.updateAppDetail(id, request));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> changeStatus(@PathVariable String id, @RequestParam("status") String status) {
        // Simple conversion string -> enum
        try {
            udpm.hn.server.infrastructure.constant.ApprovalStatus s = udpm.hn.server.infrastructure.constant.ApprovalStatus
                    .valueOf(status.toUpperCase());
            appService.changeStatus(id, s);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/featured")
    public ResponseEntity<Void> toggleFeatured(@PathVariable String id) {
        appService.toggleFeatured(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/bulk-update-homepage-order")
    public ResponseEntity<Void> bulkUpdateHomepageOrder(@RequestBody java.util.List<HomepageOrderRequest> requests) {
        appService.bulkUpdateHomepageOrder(requests);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/github-contributors")
    public ResponseEntity<java.util.List<udpm.hn.server.core.admin.app.dto.response.AppResponse.MemberResponse>> getGithubContributors(
            @RequestParam("url") String url,
            @RequestParam(value = "token", required = false) String token) {
        return ResponseEntity.ok(appService.getGithubContributors(url, token));
    }

    @PostMapping("/{id}/duplicate")
    public ResponseEntity<AppResponse> duplicateApp(@PathVariable String id) {
        return new ResponseEntity<>(appService.duplicateApp(id), HttpStatus.CREATED);
    }

    @DeleteMapping("/batch")
    public ResponseEntity<Void> deleteApps(@RequestBody java.util.List<String> ids) {
        appService.deleteApps(ids);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/batch/status")
    public ResponseEntity<Void> changeStatusApps(
            @RequestBody java.util.List<String> ids,
            @RequestParam("status") String status) {
        try {
            udpm.hn.server.infrastructure.constant.ApprovalStatus s = udpm.hn.server.infrastructure.constant.ApprovalStatus
                    .valueOf(status.toUpperCase());
            appService.changeStatusApps(ids, s);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}