package udpm.hn.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import udpm.hn.server.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, String> {
}
