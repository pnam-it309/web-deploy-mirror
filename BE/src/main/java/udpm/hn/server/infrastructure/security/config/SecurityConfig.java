package udpm.hn.server.infrastructure.security.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import udpm.hn.server.infrastructure.config.global.GlobalVariables;
import udpm.hn.server.infrastructure.exception.RestAuthenticationEntryPoint;
import udpm.hn.server.infrastructure.security.oauth2.CustomOAuth2UserService;
import udpm.hn.server.infrastructure.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import udpm.hn.server.infrastructure.security.oauth2.OAuth2AuthenticationFailureHandler;
import udpm.hn.server.infrastructure.security.oauth2.OAuth2AuthenticationSuccessHandler;
import udpm.hn.server.infrastructure.security.service.CustomUserDetailsService;
import udpm.hn.server.infrastructure.security.service.TokenProvider;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import udpm.hn.server.infrastructure.security.filter.TokenAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import static udpm.hn.server.utils.Helper.appendWildcard;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true)
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
        private final CorsConfigurationSource corsConfigurationSource;
        private TokenAuthenticationFilter tokenAuthenticationFilter;

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

                http
                                .csrf(AbstractHttpConfigurer::disable)
                                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .formLogin(AbstractHttpConfigurer::disable)
                                .httpBasic(AbstractHttpConfigurer::disable)
                                .exceptionHandling(e -> e.authenticationEntryPoint(new RestAuthenticationEntryPoint()));

                // Public and API endpoints
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
                                                "/login/oauth2/code/**",
                                                appendWildcard(MappingConstants.API_AUTH_PREFIX),
                                                appendWildcard(MappingConstants.API_CUSTOMER_PREFIX),
                                                appendWildcard(MappingConstants.API_ADMIN_PREFIX))
                                .permitAll()
                                .anyRequest()
                                .permitAll());

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