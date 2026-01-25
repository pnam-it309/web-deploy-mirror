package udpm.hn.server.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import udpm.hn.server.entity.AuditLog;

/**
 * JPA Specification for AuditLog filtering
 * Replaces @Query methods with type-safe, reusable specifications
 */
public class AuditLogSpecification {

    /**
     * Filter by entity type
     * 
     * @param entityType - entity type (e.g., "App", "Domain")
     * @return Specification for entity type filtering
     */
    public static Specification<AuditLog> hasEntityType(String entityType) {
        return (root, query, cb) -> {
            if (entityType == null || entityType.trim().isEmpty()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("entityType"), entityType);
        };
    }

    /**
     * Filter by entity ID
     * 
     * @param entityId - entity ID
     * @return Specification for entity ID filtering
     */
    public static Specification<AuditLog> hasEntityId(String entityId) {
        return (root, query, cb) -> {
            if (entityId == null || entityId.trim().isEmpty()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("entityId"), entityId);
        };
    }

    /**
     * Filter by user email
     * 
     * @param userEmail - user email who performed the action
     * @return Specification for user email filtering
     */
    public static Specification<AuditLog> byUser(String userEmail) {
        return (root, query, cb) -> {
            if (userEmail == null || userEmail.trim().isEmpty()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("userEmail"), userEmail);
        };
    }

    /**
     * Filter by action type
     * 
     * @param action - action type (e.g., "CREATE", "UPDATE", "DELETE")
     * @return Specification for action filtering
     */
    public static Specification<AuditLog> hasAction(String action) {
        return (root, query, cb) -> {
            if (action == null || action.trim().isEmpty()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("action"), action);
        };
    }

    /**
     * Filter logs created after a certain date
     * 
     * @param timestamp - start timestamp (milliseconds)
     * @return Specification for date filtering
     */
    public static Specification<AuditLog> createdAfter(Long timestamp) {
        return (root, query, cb) -> {
            if (timestamp == null) {
                return cb.conjunction();
            }
            return cb.greaterThanOrEqualTo(root.get("createdAt"), timestamp);
        };
    }

    /**
     * Filter logs created before a certain date
     * 
     * @param timestamp - end timestamp (milliseconds)
     * @return Specification for date filtering
     */
    public static Specification<AuditLog> createdBefore(Long timestamp) {
        return (root, query, cb) -> {
            if (timestamp == null) {
                return cb.conjunction();
            }
            return cb.lessThanOrEqualTo(root.get("createdAt"), timestamp);
        };
    }

    /**
     * Order by creation date descending
     * 
     * @return Specification with ordering
     */
    public static Specification<AuditLog> orderByCreatedAtDesc() {
        return (root, query, cb) -> {
            if (query != null) {
                query.orderBy(cb.desc(root.get("createdAt")));
            }
            return cb.conjunction();
        };
    }

    /**
     * Get audit logs for a specific entity, ordered by date
     * 
     * @param entityType - entity type
     * @param entityId   - entity ID
     * @return Combined specification
     */
    public static Specification<AuditLog> forEntity(String entityType, String entityId) {
        return Specification
                .where(hasEntityType(entityType))
                .and(hasEntityId(entityId))
                .and(orderByCreatedAtDesc());
    }

    /**
     * Get audit logs for a specific user, ordered by date
     * 
     * @param userEmail - user email
     * @return Combined specification
     */
    public static Specification<AuditLog> forUser(String userEmail) {
        return Specification
                .where(byUser(userEmail))
                .and(orderByCreatedAtDesc());
    }
}
