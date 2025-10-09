package udpm.hn.server.infrastructure.core.config.dbgenerator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Admin;
import udpm.hn.server.infrastructure.core.constant.Roles;
import udpm.hn.server.repository.AdminRepository;

import java.util.Optional;

@Repository
public interface DBGAdminRepository extends AdminRepository {
    Optional<Admin> findByEmail(String email);
}
