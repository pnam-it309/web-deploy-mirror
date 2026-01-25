package udpm.hn.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Admin;

@Repository
@org.springframework.context.annotation.Primary
public interface AdminRepository extends JpaRepository<Admin, String> {
}
