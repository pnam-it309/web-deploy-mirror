package udpm.hn.server.infrastructure.core.security.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import udpm.hn.server.entity.Admin;
import udpm.hn.server.infrastructure.core.constant.CookieConstant;
import udpm.hn.server.infrastructure.core.constant.OAuth2Constant;
import udpm.hn.server.infrastructure.core.exception.RedirectException;
import udpm.hn.server.infrastructure.core.security.repository.StaffAuthRepository;
import udpm.hn.server.infrastructure.core.security.repository.StaffRoleAuthRepository;
import udpm.hn.server.infrastructure.core.security.response.TokenInfoResponse;
import udpm.hn.server.infrastructure.core.security.user.UserPrincipal;
import udpm.hn.server.utils.CookieUtils;
import io.jsonwebtoken.Jwts;

import java.util.*;

@Service
@Slf4j
public class TokenProvider {

    private final String tokenSecret;
    private final long TOKEN_EXP = System.currentTimeMillis() + 2 * 60 * 60 * 100000;
    private final StaffAuthRepository staffAuthRepository;
    private final HttpServletRequest httpServletRequest;
    private final StaffRoleAuthRepository staffRoleAuthRepository;

    public TokenProvider(String tokenSecret, 
                        StaffAuthRepository staffAuthRepository, 
                        HttpServletRequest httpServletRequest, 
                        StaffRoleAuthRepository staffRoleAuthRepository) {
        this.tokenSecret = tokenSecret;
        this.staffAuthRepository = staffAuthRepository;
        this.httpServletRequest = httpServletRequest;
        this.staffRoleAuthRepository = staffRoleAuthRepository;
    }

    public String createToken(Authentication authentication) throws BadRequestException, JsonProcessingException {

        log.info("Đã chạy vào tạo token :{}", authentication.toString());

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Optional<String> screenForRole = CookieUtils.getCookie(httpServletRequest, OAuth2Constant.SCREEN_FOR_ROLE_COOKIE_NAME)
                .map(Cookie::getValue);

        TokenInfoResponse tokenInfoResponse = new TokenInfoResponse();

        if (screenForRole.isEmpty()) {
            throw new RedirectException(CookieConstant.ACCOUNT_NOT_EXIST);
        } else {
            Admin adminUser = getCurrentStaffLogin(userPrincipal.getEmail());

            if (adminUser != null) {
                // Populate all user information in the token
                tokenInfoResponse.setUserId(adminUser.getId());
                tokenInfoResponse.setUserCode(adminUser.getCode());
                tokenInfoResponse.setUserName(adminUser.getName());
                tokenInfoResponse.setFullName(adminUser.getName());
                tokenInfoResponse.setEmail(adminUser.getEmail());
                tokenInfoResponse.setPictureUrl(adminUser.getPicture());
                tokenInfoResponse.setRoleScreen(screenForRole.get());

                // Set roles information
                List<String> rolesCode = new ArrayList<>();
                List<String> rolesName = new ArrayList<>();
                // Add default roles based on screen role
                if ("ADMIN".equals(screenForRole.get())) {
                    rolesCode.add("ADMIN");
                    rolesName.add("ADMIN");
                } else if ("CUSTOMER".equals(screenForRole.get())) {
                    rolesCode.add("CUSTOMER");
                    rolesName.add("CUSTOMER");
                }
                tokenInfoResponse.setRolesCode(rolesCode);
                tokenInfoResponse.setRolesName(rolesName);
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



    private static Map<String, Object> getBodyClaims(TokenInfoResponse tokenInfoResponse) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", tokenInfoResponse.getUserId());
        claims.put("sub", tokenInfoResponse.getUserId());
        claims.put("name", tokenInfoResponse.getFullName());
        claims.put("picture", tokenInfoResponse.getPictureUrl());
        List<String> rolesCode = tokenInfoResponse.getRolesCode();
        List<String> rolesName = tokenInfoResponse.getRolesName();
        claims.put("rolesCode", rolesCode);
        claims.put("rolesName", rolesName);
        claims.put("host", tokenInfoResponse.getHost());
        claims.put("roleScreen", tokenInfoResponse.getRoleScreen());
        claims.put("roleSwitch", tokenInfoResponse.getRoleSwitch());
        claims.put("email", tokenInfoResponse.getEmail());

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

    public List<udpm.hn.server.entity.Role> getRolesCodesFromToken(String token) {
        Claims claims = getClaimsToken(token);
        System.out.println("Claims từ token: " + claims);
        return (List<udpm.hn.server.entity.Role>) claims.get("rolesCode");
    }

    public String getEmailFromToken(String token) {
        Claims claims = getClaimsToken(token);
        String email = claims.get("email", String.class);
        if (email != null && !email.isEmpty()) {
            return email;
        }
        return claims.get("email", String.class);
    }

    // giải mã
    private Claims getClaimsToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(tokenSecret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(tokenSecret.getBytes()))
                    .build()
                    .parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }

    private Admin getCurrentStaffLogin(String email) {
        Optional<Admin> staffFPT = staffAuthRepository.findByEmail(email);
        return staffFPT.orElse(null);
    }


    // Constructor injection is handled by @RequiredArgsConstructor

}
