package udpm.hn.server.core.admin.trash.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import udpm.hn.server.core.admin.app.repository.AppManageRepository;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.entity.App;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import udpm.hn.server.utils.Helper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(MappingConstants.API_ADMIN_TRASH)
@RequiredArgsConstructor
public class AdminTrashController {

        private final AppManageRepository appRepository;

        @GetMapping("/apps")
        public ResponseEntity<?> getDeletedApps(
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "20") int size) {
                Pageable pageable = PageRequest.of(page, size);

                List<App> deletedApps = appRepository.findAll().stream()
                                .filter(app -> app.getDeletedAt() != null)
                                .sorted((a, b) -> Long.compare(b.getDeletedAt(), a.getDeletedAt()))
                                .toList();

                int start = (int) pageable.getOffset();
                int end = Math.min(start + pageable.getPageSize(), deletedApps.size());
                List<Map<String, Object>> content = deletedApps.subList(start, end).stream()
                                .map(app -> {
                                        Map<String, Object> map = new java.util.HashMap<>();
                                        map.put("id", app.getId());
                                        map.put("name", app.getName());
                                        map.put("thumbnail", app.getThumbnail() != null ? app.getThumbnail() : "");
                                        map.put("deletedAt", app.getDeletedAt());
                                        map.put("daysRemaining", 30 - (System.currentTimeMillis() - app.getDeletedAt())
                                                        / (24 * 60 * 60 * 1000));
                                        return map;
                                })
                                .collect(Collectors.toList());

                Page<Map<String, Object>> result = new PageImpl<>(content, pageable, deletedApps.size());
                return Helper.createResponseEntity(ResponseObject.successForward(result, "Lấy thùng rác thành công"));
        }

        @PostMapping("/apps/{id}/restore")
        @Transactional
        public ResponseEntity<?> restoreApp(@PathVariable String id) {
                App app = appRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("App not found"));

                if (app.getDeletedAt() == null) {
                        return Helper.createResponseEntity(ResponseObject.errorForward("App chưa bị xoá"));
                }

                app.setDeletedAt(null);
                appRepository.save(app);

                return Helper.createResponseEntity(ResponseObject.successForward(null, "Khôi phục thành công"));
        }

        @DeleteMapping("/apps/{id}/permanent")
        @Transactional
        public ResponseEntity<?> permanentDeleteApp(@PathVariable String id) {
                App app = appRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("App not found"));

                appRepository.delete(app);

                return Helper.createResponseEntity(ResponseObject.successForward(null, "Xoá vĩnh viễn thành công"));
        }

        @DeleteMapping("/apps/empty")
        @Transactional
        public ResponseEntity<?> emptyTrash() {
                List<App> deletedApps = appRepository.findAll().stream()
                                .filter(app -> app.getDeletedAt() != null)
                                .toList();

                appRepository.deleteAll(deletedApps);

                return Helper.createResponseEntity(ResponseObject.successForward(
                                Map.of("deletedCount", deletedApps.size()),
                                "Đã dọn sạch thùng rác"));
        }
}
