package udpm.hn.server.infrastructure.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import udpm.hn.server.entity.Admin;
import udpm.hn.server.infrastructure.constant.EntityStatus;
import udpm.hn.server.infrastructure.security.repository.RoleAuthRepository;
import udpm.hn.server.infrastructure.security.repository.StaffAuthRepository;
import udpm.hn.server.infrastructure.security.user.UserPrincipal;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final StaffAuthRepository staffAuthRepository;
    private final RoleAuthRepository roleAuthRepository;
    private final udpm.hn.server.repository.CustomerRepository customerRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        System.out.println("EMAIL: " + email);

        log.info("Đã chạy vào tronguser detail servie:{}", email);

        Optional<Admin> exitStaff = staffAuthRepository.findByUsernameAndStatus(email, EntityStatus.ACTIVE);

        if (exitStaff.isPresent()) {
            Admin adminPre = exitStaff.get();
            List<String> roles = roleAuthRepository.findRoleByAdminId(adminPre.getId());
            return staffAuthRepository.findById(adminPre.getId())
                    .map(staff -> UserPrincipal.create(staff, roles))
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with id : " + adminPre.getId()));
        }

        Optional<udpm.hn.server.entity.Customer> customer = customerRepository.findByEmail(email);
        if (customer.isPresent()) {
            return UserPrincipal.create(customer.get(), List.of("CUSTOMER"));
        }

        throw new UsernameNotFoundException("User not found with email: " + email);
    }

}