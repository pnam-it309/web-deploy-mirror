package udpm.hn.server.infrastructure.core.config.dbgenerator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Customer;
import udpm.hn.server.repository.CustomerRepository;

import java.util.Optional;

@Repository
public interface DBGCustomerRepository extends CustomerRepository {
    Optional<Customer> findByEmail(String email);
}
