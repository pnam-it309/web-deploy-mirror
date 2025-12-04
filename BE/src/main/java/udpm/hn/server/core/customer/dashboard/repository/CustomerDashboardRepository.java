package udpm.hn.server.core.customer.dashboard.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.core.customer.dashboard.model.response.CustomerDashboardCategoryResponse;
import udpm.hn.server.core.customer.dashboard.model.response.CustomerDashboardProductResponse;
import udpm.hn.server.entity.Product;
import udpm.hn.server.infrastructure.core.constant.EntityStatus;

import java.util.List;

@Repository
public interface CustomerDashboardRepository extends JpaRepository<Product, String> {

    @Query("SELECT new udpm.hn.server.core.customer.dashboard.model.response.CustomerDashboardProductResponse(" +
           "p.id, p.name, p.price, p.category.name, " +
           "(SELECT i.url FROM Image i WHERE i.productDetail.id = p.productDetail.id ORDER BY i.createdDate ASC LIMIT 1), " +
           "p.shortDescription, 0, 5.0, 'New') " +
           "FROM Product p " +
           "WHERE p.status = :status " +
           "ORDER BY p.createdDate DESC")
    List<CustomerDashboardProductResponse> getFeaturedProducts(EntityStatus status, Pageable pageable);

    @Query("SELECT new udpm.hn.server.core.customer.dashboard.model.response.CustomerDashboardCategoryResponse(" +
           "c.id, c.name, COUNT(p)) " +
           "FROM Category c " +
           "LEFT JOIN Product p ON p.category.id = c.id AND p.status = :status " +
           "GROUP BY c.id, c.name")
    List<CustomerDashboardCategoryResponse> getCategoriesWithCount(EntityStatus status);
}
