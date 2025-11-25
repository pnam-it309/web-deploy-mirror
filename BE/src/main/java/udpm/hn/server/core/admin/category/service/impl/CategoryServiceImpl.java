package udpm.hn.server.core.admin.category.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import udpm.hn.server.core.admin.category.dto.request.CategoryCreateRequest;
import udpm.hn.server.core.admin.category.dto.request.CategoryFilterRequest;
import udpm.hn.server.core.admin.category.dto.request.CategoryUpdateRequest;
import udpm.hn.server.core.admin.category.dto.response.CategoryResponse;
import udpm.hn.server.core.admin.category.repository.CategoryManageRepository;
import udpm.hn.server.core.admin.category.repository.CategorySpecification;
import udpm.hn.server.core.admin.category.service.CategoryService;
import udpm.hn.server.entity.Category;
import udpm.hn.server.infrastructure.core.constant.EntityStatus;
import udpm.hn.server.utils.SlugUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryManageRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryResponse> getAllCategories(CategoryFilterRequest request, Pageable pageable) {
        Specification<Category> spec = CategorySpecification.getFilter(request);
        return categoryRepository.findAll(spec, pageable)
                .map(this::convertToResponseWithChildren);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(String id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));
        return convertToResponseWithChildren(category);
    }

    @Override
    @Transactional
    public CategoryResponse createCategory(CategoryCreateRequest request) {
        Category category = new Category();
        modelMapper.map(request, category);

        if (request.getParentId() != null) {
            Category parent = categoryRepository.findById(request.getParentId())
                    .orElseThrow(() -> new EntityNotFoundException("Parent category not found with id: " + request.getParentId()));
            category.setParent(parent);
        }

        category.setStatus(EntityStatus.ACTIVE);
        category.setSlug(SlugUtils.toSlug(request.getName()));
        Category savedCategory = categoryRepository.save(category);

        // Khi tạo xong trả về response đơn giản (không cần children vì mới tạo làm gì có con)
        return convertToResponse(savedCategory);
    }

    @Override
    @Transactional
    public CategoryResponse updateCategory(String id, CategoryUpdateRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));
        modelMapper.map(request, category);

        if (request.getParentId() != null && !request.getParentId().equals(id)) {
            Category parent = categoryRepository.findById(request.getParentId())
                    .orElseThrow(() -> new EntityNotFoundException("Parent category not found with id: " + request.getParentId()));
            category.setParent(parent);
        } else if (request.getParentId() == null) {
            category.setParent(null);
        }

        category.setSlug(SlugUtils.toSlug(request.getName()));
        Category updatedCategory = categoryRepository.save(category);
        return convertToResponseWithChildren(updatedCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(String id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));

        if (!category.getChildren().isEmpty()) {
            throw new IllegalStateException("Cannot delete category with existing subcategories");
        }
        category.setStatus(EntityStatus.INACTIVE);
        categoryRepository.save(category);
    }

    // --- SỬA LỖI Ở ĐÂY: MAP THỦ CÔNG ---
    private CategoryResponse convertToResponse(Category category) {
        // Thay vì dùng ModelMapper (gây lỗi Lazy Load với children), ta map tay các trường cơ bản
        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        response.setSlug(category.getSlug());
        response.setDescription(category.getDescription());

        if (category.getParent() != null) {
            response.setParentId(category.getParent().getId());
        }

        // QUAN TRỌNG: Chúng ta KHÔNG map 'children' ở đây.
        // Việc map children sẽ do hàm convertToResponseWithChildren lo.

        return response;
    }

    private CategoryResponse convertToResponseWithChildren(Category category) {
        CategoryResponse response = convertToResponse(category);

        // Chỉ truy cập children ở đây (trong Transaction), nơi an toàn
        if (category.getChildren() != null && !category.getChildren().isEmpty()) {
            List<CategoryResponse> children = category.getChildren().stream()
                    // Lọc con ACTIVE
                    .filter(child -> child.getStatus() == EntityStatus.ACTIVE)
                    .map(this::convertToResponseWithChildren) // Đệ quy
                    .collect(Collectors.toList());
            response.setChildren(children);
        }
        return response;
    }
}