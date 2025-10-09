package udpm.hn.server.infrastructure.core.config.dbgenerator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleGenerator {

//    private final DBGRoleRepository roleRepository;
//
//    private final DBGFacilityRepository facilityRepository;
//
//    @PostConstruct
//    public void generate(){
//        List<Facility> listFacilities = facilityRepository.findAll();
//        for(Facility facility : listFacilities){
//            List<Role> isHasRole = roleRepository.findAllByFacility(facility);
//            if(isHasRole.isEmpty()){
//                List<String> roleCodes = udpm.hn.server.infrastructure.constant.Role.getAllRoles();
//                List<String> roleNames = List.of("ADMIN","Giảng viên","Sinh viên");
//                for (int i = 0;i< roleCodes.size(); i++){
//                    if(roleRepository.findByCodeAndNameAndFacility(
//                            roleCodes.get(i),
//                            roleNames.get(i),
//                            facility
//                    ).isEmpty()) {
//                        Role role = new Role();
//                        role.setCode(roleCodes.get(i));
//                        role.setName(roleNames.get(i));
//                        role.setFacility(facility);
//                        roleRepository.save(role);
//                    }
//                }
//            }
//        }
//    }
}
