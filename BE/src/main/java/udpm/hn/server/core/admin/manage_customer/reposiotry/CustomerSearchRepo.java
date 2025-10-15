package udpm.hn.server.core.admin.manage_customer.reposiotry;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import udpm.hn.server.entity.Customer;
import udpm.hn.server.infrastructure.core.constant.EntityStatus;
import udpm.hn.server.repository.CustomerRepository;

public interface CustomerSearchRepo extends CustomerRepository {
    @Query("SELECT c FROM Customer c " +
            "WHERE (:search IS NULL OR :search = '' OR LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "   OR LOWER(c.email) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "  AND (:status IS NULL OR c.status = :status)")
    Page<Customer> search(
            @Param("search") String search,
            @Param("status") EntityStatus status,
            Pageable pageable
    );
}
