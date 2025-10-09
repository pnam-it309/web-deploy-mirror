package udpm.hn.server.core.admin.brand.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import udpm.hn.server.core.admin.brand.dto.request.BrandCreateRequest;
import udpm.hn.server.core.admin.brand.dto.request.BrandUpdateRequest;
import udpm.hn.server.core.admin.brand.dto.response.BrandResponse;
import udpm.hn.server.core.admin.brand.service.BrandService;
import udpm.hn.server.infrastructure.core.constant.MappingConstants;

import java.util.UUID;

@RestController
@RequestMapping(MappingConstants.API_ADMIN_BRAND)
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    @PostMapping
    public ResponseEntity<BrandResponse> createBrand(@Valid @RequestBody BrandCreateRequest request) {
        BrandResponse response = brandService.createBrand(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BrandResponse> updateBrand(
            @PathVariable UUID id,
            @Valid @RequestBody BrandUpdateRequest request) {
        BrandResponse response = brandService.updateBrand(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BrandResponse> getBrandById(@PathVariable UUID id) {
        BrandResponse response = brandService.getBrandById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-all-brands")
    public ResponseEntity<Page<BrandResponse>> getAllBrands(
            @PageableDefault(size = 20) Pageable pageable) {
        Page<BrandResponse> response = brandService.getAllBrands(pageable);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBrand(@PathVariable UUID id) {
        brandService.deleteBrand(id);
        return ResponseEntity.noContent().build();
    }
}
