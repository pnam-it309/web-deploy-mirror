package udpm.hn.server.core.admin.brand.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import udpm.hn.server.core.admin.brand.dto.request.BrandCreateRequest;
import udpm.hn.server.core.admin.brand.dto.request.BrandFilterRequest;
import udpm.hn.server.core.admin.brand.dto.request.BrandUpdateRequest;
import udpm.hn.server.core.admin.brand.dto.response.BrandResponse;
import udpm.hn.server.core.admin.brand.repository.BrandManageRepository;
import udpm.hn.server.core.admin.brand.repository.BrandSpecification;
import udpm.hn.server.core.admin.brand.service.BrandService;
import udpm.hn.server.entity.Brand;
import udpm.hn.server.infrastructure.core.constant.EntityStatus;
import udpm.hn.server.infrastructure.core.exception.ResourceNotFoundException;
import udpm.hn.server.repository.BrandRepository;
import udpm.hn.server.utils.SlugUtils;

import java.util.UUID;
@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    // (Hãy dùng BrandRepository của bạn)
    private final BrandManageRepository brandRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public BrandResponse createBrand(BrandCreateRequest request) {
        if (brandRepository.existsByCode(request.getCode())) {
            throw new IllegalArgumentException("Brand with code " + request.getCode() + " already exists");
        }

        Brand brand = modelMapper.map(request, Brand.class);
        brand.setSlug(SlugUtils.toSlug(request.getName()));
        brand.setStatus(request.getStatus());
        Brand savedBrand = brandRepository.save(brand);
        return mapToResponse(savedBrand);
    }

    @Override
    @Transactional
    public BrandResponse updateBrand(UUID id, BrandUpdateRequest request) {
        Brand brand = brandRepository.findById(id.toString())
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with id: " + id));
        if (request.getName() != null) {
            brand.setSlug(SlugUtils.toSlug(request.getName()));
        }
        if (request.getDescription() != null) {
            brand.setDescription(request.getDescription());
        }
        if (request.getLogoUrl() != null) {
            brand.setLogoUrl(request.getLogoUrl());
        }
        if (request.getStatus() != null) {
            brand.setStatus(request.getStatus());
        }
        Brand updatedBrand = brandRepository.save(brand);
        return mapToResponse(updatedBrand);
    }

    @Override
    @Transactional(readOnly = true)
    public BrandResponse getBrandById(UUID id) {
        Brand brand = brandRepository.findById(id.toString())
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with id: " + id));
        return mapToResponse(brand);
    }


    @Override
    @Transactional(readOnly = true)
    // Sửa tham số: Thêm request
    public Page<BrandResponse> getAllBrands(BrandFilterRequest request, Pageable pageable) {
        // 1. Tạo Specification
        Specification<Brand> spec = BrandSpecification.getFilter(request);

        // 2. Gọi findAll với spec
        return brandRepository.findAll(spec, pageable)
                .map(this::mapToResponse);
    }


    @Override
    @Transactional
    public void deleteBrand(UUID id) {
        Brand brand = brandRepository.findById(id.toString())
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with id: " + id));
        // Soft delete
        brand.setStatus(EntityStatus.INACTIVE);
        brandRepository.save(brand);
    }
    private BrandResponse mapToResponse(Brand brand) {
        return modelMapper.map(brand, BrandResponse.class);
    }
}