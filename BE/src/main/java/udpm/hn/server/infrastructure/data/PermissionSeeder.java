package udpm.hn.server.infrastructure.data;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import udpm.hn.server.entity.Permission;
import udpm.hn.server.infrastructure.constant.PermissionConstants;
import udpm.hn.server.repository.PermissionRepository;

import java.util.Arrays;
import java.util.List;

/**
 * Seed initial permissions into the database on application startup
 */
@Component
@Order(1) // Run first before other seeders
@RequiredArgsConstructor
@Slf4j
public class PermissionSeeder implements CommandLineRunner {

    private final PermissionRepository permissionRepository;

    @Override
    public void run(String... args) {
        if (permissionRepository.count() > 0) {
            log.info("Permissions already seeded, skipping");
            return;
        }

        log.info("Seeding permissions...");

        List<Permission> permissions = Arrays.asList(
                // App Management
                createPermission("View Apps", PermissionConstants.VIEW_APPS, "View application list", "APP_MANAGEMENT"),
                createPermission("Create App", PermissionConstants.CREATE_APP, "Create new applications",
                        "APP_MANAGEMENT"),
                createPermission("Edit App", PermissionConstants.EDIT_APP, "Edit existing applications",
                        "APP_MANAGEMENT"),
                createPermission("Delete App", PermissionConstants.DELETE_APP, "Delete applications", "APP_MANAGEMENT"),
                createPermission("Approve App", PermissionConstants.APPROVE_APP, "Approve pending applications",
                        "APP_MANAGEMENT"),

                // Domain Management
                createPermission("View Domains", PermissionConstants.VIEW_DOMAINS, "View domain list",
                        "DOMAIN_MANAGEMENT"),
                createPermission("Create Domain", PermissionConstants.CREATE_DOMAIN, "Create new domains",
                        "DOMAIN_MANAGEMENT"),
                createPermission("Edit Domain", PermissionConstants.EDIT_DOMAIN, "Edit existing domains",
                        "DOMAIN_MANAGEMENT"),
                createPermission("Delete Domain", PermissionConstants.DELETE_DOMAIN, "Delete domains",
                        "DOMAIN_MANAGEMENT"),

                // Technology Management
                createPermission("View Technologies", PermissionConstants.VIEW_TECHNOLOGIES, "View technology list",
                        "TECHNOLOGY_MANAGEMENT"),
                createPermission("Create Technology", PermissionConstants.CREATE_TECHNOLOGY, "Create new technologies",
                        "TECHNOLOGY_MANAGEMENT"),
                createPermission("Edit Technology", PermissionConstants.EDIT_TECHNOLOGY, "Edit existing technologies",
                        "TECHNOLOGY_MANAGEMENT"),
                createPermission("Delete Technology", PermissionConstants.DELETE_TECHNOLOGY, "Delete technologies",
                        "TECHNOLOGY_MANAGEMENT"),

                // Feature Management
                createPermission("View Features", PermissionConstants.VIEW_FEATURES, "View feature list",
                        "FEATURE_MANAGEMENT"),
                createPermission("Create Feature", PermissionConstants.CREATE_FEATURE, "Create new features",
                        "FEATURE_MANAGEMENT"),
                createPermission("Edit Feature", PermissionConstants.EDIT_FEATURE, "Edit existing features",
                        "FEATURE_MANAGEMENT"),
                createPermission("Delete Feature", PermissionConstants.DELETE_FEATURE, "Delete features",
                        "FEATURE_MANAGEMENT"),

                // User Management
                createPermission("View Users", PermissionConstants.VIEW_USERS, "View user list", "USER_MANAGEMENT"),
                createPermission("Create User", PermissionConstants.CREATE_USER, "Create new users", "USER_MANAGEMENT"),
                createPermission("Edit User", PermissionConstants.EDIT_USER, "Edit existing users", "USER_MANAGEMENT"),
                createPermission("Delete User", PermissionConstants.DELETE_USER, "Delete users", "USER_MANAGEMENT"),

                // Role Management
                createPermission("View Roles", PermissionConstants.VIEW_ROLES, "View role list", "ROLE_MANAGEMENT"),
                createPermission("Create Role", PermissionConstants.CREATE_ROLE, "Create new roles", "ROLE_MANAGEMENT"),
                createPermission("Edit Role", PermissionConstants.EDIT_ROLE, "Edit existing roles", "ROLE_MANAGEMENT"),
                createPermission("Delete Role", PermissionConstants.DELETE_ROLE, "Delete roles", "ROLE_MANAGEMENT"),
                createPermission("Assign Permissions", PermissionConstants.ASSIGN_PERMISSIONS,
                        "Assign permissions to roles", "ROLE_MANAGEMENT"),

                // System Management
                createPermission("View Dashboard", PermissionConstants.VIEW_DASHBOARD, "View admin dashboard",
                        "SYSTEM"),
                createPermission("View Analytics", PermissionConstants.VIEW_ANALYTICS, "View analytics and reports",
                        "SYSTEM"),
                createPermission("Manage Settings", PermissionConstants.MANAGE_SETTINGS, "Manage system settings",
                        "SYSTEM"),
                createPermission("View Audit Log", PermissionConstants.VIEW_AUDIT_LOG, "View audit logs", "SYSTEM"),
                createPermission("Manage Backups", PermissionConstants.MANAGE_BACKUPS, "Manage database backups",
                        "SYSTEM"),

                // Content Moderation
                createPermission("Moderate Reviews", PermissionConstants.MODERATE_REVIEWS, "Moderate user reviews",
                        "MODERATION"),
                createPermission("Moderate Comments", PermissionConstants.MODERATE_COMMENTS, "Moderate user comments",
                        "MODERATION"));

        permissionRepository.saveAll(permissions);
        log.info("Successfully seeded {} permissions", permissions.size());
    }

    private Permission createPermission(String name, String code, String description, String category) {
        Permission permission = new Permission();
        permission.setName(name);
        permission.setCode(code);
        permission.setDescription(description);
        permission.setCategory(category);
        return permission;
    }
}
