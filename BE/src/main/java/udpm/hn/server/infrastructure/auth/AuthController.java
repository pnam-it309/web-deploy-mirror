package udpm.hn.server.infrastructure.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import udpm.hn.server.infrastructure.security.service.CustomUserDetailsService;
import org.springframework.web.bind.annotation.*;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import udpm.hn.server.infrastructure.security.jwt.JwtService;
import udpm.hn.server.infrastructure.auth.dto.UserDetailsImpl;
import udpm.hn.server.infrastructure.auth.dto.request.LoginRequest;
import udpm.hn.server.utils.Helper;

import java.util.HashMap;
import java.util.Map;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import udpm.hn.server.entity.Customer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@RestController
@RequestMapping(MappingConstants.API_AUTH_PREFIX)
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final udpm.hn.server.repository.CustomerRepository customerRepository;
    private final udpm.hn.server.infrastructure.security.repository.StaffAuthRepository staffAuthRepository;
    private final udpm.hn.server.infrastructure.security.repository.RoleAuthRepository roleAuthRepository;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtService.generateToken(userDetails);
            String refreshToken = jwt;

            Map<String, Object> tokens = new HashMap<>();
            tokens.put("accessToken", jwt);
            tokens.put("refreshToken", refreshToken);

            return Helper.createResponseEntity(udpm.hn.server.core.common.base.ResponseObject.successForward(tokens, "Login successful"));
        } catch (Exception e) {
            return Helper.createResponseEntity(udpm.hn.server.core.common.base.ResponseObject.errorForward("Login failed: " + e.getMessage()));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody(required = false) Map<String, String> body, HttpServletRequest request) {
        String refreshToken = null;
        if (body != null) {
            refreshToken = body.get("refreshToken");
        }
        
        if (refreshToken == null || refreshToken.isEmpty()) {
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                refreshToken = authHeader.substring(7);
            }
        }

        if (refreshToken != null && !refreshToken.isEmpty()) {
            try {
                String username = jwtService.extractUsername(refreshToken);
                if (username != null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    if (jwtService.isTokenValid(refreshToken, userDetails)) {
                        String newAccessToken = jwtService.generateToken(userDetails);
                        Map<String, String> tokens = new HashMap<>();
                        tokens.put("accessToken", newAccessToken);
                        tokens.put("refreshToken", refreshToken);

                        return Helper.createResponseEntity(udpm.hn.server.core.common.base.ResponseObject.successForward(tokens, "Refresh successful"));
                    }
                }
            } catch (Exception e) {
                return Helper.createResponseEntity(udpm.hn.server.core.common.base.ResponseObject.errorForward("Token refresh failed: " + e.getMessage()));
            }
        }
        return Helper.createResponseEntity(udpm.hn.server.core.common.base.ResponseObject.errorForward("Invalid refresh token", org.springframework.http.HttpStatus.UNAUTHORIZED));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        SecurityContextHolder.clearContext();
        return Helper.createResponseEntity(udpm.hn.server.core.common.base.ResponseObject.successForward(null, "Logout successful"));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
             return Helper.createResponseEntity(udpm.hn.server.core.common.base.ResponseObject.errorForward("Not authenticated", org.springframework.http.HttpStatus.UNAUTHORIZED));
        }

        Object principal = authentication.getPrincipal();
        String email = null;
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else if (principal instanceof org.springframework.security.oauth2.core.user.OAuth2User) {
            email = ((org.springframework.security.oauth2.core.user.OAuth2User) principal).getAttribute("email");
        } else {
            email = principal.toString();
        }

        // Try to find both Admin and Customer
        java.util.Optional<udpm.hn.server.entity.Admin> adminOpt = staffAuthRepository.findByUsernameAndStatus(email, udpm.hn.server.infrastructure.constant.EntityStatus.ACTIVE);
        java.util.Optional<udpm.hn.server.entity.Customer> customerOpt = customerRepository.findByEmail(email);

        if (adminOpt.isPresent() || customerOpt.isPresent()) {
            Map<String, Object> userData = new HashMap<>();
            java.util.Set<String> combinedRoles = new java.util.HashSet<>();

            if (adminOpt.isPresent()) {
                udpm.hn.server.entity.Admin admin = adminOpt.get();
                userData.put("id", admin.getId());
                userData.put("name", admin.getFullName());
                userData.put("email", admin.getUsername());
                combinedRoles.addAll(roleAuthRepository.findRoleByAdminId(admin.getId()));
            }

            if (customerOpt.isPresent()) {
                udpm.hn.server.entity.Customer customer = customerOpt.get();
                if (!userData.containsKey("id")) {
                    userData.put("id", customer.getId());
                    userData.put("name", customer.getFullName());
                    userData.put("email", customer.getEmail());
                }
                combinedRoles.addAll(customer.getRoles().stream().map(udpm.hn.server.entity.Role::getCode).toList());
            }

            // Ensure consistent naming for roles if needed (e.g. from constants)
            userData.put("roles", combinedRoles.stream().toList());
            return Helper.createResponseEntity(udpm.hn.server.core.common.base.ResponseObject.successForward(userData, "Get info successful"));
        }

        return Helper.createResponseEntity(udpm.hn.server.core.common.base.ResponseObject.errorForward("User data not found"));
    }
}
