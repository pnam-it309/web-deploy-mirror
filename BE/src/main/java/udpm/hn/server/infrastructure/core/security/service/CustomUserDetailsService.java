package udpm.hn.server.infrastructure.core.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import udpm.hn.server.entity.Staff;
import udpm.hn.server.infrastructure.core.constant.EntityStatus;
import udpm.hn.server.infrastructure.core.security.repository.RoleAuthRepository;
import udpm.hn.server.infrastructure.core.security.repository.StaffAuthRepository;
import udpm.hn.server.infrastructure.core.security.user.UserPrincipal;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final StaffAuthRepository staffAuthRepository;
    private final RoleAuthRepository roleAuthRepository;


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        System.out.println("EMAIL: " + email);

        log.info("Đã chạy vào tronguser detail servie:{}",email);

        Optional<Staff> exitStaff = staffAuthRepository.findByEmailFptAndStatus(email, EntityStatus.ACTIVE);

        if(exitStaff.isPresent()) {
            Staff staffPre = exitStaff.get();
            List<String> roles = roleAuthRepository.findRoleByStaffId(staffPre.getId());
            return staffAuthRepository.findById(staffPre.getId())
                    .map(staff -> UserPrincipal.create(staff,roles))
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with id : " + staffPre.getId()));
        }

            // Student authentication is not currently supported
        log.warn("Student authentication is not currently supported. Email: {}", email);
        throw new UsernameNotFoundException("User not found with email: " + email);
    }

}