package udpm.hn.server.core.admin.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import udpm.hn.server.infrastructure.core.constant.MappingConstants;

@RestController
@RequestMapping(MappingConstants.API_ADMIN_CATEGORY)
@RequiredArgsConstructor
public class CategoryController {

    @GetMapping
    public ResponseEntity<?> getAllCategories() {
        // TODO: Implement get all categories
        return ResponseEntity.ok("Get all categories - to be implemented");
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody Object categoryRequest) {
        // TODO: Implement create category
        return ResponseEntity.ok("Create category - to be implemented");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable String id) {
        // TODO: Implement get category by ID
        return ResponseEntity.ok("Get category by ID - to be implemented");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable String id, @RequestBody Object categoryRequest) {
        // TODO: Implement update category
        return ResponseEntity.ok("Update category - to be implemented");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable String id) {
        // TODO: Implement delete category
        return ResponseEntity.ok("Delete category - to be implemented");
    }
}
