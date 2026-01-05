package udpm.hn.server.infrastructure.config;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.Value;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@Configuration
@ConditionalOnProperty(
    name = "google.sheets.enabled",
    havingValue = "true",
    matchIfMissing = false
)
public class GoogleSheetsConfig {

    private static final String APPLICATION_NAME = "FPL UDPM Catalog";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS);

    @Value("${google.credentials.file.path:classpath:credentials.json}")
    private Resource credentialsFile;

    @Value("${google.application.credentials:}")
    private String credentialsJson;

    @Bean
    @ConditionalOnProperty(
        name = "google.sheets.enabled",
        havingValue = "true",
        matchIfMissing = false
    )
    public Sheets sheetsService() throws IOException, GeneralSecurityException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        return new Sheets.Builder(httpTransport, JSON_FACTORY, getCredentials(httpTransport))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    private Credential getCredentials(final NetHttpTransport httpTransport) throws IOException {
        if (credentialsJson != null && !credentialsJson.isEmpty()) {
            return GoogleCredential.fromStream(new java.io.ByteArrayInputStream(credentialsJson.getBytes()))
                    .createScoped(SCOPES);
        } else if (credentialsFile.exists()) {
            return GoogleCredential.fromStream(credentialsFile.getInputStream())
                    .createScoped(SCOPES);
        }
        throw new IOException("No Google credentials provided. Please set google.credentials.file.path or google.application.credentials");
    }
}
