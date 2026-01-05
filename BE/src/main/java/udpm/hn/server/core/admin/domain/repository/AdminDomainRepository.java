package udpm.hn.server.core.admin.domain.repository;

import org.springframework.stereotype.Repository;
import udpm.hn.server.repository.DomainRepository;

@Repository
public interface AdminDomainRepository extends DomainRepository {

    Boolean existsByName(String name);

    Boolean existsBySlug(String slug);

    Boolean existsByNameAndIdNot(String name, String id);

    Boolean existsBySlugAndIdNot(String slug, String id);

    @org.springframework.data.jpa.repository.Query("SELECT COUNT(a) FROM App a WHERE a.domain.id = :domainId")
    Long countAppsByDomainId(String domainId);
}
