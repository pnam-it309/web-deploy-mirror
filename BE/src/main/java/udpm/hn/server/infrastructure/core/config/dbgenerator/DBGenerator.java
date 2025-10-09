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
        if ("true".equals(isGenerated)) {

            generateRole();
            generateData();

        }
    }

    private void generateData() {
        Optional<Admin> staffOptional = adminRepository.findByEmail(userEmail);
        Admin admin;
        if (staffOptional.isEmpty()) {
            admin = new Admin();
            admin.setEmail(userEmail);
            admin.setCode(userCode);
            admin.setName(userName);
            admin.setStatus(EntityStatus.ACTIVE);
            adminRepository.save(admin);
        }else {
            admin = staffOptional.get();
        }

        addRoleToUser(admin, Roles.ADMIN.name());
        addRoleToUser(admin, Roles.CUSTOMER.name());
    }

    private void generateRole() {
        createRoleIfNotExist(Roles.ADMIN.name(), Roles.getVietnameseNameByRole(Roles.ADMIN.name()));
        createRoleIfNotExist(Roles.CUSTOMER.name(), Roles.getVietnameseNameByRole(Roles.CUSTOMER.name()));
    }

    private void createRoleIfNotExist(String roleCode,String roleName) {
        if (roleRepository.findByCode(roleCode).isEmpty()) {
            udpm.hn.server.entity.Role role = new udpm.hn.server.entity.Role();
            role.setCode(roleCode);
            role.setName(roleName);
            role.setStatus(EntityStatus.ACTIVE);
            roleRepository.save(role);
        }
    }

    private void addRoleToUser(Admin admin, String roleName) {
        Optional<udpm.hn.server.entity.Role> roleOptional = roleRepository.findByCode(roleName);

        if (roleOptional.isPresent()) {
            udpm.hn.server.entity.Role role = roleOptional.get();
            // Kiểm tra xem user đã có role này chưa
            if (!adminRoleRepository.existsByAdminAndRole(admin, role)) {
                AdminRole roleUser = new AdminRole();
                roleUser.setRole(role);
                roleUser.setStatus(EntityStatus.ACTIVE);
                adminRoleRepository.save(roleUser);
            }
        }
    }


}