package udpm.hn.server.infrastructure.core.config.dbgenerator.repository;

import udpm.hn.server.entity.Staff;
import udpm.hn.server.infrastructure.core.constant.Role;

public interface DBGStaffRoleRepository {
    boolean existsByStaffAndRole(Staff staff, Role role);
}
