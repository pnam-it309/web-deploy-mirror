package udpm.hn.server.infrastructure.processing.analytics;

import org.piwik.java.tracking.PiwikTracker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MatomoConfig {

    @Value("${matomo.base-url}")
    private String matomoBaseUrl;

    @Value("${matomo.site-id:1}")
    private int siteId;

    @Value("${matomo.token-auth}")
    private String tokenAuth;

    @Bean
    public PiwikTracker piwikTracker() {
        PiwikTracker tracker = new PiwikTracker(matomoBaseUrl);
        tracker.setApiToken(tokenAuth);
        return tracker;
    }

    @Bean
    public Integer matomoSiteId() {
        return siteId;
    }
}
