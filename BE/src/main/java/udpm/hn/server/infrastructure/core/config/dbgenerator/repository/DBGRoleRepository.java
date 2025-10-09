package udpm.hn.server.infrastructure.core.config.dbgenerator.repository;

import udpm.hn.server.entity.Role;
import udpm.hn.server.infrastructure.core.constant.Roles;
import udpm.hn.server.repository.RoleRepository;

import java.util.List;
import java.util.Optional;

public interface DBGRoleRepository extends RoleRepository {
    Optional<Role> findByCode(String code);
}
