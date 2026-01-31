package udpm.hn.server.infrastructure.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import udpm.hn.server.entity.Role;
import udpm.hn.server.infrastructure.config.global.GlobalVariables;
import udpm.hn.server.infrastructure.constant.GlobalVariablesConstant;
import udpm.hn.server.infrastructure.security.service.CustomUserDetailsService;
import udpm.hn.server.infrastructure.security.service.TokenProvider;

import java.io.IOException;
import java.util.List;
import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final CustomUserDetailsService customUserDetailsService;
    private final GlobalVariables globalVariables;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            // Clean state for current thread
            globalVariables.clear();
            
            String requestURI = request.getRequestURI();
            if (requestURI.startsWith("/api/v1/customer") || requestURI.startsWith("/api/v1/admin")) {
                log.debug("Processing request: {} {}", request.getMethod(), requestURI);
                // Log all headers for debugging 401 issues in deployed environment
                java.util.Enumeration<String> headerNames = request.getHeaderNames();
                if (headerNames != null) {
                    while (headerNames.hasMoreElements()) {
                        String name = headerNames.nextElement();
                        log.trace("Header: {} = {}", name, request.getHeader(name));
                    }
                }
            }

            String jwt = getJwtFromRequest(request);

            if (StringUtils.hasText(jwt)) {
                log.info("doFilterInternal==>jwt found, length={}", jwt.length());
                if (tokenProvider.validateToken(jwt)) {
                    String userId = tokenProvider.getUserIdFromToken(jwt);
                    String userEmail = tokenProvider.getEmailFromToken(jwt);
                    UserDetails userDetails = customUserDetailsService.loadUserByUsername(userEmail);
                    List<Role> roleCode = tokenProvider.getRolesCodesFromToken(jwt);

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    globalVariables.setGlobalVariable(GlobalVariablesConstant.CURRENT_USER_ID, userId);
                    globalVariables.setGlobalVariable(GlobalVariablesConstant.CURRENT_ROLE_CODE, roleCode);

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.debug("Successfully authenticated user: {}", userEmail);
                } else {
                    log.warn("Invalid JWT token provided");
                    SecurityContextHolder.clearContext();
                }
            } else {
                // No token found
                SecurityContextHolder.clearContext();
            }
        } catch (Exception ex) {
            log.error("Could not set user authentication in security context", ex);
            SecurityContextHolder.clearContext();
        }
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        String altHeader = request.getHeader("X-Authorization");
        if (StringUtils.hasText(altHeader) && altHeader.startsWith("Bearer ")) {
            return altHeader.substring(7);
        }

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            String fromCookie = Arrays.stream(cookies)
                    .filter(c -> "access_token".equalsIgnoreCase(c.getName()) || "token".equalsIgnoreCase(c.getName()))
                    .map(Cookie::getValue)
                    .filter(StringUtils::hasText)
                    .findFirst()
                    .orElse(null);
            if (StringUtils.hasText(fromCookie)) {
                return fromCookie;
            }
        }

        String fromParam = request.getParameter("access_token");
        if (!StringUtils.hasText(fromParam)) {
            fromParam = request.getParameter("token");
        }
        return StringUtils.hasText(fromParam) ? fromParam : null;
    }
}
