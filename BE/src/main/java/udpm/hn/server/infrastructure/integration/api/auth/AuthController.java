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
import java.util.Collections;
import java.util.UUID;
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
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String googleClientSecret;

    private final udpm.hn.server.repository.CustomerRepository customerRepository;

    @PostMapping("/oauth2/callback/google")
    public ResponseEntity<?> googleCallback(
            @RequestParam String code,
            @RequestParam(required = false) String state,
            @RequestParam(required = false, defaultValue = "http://localhost:6789/selection") String redirectUri
    ) {
        try {
            // 1. Exchange code for token
            RestTemplate restTemplate = new RestTemplate();
            String tokenEndpoint = "https://oauth2.googleapis.com/token";

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("code", code);
            params.add("client_id", googleClientId);
            params.add("client_secret", googleClientSecret);
            params.add("redirect_uri", redirectUri);
            params.add("grant_type", "authorization_code");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

            Map<String, Object> tokenResponse = restTemplate.postForObject(tokenEndpoint, request, Map.class);

            if (tokenResponse == null || !tokenResponse.containsKey("access_token")) {
                return ResponseEntity.status(400).body(Map.of("message", "Failed to retrieve access token from Google"));
            }

            String googleAccessToken = (String) tokenResponse.get("access_token");

            // 2. Get User Info
            String userInfoEndpoint = "https://www.googleapis.com/oauth2/v3/userinfo";
            HttpHeaders userInfoHeaders = new HttpHeaders();
            userInfoHeaders.setBearerAuth(googleAccessToken);
            HttpEntity<String> userInfoRequest = new HttpEntity<>(userInfoHeaders);

            Map<String, Object> userInfo = restTemplate.exchange(
                    userInfoEndpoint,
                    org.springframework.http.HttpMethod.GET,
                    userInfoRequest,
                    Map.class
            ).getBody();

            if (userInfo == null) {
                return ResponseEntity.status(400).body(Map.of("message", "Failed to get user info from Google"));
            }

            String email = (String) userInfo.get("email");
            String name = (String) userInfo.get("name");
            String picture = (String) userInfo.get("picture");

            UserDetails userDetails = null;

            // 3. Handle Login based on Role (state)
            if ("ADMIN".equals(state)) {
                // Admin must exist
                try {
                    userDetails = userDetailsService.loadUserByUsername(email);
                } catch (Exception e) {
                    return ResponseEntity.status(403).body(Map.of("message", "Access Denied: You are not an Admin"));
                }
            } else {
                // CUSTOMER - Create if not exists
                if (customerRepository.findByEmail(email).isEmpty()) {
                    Customer newCustomer = new Customer();
                    newCustomer.setEmail(email);
                    newCustomer.setName(name);
                    newCustomer.setCode("CUS" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
                    newCustomer.setStatus(udpm.hn.server.infrastructure.core.constant.EntityStatus.ACTIVE);
                    customerRepository.save(newCustomer);
                }
                
                // Create UserDetails for Customer
                Customer customer = customerRepository.findByEmail(email).orElseThrow();
                userDetails = new UserDetailsImpl(
                        customer.getId(),
                        customer.getCode(), // API expects username?
                        customer.getEmail(),
                        null,
                        Collections.singleton(new SimpleGrantedAuthority("CUSTOMER")) // Or ROLE_CUSTOMER
                );
            }

            // 4. Generate JWT
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtService.generateToken(userDetails);
            
            // 5. Response
            Map<String, Object> response = new HashMap<>();
            response.put("accessToken", jwt);
            response.put("refreshToken", jwt); // Reuse for now
            response.put("role", state); // Return the role
            
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("message", "Internal Server Error: " + e.getMessage()));
        }
    }
}
