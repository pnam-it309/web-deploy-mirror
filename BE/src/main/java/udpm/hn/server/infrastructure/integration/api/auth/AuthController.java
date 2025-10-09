package udpm.hn.server.infrastructure.integration.api.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import udpm.hn.server.infrastructure.core.security.service.CustomUserDetailsService;
import org.springframework.web.bind.annotation.*;
import udpm.hn.server.infrastructure.core.constant.MappingConstants;
import udpm.hn.server.infrastructure.core.security.jwt.JwtService;
import udpm.hn.server.infrastructure.integration.api.auth.dto.UserDetailsImpl;
import udpm.hn.server.infrastructure.integration.api.auth.dto.request.LoginRequest;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(MappingConstants.API_AUTH_PREFIX)
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        // Get user by email
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());
        
        // Create authentication object with null credentials since we're not using password
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            userDetails, 
            null, 
            userDetails.getAuthorities()
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate tokens
        String jwt = jwtService.generateToken(userDetails);
        String refreshToken = jwt; // For now, use the same token as refresh token

        // Prepare response
        Map<String, Object> response = new HashMap<>();
        response.put("token", jwt);
        response.put("refreshToken", refreshToken);
        response.put("id", ((UserDetailsImpl)userDetails).getId());
        response.put("email", userDetails.getUsername());
        response.put("roles", userDetails.getAuthorities());

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String refreshToken = authHeader.substring(7);
            String username = jwtService.extractUsername(refreshToken);

            if (username != null) {
                UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                        .getAuthentication().getPrincipal();

                if (jwtService.isTokenValid(refreshToken, userDetails)) {
                    String newAccessToken = jwtService.generateToken(userDetails);

                    Map<String, String> tokens = new HashMap<>();
                    tokens.put("accessToken", newAccessToken);
                    tokens.put("refreshToken", refreshToken);

                    return ResponseEntity.ok(tokens);
                }
            }
        }
        return ResponseEntity.badRequest().body("Invalid refresh token");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logout successful");
    }
    
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("User not authenticated");
        }
        
        Object principal = authentication.getPrincipal();
        
        if (principal instanceof UserDetails) {
            UserDetailsImpl userDetails = (UserDetailsImpl) principal;
            Map<String, Object> response = new HashMap<>();
            response.put("id", userDetails.getId());
            response.put("username", userDetails.getUsername());
            response.put("email", userDetails.getEmail());
            response.put("roles", userDetails.getAuthorities());
            return ResponseEntity.ok(response);
        } else if (principal instanceof String) {
            // Trường hợp principal là String (thường là username)
            Map<String, String> response = new HashMap<>();
            response.put("username", (String) principal);
            return ResponseEntity.ok(response);
        }
        
        return ResponseEntity.status(401).body("Unable to get user information");
    }
}
