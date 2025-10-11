package udpm.hn.server.infrastructure.core.security.oauth2;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import udpm.hn.server.entity.Admin;
import udpm.hn.server.infrastructure.core.constant.CookieConstant;
import udpm.hn.server.infrastructure.core.constant.EntityStatus;
import udpm.hn.server.infrastructure.core.constant.OAuth2Constant;
import udpm.hn.server.infrastructure.core.exception.OAuth2AuthenticationProcessingException;
import udpm.hn.server.infrastructure.core.security.oauth2.user.OAuth2UserInfo;
import udpm.hn.server.infrastructure.core.security.oauth2.user.OAuth2UserInfoFactory;
import udpm.hn.server.infrastructure.core.security.repository.RoleAuthRepository;
import udpm.hn.server.infrastructure.core.security.repository.StaffAuthRepository;
import udpm.hn.server.infrastructure.core.security.user.UserPrincipal;
import udpm.hn.server.utils.CookieUtils;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final HttpServletRequest httpServletRequest;
    private final HttpServletResponse httpServletResponse;
    private final RoleAuthRepository roleAuthRepository;
    private final StaffAuthRepository staffAuthRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        log.info("OAuth2 user loaded, attributes: {}", oAuth2User.getAttributes());

        try {
            OAuth2User processedUser = processOAuth2User(oAuth2UserRequest, oAuth2User);
            if (processedUser == null) {
                log.error("Failed to process OAuth2 user: processOAuth2User returned null");
                throw new OAuth2AuthenticationProcessingException("Failed to process OAuth2 user");
            }
            String userName = processedUser.getAttribute("name") != null ?
                    processedUser.getAttribute("name") :
                    processedUser.getAttribute("email");
            log.info("Successfully processed OAuth2 user: {}", userName);
            return processedUser;
        } catch (AuthenticationException ex) {
            log.error("Authentication error in OAuth2 user processing", ex);
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected error in OAuth2 user processing", ex);
            throw new InternalAuthenticationServiceException("An unexpected error occurred during OAuth2 authentication", ex);
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        log.info("Processing OAuth2 user");
        log.info("Client Name: {}", oAuth2UserRequest.getClientRegistration().getClientName());
        log.info("OAuth2User attributes: {}", oAuth2User.getAttributes());

        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory
                .getOAuth2UserInfo(
                        oAuth2UserRequest.getClientRegistration().getRegistrationId(),
                        oAuth2User.getAttributes()
                );

        log.info("Extracted user email from OAuth2UserInfo: {}", oAuth2UserInfo.getEmail());

        if (oAuth2UserInfo.getEmail() == null || oAuth2UserInfo.getEmail().isBlank()) {
            log.warn("Email is null or blank, adding failure cookie and throwing exception.");
            CookieUtils.addCookie(httpServletResponse, CookieConstant.ACCOUNT_NOT_EXIST, CookieConstant.ACCOUNT_NOT_EXIST);
            throw new OAuth2AuthenticationProcessingException(CookieConstant.ACCOUNT_NOT_EXIST);
        }

        // Only handle admin and customer roles
        Optional<Cookie> cookieOpRole = CookieUtils.getCookie(httpServletRequest, OAuth2Constant.SCREEN_FOR_ROLE_COOKIE_NAME);

        if (cookieOpRole.isPresent()) {
            String roleValue = cookieOpRole.get().getValue();
            log.info("Role cookie found: {}", roleValue);

            if (OAuth2Constant.ROLE_ADMIN.equals(roleValue)) {
                log.info("Processing as ADMIN");
                return this.processAdmin(oAuth2UserInfo, OAuth2Constant.ROLE_ADMIN);
            } else if (OAuth2Constant.ROLE_CUSTOMER.equals(roleValue)) {
                log.info("Processing as CUSTOMER");
                return this.processCustomer(oAuth2UserInfo, OAuth2Constant.ROLE_CUSTOMER);
            } else {
                log.warn("Unsupported role: {}", roleValue);
            }
        } else {
            log.warn("Role cookie not found.");
        }

        log.warn("Invalid login attempt, adding failure cookie and throwing exception.");
        CookieUtils.addCookie(httpServletResponse, CookieConstant.ACCOUNT_NOT_EXIST, CookieConstant.ACCOUNT_NOT_EXIST);
        throw new OAuth2AuthenticationProcessingException(CookieConstant.ACCOUNT_NOT_EXIST);
    }

    private OAuth2User processAdmin(OAuth2UserInfo oAuth2UserInfo, String role) {
        log.info("Processing admin user: {}", oAuth2UserInfo.getEmail());

        Optional<Admin> staffOptional = staffAuthRepository.findByEmailAndStatus(
                oAuth2UserInfo.getEmail(),
                EntityStatus.ACTIVE
        );

        if (staffOptional.isPresent()) {
            List<String> roleUser = roleAuthRepository.findRoleByStaffId(staffOptional.get().getId());

            if (roleUser.contains(role)) {
                Admin admin = staffOptional.get();
                // Update staff information
                admin.setPicture(oAuth2UserInfo.getImageUrl());
                staffAuthRepository.save(admin);
                return UserPrincipal.create(admin, oAuth2UserInfo.getAttributes(), roleUser);
            } else {
                log.warn("User {} does not have required role: {}", oAuth2UserInfo.getEmail(), role);
                CookieUtils.addCookie(httpServletResponse, CookieConstant.ACCOUNT_NOT_EXIST, CookieConstant.ACCOUNT_NOT_EXIST);
                throw new OAuth2AuthenticationProcessingException(CookieConstant.ACCOUNT_NOT_EXIST);
            }
        } else {
            log.warn("Admin user not found: {}", oAuth2UserInfo.getEmail());
            CookieUtils.addCookie(httpServletResponse, CookieConstant.ACCOUNT_NOT_EXIST, CookieConstant.ACCOUNT_NOT_EXIST);
            throw new OAuth2AuthenticationProcessingException(CookieConstant.ACCOUNT_NOT_EXIST);
        }
    }

    private OAuth2User processCustomer(OAuth2UserInfo oAuth2UserInfo, String role) {
        log.info("Processing customer user: {}", oAuth2UserInfo.getEmail());

        Optional<Admin> staffOptional = staffAuthRepository.findByEmailAndStatus(
                oAuth2UserInfo.getEmail(),
                EntityStatus.ACTIVE
        );

        if (staffOptional.isPresent()) {
            List<String> roleUser = roleAuthRepository.findRoleByStaffId(staffOptional.get().getId());

            if (roleUser.contains(role)) {
                Admin admin = staffOptional.get();
                // Update staff information
                admin.setPicture(oAuth2UserInfo.getImageUrl());
                staffAuthRepository.save(admin);
                return UserPrincipal.create(admin, oAuth2UserInfo.getAttributes(), roleUser);
            } else {
                log.warn("User {} does not have required role: {}", oAuth2UserInfo.getEmail(), role);
                CookieUtils.addCookie(httpServletResponse, CookieConstant.ACCOUNT_NOT_EXIST, CookieConstant.ACCOUNT_NOT_EXIST);
                throw new OAuth2AuthenticationProcessingException(CookieConstant.ACCOUNT_NOT_EXIST);
            }
        } else {
            log.warn("Customer user not found: {}", oAuth2UserInfo.getEmail());
            CookieUtils.addCookie(httpServletResponse, CookieConstant.ACCOUNT_NOT_EXIST, CookieConstant.ACCOUNT_NOT_EXIST);
            throw new OAuth2AuthenticationProcessingException(CookieConstant.ACCOUNT_NOT_EXIST);
        }
    }
}
