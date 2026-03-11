package udpm.hn.server.infrastructure.config.dbgenerator.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Admin;
import udpm.hn.server.repository.AdminRepository;

import java.util.Optional;

@Repository
public interface DBGAdminRepository extends AdminRepository {
    Optional<Admin> findByUsername(String username);

    @Query(value = "SELECT COUNT(*) FROM admin_roles WHERE admin_id = :adminId AND role_id = :roleId", nativeQuery = true)
    Integer existsByAdminIdAndRoleId(String adminId, String roleId);

    @org.springframework.data.jpa.repository.Modifying
    @Query(value = "INSERT IGNORE INTO admin_roles (admin_id, role_id) VALUES (:adminId, :roleId)", nativeQuery = true)
    void insertAdminRole(String adminId, String roleId);
}
