package udpm.hn.server.infrastructure.core.config.dbgenerator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Customer;
import udpm.hn.server.entity.Customerole;
import udpm.hn.server.entity.Role;
import udpm.hn.server.repository.CustomerRoleRepository;

@Repository
public interface DBGCustomerRoleRepository extends CustomerRoleRepository {
    boolean existsByCustomerAndRole(Customer customer, Role role);
}
