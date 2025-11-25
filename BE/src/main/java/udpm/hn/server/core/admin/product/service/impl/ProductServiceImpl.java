package udpm.hn.server.core.admin.product.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import udpm.hn.server.core.admin.product.dto.request.ProductCreateRequest;
import udpm.hn.server.core.admin.product.dto.request.ProductDetailRequest;
import udpm.hn.server.core.admin.product.dto.request.ProductFilterRequest;
import udpm.hn.server.core.admin.product.dto.request.ProductUpdateRequest;
import udpm.hn.server.core.admin.product.dto.response.ProductDetailResponse;
import udpm.hn.server.core.admin.product.dto.response.ProductResponse;
import udpm.hn.server.core.admin.product.repository.ProductDetailManageRepository;
import udpm.hn.server.core.admin.product.repository.ProductManageRepository;
import udpm.hn.server.core.admin.product.repository.ProductSpecification;
import udpm.hn.server.core.admin.product.service.ProductService;
import udpm.hn.server.entity.Brand;
import udpm.hn.server.entity.Category;
import udpm.hn.server.entity.Product;
import udpm.hn.server.entity.ProductDetail;
import udpm.hn.server.infrastructure.core.constant.EntityStatus;
import udpm.hn.server.repository.BrandRepository;
import udpm.hn.server.repository.CategoryRepository;
import udpm.hn.server.utils.SlugUtils;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductManageRepository productRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final ProductDetailManageRepository productDetailRepository;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional(readOnly = true)
    // Sửa tham số đầu vào: Nhận thêm FilterRequest
    public Page<ProductResponse> getAllProducts(ProductFilterRequest request, Pageable pageable) {
        // Tạo Specification từ request
        Specification<Product> spec = ProductSpecification.getFilter(request);

        // Gọi hàm findAll(spec, pageable)
        return productRepository.findAll(spec, pageable)
                .map(this::convertToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(String id) {
        Product product = findProductById(id);
        return convertToResponse(product);
    }

    @Override
    @Transactional
    public ProductResponse createProduct(ProductCreateRequest request) {
        if (productRepository.existsBySku(request.getSku())) {
            throw new IllegalArgumentException("SKU đã tồn tại: " + request.getSku());
        }

        Product product = new Product();
        // Map các trường cơ bản
        modelMapper.map(request, product);

        // Xử lý quan hệ
        product.setBrand(findBrandById(request.getBrandId()));
        product.setCategory(findCategoryById(request.getCategoryId()));

        // Áp dụng "Template Chuẩn" (Tự tạo Slug)
        product.setSlug(SlugUtils.toSlug(request.getName()));
        // (Trường 'status' đã được map từ DTO)

        Product savedProduct = productRepository.save(product);
        return convertToResponse(savedProduct);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(String id, ProductUpdateRequest request) {
        Product product = findProductById(id);

        // Map các trường cơ bản
        modelMapper.map(request, product);

        // Xử lý quan hệ
        product.setBrand(findBrandById(request.getBrandId()));
        product.setCategory(findCategoryById(request.getCategoryId()));

        // Áp dụng "Template Chuẩn" (Tự tạo Slug khi 'name' thay đổi)
        product.setSlug(SlugUtils.toSlug(request.getName()));

        Product updatedProduct = productRepository.save(product);
        return convertToResponse(updatedProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(String id) {
        Product product = findProductById(id);

        // Áp dụng "Template Chuẩn" (Xoá Mềm)
        product.setStatus(EntityStatus.INACTIVE);
        productRepository.save(product);
    }

    // --- Hàm nội bộ (private) ---

    private Product findProductById(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy Product với id: " + id));
    }

    private Brand findBrandById(String id) {
        return brandRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy Brand với id: " + id));
    }

    private Category findCategoryById(String id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy Category với id: " + id));
    }

    private ProductResponse convertToResponse(Product product) {
        ProductResponse response = modelMapper.map(product, ProductResponse.class);
        if (product.getBrand() != null) {
            response.setBrandId(product.getBrand().getId());     // <-- QUAN TRỌNG
            response.setBrandName(product.getBrand().getName());
        }
        if (product.getCategory() != null) {
            response.setCategoryId(product.getCategory().getId()); // <-- QUAN TRỌNG
            response.setCategoryName(product.getCategory().getName());
        }

        return response;
    }
    @Override
    @Transactional(readOnly = true)
    public ProductDetailResponse getProductDetail(String productId) {
        // Tìm detail theo productId
        ProductDetail detail = productDetailRepository.findByProductId(productId)
                .orElse(null); // Nếu chưa có thì trả về null (FE sẽ hiển thị form trống)

        if (detail == null) return null;

        // Convert Entity -> Response
        ProductDetailResponse response = modelMapper.map(detail, ProductDetailResponse.class);
        response.setProductId(detail.getProduct().getId());

        // Convert JsonNode -> Map
        if (detail.getSpecification() != null) {
            Map<String, Object> specMap = objectMapper.convertValue(detail.getSpecification(), Map.class);
            response.setSpecification(specMap);
        }

        return response;
    }

    @Override
    @Transactional
    public ProductDetailResponse updateProductDetail(String productId, ProductDetailRequest request) {
        Product product = findProductById(productId);

        // Tìm detail cũ HOẶC tạo mới nếu chưa có
        ProductDetail detail = productDetailRepository.findByProductId(productId)
                .orElse(new ProductDetail());

        // Nếu là mới -> set quan hệ
        if (detail.getProduct() == null) {
            detail.setProduct(product);
        }

        // Cập nhật thông tin từ request
        detail.setLongDescription(request.getLongDescription());
        detail.setPackaging(request.getPackaging());
        detail.setWeight(request.getWeight());
        detail.setDimensions(request.getDimensions());

        // Convert Map -> JsonNode để lưu vào DB
        if (request.getSpecification() != null) {
            detail.setSpecification(objectMapper.valueToTree(request.getSpecification()));
        }

        // Lưu
        ProductDetail savedDetail = productDetailRepository.save(detail);

        // Trả về response
        return getProductDetail(productId); // Gọi lại hàm get để convert cho tiện
    }
}