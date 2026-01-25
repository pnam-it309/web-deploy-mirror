package udpm.hn.server.entity;

import jakarta.persistence.*;
import lombok.*;
import udpm.hn.server.entity.base.PrimaryEntity;

@Entity
@Table(name = "audit_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog extends PrimaryEntity {

    @Column(name = "entity_type", nullable = false)
    private String entityType; // App, Domain, Feature, etc.

    @Column(name = "entity_id", nullable = false)
    private String entityId;

    @Column(name = "action", nullable = false)
    @Enumerated(EnumType.STRING)
    private AuditAction action; // CREATE, UPDATE, DELETE

    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "old_value", columnDefinition = "TEXT")
    private String oldValue; // JSON of old entity state

    @Column(name = "new_value", columnDefinition = "TEXT")
    private String newValue; // JSON of new entity state

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "user_agent")
    private String userAgent;

    public enum AuditAction {
        CREATE, UPDATE, DELETE, STATUS_CHANGE
    }
}
