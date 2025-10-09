package udpm.hn.server.infrastructure.core.security.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import udpm.hn.server.entity.Staff;
import udpm.hn.server.infrastructure.core.constant.EntityStatus;
import udpm.hn.server.repository.StaffRepository;

import java.util.Optional;

public interface StaffAuthRepository extends StaffRepository {

    @Query("SELECT s FROM Staff s WHERE s.email = :email AND s.status = :status")
    Optional<Staff> findByEmailAndStatus(
            @Param("email") String email,
            @Param("status") EntityStatus status
    );
}
