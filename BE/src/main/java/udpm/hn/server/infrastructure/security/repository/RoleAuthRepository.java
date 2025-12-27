package udpm.hn.server.infrastructure.security.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import udpm.hn.server.repository.RoleRepository;

import java.util.List;

public interface RoleAuthRepository extends RoleRepository {
    @Query("SELECT r.name FROM Customer c JOIN c.roles r WHERE c.id = :id")
    List<String> findRoleByCusId(@Param("id") String id);

    @Query("SELECT r.name FROM Admin a JOIN a.roles r WHERE a.id = :id")
    List<String> findRoleByAdminId(@Param("id") String id);

}
