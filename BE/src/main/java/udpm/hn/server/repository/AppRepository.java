package udpm.hn.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.App;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppRepository extends JpaRepository<App, String> {

    @Query("SELECT DISTINCT a FROM App a LEFT JOIN FETCH a.technologies")
    List<App> findAllWithTechnologies();

    @Query("SELECT DISTINCT a FROM App a LEFT JOIN FETCH a.technologies WHERE a.id = :id")
    Optional<App> findByIdWithTechnologies(String id);
}
