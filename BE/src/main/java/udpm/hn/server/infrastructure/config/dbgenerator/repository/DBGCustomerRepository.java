package udpm.hn.server.infrastructure.config.dbgenerator.repository;

import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Customer;
import udpm.hn.server.repository.CustomerRepository;

import java.util.Optional;

@Repository
public interface DBGCustomerRepository extends CustomerRepository {
    Optional<Customer> findByEmail(String email);
}
