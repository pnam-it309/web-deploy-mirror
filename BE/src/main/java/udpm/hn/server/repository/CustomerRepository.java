package udpm.hn.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    java.util.Optional<Customer> findByEmail(String email);
}
