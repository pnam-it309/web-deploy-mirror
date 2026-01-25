package udpm.hn.server.infrastructure.config.dbgenerator;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import udpm.hn.server.entity.Customer;
import udpm.hn.server.entity.Role;
import udpm.hn.server.infrastructure.constant.Roles;
import udpm.hn.server.infrastructure.config.dbgenerator.repository.DBGCustomerRepository;
import udpm.hn.server.infrastructure.config.dbgenerator.repository.DBGRoleRepository;
import udpm.hn.server.infrastructure.constant.EntityStatus;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Transactional
public class DBGCustomerGenerator {

    private final DBGCustomerRepository customerRepository;
    private final DBGRoleRepository roleRepository;

    @Value("${db.generator.customer.is-generated}")
    private String isGenerated;

    @Value("${db.generator.customer.email}")
    private String customerEmail;

    @Value("${db.generator.customer.code}")
    private String customerCode;

    @Value("${db.generator.customer.name}")
    private String customerName;

    @PostConstruct
    public void init() {
        if ("true".equals(isGenerated)) {
            generateCustomerData();
        }
    }

    private void generateCustomerData() {
        ensureRolesExist();

        Optional<Customer> customerOptional = customerRepository.findByEmail(customerEmail);
        Customer customer;
        if (customerOptional.isEmpty()) {
            customer = new Customer();
            customer.setEmail(customerEmail);

            customer.setFullName(customerName);
            customer.setPassword(
                    new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode("123456"));
            customer.setStatus(EntityStatus.ACTIVE);

            try {
                customer = customerRepository.save(customer);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        } else {
            customer = customerOptional.get();
        }

        addRoleToCustomer(customer, Roles.CUSTOMER.name());
    }

    private void ensureRolesExist() {
        String roleCode = Roles.CUSTOMER.name();
        String roleName = "Customer";

        if (roleRepository.findByCode(roleCode).isEmpty()) {
            Role role = new Role();
            role.setCode(roleCode);
            role.setName(roleName);
            role.setStatus(EntityStatus.ACTIVE);
            roleRepository.save(role);
        }
    }

    private void addRoleToCustomer(Customer customer, String roleName) {
        Optional<Role> roleOptional = roleRepository.findByCode(roleName);

        if (roleOptional.isPresent()) {
            Role role = roleOptional.get();
            boolean exists = customer.getRoles().stream().anyMatch(r -> r.getCode().equals(roleName));

            if (!exists) {
                customer.getRoles().add(role);
                customerRepository.save(customer);
                System.out.println("Role " + roleName + " added to customer " + customer.getEmail());
            }
        }
    }
}
