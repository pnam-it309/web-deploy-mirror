package udpm.hn.server.core.admin.category.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import udpm.hn.server.core.admin.category.dto.request.CategoryCreateRequest;
import udpm.hn.server.core.admin.category.dto.request.CategoryUpdateRequest;
import udpm.hn.server.core.admin.category.dto.response.CategoryResponse;
import udpm.hn.server.core.admin.category.service.CategoryService;
import udpm.hn.server.entity.Category;
import udpm.hn.server.repository.CategoryRepository;
import udpm.hn.server.utils.SlugUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryResponse> getAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable)
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
        
        category.setSlug(SlugUtils.toSlug(request.getName()));
        Category savedCategory = categoryRepository.save(category);
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
        
        categoryRepository.delete(category);
    }

    private CategoryResponse convertToResponse(Category category) {
        CategoryResponse response = modelMapper.map(category, CategoryResponse.class);
        if (category.getParent() != null) {
            response.setParentId(category.getParent().getId());
        }
        return response;
    }

    private CategoryResponse convertToResponseWithChildren(Category category) {
        CategoryResponse response = convertToResponse(category);
        if (!category.getChildren().isEmpty()) {
            List<CategoryResponse> children = category.getChildren().stream()
                    .map(this::convertToResponseWithChildren)
                    .collect(Collectors.toList());
            response.setChildren(children);
        }
        return response;
    }
}
