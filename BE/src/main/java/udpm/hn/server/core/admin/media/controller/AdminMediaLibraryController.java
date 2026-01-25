package udpm.hn.server.core.admin.media.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import udpm.hn.server.core.admin.media.service.MediaLibraryService;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.entity.MediaLibrary;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import udpm.hn.server.repository.MediaLibraryRepository;
import udpm.hn.server.repository.specification.MediaLibrarySpecification;
import udpm.hn.server.utils.Helper;

import java.util.Map;

@RestController
@RequestMapping(MappingConstants.API_ADMIN_MEDIA)
@RequiredArgsConstructor
public class AdminMediaLibraryController {

    private final MediaLibraryRepository mediaLibraryRepository;
    private final MediaLibraryService mediaLibraryService;

    /**
     * Get all media with optional filtering
     * Now uses MediaLibrarySpecification for dynamic filtering
     */
    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String folder,
            @RequestParam(required = false) String search) {
        Pageable pageable = PageRequest.of(page, size);

        // Build specification based on filters
        Specification<MediaLibrary> spec = Specification.where(null);

        if (search != null && !search.isEmpty()) {
            spec = spec.and(MediaLibrarySpecification.searchByFilename(search));
        }

        if (type != null && !type.isEmpty()) {
            spec = spec.and(MediaLibrarySpecification.hasFileType(type));
        }

        if (folder != null && !folder.isEmpty()) {
            spec = spec.and(MediaLibrarySpecification.inFolder(folder));
        }

        // Always order by created date descending
        spec = spec.and(MediaLibrarySpecification.orderByCreatedAtDesc());

        return Helper.createResponseEntity(ResponseObject.successForward(
                mediaLibraryRepository.findAll(spec, pageable),
                "Lấy media library thành công"));
    }

    @GetMapping("/folders")
    public ResponseEntity<?> getFolders() {
        return Helper.createResponseEntity(ResponseObject.successForward(
                mediaLibraryRepository.findDistinctFolders(),
                "Lấy danh sách thư mục thành công"));
    }

    @GetMapping("/tags")
    public ResponseEntity<?> getTags() {
        return Helper.createResponseEntity(ResponseObject.successForward(
                mediaLibraryRepository.findDistinctTags(),
                "Lấy danh sách tags thành công"));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody MediaLibrary request) {
        MediaLibrary saved = mediaLibraryService.create(request);
        return Helper.createResponseEntity(ResponseObject.successForward(saved, "Thêm media thành công"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody Map<String, Object> updates) {
        MediaLibrary saved = mediaLibraryService.update(id, updates);
        return Helper.createResponseEntity(ResponseObject.successForward(saved, "Cập nhật thành công"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        mediaLibraryService.delete(id);
        return Helper.createResponseEntity(ResponseObject.successForward(null, "Xoá thành công"));
    }

    @PostMapping("/{id}/increment-usage")
    public ResponseEntity<?> incrementUsage(@PathVariable String id) {
        mediaLibraryService.incrementUsage(id);
        return Helper.createResponseEntity(ResponseObject.successForward(null, "OK"));
    }
}
