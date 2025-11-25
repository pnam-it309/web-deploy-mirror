package udpm.hn.server.core.admin.brand.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import udpm.hn.server.core.admin.brand.dto.request.BrandCreateRequest;
import udpm.hn.server.core.admin.brand.dto.request.BrandFilterRequest;
import udpm.hn.server.core.admin.brand.dto.request.BrandUpdateRequest;
import udpm.hn.server.core.admin.brand.dto.response.BrandResponse;

import java.util.UUID;

public interface BrandService {
    BrandResponse createBrand(BrandCreateRequest request);
    BrandResponse updateBrand(UUID id, BrandUpdateRequest request);
    BrandResponse getBrandById(UUID id);
    Page<BrandResponse> getAllBrands(BrandFilterRequest request,Pageable pageable);
    void deleteBrand(UUID id);
}
