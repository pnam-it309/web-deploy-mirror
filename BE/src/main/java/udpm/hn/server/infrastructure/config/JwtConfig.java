package udpm.hn.server.infrastructure.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import udpm.hn.server.infrastructure.security.service.TokenProvider;
import udpm.hn.server.infrastructure.security.repository.StaffAuthRepository;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
@RequiredArgsConstructor
public class JwtConfig {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private final StaffAuthRepository staffAuthRepository;

    // @Bean
    // @RequestScope
    // public HttpServletRequest httpServletRequest() {
    // return null; // Will be resolved by Spring's RequestContextFilter
    // }

    @Bean
    public TokenProvider tokenProvider(
            HttpServletRequest httpServletRequest) {
        return new TokenProvider(
                jwtSecret,
                staffAuthRepository,
                httpServletRequest);
    }
}
