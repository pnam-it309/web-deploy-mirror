package udpm.hn.server.core.customer.ViewProduct.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import udpm.hn.server.core.common.base.PageableObject;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.customer.ViewProduct.model.Request.ProductFilterRequest;
import udpm.hn.server.core.customer.ViewProduct.model.Response.ProductDetailResponse;
import udpm.hn.server.core.customer.ViewProduct.model.Response.ProductListResponse;
import udpm.hn.server.core.customer.ViewProduct.repository.ViewProductSpecification;
import udpm.hn.server.core.customer.ViewProduct.service.ViewProductService;
import udpm.hn.server.entity.Product;
import udpm.hn.server.repository.ProductRepository;
import udpm.hn.server.repository.WishlistRepository;
import udpm.hn.server.utils.UserContextHelper;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@org.springframework.transaction.annotation.Transactional(readOnly = true)
public class ViewProductServiceImpl implements ViewProductService {

    private final ProductRepository productRepository;
    private final WishlistRepository wishlistRepository;
    private final UserContextHelper userContextHelper;

    @Override
    public ResponseObject<?> getAllProducts(ProductFilterRequest request) {
        // Handle Sort
        Sort sort = Sort.by(Sort.Direction.DESC, "createdDate"); // Default
        if (request.getSort() != null) {
            switch (request.getSort()) {
                case "price-asc":
                    sort = Sort.by(Sort.Direction.ASC, "price");
                    break;
                case "price-desc":
                    sort = Sort.by(Sort.Direction.DESC, "price");
                    break;
                case "name-asc":
                    sort = Sort.by(Sort.Direction.ASC, "name");
                    break;
                case "name-desc":
                    sort = Sort.by(Sort.Direction.DESC, "name");
                    break;
                case "featured":
                default:
                    // default sort
                    break;
            }
        }

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);
        Page<Product> productPage = productRepository.findAll(ViewProductSpecification.getFilter(request), pageable);

        // Get Current User Wishlist
        String userId = userContextHelper.getCurrentUserId();
        java.util.Set<String> wishlistProductIds = new java.util.HashSet<>();
        if (userId != null) {
            wishlistRepository.findByUserId(userId).forEach(w -> wishlistProductIds.add(w.getProductId()));
        }

        // Convert to Response DTO
        Page<ProductListResponse> responsePage = productPage.map(product -> {
            ProductListResponse response = new ProductListResponse(product);
            response.setIsFavorite(wishlistProductIds.contains(product.getId()));
            return response;
        });

        return new ResponseObject<>(
                PageableObject.of(responsePage),
                HttpStatus.OK,
                "Get products successfully"
        );
    }

    @Override
    public ResponseObject<?> getProductDetail(String id) {
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isPresent()) {
            return new ResponseObject<>(
                    new ProductDetailResponse(productOpt.get()),
                    HttpStatus.OK,
                    "Get product detail successfully"
            );
        } else {
            return new ResponseObject<>(
                    null,
                    HttpStatus.NOT_FOUND,
                    "Product not found"
            );
        }
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public ResponseObject<?> addToWishlist(String productId) {
        String userId = userContextHelper.getCurrentUserId();
        if (userId == null) {
            return new ResponseObject<>(null, HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        if (wishlistRepository.existsByUserIdAndProductId(userId, productId)) {
            return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Product already in wishlist");
        }

        udpm.hn.server.entity.Wishlist wishlist = new udpm.hn.server.entity.Wishlist();
        wishlist.setUserId(userId);
        wishlist.setProductId(productId);
        wishlistRepository.save(wishlist);

        return new ResponseObject<>(null, HttpStatus.OK, "Added to wishlist successfully");
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public ResponseObject<?> removeFromWishlist(String productId) {
        String userId = userContextHelper.getCurrentUserId();
        if (userId == null) {
            return new ResponseObject<>(null, HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        wishlistRepository.deleteByUserIdAndProductId(userId, productId);

        return new ResponseObject<>(null, HttpStatus.OK, "Removed from wishlist successfully");
    }

    @Override
    public ResponseObject<?> getWishlist() {
        String userId = userContextHelper.getCurrentUserId();
        if (userId == null) {
            return new ResponseObject<>(null, HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        java.util.List<udpm.hn.server.entity.Wishlist> wishlists = wishlistRepository.findByUserId(userId);
        
        // Fetch products details
        java.util.List<String> productIds = wishlists.stream()
                .map(udpm.hn.server.entity.Wishlist::getProductId)
                .collect(java.util.stream.Collectors.toList());
                
        java.util.List<Product> products = productRepository.findAllById(productIds);
        
        java.util.List<ProductListResponse> responseList = products.stream()
                .map(p -> {
                    ProductListResponse res = new ProductListResponse(p);
                    res.setIsFavorite(true);
                    return res;
                })
                .collect(java.util.stream.Collectors.toList());

        return new ResponseObject<>(responseList, HttpStatus.OK, "Get wishlist successfully");
    }
}
