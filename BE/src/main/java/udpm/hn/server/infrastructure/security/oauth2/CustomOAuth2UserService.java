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

    private void assignCustomerRole(Customer customer) {
        try {
            Optional<Role> customerRoleOptional = roleAuthRepository.findByName(Roles.CUSTOMER.name());
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
            List<String> roleUser = admin.getRoles().stream().map(Role::getName).collect(Collectors.toList());

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
            customer.setStatus(EntityStatus.ACTIVE);
            customer = customerAuthRepository.save(customer);
            assignCustomerRole(customer);
        }

        List<String> roleUser = customer.getRoles().stream().map(Role::getName).collect(Collectors.toList());

        if (roleUser.contains(role)) {
            return UserPrincipal.create(customer, oAuth2UserInfo.getAttributes(), roleUser);
        }

        CookieUtils.addCookie(httpServletResponse, CookieConstant.ACCOUNT_NOT_EXIST, CookieConstant.ACCOUNT_NOT_EXIST);
        throw new OAuth2AuthenticationProcessingException(CookieConstant.ACCOUNT_NOT_EXIST);
    }
}
