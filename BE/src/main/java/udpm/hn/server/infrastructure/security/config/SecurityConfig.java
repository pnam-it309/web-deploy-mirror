package udpm.hn.server.infrastructure.security.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;
import udpm.hn.server.infrastructure.config.global.GlobalVariables;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import udpm.hn.server.infrastructure.exception.RestAuthenticationEntryPoint;
import udpm.hn.server.infrastructure.security.filter.TokenAuthenticationFilter;
import udpm.hn.server.infrastructure.security.oauth2.CustomOAuth2UserService;
import udpm.hn.server.infrastructure.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import udpm.hn.server.infrastructure.security.oauth2.OAuth2AuthenticationFailureHandler;
import udpm.hn.server.infrastructure.security.oauth2.OAuth2AuthenticationSuccessHandler;
import udpm.hn.server.infrastructure.security.service.CustomUserDetailsService;
import udpm.hn.server.infrastructure.security.service.TokenProvider;

import static udpm.hn.server.utils.Helper.appendWildcard;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
@Slf4j
@org.springframework.core.annotation.Order(1)
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final TokenProvider tokenProvider;
    private final CustomUserDetailsService customUserDetailsService;
    private final GlobalVariables globalVariables;
    // Bean này được inject vào, đảm bảo bạn đã định nghĩa nó ở 1 file config khác
    // (GlobalConfig hoặc CorsConfig)
    @org.springframework.beans.factory.annotation.Qualifier("corsConfigurationSource")
    private final CorsConfigurationSource corsConfigurationSource;

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(tokenProvider, customUserDetailsService, globalVariables);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailsService);
        return new ProviderManager(provider);
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("Initializing security filter chain");
        log.info("Customer API prefix: {}", MappingConstants.API_CUSTOMER_PREFIX);
        log.info("Customer API with wildcard: {}", appendWildcard(MappingConstants.API_CUSTOMER_PREFIX));

        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource)) // Sử dụng cấu hình
                // CORS inject vào
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .exceptionHandling(e -> e.authenticationEntryPoint(new RestAuthenticationEntryPoint()));

        // Add debug filter to log all requests
        http.addFilterBefore((request, response, chain) -> {
            jakarta.servlet.http.HttpServletRequest httpRequest = (jakarta.servlet.http.HttpServletRequest) request;
            String requestURI = httpRequest.getRequestURI();
            String method = httpRequest.getMethod();
            log.debug("Security Filter processing: {} {}", method, requestURI);

            if (requestURI.startsWith("/customer") || requestURI.startsWith("/admin")) {
                log.info("API Request detected: {} {}", method, requestURI);
            }

            chain.doFilter(request, response);
        }, org.springframework.security.web.context.SecurityContextHolderFilter.class);

        // IMPORTANT: Consolidate ALL authorization rules in ONE place to prevent
        // fallthrough to static resources
        http.authorizeHttpRequests(auth -> auth
                // 1. Allow all API endpoints starting with /api/v1/
                // This covers /api/v1/customer/**, /api/v1/admin/**, /api/v1/auth/**, /api/v1/common/**
                .requestMatchers("/api/v1/**")
                .permitAll()

                // 2. Static Resources & Public Endpoints
                .requestMatchers(
                        "/",
                        "/error",
                        "/favicon.ico",
                        "/assets/**",
                        "/*.png", "/*.gif", "/*.svg", "/*.jpg", 
                        "/*.html", "/*.css", "/*.js",
                        "/auth/**",
                        "/oauth2/**",
                        "/login/oauth2/code/**")
                .permitAll()

                // 4. Any other request must be authenticated (prevents fallthrough to static
                // resources)
                .anyRequest().authenticated());

        // OAuth2 configuration
        http.oauth2Login(oauth2 -> oauth2
                .authorizationEndpoint(a -> a.baseUri("/oauth2/authorization"))
                .redirectionEndpoint(r -> r.baseUri("/login/oauth2/code/*"))
                .userInfoEndpoint(u -> u.userService(customOAuth2UserService))
                .authorizationEndpoint(
                        a -> a.authorizationRequestRepository(
                                httpCookieOAuth2AuthorizationRequestRepository))
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler));

        // Add token authentication filter
        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}