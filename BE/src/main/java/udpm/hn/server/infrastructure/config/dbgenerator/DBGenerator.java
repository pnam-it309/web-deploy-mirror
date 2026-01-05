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

    @PostConstruct
    public void init() {
        if ("true".equals(isGenerated)) {
            generateRole();
            generateData();
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

        addRoleToUser(admin, Roles.ADMIN.name());
        addRoleToUser(admin, Roles.CUSTOMER.name());
    }

    private void generateRole() {
        createRoleIfNotExist(Roles.ADMIN.name(), Roles.getVietnameseNameByRole(Roles.ADMIN.name()));
        createRoleIfNotExist(Roles.CUSTOMER.name(), Roles.getVietnameseNameByRole(Roles.CUSTOMER.name()));
    }

    private void createRoleIfNotExist(String roleCode, String roleName) {
        if (roleRepository.findByName(roleCode).isEmpty()) {
            Role role = new Role();
            role.setName(roleCode);
            role.setDescription(roleName);
            role.setStatus(EntityStatus.ACTIVE);
            roleRepository.save(role);
        }
    }

    private void addRoleToUser(Admin admin, String roleName) {
        Optional<Role> roleOptional = roleRepository.findByName(roleName);

        if (roleOptional.isPresent()) {
            Role role = roleOptional.get();
            boolean alreadyHasRole = admin.getRoles().stream().anyMatch(r -> r.getName().equals(roleName));

            if (!alreadyHasRole) {
                admin.getRoles().add(role);
                adminRepository.save(admin);
                System.out.println("Role " + roleName + " added to admin " + admin.getUsername());
            }
        }
    }
}