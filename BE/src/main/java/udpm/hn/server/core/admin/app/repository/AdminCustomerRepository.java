package udpm.hn.server.core.admin.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Customer;
import udpm.hn.server.repository.CustomerRepository;

@Repository
public interface AdminCustomerRepository extends CustomerRepository {

    @Query("""
                SELECT c FROM Customer c
                WHERE (:keyword IS NULL OR :keyword = '' OR c.fullName LIKE %:keyword% OR c.email LIKE %:keyword%)
            """)
    Page<Customer> search(String keyword, Pageable pageable);
}
