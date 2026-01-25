package udpm.hn.server.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.AuditLog;

/**
 * AuditLog Repository
 * Now supports JPA Specifications for dynamic filtering
 * 
 * Usage with Specification:
 * 
 * <pre>
 * Specification<AuditLog> spec = AuditLogSpecification.forEntity("App", appId);
 * List<AuditLog> logs = repository.findAll(spec);
 * </pre>
 */
@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, String>, JpaSpecificationExecutor<AuditLog> {

    Page<AuditLog> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<AuditLog> findByEntityTypeOrderByCreatedAtDesc(String entityType, Pageable pageable);

    Page<AuditLog> findByEntityIdOrderByCreatedAtDesc(String entityId, Pageable pageable);

    // Removed: findByEntityTypeAndEntityId - use AuditLogSpecification.forEntity()
    // instead
    // Removed: findByUserEmail - use AuditLogSpecification.forUser() instead
}
