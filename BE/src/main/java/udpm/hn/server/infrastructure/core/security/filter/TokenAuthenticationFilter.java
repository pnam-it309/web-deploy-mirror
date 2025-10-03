package udpm.hn.server.infrastructure.core.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import udpm.hn.server.infrastructure.core.security.service.CustomUserDetailsService;
import udpm.hn.server.infrastructure.core.security.service.TokenProvider;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            log.debug("Processing authentication for: {}", request.getRequestURI());
            
            String jwt = getJwtFromRequest(request);
            
            if (jwt != null && !jwt.isEmpty()) {
                log.debug("JWT token found: {}...", jwt.substring(0, Math.min(10, jwt.length())));
                
                if (tokenProvider.validateToken(jwt)) {
                    String userEmail = tokenProvider.getEmailFromToken(jwt);
                    
                    if (userEmail != null && !userEmail.isEmpty()) {
                        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userEmail);
                        
                        if (userDetails != null) {
                            log.debug("Authenticated user: {}", userEmail);
                            
                            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );
                            
                            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                        } else {
                            log.warn("User not found with email: {}", userEmail);
                        }
                    } else {
                        log.warn("No email found in JWT token");
                    }
                } else {
                    log.warn("Invalid JWT token");
                }
            } else {
                log.debug("No JWT token found in request headers");
            }
        } catch (Exception ex) {
            log.error("Failed to set user authentication in security context", ex);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed: " + ex.getMessage());
            return;
        }
        
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
