package udpm.hn.server.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Properties;

@Configuration
public class EnvConfig {

    @PostConstruct
    public void loadEnvFile() {
        try {
            // Try to load .env file from the current working directory
            String currentDir = System.getProperty("user.dir");
            String envFilePath = Paths.get(currentDir, ".env").toString();

            try (InputStream input = new FileInputStream(envFilePath)) {
                Properties props = new Properties();
                props.load(input);

                // Set system properties for Spring Boot to use
                props.forEach((key, value) -> {
                    if (value != null && !value.toString().trim().isEmpty()) {
                        System.setProperty(key.toString(), value.toString());
                    }
                });
            }
        } catch (IOException e) {
            // .env file not found or couldn't be loaded, use existing environment variables
            // This is fine - Spring Boot will use existing env vars or defaults from application.properties
        }
    }
}
