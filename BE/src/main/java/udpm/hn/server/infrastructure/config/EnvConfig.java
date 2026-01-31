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
        String currentDir = System.getProperty("user.dir");
        java.io.File envFile = new java.io.File(currentDir, ".env");

        // If not found in current dir, check parent (common in IDEs)
        if (!envFile.exists()) {
            java.io.File parentEnv = new java.io.File(new java.io.File(currentDir).getParent(), ".env");
            if (parentEnv.exists()) {
                envFile = parentEnv;
            }
        }

        if (envFile.exists()) {
            System.out.println(">>> EnvConfig: Loading environment variables from " + envFile.getAbsolutePath());
            try (InputStream input = new FileInputStream(envFile)) {
                Properties props = new Properties();
                props.load(input);

                props.forEach((key, value) -> {
                    if (value != null && !value.toString().trim().isEmpty()) {
                        String k = key.toString();
                        String v = value.toString();
                        System.setProperty(k, v);
                    }
                });
                System.out.println(">>> EnvConfig: Successfully loaded " + props.size() + " variables.");
            } catch (IOException e) {
                System.err.println(">>> EnvConfig: Error loading .env file: " + e.getMessage());
            }
        } else {
            System.out.println(">>> EnvConfig: .env file NOT found in " + currentDir + " or its parent.");
        }
    }
}
