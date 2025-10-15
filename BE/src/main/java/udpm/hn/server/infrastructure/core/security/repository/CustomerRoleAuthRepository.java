package udpm.hn.server.infrastructure.core.security.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.repository.CustomerRoleRepository;

import java.util.List;

@Repository
public interface CustomerRoleAuthRepository extends CustomerRoleRepository {

    @Query(value = """
              SELECT DISTINCT r.name FROM Role r
                        JOIN Customerole cr ON r.id = cr.role.id
                        WHERE cr.customer.id = :customerId
            """)
    List<String> getRoleNamesByCustomerId(String customerId);

    @Query(value = """
                SELECT DISTINCT r.code FROM Role r
                    JOIN Customerole cr ON r.id = cr.role.id
                    WHERE cr.customer.id = :customerId AND cr.status = 0
            """)
    List<String> getRoleCodesByCustomerId(String customerId);

}
