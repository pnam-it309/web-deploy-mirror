package udpm.hn.server.infrastructure.core.config.dbgenerator;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import udpm.hn.server.entity.Role;
import udpm.hn.server.infrastructure.core.config.dbgenerator.repository.DBGRoleRepository;
import udpm.hn.server.infrastructure.core.constant.EntityStatus;
import udpm.hn.server.infrastructure.core.constant.Roles;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RoleGenerator {

    private final DBGRoleRepository roleRepository;

    @PostConstruct
    public void generate() {
        List<Role> existingRoles = roleRepository.findAll();
        if (existingRoles.isEmpty()) {
            List<String> roleCodes = Roles.getAllRoles();
            List<String> roleNames = List.of("ADMIN", "CUSTOMER");
            
            for (int i = 0; i < roleCodes.size(); i++) {
                if (roleRepository.findByCode(roleCodes.get(i)).isEmpty()) {
                    Role role = new Role();
                    role.setCode(roleCodes.get(i));
                    role.setName(roleNames.get(i));
                    role.setStatus(EntityStatus.ACTIVE);
                    roleRepository.save(role);
                }
            }
        }
    }
}
