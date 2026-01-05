package udpm.hn.server.infrastructure.config.dbgenerator.repository;

import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Admin;
import udpm.hn.server.repository.AdminRepository;

import java.util.Optional;

@Repository
public interface DBGAdminRepository extends AdminRepository {
    Optional<Admin> findByUsername(String username);
}
