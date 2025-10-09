package udpm.hn.server.core.admin.products.reponsitory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import udpm.hn.server.core.admin.products.modal.reponse.ProductsReponse;
import udpm.hn.server.core.admin.products.modal.request.ProductsRequest;
import udpm.hn.server.repository.ProductRepository;
@Repository
public interface ProductExtendReponsitory extends ProductRepository {
    @Query(
            value = """
  SELECT
           ROW_NUMBER() OVER (ORDER BY p.created_date DESC) AS orderNumber,
           p.id as id,
           p.sku AS sku,
           p.name AS name,
           p.short_description AS shortDescription,
           p.price AS price,
           p.stock_quantity AS stockQuantity,
           p.unit AS unit,
           b.name AS brandName,
           c.name AS categoryName,
           pd.id AS productDetailId,
           p.status as status,
           i.url as url,
           p.created_date AS createdDate
        FROM catalog_web.products p
        JOIN catalog_web.product_details pd ON pd.product_id = p.id
        JOIN catalog_web.categories c ON c.id = p.category_id
        JOIN catalog_web.brands b ON b.id = p.brand_id
        JOIN catalog_web.images i ON i.product_detail_id = pd.id
        WHERE
           (
             (:#{#request.sku} IS NULL OR LOWER(p.sku) LIKE CONCAT('%', LOWER(:#{#request.sku}), '%'))
             OR
             (:#{#request.name} IS NULL OR LOWER(p.name) LIKE CONCAT('%', LOWER(:#{#request.name}), '%'))
           )
           AND (:#{#request.brandId} IS NULL OR b.id = :#{#request.brandId})
           AND (:#{#request.categoryId} IS NULL OR c.id = :#{#request.categoryId})
               AND (:#{#request.status} IS NULL OR p.status = :#{#request.status})
        """,
            countQuery = """
        SELECT COUNT(DISTINCT p.id)
        FROM catalog_web.products p
        JOIN catalog_web.product_details pd ON pd.product_id = p.id
        JOIN catalog_web.categories c ON c.id = p.category_id
        JOIN catalog_web.brands b ON b.id = p.brand_id
        JOIN catalog_web.images i ON i.product_detail_id = pd.id
        WHERE
           (
             (:#{#request.sku} IS NULL OR LOWER(p.sku) LIKE CONCAT('%', LOWER(:#{#request.sku}), '%'))
             AND
             (:#{#request.name} IS NULL OR LOWER(p.name) LIKE CONCAT('%', LOWER(:#{#request.name}), '%'))
           )
           AND (:#{#request.brandId} IS NULL OR b.id = :#{#request.brandId})
           AND (:#{#request.categoryId} IS NULL OR c.id = :#{#request.categoryId})
            AND (:#{#request.status} IS NULL OR p.status = :#{#request.status})
        """,
            nativeQuery = true
    )
    Page<ProductsReponse> findAllByFilters(@Param("request") ProductsRequest request, Pageable pageable);

}
