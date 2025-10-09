package udpm.hn.server.infrastructure.core.config.dbgenerator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Admin;
import udpm.hn.server.entity.AdminRole;
import udpm.hn.server.entity.Role;
import udpm.hn.server.repository.AdminRoleRepository;

@Repository
public interface DBGAdminRoleRepository extends AdminRoleRepository {
    boolean existsByAdminAndRole(Admin admin, Role role);

}
