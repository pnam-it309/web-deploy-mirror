package udpm.hn.server.core.admin.category.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import udpm.hn.server.core.admin.category.dto.request.CategoryCreateRequest;
import udpm.hn.server.core.admin.category.dto.request.CategoryFilterRequest;
import udpm.hn.server.core.admin.category.dto.request.CategoryUpdateRequest;
import udpm.hn.server.core.admin.category.dto.response.CategoryResponse;
import udpm.hn.server.core.admin.category.service.CategoryService;
import udpm.hn.server.infrastructure.core.constant.MappingConstants;

@RestController
@RequestMapping(MappingConstants.API_ADMIN_CATEGORY)
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/get-all-categories")
    public ResponseEntity<Page<CategoryResponse>> getAllCategories(
            @ModelAttribute CategoryFilterRequest request, // <-- Thêm @ModelAttribute để hứng params
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(categoryService.getAllCategories(request, pageable));
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(
            @Valid @RequestBody CategoryCreateRequest request) {
        return new ResponseEntity<>(
                categoryService.createCategory(request),
                HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(
            @PathVariable String id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable String id,
            @Valid @RequestBody CategoryUpdateRequest request) {
        return ResponseEntity.ok(categoryService.updateCategory(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable String id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

}
