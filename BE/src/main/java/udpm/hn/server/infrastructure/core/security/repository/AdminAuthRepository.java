package udpm.hn.server.infrastructure.core.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Admin;
import udpm.hn.server.infrastructure.core.constant.EntityStatus;

import java.util.Optional;

@Repository
public interface AdminAuthRepository extends JpaRepository<Admin, String> {

    @Query("SELECT a FROM Admin a WHERE (a.username = :usernameOrEmail OR a.email = :usernameOrEmail) AND a.status = :status")
    Optional<Admin> findByUsernameOrEmailAndStatus(@Param("usernameOrEmail") String usernameOrEmail,
                                                   @Param("usernameOrEmail") String usernameOrEmail2,
                                                   @Param("status") EntityStatus status);

    @Query("SELECT a FROM Admin a WHERE a.username = :username AND a.status = :status")
    Optional<Admin> findByUsernameAndStatus(@Param("username") String username, @Param("status") EntityStatus status);

    @Query("SELECT a FROM Admin a WHERE a.email = :email AND a.status = :status")
    Optional<Admin> findByEmailAndStatus(@Param("email") String email, @Param("status") EntityStatus status);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
