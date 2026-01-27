package udpm.hn.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.Import;
import udpm.hn.server.infrastructure.config.HttpClientConfig;

@SpringBootApplication
@Import(HttpClientConfig.class)
public class BeApplication {

    public static void main(String[] args) {
        SpringApplication.run(BeApplication.class, args);
    }

}
