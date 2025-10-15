package udpm.hn.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import udpm.hn.server.entity.Admin;
import udpm.hn.server.entity.AdminRole;
import udpm.hn.server.entity.Role;

import java.util.List;
import java.util.Optional;

public interface AdminRoleRepository extends JpaRepository<AdminRole, String> {
    List<AdminRole> findByAdminIdAndRoleId(String idAdmin, String  idRole);

    Optional<AdminRole> findByRoleIdAndAdminId(String idRole , String idAdmin);

    boolean existsByAdminAndRole(Admin admin, Role role);

}
