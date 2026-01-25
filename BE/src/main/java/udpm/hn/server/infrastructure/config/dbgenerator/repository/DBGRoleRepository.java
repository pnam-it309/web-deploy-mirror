package udpm.hn.server.infrastructure.config.dbgenerator.repository;

import udpm.hn.server.entity.Role;
import udpm.hn.server.repository.RoleRepository;

import java.util.Optional;

public interface DBGRoleRepository extends RoleRepository {
    Optional<Role> findByCode(String code);
}
