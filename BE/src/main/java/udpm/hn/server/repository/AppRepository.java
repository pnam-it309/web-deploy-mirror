package udpm.hn.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.App;

@Repository
public interface AppRepository extends JpaRepository<App, String> {
}
