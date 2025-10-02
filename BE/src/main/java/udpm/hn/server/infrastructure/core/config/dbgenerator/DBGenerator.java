package udpm.hn.server.infrastructure.core.config.dbgenerator;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DBGenerator {
    @Value("${db.generator.is-generated:false}")
    private String isGenerated;

    @Value("${db.generator.user-email:nickhunter3009@gmail.com}")
    private String userEmail;

    @Value("${db.generator.user-code:ADMIN001}")
    private String userCode;

    @Value("${db.generator.user-name:Admin}")
    private String userName;



    @PostConstruct
    public void init() {
        if ("true".equals(isGenerated)) {

            generateRole();
            generateData();

        }
    }

    private void generateData() {

    }

    private void generateRole() {
    }

    private void createRoleIfNotExist(String roleCode,String roleName) {

    }

    private void addRoleToUser() {

    }

}