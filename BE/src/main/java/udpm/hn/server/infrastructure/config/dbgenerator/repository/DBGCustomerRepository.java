package udpm.hn.server.infrastructure.config.dbgenerator.repository;

import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Customer;
import udpm.hn.server.repository.CustomerRepository;

import java.util.Optional;

@Repository
public interface DBGCustomerRepository extends CustomerRepository {
    Optional<Customer> findByEmail(String email);

    @org.springframework.data.jpa.repository.Query(value = "SELECT COUNT(*) FROM customer_roles WHERE customer_id = :customerId AND role_id = :roleId", nativeQuery = true)
    Integer existsByCustomerIdAndRoleId(String customerId, String roleId);

    @org.springframework.data.jpa.repository.Modifying
    @org.springframework.data.jpa.repository.Query(value = "INSERT IGNORE INTO customer_roles (customer_id, role_id) VALUES (:customerId, :roleId)", nativeQuery = true)
    void insertCustomerRole(String customerId, String roleId);
}
