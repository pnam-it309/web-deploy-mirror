package udpm.hn.server.infrastructure.security.oauth2;

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
import udpm.hn.server.entity.Customer;
import udpm.hn.server.entity.Role;
import udpm.hn.server.infrastructure.constant.CookieConstant;
import udpm.hn.server.infrastructure.constant.EntityStatus;
import udpm.hn.server.infrastructure.constant.OAuth2Constant;
import udpm.hn.server.infrastructure.constant.Roles;
import udpm.hn.server.infrastructure.exception.OAuth2AuthenticationProcessingException;
import udpm.hn.server.infrastructure.security.oauth2.user.OAuth2UserInfo;
import udpm.hn.server.infrastructure.security.oauth2.user.OAuth2UserInfoFactory;
import udpm.hn.server.infrastructure.security.repository.CustomerAuthRepository;
import udpm.hn.server.infrastructure.security.repository.RoleAuthRepository;
import udpm.hn.server.infrastructure.security.repository.StaffAuthRepository;
import udpm.hn.server.infrastructure.security.user.UserPrincipal;
import udpm.hn.server.utils.CookieUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final HttpServletRequest httpServletRequest;
    private final HttpServletResponse httpServletResponse;
    private final RoleAuthRepository roleAuthRepository;
    private final StaffAuthRepository staffAuthRepository;
    private final CustomerAuthRepository customerAuthRepository;

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
            String userName = processedUser.getAttribute("name") != null ? processedUser.getAttribute("name")
                    : processedUser.getAttribute("email");
            log.info("Successfully processed OAuth2 user: {}", userName);
            return processedUser;
        } catch (AuthenticationException ex) {
            log.error("Authentication error in OAuth2 user processing", ex);
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected error in OAuth2 user processing", ex);
            throw new InternalAuthenticationServiceException(
                    "An unexpected error occurred during OAuth2 authentication", ex);
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        log.info("Processing OAuth2 user");

        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory
                .getOAuth2UserInfo(
                        oAuth2UserRequest.getClientRegistration().getRegistrationId(),
                        oAuth2User.getAttributes());

        if (oAuth2UserInfo.getEmail() == null || oAuth2UserInfo.getEmail().isBlank()) {
            CookieUtils.addCookie(httpServletResponse, CookieConstant.ACCOUNT_NOT_EXIST,
                    CookieConstant.ACCOUNT_NOT_EXIST);
            throw new OAuth2AuthenticationProcessingException(CookieConstant.ACCOUNT_NOT_EXIST);
        }

        // Special handling for Gmail for easier testing
        if (oAuth2UserInfo.getEmail().toLowerCase().endsWith("@gmail.com")) {
            return this.processGmailUser(oAuth2UserInfo);
        }

        Optional<Cookie> cookieOpRole = CookieUtils.getCookie(httpServletRequest,
                OAuth2Constant.SCREEN_FOR_ROLE_COOKIE_NAME);

        if (cookieOpRole.isPresent()) {
            String roleValue = cookieOpRole.get().getValue();
            if (OAuth2Constant.ROLE_ADMIN.equals(roleValue)) {
                return this.processAdmin(oAuth2UserInfo, OAuth2Constant.ROLE_ADMIN);
            } else if (OAuth2Constant.ROLE_CUSTOMER.equals(roleValue)) {
                return this.processCustomer(oAuth2UserInfo, OAuth2Constant.ROLE_CUSTOMER);
            }
        }

        CookieUtils.addCookie(httpServletResponse, CookieConstant.ACCOUNT_NOT_EXIST, CookieConstant.ACCOUNT_NOT_EXIST);
        throw new OAuth2AuthenticationProcessingException(CookieConstant.ACCOUNT_NOT_EXIST);
    }

    private OAuth2User processGmailUser(OAuth2UserInfo oAuth2UserInfo) {
        log.info("Processing Gmail user for dual roles: {}", oAuth2UserInfo.getEmail());

        // 1. Ensure Customer exists and is active
        Optional<Customer> customerOptional = customerAuthRepository.findByEmailAndStatus(
                oAuth2UserInfo.getEmail(),
                EntityStatus.ACTIVE);

        Customer customer;
        if (customerOptional.isPresent()) {
            customer = customerOptional.get();
        } else {
            customer = new Customer();
            customer.setEmail(oAuth2UserInfo.getEmail());
            customer.setFullName(oAuth2UserInfo.getName());
            customer.setPassword(java.util.UUID.randomUUID().toString());
            customer.setAvatar(oAuth2UserInfo.getImageUrl());
            customer.setStatus(EntityStatus.ACTIVE);
            customer = customerAuthRepository.save(customer);
            assignCustomerRole(customer);
        }

        // 2. Ensure Admin exists and is active
        Optional<Admin> adminOptional = staffAuthRepository.findByUsernameAndStatus(
                oAuth2UserInfo.getEmail(),
                EntityStatus.ACTIVE);

        Admin admin;
        if (adminOptional.isPresent()) {
            admin = adminOptional.get();
        } else {
            admin = new Admin();
            admin.setUsername(oAuth2UserInfo.getEmail());
            admin.setFullName(oAuth2UserInfo.getName());
            admin.setPassword(java.util.UUID.randomUUID().toString());
            admin.setStatus(EntityStatus.ACTIVE);
            
            // Assign ADMIN role
            Optional<Role> adminRoleOptional = roleAuthRepository.findByCode(Roles.ADMIN.name());
            if (adminRoleOptional.isPresent()) {
                admin.getRoles().add(adminRoleOptional.get());
            }
            admin = staffAuthRepository.save(admin);
        }

        // 3. Combine roles
        List<String> combinedRoles = new java.util.ArrayList<>();
        combinedRoles.addAll(customer.getRoles().stream().map(Role::getCode).toList());
        combinedRoles.addAll(admin.getRoles().stream().map(Role::getCode).toList());
        combinedRoles = combinedRoles.stream().distinct().collect(Collectors.toList());

        // Add default role codes if missing
        if (!combinedRoles.contains(OAuth2Constant.ROLE_ADMIN)) combinedRoles.add(OAuth2Constant.ROLE_ADMIN);
        if (!combinedRoles.contains(OAuth2Constant.ROLE_CUSTOMER)) combinedRoles.add(OAuth2Constant.ROLE_CUSTOMER);

        return UserPrincipal.create(admin, oAuth2UserInfo.getAttributes(), combinedRoles);
    }

    private void assignCustomerRole(Customer customer) {
        try {
            Optional<Role> customerRoleOptional = roleAuthRepository.findByCode(Roles.CUSTOMER.name());
            if (customerRoleOptional.isPresent()) {
                customer.getRoles().add(customerRoleOptional.get());
                customerAuthRepository.save(customer);
            } else {
                throw new OAuth2AuthenticationProcessingException("CUSTOMER role not configured");
            }
        } catch (Exception e) {
            log.error("Error assigning role: {}", e.getMessage(), e);
            throw new OAuth2AuthenticationProcessingException("Error assigning role");
        }
    }

    private OAuth2User processAdmin(OAuth2UserInfo oAuth2UserInfo, String role) {
        Optional<Admin> staffOptional = staffAuthRepository.findByUsernameAndStatus(
                oAuth2UserInfo.getEmail(),
                EntityStatus.ACTIVE);

        if (staffOptional.isPresent()) {
            Admin admin = staffOptional.get();
            List<String> roleUser = admin.getRoles().stream().map(Role::getCode).collect(Collectors.toList());

            if (roleUser.contains(role)) {
                staffAuthRepository.save(admin);
                return UserPrincipal.create(admin, oAuth2UserInfo.getAttributes(), roleUser);
            }
        }

        CookieUtils.addCookie(httpServletResponse, CookieConstant.ACCOUNT_NOT_EXIST, CookieConstant.ACCOUNT_NOT_EXIST);
        throw new OAuth2AuthenticationProcessingException(CookieConstant.ACCOUNT_NOT_EXIST);
    }

    private OAuth2User processCustomer(OAuth2UserInfo oAuth2UserInfo, String role) {
        Optional<Customer> customerOptional = customerAuthRepository.findByEmailAndStatus(
                oAuth2UserInfo.getEmail(),
                EntityStatus.ACTIVE);

        Customer customer;
        if (customerOptional.isPresent()) {
            customer = customerOptional.get();
            customer.setFullName(oAuth2UserInfo.getName());
            customerAuthRepository.save(customer);
        } else {
            customer = new Customer();
            customer.setEmail(oAuth2UserInfo.getEmail());
            customer.setFullName(oAuth2UserInfo.getName());
            customer.setPassword(java.util.UUID.randomUUID().toString()); // Set dummy password
            customer.setAvatar(oAuth2UserInfo.getImageUrl());
            customer.setStatus(EntityStatus.ACTIVE);
            customer = customerAuthRepository.save(customer);
            assignCustomerRole(customer);
        }

        List<String> roleUser = customer.getRoles().stream().map(Role::getCode).collect(Collectors.toList());

        if (roleUser.contains(role)) {
            return UserPrincipal.create(customer, oAuth2UserInfo.getAttributes(), roleUser);
        }

        CookieUtils.addCookie(httpServletResponse, CookieConstant.ACCOUNT_NOT_EXIST, CookieConstant.ACCOUNT_NOT_EXIST);
        throw new OAuth2AuthenticationProcessingException(CookieConstant.ACCOUNT_NOT_EXIST);
    }
}
