package udpm.hn.server.core.admin.technology.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import udpm.hn.server.core.admin.technology.dto.request.TechnologyRequest;
import udpm.hn.server.core.admin.technology.service.TechnologyService;
import udpm.hn.server.infrastructure.constant.MappingConstants;

@RestController
@RequestMapping(MappingConstants.API_ADMIN_TECHNOLOGY)
@RequiredArgsConstructor
public class TechnologyController {

    private final TechnologyService technologyService;

    @GetMapping
    public ResponseEntity<?> getAllTechnologies(
            @RequestParam(required = false, defaultValue = "false") boolean unpaged,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        if (unpaged) {
            return ResponseEntity.ok(technologyService.getAllTechnologies());
        }
        return ResponseEntity.ok(technologyService.getTechnologies(pageable));
    }

    @PostMapping
    public ResponseEntity<?> createTechnology(@RequestBody TechnologyRequest request) {
        return ResponseEntity.ok(technologyService.createTechnology(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTechnology(@PathVariable String id, @RequestBody TechnologyRequest request) {
        return ResponseEntity.ok(technologyService.updateTechnology(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTechnology(@PathVariable String id) {
        technologyService.deleteTechnology(id);
        return ResponseEntity.noContent().build();
    }
}