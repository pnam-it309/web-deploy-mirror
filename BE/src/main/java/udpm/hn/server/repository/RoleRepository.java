package udpm.hn.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Role;

import java.util.Optional;

@Repository
@org.springframework.context.annotation.Primary
public interface RoleRepository extends JpaRepository<Role, String> {

    Optional<Role> findByCode(String code);

    @Query("SELECT r FROM Role r LEFT JOIN FETCH r.permissions WHERE r.id = :id")
    Optional<Role> findByIdWithPermissions(@Param("id") String id);
}
