package udpm.hn.server.infrastructure.core.config.dbgenerator.repository;

import udpm.hn.server.infrastructure.core.constant.Role;

import java.util.List;
import java.util.Optional;

public interface DBGRoleRepository {
    Optional<Role> findByCode(String code);

    List<Role> findAllByFacility();

    Optional<Role> findByCodeAndNameAndFacility(String s, String s1);
}
