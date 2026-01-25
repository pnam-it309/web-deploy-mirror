package udpm.hn.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.TwoFactorAuth;

import java.util.Optional;

@Repository
public interface TwoFactorAuthRepository extends JpaRepository<TwoFactorAuth, String> {
    Optional<TwoFactorAuth> findByAdminId(String adminId);

    boolean existsByAdminId(String adminId);
}
