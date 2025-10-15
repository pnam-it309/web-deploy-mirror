package udpm.hn.server.infrastructure.core.config.dbgenerator;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import udpm.hn.server.entity.Admin;
import udpm.hn.server.entity.AdminRole;
import udpm.hn.server.infrastructure.core.constant.Roles;
import udpm.hn.server.infrastructure.core.config.dbgenerator.repository.*;
import udpm.hn.server.infrastructure.core.constant.EntityStatus;


import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DBGenerator {
    @Value("${db.generator.is-generated}")
    private String isGenerated;

    @Value("${db.generator.user-email}")
    private String userEmail;

    @Value("${db.generator.user-code}")
    private String userCode;

    @Value("${db.generator.user-name}")
    private String userName;

    private final DBGAdminRepository adminRepository;

    private final DBGRoleRepository roleRepository;

    private final DBGAdminRoleRepository adminRoleRepository;

    @PostConstruct
    public void init() {
        System.out.println("=== DBGenerator init() called ===");
        System.out.println("isGenerated: " + isGenerated);
        System.out.println("userEmail: " + userEmail);
        System.out.println("userCode: " + userCode);
        System.out.println("userName: " + userName);

        if ("true".equals(isGenerated)) {
            System.out.println("DBGenerator is enabled, calling generateRole() and generateData()...");
            generateRole();
            generateData();
        } else {
            System.out.println("DBGenerator is disabled, skipping data generation");
        }
        System.out.println("=== DBGenerator init() finished ===\n");
    }

    private void generateData() {
        System.out.println("=== DBGenerator generateData() called ===");
        Optional<Admin> staffOptional = adminRepository.findByEmail(userEmail);
        Admin admin;
        if (staffOptional.isEmpty()) {
            System.out.println("Admin not found, creating new admin...");
            admin = new Admin();
            admin.setEmail(userEmail);
            admin.setCode(userCode);
            admin.setName(userName);
            admin.setStatus(EntityStatus.ACTIVE);
            admin = adminRepository.save(admin);
            System.out.println("Admin created successfully: " + admin.getName() + " (ID: " + admin.getId() + ")");
        }else {
            admin = staffOptional.get();
            System.out.println("Admin already exists: " + admin.getName() + " (ID: " + admin.getId() + ")");
        }

        addRoleToUser(admin, Roles.ADMIN.name());
        addRoleToUser(admin, Roles.CUSTOMER.name());
        System.out.println("=== DBGenerator generateData() finished ===\n");
    }

    private void generateRole() {
        System.out.println("=== DBGenerator generateRole() called ===");
        createRoleIfNotExist(Roles.ADMIN.name(), Roles.getVietnameseNameByRole(Roles.ADMIN.name()));
        createRoleIfNotExist(Roles.CUSTOMER.name(), Roles.getVietnameseNameByRole(Roles.CUSTOMER.name()));
        System.out.println("=== DBGenerator generateRole() finished ===\n");
    }

    private void createRoleIfNotExist(String roleCode,String roleName) {
        System.out.println("Checking if role exists: " + roleCode);
        if (roleRepository.findByCode(roleCode).isEmpty()) {
            System.out.println("Creating role: " + roleCode + " - " + roleName);
            udpm.hn.server.entity.Role role = new udpm.hn.server.entity.Role();
            role.setCode(roleCode);
            role.setName(roleName);
            role.setStatus(EntityStatus.ACTIVE);
            udpm.hn.server.entity.Role savedRole = roleRepository.save(role);
            System.out.println("Role created successfully: " + savedRole.getName() + " (ID: " + savedRole.getId() + ")");
        } else {
            System.out.println("Role already exists: " + roleCode);
        }
    }

    private void addRoleToUser(Admin admin, String roleName) {
        System.out.println("=== Adding role to admin: " + roleName + " ===");
        System.out.println("Admin ID: " + admin.getId());
        System.out.println("Admin Email: " + admin.getEmail());

        Optional<udpm.hn.server.entity.Role> roleOptional = roleRepository.findByCode(roleName);

        if (roleOptional.isPresent()) {
            udpm.hn.server.entity.Role role = roleOptional.get();
            System.out.println("Found role: " + role.getName() + " (ID: " + role.getId() + ")");

            // Kiểm tra xem user đã có role này chưa
            boolean alreadyHasRole = adminRoleRepository.existsByAdminAndRole(admin, role);
            System.out.println("Admin already has role '" + roleName + "': " + alreadyHasRole);

            if (!alreadyHasRole) {
                System.out.println("Creating AdminRole for admin: " + admin.getEmail() + " with role: " + roleName);
                AdminRole adminRole = new AdminRole();
                adminRole.setAdmin(admin);
                adminRole.setRole(role);
                adminRole.setStatus(EntityStatus.ACTIVE);

                try {
                    AdminRole savedAdminRole = adminRoleRepository.save(adminRole);
                    System.out.println("✅ AdminRole created successfully!");
                    System.out.println("AdminRole ID: " + savedAdminRole.getId());
                    System.out.println("AdminRole Status: " + savedAdminRole.getStatus());
                } catch (Exception e) {
                    System.out.println("❌ ERROR: Failed to save AdminRole!");
                    System.out.println("Error: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                System.out.println("Admin already has role '" + roleName + "', skipping creation");
            }
        } else {
            System.out.println("ERROR: Role not found: " + roleName);
        }
        System.out.println("=== Finished adding role to admin ===\n");
    }


}