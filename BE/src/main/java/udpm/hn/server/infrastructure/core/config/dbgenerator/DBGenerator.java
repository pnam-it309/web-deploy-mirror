package udpm.hn.server.infrastructure.core.config.dbgenerator;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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