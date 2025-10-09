package udpm.hn.server.infrastructure.core.config.dbgenerator;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import udpm.hn.server.infrastructure.core.config.dbgenerator.repository.DBGRoleRepository;
import udpm.hn.server.infrastructure.core.config.dbgenerator.repository.DBGStaffRepository;
import udpm.hn.server.infrastructure.core.config.dbgenerator.repository.DBGStaffRoleRepository;
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

//    private final DBGStaffRepository staffRepository;
//
//    private final DBGRoleRepository roleRepository;
//
//    private final DBGStaffRoleRepository staffRoleRepository;

    @PostConstruct
    public void init() {
        if ("true".equals(isGenerated)) {

            generateRole();
            generateData();

        }
    }

    private void generateData() {
//        Optional<Staff> staffOptional = staffRepository.findByEmailFpt(userEmail);
//        Staff staff;
//        if (staffOptional.isEmpty()) {
//            staff = new Staff();
//            staff.setEmailFpt(userEmail);
//            staff.setCode(userCode);
//            staff.setName(userName);
//            staff.setStatus(EntityStatus.ACTIVE);
//            staffRepository.save(staff);
//        }else {
//            staff = staffOptional.get();
//        }
//
//        addRoleToUser(staff, Role.ADMIN.name());
//        addRoleToUser(staff, Role.MANAGE.name());
//        addRoleToUser(staff, "MANAGE");
//        addRoleToUser(staff, "MEMBER");
    }

    private void generateRole() {
//        createRoleIfNotExist(Role.ADMIN.name(), Role.getVietnameseNameByRole(Role.ADMIN.name()));
//        createRoleIfNotExist(Role.MANAGE.name(), Role.getVietnameseNameByRole(Role.MANAGE.name()));
//        createRoleIfNotExist(Role.MEMBER.name(), Role.getVietnameseNameByRole(Role.MEMBER.name()));
    }

    private void createRoleIfNotExist(String roleCode,String roleName) {
//        if (roleRepository.findByCode(roleCode).isEmpty()) {
//            udpm.hn.server.entity.Role role = new udpm.hn.server.entity.Role();
//            role.setCode(roleCode);
//            role.setName(roleName);
//            role.setStatus(EntityStatus.ACTIVE);
//            roleRepository.save(role);
//        }
    }

//    private void addRoleToUser(Staff staff, String roleName) {
//        Optional<udpm.hn.server.entity.Role> roleOptional = roleRepository.findByCode(roleName);
//
//        if (roleOptional.isPresent()) {
//            udpm.hn.server.entity.Role role = roleOptional.get();
//            // Kiểm tra xem user đã có role này chưa
//            if (!staffRoleRepository.existsByStaffAndRole(staff, role)) {
//                StaffRole roleUser = new StaffRole();
//                roleUser.setStaff(staff);
//                roleUser.setRole(role);
//                roleUser.setStatus(EntityStatus.ACTIVE);
//                staffRoleRepository.save(roleUser);
//            }
//        }
//     }

}