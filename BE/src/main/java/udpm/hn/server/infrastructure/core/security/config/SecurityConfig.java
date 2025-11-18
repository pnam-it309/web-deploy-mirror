package udpm.hn.server.infrastructure.core.security.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import udpm.hn.server.infrastructure.core.config.global.GlobalVariables;
import udpm.hn.server.infrastructure.core.exception.RestAuthenticationEntryPoint;
import udpm.hn.server.infrastructure.core.security.oauth2.CustomOAuth2UserService;
import udpm.hn.server.infrastructure.core.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import udpm.hn.server.infrastructure.core.security.oauth2.OAuth2AuthenticationFailureHandler;
import udpm.hn.server.infrastructure.core.security.oauth2.OAuth2AuthenticationSuccessHandler;
import udpm.hn.server.infrastructure.core.security.service.CustomUserDetailsService;
import udpm.hn.server.infrastructure.core.security.service.TokenProvider;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import udpm.hn.server.infrastructure.core.constant.MappingConstants;
import udpm.hn.server.infrastructure.core.security.filter.TokenAuthenticationFilter;
import udpm.hn.server.utils.Helper;

import static udpm.hn.server.utils.Helper.appendWildcard;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true
)
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final TokenProvider tokenProvider;
    private final CustomUserDetailsService customUserDetailsService;
    private final GlobalVariables globalVariables;
    private TokenAuthenticationFilter tokenAuthenticationFilter;


    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(tokenProvider,customUserDetailsService,globalVariables);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder
    ) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailsService);
        return new ProviderManager(provider);
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("Initializing security filter chain");

        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(AbstractHttpConfigurer::disable)
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .exceptionHandling(e -> e.authenticationEntryPoint(new RestAuthenticationEntryPoint()));

        // Public endpoints
        http.authorizeHttpRequests(auth -> auth
            .requestMatchers(
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
                "/auth/**",
                "/oauth2/**",
                appendWildcard(MappingConstants.API_AUTH_PREFIX)
            ).permitAll()
        );

        // Public API endpoints
        http.authorizeHttpRequests(auth -> auth
            .requestMatchers(appendWildcard(MappingConstants.API_PUBLIC_CATEGORIES)).permitAll()
            .requestMatchers(appendWildcard(MappingConstants.API_PUBLIC_PRODUCTS)).permitAll()
            .requestMatchers(appendWildcard(MappingConstants.API_CUSTOMER_REGISTER)).permitAll()
        );

        // Customer API endpoints
        http.authorizeHttpRequests(auth -> auth
            .requestMatchers(appendWildcard(MappingConstants.API_CUSTOMER_VIEW_PRODUCT)).hasAuthority("CUSTOMER")
        );

        // Admin API endpoints
        http.authorizeHttpRequests(auth -> auth
            .requestMatchers(appendWildcard(MappingConstants.API_ADMIN_PREFIX + "/*")).hasAuthority("ADMIN")
            .requestMatchers(appendWildcard(MappingConstants.API_ADMIN_IMPORT)).hasAuthority("ADMIN")
            .requestMatchers(appendWildcard(MappingConstants.API_ADMIN_PRODUCT)).hasAuthority("ADMIN")
            .requestMatchers(appendWildcard(MappingConstants.API_ADMIN_ORDER)).hasAuthority("ADMIN")
            .requestMatchers(appendWildcard(MappingConstants.API_ADMIN_WAREHOUSE)).hasAuthority("ADMIN")
            .requestMatchers(appendWildcard(MappingConstants.API_ADMIN_CUSTOMER)).hasAuthority("ADMIN")
            .requestMatchers(appendWildcard(MappingConstants.API_ADMIN_CATEGORY)).hasAuthority("ADMIN")
            .requestMatchers(appendWildcard(MappingConstants.API_ADMIN_BRAND)).hasAuthority("ADMIN")
        );

        // OAuth2 configuration
        http.oauth2Login(oauth2 -> oauth2
            .authorizationEndpoint(a -> a.baseUri("/oauth2/authorization"))
            .redirectionEndpoint(r -> r.baseUri("/oauth2/callback/**"))
            .userInfoEndpoint(u -> u.userService(customOAuth2UserService))
            .authorizationEndpoint(a -> a.authorizationRequestRepository(httpCookieOAuth2AuthorizationRequestRepository))
            .successHandler(oAuth2AuthenticationSuccessHandler)
            .failureHandler(oAuth2AuthenticationFailureHandler)
        );

        // Add token authentication filter
        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


}