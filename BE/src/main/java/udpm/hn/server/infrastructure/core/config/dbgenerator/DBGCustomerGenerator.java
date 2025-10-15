package udpm.hn.server.infrastructure.core.config.dbgenerator;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import udpm.hn.server.entity.Customer;
import udpm.hn.server.entity.Customerole;
import udpm.hn.server.infrastructure.core.constant.Roles;
import udpm.hn.server.infrastructure.core.config.dbgenerator.repository.*;
import udpm.hn.server.infrastructure.core.constant.EntityStatus;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Transactional
public class DBGCustomerGenerator {

    private final DBGCustomerRepository customerRepository;
    private final DBGRoleRepository roleRepository;
    private final DBGCustomerRoleRepository customerRoleRepository;

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
        System.out.println("=== DBGCustomerGenerator init() called ===");
        System.out.println("isGenerated: " + isGenerated);
        System.out.println("customerEmail: " + customerEmail);
        System.out.println("customerCode: " + customerCode);
        System.out.println("customerName: " + customerName);

        if ("true".equals(isGenerated)) {
            System.out.println("Generator is enabled, calling generateCustomerData()...");
            generateCustomerData();
        } else {
            System.out.println("Generator is disabled, skipping customer data generation");
        }
        System.out.println("=== DBGCustomerGenerator init() finished ===\n");
    }

    private void generateCustomerData() {
        System.out.println("=== Starting generateCustomerData ===");
        System.out.println("Checking if we can access Customerole repository...");

        try {
            // Try to perform a simple count query to check if table exists
            System.out.println("Attempting to count Customerole records...");
            long count = customerRoleRepository.count();
            System.out.println("✅ Customerole table exists! Current count: " + count);
        } catch (Exception e) {
            System.out.println("❌ Customerole table may not exist or is not accessible");
            System.out.println("Error: " + e.getMessage());
            System.out.println("This suggests the 'customer_role' table is missing from the database.");
            System.out.println("Please ensure JPA/Hibernate DDL auto-creation is enabled or create the table manually.");
        }

        // Ensure roles exist before creating customer
        ensureRolesExist();

        Optional<Customer> customerOptional = customerRepository.findByEmail(customerEmail);
        Customer customer;
        if (customerOptional.isEmpty()) {
            System.out.println("Customer not found, creating new customer...");
            customer = new Customer();
            customer.setEmail(customerEmail);
            customer.setCode(customerCode);
            customer.setName(customerName);
            customer.setStatus(EntityStatus.ACTIVE);

            try {
                customer = customerRepository.save(customer);
                System.out.println("Customer created successfully!");
                System.out.println("Customer ID: " + customer.getId());
                System.out.println("Customer Email: " + customer.getEmail());
            } catch (Exception e) {
                System.out.println("ERROR: Failed to save Customer to database!");
                System.out.println("Error message: " + e.getMessage());
                e.printStackTrace();
                return; // Exit if customer creation fails
            }
        } else {
            customer = customerOptional.get();
            System.out.println("Customer already exists:");
            System.out.println("Customer ID: " + customer.getId());
            System.out.println("Customer Email: " + customer.getEmail());
        }

        addRoleToCustomer(customer, Roles.CUSTOMER.name());
        System.out.println("=== Finished generateCustomerData ===\n");
    }

    private void ensureRolesExist() {
        System.out.println("=== Ensuring roles exist ===");

        // Check and create CUSTOMER role
        String roleCode = Roles.CUSTOMER.name();
        String roleName = "Customer";

        System.out.println("Checking if role exists: " + roleCode);
        Optional<udpm.hn.server.entity.Role> roleOptional = roleRepository.findByCode(roleCode);

        if (roleOptional.isEmpty()) {
            System.out.println("Creating role: " + roleCode + " - " + roleName);
            udpm.hn.server.entity.Role role = new udpm.hn.server.entity.Role();
            role.setCode(roleCode);
            role.setName(roleName);
            role.setStatus(EntityStatus.ACTIVE);

            try {
                udpm.hn.server.entity.Role savedRole = roleRepository.save(role);
                System.out.println("✅ Role created successfully: " + savedRole.getName() + " (ID: " + savedRole.getId() + ")");
            } catch (Exception e) {
                System.out.println("❌ ERROR: Failed to create role " + roleCode);
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("✅ Role already exists: " + roleCode + " (ID: " + roleOptional.get().getId() + ")");
        }

        System.out.println("=== Finished ensuring roles exist ===\n");
    }

    private void addRoleToCustomer(Customer customer, String roleName) {
        System.out.println("=== Starting addRoleToCustomer ===");
        System.out.println("Customer ID: " + customer.getId());
        System.out.println("Customer Email: " + customer.getEmail());
        System.out.println("Role Name: " + roleName);

        Optional<udpm.hn.server.entity.Role> roleOptional = roleRepository.findByCode(roleName);

        if (roleOptional.isPresent()) {
            udpm.hn.server.entity.Role role = roleOptional.get();
            System.out.println("Found role: " + role.getName() + " (ID: " + role.getId() + ")");

            // Kiểm tra xem customer đã có role này chưa
            boolean exists = customerRoleRepository.existsByCustomerAndRole(customer, role);
            System.out.println("Customer already has role: " + exists);

            if (!exists) {
                System.out.println("Creating new Customerole...");
                Customerole customerRole = new Customerole();
                customerRole.setCustomer(customer);
                customerRole.setRole(role);
                customerRole.setStatus(EntityStatus.ACTIVE);

                try {
                    Customerole savedCustomerRole = customerRoleRepository.save(customerRole);
                    System.out.println("Customerole saved successfully!");
                    System.out.println("Customerole ID: " + savedCustomerRole.getId());
                    System.out.println("Customerole Status: " + savedCustomerRole.getStatus());

                    // Verify it was actually saved by trying to find it
                    System.out.println("Verifying Customerole was saved...");
                    boolean verifyExists = customerRoleRepository.existsByCustomerAndRole(customer, role);
                    System.out.println("Verification - Customerole exists in DB: " + verifyExists);

                    if (verifyExists) {
                        System.out.println("✅ Customerole successfully persisted to database!");
                    } else {
                        System.out.println("❌ WARNING: Customerole not found in database after save!");
                    }

                } catch (Exception e) {
                    System.out.println("ERROR: Failed to save Customerole to database!");
                    System.out.println("Error message: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                System.out.println("Customer already has this role, skipping creation");
            }
        } else {
            System.out.println("ERROR: Role not found: " + roleName);
        }
        System.out.println("=== Finished addRoleToCustomer ===\n");
    }
}
