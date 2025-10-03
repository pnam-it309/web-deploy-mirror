package udpm.hn.server.infrastructure.processing.analytics;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MatomoConfig {

    @Value("${matomo.site-id:1}")
    private int siteId;

    @Bean
    public Integer matomoSiteId() {
        return siteId;
    }
}
