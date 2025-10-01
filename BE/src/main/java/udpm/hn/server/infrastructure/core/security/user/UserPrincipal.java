package udpm.hn.server.infrastructure.core.security.user;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import udpm.hn.server.entity.Admin;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Slf4j
public class UserPrincipal implements OAuth2User, UserDetails {
    // User identification
    @Getter
    private final String id;

    @Getter
    private final String email;

    private final String username;

    @Getter
    private final Admin.Role role;

    private String password;

    private final Collection<? extends GrantedAuthority> authorities;

    @Setter
    private Map<String, Object> attributes;

    public UserPrincipal(String id, String email, String username, Admin.Role role, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.role = role;
        this.authorities = authorities;
    }

    public static UserPrincipal create(Admin admin, List<String> roles) {
        // Convert role enum to granted authorities
        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();

        log.info("Creating UserPrincipal for Admin: {} with roles: {}", admin.getUsername(), authorities);

        UserPrincipal userPrincipal = new UserPrincipal(
                admin.getId(),
                admin.getEmail(),
                admin.getUsername(),
                admin.getRole(),
                authorities
        );

        return userPrincipal;
    }

    public static UserPrincipal create(Admin admin, Map<String, Object> attributes, List<String> roles) {
        UserPrincipal userPrincipal = UserPrincipal.create(admin, roles);
        userPrincipal.setAttributes(attributes);
        return userPrincipal;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return String.valueOf(id);
    }
}
