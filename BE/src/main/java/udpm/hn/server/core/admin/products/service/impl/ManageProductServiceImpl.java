package udpm.hn.server.core.admin.products.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import udpm.hn.server.core.admin.products.modal.reponse.ProductsReponse;
import udpm.hn.server.core.admin.products.modal.request.ImageRequest;
import udpm.hn.server.core.admin.products.modal.request.ProductsCreAndUpdateRequest;
import udpm.hn.server.core.admin.products.modal.request.ProductsRequest;
import udpm.hn.server.core.admin.products.reponsitory.ProductExtendReponsitory;
import udpm.hn.server.core.admin.products.service.ManageProductService;
import udpm.hn.server.core.common.base.PageableObject;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.entity.*;
import udpm.hn.server.infrastructure.core.constant.EntityStatus;
import udpm.hn.server.repository.BrandRepository;
import udpm.hn.server.repository.CategoryRepository;
import udpm.hn.server.repository.ImageRepository;
import udpm.hn.server.repository.ProductDetailRepository;
import udpm.hn.server.utils.Helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Validated
public class ManageProductServiceImpl implements ManageProductService {
    private final ProductExtendReponsitory productExtendReponsitory;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final ProductDetailRepository productDetailRepository;
    private final ImageRepository imageRepository;
    @Override
    public ResponseObject<?> getAllProducts(ProductsRequest request) {
        Pageable pageable = Helper.createPageable(request,"createdDate");
        return new ResponseObject<>(PageableObject.of(productExtendReponsitory.findAllByFilters(request,pageable)),
                HttpStatus.OK,
                "Lấy danh sách sản phẩm  thành công"
        );
    }

    @Override
    public ResponseObject<?> createProduct(ProductsCreAndUpdateRequest request) {


            // Kiểm tra Brand
            Optional<Brand> brandOpt = brandRepository.findById(request.getBrandId());
            if (brandOpt.isEmpty()) {
                return new ResponseObject<>(null, HttpStatus.CONFLICT, "Thương hiệu không tồn tại!");
            }

            // Kiểm tra Category
            Optional<Category> categoryOpt = categoryRepository.findById(request.getCategoryID());
            if (categoryOpt.isEmpty()) {
                return new ResponseObject<>(null, HttpStatus.CONFLICT, "Loại không tồn tại!");
            }

            // Tạo mới Product
            Product product = new Product();
            product.setName(request.getName());

            product.setSku(request.getSku());
            product.setPrice(request.getPrice());
            product.setStockQuantity(request.getStockQuantity());
            product.setBrand(brandOpt.get());
            product.setCategory(categoryOpt.get());
            product.setStatus(EntityStatus.ACTIVE); // Hoặc từ request nếu có

            // Tạo ProductDetail
            ProductDetail detail = new ProductDetail();
            detail.setProduct(product); // Thiết lập liên kết 2 chiều
            product.setProductDetail(detail);
            detail.setLongDescription(request.getLongDescription());
            detail.setSpecification(request.getSpecification());
            detail.setPackaging(request.getPackaging());
            detail.setWeight(request.getWeight());
            detail.setDimensions(request.getDimensions());

            // Tạo danh sách ảnh nếu có
            if (request.getImages() != null && !request.getImages().isEmpty()) {
                List<Image> images = new ArrayList<>();
                for (ImageRequest imgReq : request.getImages()) {
                    Image img = new Image();
                    img.setProductDetail(detail);
                    img.setUrl(imgReq.getUrl());
                    img.setAltText(imgReq.getAltText());
                    img.setSortOrder(imgReq.getSortOrder());
                    images.add(img);
                }
                detail.setImages(images);
            }

            // Lưu product sẽ cascade xuống detail và ảnh (nếu thiết lập cascade đúng)
        productExtendReponsitory.save(product);

            return new ResponseObject<>(product, HttpStatus.CREATED, "Tạo sản phẩm thành công!");
        }

    @Override
    public ResponseObject<?> updateProduct(String productId,ProductsCreAndUpdateRequest request) {
        Optional<Product> optionalProduct = productExtendReponsitory.findById(productId);
        if (optionalProduct.isEmpty()) {
            return new ResponseObject<>(null, HttpStatus.NOT_FOUND, "Sản phẩm không tồn tại!");
        }
        Product product = optionalProduct.get();

        // 2. Kiểm tra Brand
        Brand brand = null;
        if (request.getBrandId() != null) {
            Optional<Brand> optionalBrand = brandRepository.findById(request.getBrandId());
            if (optionalBrand.isEmpty()) {
                return new ResponseObject<>(null, HttpStatus.CONFLICT, "Thương hiệu không tồn tại!");
            }
            brand = optionalBrand.get();
        }

        // 3. Kiểm tra Category
        Category category = null;
        if (request.getCategoryID() != null) {
            Optional<Category> optionalCategory = categoryRepository.findById(request.getCategoryID());
            if (optionalCategory.isEmpty()) {
                return new ResponseObject<>(null, HttpStatus.CONFLICT, "Loại không tồn tại!");
            }
            category = optionalCategory.get();
        }

        // 4. Cập nhật Product cơ bản
        product.setSku(request.getSku());
        product.setName(request.getName());
        // Nếu slug bạn generate từ name thì nhớ cập nhật đúng

        product.setShortDescription(request.getShortDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setStatus(request.getStatus());
        if (brand != null) product.setBrand(brand);
        if (category != null) product.setCategory(category);

        // 5. Cập nhật ProductDetail
        ProductDetail detail = product.getProductDetail();
        if (detail == null) {
            detail = new ProductDetail();
            detail.setProduct(product);
        }
        detail.setLongDescription(request.getLongDescription());
        detail.setSpecification(request.getSpecification());
        detail.setPackaging(request.getPackaging());
        detail.setWeight(request.getWeight());
        detail.setDimensions(request.getDimensions());

        // 6. Cập nhật ảnh
        if (request.getImages() != null && !request.getImages().isEmpty()) {
            // Xóa ảnh cũ (orphanRemoval = true sẽ tự xóa trong DB)
            detail.getImages().clear();

            // Thêm ảnh mới
            for (ImageRequest imgReq : request.getImages()) {
                Image img = new Image();
                img.setUrl(imgReq.getUrl());
                img.setAltText(imgReq.getAltText());
                img.setSortOrder(imgReq.getSortOrder());
                img.setProductDetail(detail);
                detail.getImages().add(img);
            }
        }

        // 7. Liên kết detail với product (nếu mới tạo)
        product.setProductDetail(detail);

        // 8. Lưu product (cascade lưu cả detail và ảnh)
        productExtendReponsitory.save(product);

        return new ResponseObject<>(product, HttpStatus.OK, "Cập nhật sản phẩm thành công!");
    }

    @Override
    public ResponseObject<?> changeStatusProduct(String productId) {
        Optional<Product> existingProduct = productExtendReponsitory.findById(productId);
        if (existingProduct.isPresent()) {
            Product product = existingProduct.get();
            if (product.getStatus().equals(EntityStatus.INACTIVE)) {
                product.setStatus(EntityStatus.ACTIVE);
            } else {
                product.setStatus(EntityStatus.INACTIVE);
            }
            productExtendReponsitory.save(product);
            return new ResponseObject<>(null, HttpStatus.OK, "Thay đổi trạng thái sản phẩn thành công!");
        }
        return new ResponseObject<>(null, HttpStatus.NOT_FOUND, "Sản phẩm không tồn tại!");

    }
}
