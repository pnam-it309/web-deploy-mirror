package udpm.hn.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import udpm.hn.server.entity.Customer;
import udpm.hn.server.entity.Customerole;
import udpm.hn.server.entity.Role;

public interface CustomerRoleRepository extends JpaRepository<Customerole, String> {
    boolean existsByCustomerAndRole(Customer customer, Role role);
}
