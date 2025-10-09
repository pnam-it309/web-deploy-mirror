package udpm.hn.server.infrastructure.core.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.AdminRole;
import udpm.hn.server.repository.StaffRoleRepository;

import java.util.List;
import java.util.Optional;

public interface StaffRoleAuthRepository extends StaffRoleRepository {

    @Query(value = """
              SELECT DISTINCT r.name FROM Role r
                        JOIN AdminRole ar ON r.id = ar.role.id
                        WHERE ar.admin.id = :adminId
            """)
    List<String> getRoleNamesByAdminId(String adminId);

    @Query(value = """
                SELECT DISTINCT r.code FROM Role r
                    JOIN AdminRole ar ON r.id = ar.role.id
                    WHERE ar.admin.id = :adminId AND ar.status = 0
            """)
    List<String> getRoleCodesByAdminId(String adminId);
    
}
