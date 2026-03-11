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

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@org.springframework.core.annotation.Order(2)
public class DBGenerator implements CommandLineRunner {
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

    @Override
    public void run(String... args) {
        if ("true".equals(isGenerated)) {
            System.out.println("=== Starting DBGenerator ===");
            generateData();
            generateSampleData();
            System.out.println("=== DBGenerator Finished ===");
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



    private void addRoleToUser(Admin admin, String roleName) {
        Optional<Role> roleOptional = roleRepository.findByCode(roleName);

        if (roleOptional.isPresent()) {
            Role role = roleOptional.get();
            try {
                adminRepository.insertAdminRole(admin.getId(), role.getId());
                System.out.println("✅ Role " + roleName + " assigned to admin " + admin.getUsername());
            } catch (Exception e) {
                System.out.println("Role " + roleName + " already exists or could not be assigned to " + admin.getUsername());
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