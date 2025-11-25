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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import udpm.hn.server.entity.Admin;
import udpm.hn.server.entity.Role;
import udpm.hn.server.infrastructure.core.constant.CookieConstant;
import udpm.hn.server.infrastructure.core.constant.OAuth2Constant;
import udpm.hn.server.infrastructure.core.exception.RedirectException;
import udpm.hn.server.infrastructure.core.security.repository.StaffAuthRepository;
import udpm.hn.server.infrastructure.core.security.repository.StaffRoleAuthRepository;
import udpm.hn.server.infrastructure.core.security.response.TokenInfoResponse;
import udpm.hn.server.infrastructure.core.security.user.UserPrincipal;
import udpm.hn.server.utils.CookieUtils;
import io.jsonwebtoken.Jwts;

// --- [THÊM IMPORT MỚI] ---
import io.jsonwebtoken.io.Decoders;
import java.security.Key;
// -------------------------

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TokenProvider {

    private final String tokenSecret;
    // Sửa thành 8 giờ
    private final long TOKEN_EXP = 8 * 60 * 60 * 1000L; // 8 giờ
    private final long TOKEN_EXP_DATE = System.currentTimeMillis() + TOKEN_EXP;
    private final StaffAuthRepository staffAuthRepository;
    private final HttpServletRequest httpServletRequest;
    private final StaffRoleAuthRepository staffRoleAuthRepository;

    public TokenProvider(@Value("${jwt.secret}") String tokenSecret, // Lấy secret từ config
                         StaffAuthRepository staffAuthRepository,
                         HttpServletRequest httpServletRequest,
                         StaffRoleAuthRepository staffRoleAuthRepository) {
        this.tokenSecret = tokenSecret;
        this.staffAuthRepository = staffAuthRepository;
        this.httpServletRequest = httpServletRequest;
        this.staffRoleAuthRepository = staffRoleAuthRepository;
    }

    // --- [THÊM HÀM MỚI: Đồng bộ "Chìa khoá"] ---
    // (Copy logic từ JwtService.java)
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(tokenSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    // ----------------------------------------

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

            // --- [ĐIỀN THÔNG TIN (Giữ nguyên)] ---
            tokenInfoResponse.setRoleScreen(screenForRole.get());
            tokenInfoResponse.setUserId(userPrincipal.getId());
            tokenInfoResponse.setFullName(userPrincipal.getName());

            Object pictureObj = userPrincipal.getAttribute("picture");
            if (pictureObj != null) {
                tokenInfoResponse.setPictureUrl(pictureObj.toString());
            }

            tokenInfoResponse.setEmailFPT(userPrincipal.getEmail());
            tokenInfoResponse.setEmailSV(userPrincipal.getEmail());

            if (adminUser != null) {
                tokenInfoResponse.setUserCode(adminUser.getCode());
            }

            List<String> roles = userPrincipal.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
            tokenInfoResponse.setRolesCode(roles);
            tokenInfoResponse.setRolesName(roles);
            // --- [KẾT THÚC] ---
        }

        String subject = new ObjectMapper().writeValueAsString(tokenInfoResponse);
        Map<String, Object> claims = getBodyClaims(tokenInfoResponse);

        String token = Jwts.builder()
                .setSubject(subject)
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(TOKEN_EXP_DATE))
                .setIssuer("udpm.hn")
                // ĐÃ SỬA: Dùng "Chìa khoá" đã Base64-Decode
                .signWith(getSignInKey())
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
                .setExpiration(new Date(TOKEN_EXP_DATE))
                .setIssuer("udpm.hn")
                // ĐÃ SỬA: Dùng "Chìa khoá" đã Base64-Decode
                .signWith(getSignInKey())
                .compact();
    }



    private static Map<String, Object> getBodyClaims(TokenInfoResponse tokenInfoResponse) {
        // ... (Hàm này giữ nguyên) ...
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

        String email = tokenInfoResponse.getEmailFPT() != null
                ? tokenInfoResponse.getEmailFPT()
                : tokenInfoResponse.getEmailSV();
        claims.put("email", email);

        return claims;
    }

    // ... (Các hàm get...FromToken giữ nguyên) ...

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
                // ĐÃ SỬA: Dùng "Chìa khoá" đã Base64-Decode
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder()
                    // ĐÃ SỬA: Dùng "Chìa khoá" đã Base64-Decode
                    .setSigningKey(getSignInKey())
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

    // ... (Các hàm còn lại giữ nguyên) ...
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

    public List<Role> getRolesCodesFromToken(String token) {
        Claims claims = getClaimsToken(token);
        System.out.println("Claims từ token: " + claims);
        return (List<Role>) claims.get("rolesCode");
    }

}