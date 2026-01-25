package udpm.hn.server.infrastructure.security.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import udpm.hn.server.core.admin.role.service.RoleService;

import udpm.hn.server.infrastructure.security.annotation.RequiresPermission;
import udpm.hn.server.infrastructure.security.user.UserPrincipal;

import java.lang.reflect.Method;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class PermissionCheckAspect {

    private final RoleService roleService;

    @Around("@annotation(udpm.hn.server.infrastructure.security.annotation.RequiresPermission)")
    public Object checkPermission(ProceedingJoinPoint joinPoint) throws Throwable {
        // Get the method and annotation
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RequiresPermission annotation = method.getAnnotation(RequiresPermission.class);

        // Get current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User not authenticated");
        }

        // Get user ID from principal
        String userId = null;
        Object principal = authentication.getPrincipal();

        if (principal instanceof UserPrincipal) {
            userId = ((UserPrincipal) principal).getId();
        } else if (principal instanceof String) {
            userId = (String) principal;
        }

        if (userId == null) {
            throw new IllegalStateException("Unable to determine user identity");
        }

        // Check permission
        String requiredPermission = annotation.value();
        boolean hasPermission = roleService.hasPermission(userId, requiredPermission);

        if (!hasPermission) {
            log.warn("Permission denied for user {} - required: {}", userId, requiredPermission);
            throw new SecurityException(annotation.message() + " - Required: " + requiredPermission);
        }

        log.debug("Permission check passed for user {} - permission: {}", userId, requiredPermission);

        // Permission check passed, proceed with method execution
        return joinPoint.proceed();
    }
}
