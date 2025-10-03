-- Insert default roles
INSERT INTO roles (id, code, name, description, status, created_date, last_modified_date)
VALUES
    ('role-admin-001', 'ADMIN', 'Administrator', 'Full system administrator with all permissions', 1, NOW(), NOW()),
    ('role-customer-001', 'CUSTOMER', 'Customer', 'Regular customer with limited permissions', 1, NOW(), NOW())
ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    description = VALUES(description),
    status = VALUES(status);

-- Insert default admin user (password: 123456)
INSERT INTO admins (id, username, password_hash, display_name, email, role, status, created_date, last_modified_date)
VALUES
    ('admin-default-001', 'admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeEw7dUtKKOCmWomc8GefOumhU8cVt6K', 'Default Administrator', 'admin@udpm.com', 0, 1, NOW(), NOW())
ON DUPLICATE KEY UPDATE
    display_name = VALUES(display_name),
    email = VALUES(email),
    role = VALUES(role),
    status = VALUES(status);

-- Assign admin role to default admin user
INSERT INTO admin_roles (admin_id, role_id, status, created_date, last_modified_date)
VALUES
    ('admin-default-001', 'role-admin-001', 1, NOW(), NOW())
ON DUPLICATE KEY UPDATE
    status = VALUES(status);
