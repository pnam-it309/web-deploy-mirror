package udpm.hn.server.core.admin.domain.repository;

import org.springframework.stereotype.Repository;
import udpm.hn.server.repository.DomainRepository;

@Repository
public interface DomainManageRepository extends DomainRepository {
    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, String id);
}