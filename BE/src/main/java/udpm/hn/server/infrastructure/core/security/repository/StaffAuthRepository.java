package udpm.hn.server.infrastructure.core.security.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import udpm.hn.server.entity.Admin;
import udpm.hn.server.infrastructure.core.constant.EntityStatus;
import udpm.hn.server.repository.StaffRepository;

import java.util.Optional;

public interface StaffAuthRepository extends StaffRepository {

    @Query("SELECT s FROM Admin s WHERE s.email = :email AND s.status = :status")
    Optional<Admin> findByEmailAndStatus(
            @Param("email") String email,
            @Param("status") EntityStatus status
    );
}
