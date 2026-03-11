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

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;

@Component
@RequiredArgsConstructor
@Transactional
@Order(3)
public class DBGCustomerGenerator implements CommandLineRunner {

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

    @Override
    public void run(String... args) {
        if ("true".equals(isGenerated)) {
            System.out.println("=== Starting DBGCustomerGenerator ===");
            generateCustomerData();
            System.out.println("=== DBGCustomerGenerator Finished ===");
        }
    }

    private void generateCustomerData() {
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



    private void addRoleToCustomer(Customer customer, String roleName) {
        Optional<Role> roleOptional = roleRepository.findByCode(roleName);

        if (roleOptional.isPresent()) {
            Role role = roleOptional.get();
            try {
                customerRepository.insertCustomerRole(customer.getId(), role.getId());
                System.out.println("✅ Role " + roleName + " assigned to customer " + customer.getEmail());
            } catch (Exception e) {
                System.out.println("Role " + roleName + " already exists or could not be assigned to " + customer.getEmail());
            }
        }
    }
}
