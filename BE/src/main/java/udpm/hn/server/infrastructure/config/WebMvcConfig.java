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
        // 1. Disable default resource handling for generic paths to avoid conflicts with APIs
        
        // 2. Explicitly map static resources
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
                
        registry.addResourceHandler("/assets/**")
                .addResourceLocations("classpath:/static/assets/");
                
        registry.addResourceHandler("/favicon.ico")
                .addResourceLocations("classpath:/static/favicon.ico");

        // 3. DO NOT map /** or root paths here. 
        // Let the DispatcherServlet handle everything else, which will route to Controllers.
    }
}
