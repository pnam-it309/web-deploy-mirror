package udpm.hn.server.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("FPL UDPM Catalog API")
                        .description(
                                "API documentation for FPL UDPM Catalog - Product management system for FPT Polytechnic")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("FPL UDPM Team")
                                .email("udpm@fpt.edu.vn"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:9999")
                                .description("Development Server"),
                        new Server()
                                .url("https://api.fpl-catalog.edu.vn")
                                .description("Production Server")));
    }
}
