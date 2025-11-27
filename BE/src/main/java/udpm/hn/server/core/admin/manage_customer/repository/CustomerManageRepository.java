package udpm.hn.server.core.admin.manage_customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Customer;

@Repository
public interface CustomerManageRepository extends JpaRepository<Customer, String>, JpaSpecificationExecutor<Customer> {
}