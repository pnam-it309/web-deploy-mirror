package udpm.hn.server.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Customer;

import java.util.List;
import java.util.Optional;

@Repository
@org.springframework.context.annotation.Primary
public interface CustomerRepository extends JpaRepository<Customer, String> {
    // 1. Hàm tìm chính xác (để logic lưu hoạt động)
    Optional<Customer> findByEmail(String email);

    @Query("""
                SELECT c FROM Customer c
                WHERE LOWER(c.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(c.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))
            """)
    List<Customer> searchByEmailOrName(@Param("keyword") String keyword);
}
