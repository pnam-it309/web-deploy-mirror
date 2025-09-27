package udpm.hn.server.infrastructure.processing.analytics;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.analytics.Analytics;
import com.google.api.services.analytics.AnalyticsScopes;

import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Configuration
@RequiredArgsConstructor
public class GoogleAnalyticsConfig {

    @Value("${google.analytics.credentials.path}")
    private String credentialsPath;

    @Value("${google.analytics.view.id}")
    private String viewId;

    @Value("${spring.application.name}")
    private String applicationName;

    @Bean
    public AnalyticsReporting getAnalyticsReporting() throws GeneralSecurityException, IOException {
        final HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        GoogleCredentials credential = GoogleCredentials.fromStream(
                new ClassPathResource(credentialsPath).getInputStream())
                .createScoped(AnalyticsReportingScopes.all());

        return new AnalyticsReporting.Builder(
                httpTransport,
                GsonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credential))
                .setApplicationName(applicationName)
                .build();
    }

    @Bean
    public Analytics getAnalytics() throws GeneralSecurityException, IOException {
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        GoogleCredentials credential = GoogleCredentials.fromStream(
                new ClassPathResource(credentialsPath).getInputStream())
                .createScoped(Collections.singleton(AnalyticsScopes.ANALYTICS_READONLY));

        return new Analytics.Builder(httpTransport, 
                GsonFactory.getDefaultInstance(), 
                new HttpCredentialsAdapter(credential))
                .setApplicationName(applicationName)
                .build();
    }

    @Bean
    public String viewId() {
        return viewId;
    }
}
