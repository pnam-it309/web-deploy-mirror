package udpm.hn.server.infrastructure.config.dbgenerator;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import udpm.hn.server.entity.Admin;
import udpm.hn.server.entity.Role;
import udpm.hn.server.infrastructure.constant.Roles;
import udpm.hn.server.infrastructure.config.dbgenerator.repository.DBGAdminRepository;
import udpm.hn.server.infrastructure.config.dbgenerator.repository.DBGRoleRepository;
import udpm.hn.server.infrastructure.constant.EntityStatus;

import javax.sql.DataSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import java.sql.Connection;
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
    private final DataSource dataSource;

    @PostConstruct
    public void init() {
        if ("true".equals(isGenerated)) {
            generateRole();
            generateData();
            generateSampleData();
        }
    }

    @org.springframework.transaction.annotation.Transactional
    protected void generateData() {
        Optional<Admin> adminOptional = adminRepository.findByUsername(userEmail);
        Admin admin;
        if (adminOptional.isEmpty()) {
            admin = new Admin();
            admin.setUsername(userEmail);
            admin.setFullName(userName);
            admin.setPassword(new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode("123456"));
            admin.setStatus(EntityStatus.ACTIVE);
            admin = adminRepository.save(admin);
        } else {
            admin = adminOptional.get();
        }

        // Refresh the admin entity to ensure roles collection is properly loaded
        admin = adminRepository.findById(admin.getId()).orElse(admin);

        addRoleToUser(admin, Roles.ADMIN.name());
        addRoleToUser(admin, Roles.CUSTOMER.name());
    }

    private void generateRole() {
        createRoleIfNotExist(Roles.ADMIN.name(), Roles.getVietnameseNameByRole(Roles.ADMIN.name()));
        createRoleIfNotExist(Roles.CUSTOMER.name(), Roles.getVietnameseNameByRole(Roles.CUSTOMER.name()));
    }

    private void createRoleIfNotExist(String roleCode, String roleName) {
        if (roleRepository.findByCode(roleCode).isEmpty()) {
            Role role = new Role();
            role.setCode(roleCode);
            role.setName(roleName);
            role.setStatus(EntityStatus.ACTIVE);
            roleRepository.save(role);
        }
    }

    private void addRoleToUser(Admin admin, String roleName) {
        Optional<Role> roleOptional = roleRepository.findByCode(roleName);

        if (roleOptional.isPresent()) {
            Role role = roleOptional.get();
            // Initialize roles collection if null
            if (admin.getRoles() == null) {
                admin.setRoles(new java.util.HashSet<>());
            }

            boolean alreadyHasRole = admin.getRoles().stream()
                    .anyMatch(r -> r.getCode().equals(roleName));

            if (!alreadyHasRole) {
                admin.getRoles().add(role);
                adminRepository.save(admin);
                System.out.println("Role " + roleName + " added to admin " + admin.getUsername());
            } else {
                System.out.println("Admin " + admin.getUsername() + " already has role " + roleName);
            }
        }
    }

    private void generateSampleData() {
        System.out.println("Executing data.sql...");
        try (Connection connection = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(connection, new ClassPathResource("data.sql"));
            System.out.println("Executed data.sql successfully.");
        } catch (Exception e) {
            System.err.println("Error executing data.sql: " + e.getMessage());
        }
    }
}