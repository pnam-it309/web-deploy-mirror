package udpm.hn.server.core.admin.role.service;

import udpm.hn.server.core.admin.role.model.request.RoleRequest;
import udpm.hn.server.core.admin.role.model.response.PermissionResponse;
import udpm.hn.server.core.admin.role.model.response.RoleResponse;

import java.util.List;
import java.util.Set;

public interface RoleService {

    /**
     * Get all roles
     */
    List<RoleResponse> getAllRoles();

    /**
     * Get role by ID with permissions
     */
    RoleResponse getRoleById(String id);

    /**
     * Create new role
     */
    RoleResponse createRole(RoleRequest request);

    /**
     * Update existing role
     */
    RoleResponse updateRole(String id, RoleRequest request);

    /**
     * Delete role
     */
    void deleteRole(String id);

    /**
     * Assign permissions to role
     */
    RoleResponse assignPermissionsToRole(String roleId, Set<String> permissionIds);

    /**
     * Get all permissions available in the system
     */
    List<PermissionResponse> getAllPermissions();

    /**
     * Get user's aggregated permissions from all their roles
     */
    Set<String> getUserPermissions(String userId);

    /**
     * Check if user has specific permission
     */
    boolean hasPermission(String userId, String permissionCode);
}
