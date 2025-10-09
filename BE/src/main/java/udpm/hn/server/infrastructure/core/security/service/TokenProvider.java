package udpm.hn.server.infrastructure.core.security.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import udpm.hn.server.entity.Admin;
import udpm.hn.server.infrastructure.core.constant.CookieConstant;
import udpm.hn.server.infrastructure.core.constant.EntityStatus;
import udpm.hn.server.infrastructure.core.constant.OAuth2Constant;
import udpm.hn.server.infrastructure.core.exception.RedirectException;
import udpm.hn.server.infrastructure.core.security.repository.AdminAuthRepository;
import udpm.hn.server.infrastructure.core.security.response.TokenInfoResponse;
import udpm.hn.server.infrastructure.core.security.user.UserPrincipal;
import udpm.hn.server.utils.CookieUtils;
import io.jsonwebtoken.Jwts;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenProvider {

    @Value("${jwt.secret:U3VwZXJTZWNyZXRLZXlGb3JKd3RTaWduYXR1cmUxMjM0NTY3ODk=}")
    private String tokenSecret;

    private final long TOKEN_EXP = System.currentTimeMillis() + 2 * 60 * 60 * 100000;

    private final AdminAuthRepository adminAuthRepository;
    private final HttpServletRequest httpServletRequest;

    public String createToken(Authentication authentication) throws BadRequestException, JsonProcessingException {

        log.info("Creating token for authentication: {}", authentication.toString());

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Optional<String> screenForRole = CookieUtils.getCookie(httpServletRequest, OAuth2Constant.SCREEN_FOR_ROLE_COOKIE_NAME)
                .map(Cookie::getValue);

        TokenInfoResponse tokenInfoResponse = new TokenInfoResponse();

        if (screenForRole.isEmpty()) {
            throw new RedirectException(CookieConstant.ACCOUNT_NOT_EXIST);
        } else {
            Admin adminUser = getCurrentAdminLogin(userPrincipal.getEmail());

            if (adminUser != null) {
                tokenInfoResponse = getTokenSubjectResponse(adminUser);
                tokenInfoResponse.setRoleScreen(screenForRole.get());
            } else {
                throw new RedirectException(CookieConstant.ACCOUNT_NOT_EXIST);
            }
        }

        String subject = new ObjectMapper().writeValueAsString(tokenInfoResponse);
        Map<String, Object> claims = getBodyClaims(tokenInfoResponse);

        String token = Jwts.builder()
                .setSubject(subject)
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(TOKEN_EXP))
                .setIssuer("udpm.hn")
                .signWith(Keys.hmacShaKeyFor(tokenSecret.getBytes()))
                .compact();
        System.out.println("Generated Token: " + token);
        return token;
    }

    public String generateToken(Map<String, Object> claims) throws JsonProcessingException {
        String subject = new ObjectMapper().writeValueAsString(claims);
        return Jwts.builder()
                .setSubject(subject)
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(TOKEN_EXP))
                .setIssuer("udpm.hn")
                .signWith(Keys.hmacShaKeyFor(tokenSecret.getBytes()))
                .compact();
    }

    private TokenInfoResponse getTokenSubjectResponse(Admin admin) throws JsonProcessingException {
        TokenInfoResponse response = new TokenInfoResponse();
        response.setUserId(admin.getId());
        response.setFullName(admin.getDisplayName());
        response.setUserCode(admin.getUsername());
        response.setPictureUrl(""); // Admin doesn't have picture field yet
        response.setEmailFe(admin.getEmail() != null ? admin.getEmail() : "");
        response.setEmailFPT(admin.getEmail() != null ? admin.getEmail() : "");
        response.setRolesCode(List.of(admin.getRole().name()));
        response.setRolesName(List.of(admin.getRole().name()));
        response.setHost(httpServletRequest.getRemoteHost());
        response.setRoleSwitch("true");
        response.setIdFacility(null); // No facility concept

        return response;
    }

    private static Map<String, Object> getBodyClaims(TokenInfoResponse tokenInfoResponse) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", tokenInfoResponse.getUserId());
        claims.put("userName", tokenInfoResponse.getUserName());
        claims.put("userCode", tokenInfoResponse.getUserCode());
        claims.put("fullName", tokenInfoResponse.getFullName());
        claims.put("pictureUrl", tokenInfoResponse.getPictureUrl());
        List<String> rolesCode = tokenInfoResponse.getRolesCode();
        List<String> rolesName = tokenInfoResponse.getRolesName();
        claims.put("rolesCode", rolesCode);
        claims.put("rolesName", rolesName);
        claims.put("host", tokenInfoResponse.getHost());
        claims.put("roleScreen", tokenInfoResponse.getRoleScreen());
        claims.put("roleSwitch", tokenInfoResponse.getRoleSwitch());
        claims.put("idFacility", tokenInfoResponse.getIdFacility());
        claims.put("email", tokenInfoResponse.getEmailFPT() != null ? tokenInfoResponse.getEmailFPT() : tokenInfoResponse.getEmailSV());

        return claims;
    }

    public String getIdFacilityFromToken(String token) {
        Claims claims = getClaimsToken(token);
        String idFacility = claims.get("idFacility", String.class);
        return (idFacility != null && !idFacility.isEmpty()) ? idFacility : null;
    }

    public String getUserIdFromToken(String token) {
        Claims claims = getClaimsToken(token);
        return String.valueOf(claims.get("userId"));
    }

    public String getRolesFromToken(String token) {
        Claims claims = getClaimsToken(token);
        return String.valueOf(claims.get("roleScreen"));
    }

    @SuppressWarnings("unchecked")
    public List<String> getRolesCodesFromToken(String token) {
        Claims claims = getClaimsToken(token);
        System.out.println("Claims from token: " + claims);
        List<String> roles = claims.get("rolesCode", List.class);
        return roles != null ? roles : Collections.emptyList();
    }

    public String getEmailFromToken(String token) {
        try {
            if (token == null || token.isEmpty()) {
                log.warn("Token is null or empty");
                return null;
            }
            
            Claims claims = getClaimsToken(token);
            if (claims == null) {
                log.warn("No claims found in token");
                return null;
            }
            
            // Try to get email from claims
            String email = claims.get("email", String.class);
            if (email != null && !email.isEmpty()) {
                return email;
            }
            
            // Fallback to emailFPT or emailSV if available
            email = claims.get("emailFPT", String.class);
            if (email == null || email.isEmpty()) {
                email = claims.get("emailSV", String.class);
            }
            
            return email;
        } catch (Exception e) {
            log.error("Error getting email from token: {}", e.getMessage());
            return null;
        }
    }

    // Decode JWT token and return claims
    private Claims getClaimsToken(String token) {
        try {
            if (token == null || token.isEmpty()) {
                log.warn("Token is null or empty");
                return null;
            }
            
            return Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(tokenSecret.getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.error("Error parsing JWT token: {}", e.getMessage());
            return null;
        }
    }

    public boolean validateToken(String authToken) {
        if (authToken == null || authToken.isEmpty()) {
            log.warn("Token is null or empty");
            return false;
        }

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(tokenSecret.getBytes()))
                    .build()
                    .parseClaimsJws(authToken)
                    .getBody();

            // Additional validation
            if (claims.getExpiration() == null) {
                log.error("Token has no expiration");
                return false;
            }

            if (claims.getExpiration().before(new Date())) {
                log.error("Token has expired");
                return false;
            }

            // Check if email exists in claims
            String email = claims.get("email", String.class);
            if (email == null || email.isEmpty()) {
                // Try alternative email fields
                email = claims.get("emailFPT", String.class);
                if (email == null || email.isEmpty()) {
                    email = claims.get("emailSV", String.class);
                    if (email == null || email.isEmpty()) {
                        log.error("No email found in token claims");
                        return false;
                    }
                }
            }

            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature: {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token: {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            log.error("JWT token is expired: {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.error("JWT token is unsupported: {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty: {}", ex.getMessage());
        } catch (Exception ex) {
            log.error("Error validating token: {}", ex.getMessage());
        }
        
        return false;
    }

    private Admin getCurrentAdminLogin(String email) {
        Optional<Admin> admin = adminAuthRepository.findByEmailAndStatus(email, EntityStatus.ACTIVE);
        return admin.orElse(null);
    }
}
