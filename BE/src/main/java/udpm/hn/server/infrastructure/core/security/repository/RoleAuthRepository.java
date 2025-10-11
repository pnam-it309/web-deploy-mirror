package udpm.hn.server.infrastructure.core.security.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import udpm.hn.server.repository.RoleRepository;

import java.util.List;

public interface RoleAuthRepository extends RoleRepository {
    @Query(
            value = """

                    SELECT DISTINCT
                          r.code
                      FROM
                          Role r
                      LEFT JOIN
                       AdminRole ar ON r.id = ar.role.id
                      WHERE
                          ar.admin.id = :id
                    """
    )
    List<String> findRoleByAdminId(@Param("id") String id);

    @Query(
            value = """

                    SELECT DISTINCT
                          r.code
                      FROM
                          Role r
                      LEFT JOIN
                       AdminRole sr ON r.id = sr.role.id
                      WHERE
                          sr.admin.id = :id
                    """
    )
    List<String> findRoleByStaffId(@Param("id") String id);

}
