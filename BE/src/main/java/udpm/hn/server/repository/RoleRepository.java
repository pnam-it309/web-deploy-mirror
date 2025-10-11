package udpm.hn.server.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Role;
import udpm.hn.server.infrastructure.core.constant.EntityStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Role findRolesByCode(String code);

    Optional<Role> findFirstByCode(String code);

    @Query(
            value = """
                            select r from AdminRole st
                            join Role r on r.id = st.role.id
                            join Admin s on s.id = st.admin.id
                            where s.id = :idStaff
                    """
    )
    List<Role> findRoleByStaff(@Param("idStaff") String idStaff);


}
