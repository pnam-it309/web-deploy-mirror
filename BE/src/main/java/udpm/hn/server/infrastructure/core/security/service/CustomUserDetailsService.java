package udpm.hn.server.infrastructure.core.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import udpm.hn.server.entity.Admin;
import udpm.hn.server.infrastructure.core.constant.EntityStatus;
import udpm.hn.server.infrastructure.core.security.repository.AdminAuthRepository;
import udpm.hn.server.infrastructure.core.security.repository.RoleAuthRepository;
import udpm.hn.server.infrastructure.core.security.user.UserPrincipal;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final AdminAuthRepository adminAuthRepository;
    private final RoleAuthRepository roleAuthRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        log.info("Loading user details for username: {}", username);

        // Find admin by username (can be email or username)
        Optional<Admin> existingAdmin = adminAuthRepository.findByUsernameOrEmailAndStatus(username, username, EntityStatus.ACTIVE);

        if (existingAdmin.isPresent()) {
            Admin admin = existingAdmin.get();
            List<String> roles = roleAuthRepository.findRoleByAdminId(admin.getId());

            log.info("Found admin: {} with roles: {}", admin.getUsername(), roles);

            return adminAuthRepository.findById(admin.getId())
                    .map(adminUser -> UserPrincipal.create(adminUser, roles))
                    .orElseThrow(() -> new UsernameNotFoundException("Admin not found with id: " + admin.getId()));
        }

        log.warn("Admin not found with username: {}", username);
        throw new UsernameNotFoundException("User not found with username: " + username);
    }
}