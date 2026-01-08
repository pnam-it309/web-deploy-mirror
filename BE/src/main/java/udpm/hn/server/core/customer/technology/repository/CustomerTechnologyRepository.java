package udpm.hn.server.core.customer.technology.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Technology;
import udpm.hn.server.repository.TechnologyRepository;

import java.util.List;

@Repository
public interface CustomerTechnologyRepository extends TechnologyRepository {

    @Query("SELECT t FROM Technology t WHERE t.status = :status")
    List<Technology> findAllActive(udpm.hn.server.infrastructure.constant.EntityStatus status);
}
