package udpm.hn.server.core.admin.audit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import udpm.hn.server.repository.AuditLogRepository;
import udpm.hn.server.repository.specification.AuditLogSpecification;
import udpm.hn.server.utils.Helper;

@RestController
@RequestMapping(MappingConstants.API_AUDIT_LOGS)
@RequiredArgsConstructor
public class AdminAuditLogController {

        private final AuditLogRepository auditLogRepository;

        @GetMapping
        public ResponseEntity<?> getAll(
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "20") int size) {
                Pageable pageable = PageRequest.of(page, size);
                return Helper.createResponseEntity(ResponseObject.successForward(
                                auditLogRepository.findAllByOrderByCreatedAtDesc(pageable),
                                "Lấy danh sách audit log thành công"));
        }

        @GetMapping("/entity/{entityType}")
        public ResponseEntity<?> getByEntityType(
                        @PathVariable String entityType,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "20") int size) {
                Pageable pageable = PageRequest.of(page, size);
                return Helper.createResponseEntity(ResponseObject.successForward(
                                auditLogRepository.findByEntityTypeOrderByCreatedAtDesc(entityType, pageable),
                                "Lấy audit log theo loại thành công"));
        }

        /**
         * Get audit logs for a specific entity
         * Now uses AuditLogSpecification.forEntity()
         */
        @GetMapping("/entity/{entityType}/{entityId}")
        public ResponseEntity<?> getByEntity(
                        @PathVariable String entityType,
                        @PathVariable String entityId) {
                Specification<udpm.hn.server.entity.AuditLog> spec = AuditLogSpecification.forEntity(entityType,
                                entityId);
                return Helper.createResponseEntity(ResponseObject.successForward(
                                auditLogRepository.findAll(spec),
                                "Lấy lịch sử thay đổi thành công"));
        }

        /**
         * Get audit logs by user
         * Now uses AuditLogSpecification.forUser() with pagination
         */
        @GetMapping("/user/{userEmail}")
        public ResponseEntity<?> getByUser(
                        @PathVariable String userEmail,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "20") int size) {
                Pageable pageable = PageRequest.of(page, size);
                Specification<udpm.hn.server.entity.AuditLog> spec = AuditLogSpecification.forUser(userEmail);
                return Helper.createResponseEntity(ResponseObject.successForward(
                                auditLogRepository.findAll(spec, pageable),
                                "Lấy audit log theo user thành công"));
        }
}
