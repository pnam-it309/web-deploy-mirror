package udpm.hn.server.core.admin.brand.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import udpm.hn.server.core.admin.brand.dto.request.BrandCreateRequest;
import udpm.hn.server.core.admin.brand.dto.request.BrandUpdateRequest;
import udpm.hn.server.core.admin.brand.dto.response.BrandResponse;
import udpm.hn.server.core.admin.brand.repository.BrandManageRepository;
import udpm.hn.server.core.admin.brand.service.BrandService;
import udpm.hn.server.entity.Brand;
import udpm.hn.server.infrastructure.core.constant.EntityStatus;
import udpm.hn.server.infrastructure.core.exception.ResourceNotFoundException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final BrandManageRepository brandRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public BrandResponse createBrand(BrandCreateRequest request) {
        // Check if brand with the same code already exists
        if (brandRepository.existsByCode(request.getCode())) {
            throw new IllegalArgumentException("Brand with code " + request.getCode() + " already exists");
        }

        // Map request to entity
        Brand brand = modelMapper.map(request, Brand.class);
        brand.setStatus(EntityStatus.ACTIVE);
        
        // Save the brand
        Brand savedBrand = brandRepository.save(brand);
        
        // Map and return the response
        return mapToResponse(savedBrand);
    }

    @Override
    @Transactional
    public BrandResponse updateBrand(UUID id, BrandUpdateRequest request) {
        // Find the existing brand
        Brand brand = brandRepository.findById(id.toString())
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with id: " + id));

        // Update fields if they are not null in the request
        if (request.getName() != null) {
            brand.setName(request.getName());
        }
        if (request.getDescription() != null) {
            brand.setDescription(request.getDescription());
        }
        if (request.getLogoUrl() != null) {
            brand.setLogoUrl(request.getLogoUrl());
        }
        
        // Save the updated brand
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
    public Page<BrandResponse> getAllBrands(Pageable pageable) {
        return brandRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    @Override
    @Transactional
    public void deleteBrand(UUID id) {
        Brand brand = brandRepository.findById(id.toString())
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with id: " + id));
        
        // Soft delete by setting status to INACTIVE
        brand.setStatus(EntityStatus.INACTIVE);
        
        brandRepository.save(brand);
    }

    private BrandResponse mapToResponse(Brand brand) {
        return modelMapper.map(brand, BrandResponse.class);
    }
}
