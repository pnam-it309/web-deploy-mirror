package udpm.hn.server.infrastructure.security.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Admin;
import udpm.hn.server.infrastructure.constant.EntityStatus;
import udpm.hn.server.repository.AdminRepository;

import java.util.Optional;

@Repository
public interface StaffAuthRepository extends AdminRepository {

    @Query("SELECT s FROM Admin s WHERE s.username = :username AND s.status = :status")
    Optional<Admin> findByUsernameAndStatus(
            @Param("username") String username,
            @Param("status") EntityStatus status);

    Optional<Admin> findByUsername(String username);
}
