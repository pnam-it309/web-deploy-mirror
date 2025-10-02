package udpm.hn.server.infrastructure.core.security.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import udpm.hn.server.infrastructure.core.security.service.CustomUserDetailsService;
import udpm.hn.server.infrastructure.core.security.service.TokenProvider;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import udpm.hn.server.infrastructure.core.constant.MappingConstants;
import udpm.hn.server.infrastructure.core.constant.Role;
import udpm.hn.server.infrastructure.core.security.filter.TokenAuthenticationFilter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static udpm.hn.server.utils.Helper.appendWildcard;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    @Value("${frontend.url:http://localhost:6789}")
    private String allowedOrigin;

    private final CorsConfigurationSource corsConfigurationSource;
    private final TokenProvider tokenProvider;
    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(tokenProvider, customUserDetailsService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    public AuthenticationManager authenticationManager(
            PasswordEncoder passwordEncoder,
            UserDetailsService userDetailsService
    ) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailsService);
        return new ProviderManager(provider);
    }

//     @Bean
//     CorsConfigurationSource corsConfigurationSource() {
//         final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//         CorsConfiguration config = new CorsConfiguration();
//         source.registerCorsConfiguration("/**", config.applyPermitDefaultValues());
//         config.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type", "*"));
//         config.setAllowedOrigins(Collections.singletonList(allowedOrigin));
//         config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PUT", "OPTIONS", "PATCH", "DELETE"));
//         config.setAllowCredentials(true);
//         config.setExposedHeaders(List.of("Authorization"));
//         return source;
//     }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        log.info("Security configuration initialized");

        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(c -> c.configurationSource(corsConfigurationSource));
        http.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);

        // Public endpoints - no authentication required
        http.authorizeHttpRequests(auth -> auth.requestMatchers(
                        "/",
                        "/error",
                        "/favicon.ico",
                        "/*/*.png",
                        "/*/*.gif",
                        "/*/*.svg",
                        "/*/*.jpg",
                        "/*/*.html",
                        "/*/*.css",
                        "/*/*.js",
                        "/ws/**"
                )
                .permitAll());

        // Authentication endpoints
        http.authorizeHttpRequests(
                auth -> auth.requestMatchers(
                        "/auth/**",
                        appendWildcard(MappingConstants.API_AUTH_PREFIX)
                )
                        .permitAll()
        );

        // Public API endpoints
        http.authorizeHttpRequests(
                auth -> auth
                        .requestMatchers(appendWildcard(MappingConstants.API_ADMIN_CATEGORY)).permitAll()
        );

        // Protected API endpoints
        http.authorizeHttpRequests(
                auth -> auth
                        .requestMatchers(appendWildcard(MappingConstants.API_ADMIN_PREFIX)).hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers(appendWildcard(MappingConstants.API_CUSTOMER_PREFIX)).hasAnyAuthority(Role.CUSTOMER.name(), Role.ADMIN.name())
        );

        // All other requests require authentication
        http.authorizeHttpRequests(auth -> auth.anyRequest().authenticated());

        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}