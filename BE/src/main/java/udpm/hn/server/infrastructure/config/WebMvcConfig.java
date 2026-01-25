package udpm.hn.server.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC configuration to explicitly define resource handling
 * and prevent API paths from being treated as static resources.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Explicitly configure static resource locations
        // This prevents Spring Boot's default resource handler from
        // intercepting API requests like /customer/apps, /customer/technologies, etc.
        
        registry.addResourceHandler(
                "/static/**",
                "/assets/**",
                "/css/**",
                "/js/**",
                "/images/**",
                "/favicon.ico"
        )
        .addResourceLocations(
                "classpath:/static/",
                "classpath:/public/",
                "classpath:/resources/",
                "classpath:/META-INF/resources/"
        );
        
        // Important: Do NOT add a catch-all /** resource handler
        // This would cause API paths to be treated as static resources
    }
}
