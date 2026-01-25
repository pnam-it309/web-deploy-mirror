package udpm.hn.server.infrastructure.config.dbgenerator;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import udpm.hn.server.entity.Role;
import udpm.hn.server.infrastructure.config.dbgenerator.repository.DBGRoleRepository;
import udpm.hn.server.infrastructure.constant.EntityStatus;
import udpm.hn.server.infrastructure.constant.Roles;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RoleGenerator {

    private final DBGRoleRepository roleRepository;

    @PostConstruct
    public void generate() {
        System.out.println("=== RoleGenerator generate() called ===");

        // Always ensure essential roles exist
        ensureRoleExists(Roles.ADMIN.name(), "Administrator");
        ensureRoleExists(Roles.CUSTOMER.name(), "Customer");

        System.out.println("=== RoleGenerator generate() finished ===\n");
    }

    private void ensureRoleExists(String roleCode, String roleName) {
        System.out.println("Checking role: " + roleCode);

        Optional<Role> existingRole = roleRepository.findByCode(roleCode);
        if (existingRole.isEmpty()) {
            System.out.println("Creating role: " + roleCode + " - " + roleName);
            Role role = new Role();
            role.setCode(roleCode);
            role.setName(roleName);
            role.setStatus(EntityStatus.ACTIVE);

            try {
                Role savedRole = roleRepository.save(role);
                System.out.println("✅ Role created: " + savedRole.getCode() + " (ID: " + savedRole.getId() + ")");
            } catch (Exception e) {
                System.out.println("❌ ERROR: Failed to create role " + roleCode);
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("✅ Role already exists: " + roleCode + " (ID: " + existingRole.get().getId() + ")");
        }
    }
}
