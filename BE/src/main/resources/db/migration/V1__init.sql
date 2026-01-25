-- Init Schema for FPL UDPM Catalog

-- ----------------------------
-- Standard Tables from Entities
-- ----------------------------

CREATE TABLE IF NOT EXISTS roles (
    id VARCHAR(36) PRIMARY KEY,
    created_at BIGINT,
    last_modified_date BIGINT,
    created_by VARCHAR(255),
    last_modified_by VARCHAR(255),
    status INT,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS admins (
    id VARCHAR(36) PRIMARY KEY,
    created_at BIGINT,
    last_modified_date BIGINT,
    created_by VARCHAR(255),
    last_modified_by VARCHAR(255),
    status INT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS admin_roles (
    admin_id VARCHAR(36) NOT NULL,
    role_id VARCHAR(36) NOT NULL,
    PRIMARY KEY (admin_id, role_id),
    CONSTRAINT fk_ar_admin FOREIGN KEY (admin_id) REFERENCES admins(id),
    CONSTRAINT fk_ar_role FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE IF NOT EXISTS customers (
    id VARCHAR(36) PRIMARY KEY,
    created_at BIGINT,
    last_modified_date BIGINT,
    created_by VARCHAR(255),
    last_modified_by VARCHAR(255),
    status INT,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(255),
    phone VARCHAR(20),
    avatar VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS customer_roles (
    customer_id VARCHAR(36) NOT NULL,
    role_id VARCHAR(36) NOT NULL,
    PRIMARY KEY (customer_id, role_id),
    CONSTRAINT fk_cr_customer FOREIGN KEY (customer_id) REFERENCES customers(id),
    CONSTRAINT fk_cr_role FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE IF NOT EXISTS domains (
    id VARCHAR(36) PRIMARY KEY,
    created_at BIGINT,
    last_modified_date BIGINT,
    created_by VARCHAR(255),
    last_modified_by VARCHAR(255),
    status INT,
    version BIGINT,
    name VARCHAR(255) NOT NULL,
    slug VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    icon VARCHAR(255),
    color VARCHAR(50),
    parent_id VARCHAR(36),
    sort_order INT
);

CREATE TABLE IF NOT EXISTS technologies (
    id VARCHAR(36) PRIMARY KEY,
    created_at BIGINT,
    last_modified_date BIGINT,
    created_by VARCHAR(255),
    last_modified_by VARCHAR(255),
    status INT,
    version BIGINT,
    name VARCHAR(255) NOT NULL UNIQUE,
    icon VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS apps (
    id VARCHAR(36) PRIMARY KEY,
    created_at BIGINT,
    last_modified_date BIGINT,
    created_by VARCHAR(255),
    last_modified_by VARCHAR(255),
    status INT,
    version BIGINT,
    domain_id VARCHAR(36) NOT NULL,
    name VARCHAR(255) NOT NULL,
    sku VARCHAR(50),
    short_description VARCHAR(500),
    thumbnail VARCHAR(255),
    view_count BIGINT DEFAULT 0,
    is_featured BOOLEAN DEFAULT FALSE,
    homepage_sort_order INT,
    is_featured_video BOOLEAN DEFAULT FALSE,
    approval_status VARCHAR(50),
    meta_title VARCHAR(100),
    meta_description VARCHAR(300),
    meta_keywords VARCHAR(200),
    og_image VARCHAR(500),
    deleted_at BIGINT,
    publish_at BIGINT,
    CONSTRAINT fk_app_domain FOREIGN KEY (domain_id) REFERENCES domains(id)
);

CREATE TABLE IF NOT EXISTS app_technologies (
    app_id VARCHAR(36) NOT NULL,
    technology_id VARCHAR(36) NOT NULL,
    PRIMARY KEY (app_id, technology_id),
    CONSTRAINT fk_at_app FOREIGN KEY (app_id) REFERENCES apps(id),
    CONSTRAINT fk_at_tech FOREIGN KEY (technology_id) REFERENCES technologies(id)
);

CREATE TABLE IF NOT EXISTS app_details (
    id VARCHAR(36) PRIMARY KEY,
    created_at BIGINT,
    last_modified_date BIGINT,
    created_by VARCHAR(255),
    last_modified_by VARCHAR(255),
    status INT,
    app_id VARCHAR(36) NOT NULL,
    long_description TEXT,
    demo_url VARCHAR(500),
    source_url VARCHAR(500),
    specifications TEXT,
    CONSTRAINT fk_ad_app FOREIGN KEY (app_id) REFERENCES apps(id)
);

CREATE TABLE IF NOT EXISTS two_factor_auth (
    id VARCHAR(255) PRIMARY KEY,
    created_at BIGINT,
    created_by VARCHAR(255),
    last_modified_date BIGINT,
    last_modified_by VARCHAR(255),
    admin_id VARCHAR(255) NOT NULL UNIQUE,
    secret VARCHAR(500) NOT NULL,
    backup_codes TEXT,
    enabled BOOLEAN NOT NULL DEFAULT FALSE,
    verified_at BIGINT,
    CONSTRAINT fk_2fa_admin FOREIGN KEY (admin_id) REFERENCES admins(id) ON DELETE CASCADE
);

CREATE INDEX idx_2fa_admin_id ON two_factor_auth(admin_id);
CREATE INDEX idx_2fa_enabled ON two_factor_auth(enabled);

-- ----------------------------
-- Tables referenced in V002 but definition not explicitly found, creating basic structure
-- ----------------------------

CREATE TABLE IF NOT EXISTS reviews (
    id VARCHAR(36) PRIMARY KEY,
    created_at BIGINT,
    last_modified_date BIGINT,
    status INT,
    app_id VARCHAR(36),
    customer_id VARCHAR(36),
    rating INT,
    comment TEXT,
    moderation_status VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS audit_logs (
    id VARCHAR(36) PRIMARY KEY,
    created_at BIGINT,
    last_modified_date BIGINT,
    status INT,
    entity_type VARCHAR(50),
    entity_id VARCHAR(36),
    user_email VARCHAR(255),
    action VARCHAR(50),
    details TEXT
);

CREATE TABLE IF NOT EXISTS search_queries (
    id VARCHAR(36) PRIMARY KEY,
    created_at BIGINT,
    last_modified_date BIGINT,
    status INT,
    query_text VARCHAR(255),
    has_results BOOLEAN
);

CREATE TABLE IF NOT EXISTS features (
    id VARCHAR(36) PRIMARY KEY,
    created_at BIGINT,
    last_modified_date BIGINT,
    status INT,
    app_id VARCHAR(36),
    title VARCHAR(255),
    description TEXT,
    sort_order INT
);

CREATE TABLE IF NOT EXISTS app_members (
    id VARCHAR(36) PRIMARY KEY,
    created_at BIGINT,
    last_modified_date BIGINT,
    status INT,
    app_id VARCHAR(36),
    customer_id VARCHAR(36),
    role VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS media_library (
    id VARCHAR(36) PRIMARY KEY,
    created_at BIGINT,
    last_modified_date BIGINT,
    status INT,
    folder VARCHAR(255),
    file_type VARCHAR(50),
    file_name VARCHAR(255),
    file_url VARCHAR(500),
    file_size BIGINT
);

CREATE TABLE IF NOT EXISTS product_likes (
    id VARCHAR(36) PRIMARY KEY,
    created_at BIGINT,
    last_modified_date BIGINT,
    status INT,
    app_id VARCHAR(36),
    customer_id VARCHAR(36)
);

CREATE TABLE IF NOT EXISTS email_subscriptions (
    id VARCHAR(36) PRIMARY KEY,
    created_at BIGINT,
    last_modified_date BIGINT,
    status INT,
    email VARCHAR(255),
    domain_id VARCHAR(36),
    is_verified BOOLEAN
);

CREATE TABLE IF NOT EXISTS push_subscriptions (
    id VARCHAR(36) PRIMARY KEY,
    created_at BIGINT,
    last_modified_date BIGINT,
    status INT,
    user_email VARCHAR(255),
    endpoint TEXT,
    p256dh TEXT,
    auth TEXT
);
