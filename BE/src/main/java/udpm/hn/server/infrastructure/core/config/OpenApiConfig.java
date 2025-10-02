package udpm.hn.server.infrastructure.core.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${openapi.dev-url:http://localhost:9999}")
    private String devUrl;

    @Value("${openapi.prod-url:https://your-production-url.com}")
    private String prodUrl;

    @Bean
    public OpenAPI myOpenAPI() {
        Server devServer = new Server()
                .url(devUrl)
                .description("Development server");

        Server prodServer = new Server()
                .url(prodUrl)
                .description("Production server");

        Contact contact = new Contact()
                .email("contact@example.com")
                .name("FPL UDPM Catalog Team");

        License mitLicense = new License()
                .name("MIT License")
                .url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
                .title("FPL UDPM Catalog API")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints for Google Sheets and Excel integration.")
                .license(mitLicense);

        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer, prodServer));
    }
}
