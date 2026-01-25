package udpm.hn.server.core.admin.role.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import udpm.hn.server.core.admin.role.model.request.RoleRequest;
import udpm.hn.server.core.admin.role.model.response.PermissionResponse;
import udpm.hn.server.core.admin.role.model.response.RoleResponse;
import udpm.hn.server.core.admin.role.service.RoleService;
import udpm.hn.server.entity.Admin;
import udpm.hn.server.entity.Permission;
import udpm.hn.server.entity.Role;
import udpm.hn.server.infrastructure.exception.ResourceNotFoundException;
import udpm.hn.server.repository.AdminRepository;
import udpm.hn.server.repository.PermissionRepository;
import udpm.hn.server.repository.RoleRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final AdminRepository adminRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public List<RoleResponse> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(this::mapToRoleResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public RoleResponse getRoleById(String id) {
        Role role = roleRepository.findByIdWithPermissions(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "id", id));
        return mapToRoleResponse(role);
    }

    @Override
    @Transactional
    public RoleResponse createRole(RoleRequest request) {
        // Check if role name already exists
        if (roleRepository.findByCode(request.getName()).isPresent()) {
            throw new IllegalArgumentException("Role name already exists: " + request.getName());
        }

        Role role = new Role();
        role.setCode(request.getName());
        role.setName(request.getDescription());

        // Assign permissions if provided
        if (request.getPermissionIds() != null && !request.getPermissionIds().isEmpty()) {
            Set<Permission> permissions = new HashSet<>(
                    permissionRepository.findAllById(request.getPermissionIds()));
            role.setPermissions(permissions);
        }

        Role savedRole = roleRepository.save(role);
        log.info("Created new role: {} (ID: {})", savedRole.getCode(), savedRole.getId());

        return mapToRoleResponse(savedRole);
    }

    @Override
    @Transactional
    public RoleResponse updateRole(String id, RoleRequest request) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "id", id));

        // Check name uniqueness if changed
        if (!role.getCode().equals(request.getName())) {
            if (roleRepository.findByCode(request.getName()).isPresent()) {
                throw new IllegalArgumentException("Role name already exists: " + request.getName());
            }
            role.setCode(request.getName());
        }

        role.setName(request.getDescription());

        // Update permissions if provided
        if (request.getPermissionIds() != null) {
            Set<Permission> permissions = new HashSet<>(
                    permissionRepository.findAllById(request.getPermissionIds()));
            role.setPermissions(permissions);
        }

        Role updatedRole = roleRepository.save(role);
        log.info("Updated role: {} (ID: {})", updatedRole.getCode(), updatedRole.getId());

        return mapToRoleResponse(updatedRole);
    }

    @Override
    @Transactional
    public void deleteRole(String id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "id", id));

        // Check if any admin has this role
        // long adminCount = adminRepository.count();
        // Simple check - in production, you'd want a proper query

        roleRepository.delete(role);
        log.info("Deleted role: {} (ID: {})", role.getCode(), role.getId());
    }

    @Override
    @Transactional
    public RoleResponse assignPermissionsToRole(String roleId, Set<String> permissionIds) {
        Role role = roleRepository.findByIdWithPermissions(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "id", roleId));

        Set<Permission> permissions = new HashSet<>(
                permissionRepository.findAllById(permissionIds));

        if (permissions.size() != permissionIds.size()) {
            throw new IllegalArgumentException("Some permission IDs are invalid");
        }

        role.setPermissions(permissions);
        Role updatedRole = roleRepository.save(role);

        log.info("Assigned {} permissions to role: {}", permissions.size(), role.getCode());

        return mapToRoleResponse(updatedRole);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PermissionResponse> getAllPermissions() {
        return permissionRepository.findAll().stream()
                .map(permission -> modelMapper.map(permission, PermissionResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Set<String> getUserPermissions(String userId) {
        Admin admin = adminRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin", "id", userId));

        // Aggregate all permissions from all roles
        Set<String> permissionCodes = new HashSet<>();
        for (Role role : admin.getRoles()) {
            Role roleWithPermissions = roleRepository.findByIdWithPermissions(role.getId())
                    .orElse(role);
            roleWithPermissions.getPermissions().forEach(permission -> permissionCodes.add(permission.getCode()));
        }

        return permissionCodes;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasPermission(String userId, String permissionCode) {
        Set<String> userPermissions = getUserPermissions(userId);
        return userPermissions.contains(permissionCode);
    }

    // Helper method to map Role to RoleResponse
    private RoleResponse mapToRoleResponse(Role role) {
        RoleResponse response = new RoleResponse();
        response.setId(role.getId());
        response.setName(role.getCode());
        response.setDescription(role.getName());
        response.setCreatedAt(role.getCreatedAt());
        response.setUpdatedAt(role.getLastModifiedDate());

        if (role.getPermissions() != null) {
            Set<PermissionResponse> permissionResponses = role.getPermissions().stream()
                    .map(permission -> modelMapper.map(permission, PermissionResponse.class))
                    .collect(Collectors.toSet());
            response.setPermissions(permissionResponses);
        } else {
            response.setPermissions(new HashSet<>());
        }

        return response;
    }
}
