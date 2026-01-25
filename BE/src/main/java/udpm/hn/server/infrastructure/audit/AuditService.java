package udpm.hn.server.infrastructure.audit;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import udpm.hn.server.entity.AuditLog;
import udpm.hn.server.repository.AuditLogRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditService {

    private final AuditLogRepository auditLogRepository;
    private final ObjectMapper objectMapper;

    public void log(String entityType, String entityId, AuditLog.AuditAction action,
            Object oldValue, Object newValue, String userEmail, String userName) {
        try {
            AuditLog auditLog = AuditLog.builder()
                    .entityType(entityType)
                    .entityId(entityId)
                    .action(action)
                    .oldValue(oldValue != null ? objectMapper.writeValueAsString(oldValue) : null)
                    .newValue(newValue != null ? objectMapper.writeValueAsString(newValue) : null)
                    .userEmail(userEmail)
                    .userName(userName)
                    .ipAddress(getClientIp())
                    .userAgent(getUserAgent())
                    .build();

            auditLogRepository.save(auditLog);
            log.debug("Audit log created: {} {} on {} {}", action, entityType, entityId, userEmail);
        } catch (Exception e) {
            log.error("Failed to create audit log", e);
        }
    }

    public void logCreate(String entityType, String entityId, Object newValue, String userEmail, String userName) {
        log(entityType, entityId, AuditLog.AuditAction.CREATE, null, newValue, userEmail, userName);
    }

    public void logUpdate(String entityType, String entityId, Object oldValue, Object newValue, String userEmail,
            String userName) {
        log(entityType, entityId, AuditLog.AuditAction.UPDATE, oldValue, newValue, userEmail, userName);
    }

    public void logDelete(String entityType, String entityId, Object oldValue, String userEmail, String userName) {
        log(entityType, entityId, AuditLog.AuditAction.DELETE, oldValue, null, userEmail, userName);
    }

    public void logStatusChange(String entityType, String entityId, Object oldValue, Object newValue, String userEmail,
            String userName) {
        log(entityType, entityId, AuditLog.AuditAction.STATUS_CHANGE, oldValue, newValue, userEmail, userName);
    }

    private String getClientIp() {
        try {
            ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs != null) {
                HttpServletRequest request = attrs.getRequest();
                String xForwardedFor = request.getHeader("X-Forwarded-For");
                if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
                    return xForwardedFor.split(",")[0].trim();
                }
                return request.getRemoteAddr();
            }
        } catch (Exception e) {
            log.debug("Could not get client IP", e);
        }
        return null;
    }

    private String getUserAgent() {
        try {
            ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs != null) {
                return attrs.getRequest().getHeader("User-Agent");
            }
        } catch (Exception e) {
            log.debug("Could not get user agent", e);
        }
        return null;
    }
}
