package udpm.hn.server.core.admin.category.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import udpm.hn.server.core.admin.category.dto.request.CategoryCreateRequest;
import udpm.hn.server.core.admin.category.dto.request.CategoryUpdateRequest;
import udpm.hn.server.core.admin.category.dto.response.CategoryResponse;

public interface CategoryService {
    Page<CategoryResponse> getAllCategories(Pageable pageable);
    CategoryResponse getCategoryById(String id);
    CategoryResponse createCategory(CategoryCreateRequest request);
    CategoryResponse updateCategory(String id, CategoryUpdateRequest request);
    void deleteCategory(String id);
}
