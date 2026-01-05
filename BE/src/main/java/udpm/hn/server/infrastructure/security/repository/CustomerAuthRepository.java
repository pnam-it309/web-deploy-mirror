package udpm.hn.server.infrastructure.security.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import udpm.hn.server.entity.Customer;
import udpm.hn.server.infrastructure.constant.EntityStatus;
import udpm.hn.server.repository.CustomerRepository;

import java.util.Optional;

public interface CustomerAuthRepository extends CustomerRepository {

    @Query("SELECT c FROM Customer c WHERE c.email = :email AND c.status = :status")
    Optional<Customer> findByEmailAndStatus(
            @Param("email") String email,
            @Param("status") EntityStatus status
    );
}
