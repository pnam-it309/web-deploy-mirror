package udpm.hn.server.infrastructure.integration.api.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import udpm.hn.server.infrastructure.integration.api.auth.dto.LoginRequest;
import udpm.hn.server.infrastructure.integration.api.auth.dto.UserDetailsImpl;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

//    private final AuthenticationManager authenticationManager;
//    private final JwtService jwtService;
//
//    @PostMapping("/login")
//    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
//        );
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
//
//        String jwt = jwtService.generateToken(userDetails);
//        String refreshToken = jwtService.generateRefreshToken(userDetails);
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("token", jwt);
//        response.put("refreshToken", refreshToken);
//        response.put("id", userDetails.getId());
//        response.put("username", userDetails.getUsername());
//        response.put("email", userDetails.getEmail());
//        response.put("roles", userDetails.getAuthorities());
//
//        return ResponseEntity.ok(response);
//    }
//
//    @PostMapping("/refresh-token")
//    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
//        String authHeader = request.getHeader("Authorization");
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            String refreshToken = authHeader.substring(7);
//            String username = jwtService.extractUsername(refreshToken);
//
//            if (username != null) {
//                UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
//                        .getAuthentication().getPrincipal();
//
//                if (jwtService.isTokenValid(refreshToken, userDetails)) {
//                    String newAccessToken = jwtService.generateToken(userDetails);
//
//                    Map<String, String> tokens = new HashMap<>();
//                    tokens.put("accessToken", newAccessToken);
//                    tokens.put("refreshToken", refreshToken);
//
//                    return ResponseEntity.ok(tokens);
//                }
//            }
//        }
//        return ResponseEntity.badRequest().body("Invalid refresh token");
//    }
//
//    @PostMapping("/logout")
//    public ResponseEntity<?> logoutUser() {
//        SecurityContextHolder.clearContext();
//        return ResponseEntity.ok("Logout successful");
//    }
}
