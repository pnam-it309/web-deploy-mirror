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

-- Data Insertion Script for FPL UDPM Catalog
-- Sources:
-- Điểm danh: https://youtu.be/ffn-vYxPXlA
-- Scurm: https://youtu.be/QBcDDEY7Nrg
-- Bán hàng(xưởng): https://youtu.be/HQPwbLLFaKE
-- Repos: See below

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 1. Insert Domains
-- ----------------------------
DELETE FROM `domains` WHERE `id` IN (
    'd0000000-0000-0000-0000-000000000001',
    'd0000000-0000-0000-0000-000000000002',
    'd0000000-0000-0000-0000-000000000003',
    'd0000000-0000-0000-0000-000000000004'
);

-- Note: status = 0 (ACTIVE)
INSERT INTO `domains` (`id`, `created_at`, `last_modified_date`, `status`, `version`, `name`, `slug`, `description`, `icon`, `color`, `parent_id`, `sort_order`) VALUES
('d0000000-0000-0000-0000-000000000001', UNIX_TIMESTAMP()*1000, UNIX_TIMESTAMP()*1000, 0, 0, 'Academic Management', 'academic-management', 'Tools for school and student management', 'school', '#4CAF50', NULL, 1),
('d0000000-0000-0000-0000-000000000002', UNIX_TIMESTAMP()*1000, UNIX_TIMESTAMP()*1000, 0, 0, 'Project Management', 'project-management', 'Tools for agile and project tracking', 'assignment', '#2196F3', NULL, 2),
('d0000000-0000-0000-0000-000000000003', UNIX_TIMESTAMP()*1000, UNIX_TIMESTAMP()*1000, 0, 0, 'Business Tools', 'business-tools', 'E-commerce and business solutions', 'business_center', '#FF9800', NULL, 3),
('d0000000-0000-0000-0000-000000000004', UNIX_TIMESTAMP()*1000, UNIX_TIMESTAMP()*1000, 0, 0, 'Social & Community', 'social-community', 'Community platforms and social tools', 'people', '#9C27B0', NULL, 4);

-- ----------------------------
-- 2. Insert Technologies
-- ----------------------------
-- Deleting by name because unique constraint is on name
DELETE FROM `technologies` WHERE `name` IN ('Java', 'Spring Boot', 'Vue.js', 'MySQL', 'Tailwind CSS');
DELETE FROM `technologies` WHERE `id` IN (
    't0000000-0000-0000-0000-000000000001',
    't0000000-0000-0000-0000-000000000002',
    't0000000-0000-0000-0000-000000000003',
    't0000000-0000-0000-0000-000000000004'
);

INSERT INTO `technologies` (`id`, `created_at`, `last_modified_date`, `status`, `version`, `name`, `icon`) VALUES
('t0000000-0000-0000-0000-000000000001', UNIX_TIMESTAMP()*1000, UNIX_TIMESTAMP()*1000, 0, 0, 'Java', 'java-icon'),
('t0000000-0000-0000-0000-000000000002', UNIX_TIMESTAMP()*1000, UNIX_TIMESTAMP()*1000, 0, 0, 'Spring Boot', 'spring-icon'),
('t0000000-0000-0000-0000-000000000003', UNIX_TIMESTAMP()*1000, UNIX_TIMESTAMP()*1000, 0, 0, 'Vue.js', 'vue-icon'),
('t0000000-0000-0000-0000-000000000004', UNIX_TIMESTAMP()*1000, UNIX_TIMESTAMP()*1000, 0, 0, 'MySQL', 'mysql-icon'),
('t0000000-0000-0000-0000-000000000005', UNIX_TIMESTAMP()*1000, UNIX_TIMESTAMP()*1000, 0, 0, 'Tailwind CSS', 'https://upload.wikimedia.org/wikipedia/commons/d/d5/Tailwind_CSS_Logo.svg');

-- ----------------------------
-- 3. Insert Apps
-- ----------------------------

DELETE FROM `apps` WHERE `id` IN (
    'a0000000-0000-0000-0000-000000000001',
    'a0000000-0000-0000-0000-000000000002',
    'a0000000-0000-0000-0000-000000000003',
    'a0000000-0000-0000-0000-000000000004',
    'a0000000-0000-0000-0000-000000000005',
    'a0000000-0000-0000-0000-000000000006'
);

-- App 1: FPL Student Attendance
INSERT INTO `apps` (`id`, `created_at`, `last_modified_date`, `status`, `version`, `domain_id`, `name`, `sku`, `short_description`, `thumbnail`, `view_count`, `is_featured`, `homepage_sort_order`, `approval_status`, `meta_title`, `meta_description`, `meta_keywords`, `og_image`, `deleted_at`, `publish_at`) VALUES
('a0000000-0000-0000-0000-000000000001', UNIX_TIMESTAMP()*1000, UNIX_TIMESTAMP()*1000, 0, 0, 'd0000000-0000-0000-0000-000000000001', 'FPL Student Attendance', 'ATT-001', 'Hệ thống điểm danh sinh viên FPL', 'https://img.youtube.com/vi/ffn-vYxPXlA/maxresdefault.jpg', 120, b'1', 1, 'APPROVED', 'FPL Student Attendance', 'Hệ thống điểm danh', 'attendance, fpl', NULL, NULL, UNIX_TIMESTAMP()*1000);

-- App 2: FPL Scrum Helper
INSERT INTO `apps` (`id`, `created_at`, `last_modified_date`, `status`, `version`, `domain_id`, `name`, `sku`, `short_description`, `thumbnail`, `view_count`, `is_featured`, `homepage_sort_order`, `approval_status`, `meta_title`, `meta_description`, `meta_keywords`, `og_image`, `deleted_at`, `publish_at`) VALUES
('a0000000-0000-0000-0000-000000000002', UNIX_TIMESTAMP()*1000, UNIX_TIMESTAMP()*1000, 0, 0, 'd0000000-0000-0000-0000-000000000002', 'FPL Scrum Helper', 'SCR-001', 'Công cụ hỗ trợ quản lý dự án theo mô hình Agile/Scrum', 'https://img.youtube.com/vi/QBcDDEY7Nrg/maxresdefault.jpg', 95, b'1', 2, 'APPROVED', 'FPL Scrum Helper', 'Quản lý dự án Scrum', 'scrum, agile, project', NULL, NULL, UNIX_TIMESTAMP()*1000);

-- App 3: FPL Sell OpenSource
INSERT INTO `apps` (`id`, `created_at`, `last_modified_date`, `status`, `version`, `domain_id`, `name`, `sku`, `short_description`, `thumbnail`, `view_count`, `is_featured`, `homepage_sort_order`, `approval_status`, `meta_title`, `meta_description`, `meta_keywords`, `og_image`, `deleted_at`, `publish_at`) VALUES
('a0000000-0000-0000-0000-000000000003', UNIX_TIMESTAMP()*1000, UNIX_TIMESTAMP()*1000, 0, 0, 'd0000000-0000-0000-0000-000000000003', 'FPL Sell OpenSource', 'SEL-001', 'Hệ thống bán hàng và quản lý kho', 'https://img.youtube.com/vi/HQPwbLLFaKE/maxresdefault.jpg', 150, b'1', 3, 'APPROVED', 'FPL Sell', 'Hệ thống bán hàng', 'sell, ecommerce', NULL, NULL, UNIX_TIMESTAMP()*1000);

-- App 4: FPL UDPM Auto Grading System
INSERT INTO `apps` (`id`, `created_at`, `last_modified_date`, `status`, `version`, `domain_id`, `name`, `sku`, `short_description`, `thumbnail`, `view_count`, `is_featured`, `homepage_sort_order`, `approval_status`, `meta_title`, `meta_description`, `meta_keywords`, `og_image`, `deleted_at`, `publish_at`) VALUES
('a0000000-0000-0000-0000-000000000004', UNIX_TIMESTAMP()*1000, UNIX_TIMESTAMP()*1000, 0, 0, 'd0000000-0000-0000-0000-000000000001', 'FPL Auto Grading System', 'GRD-001', 'Hệ thống chấm điểm tự động cho sinh viên UDPM', NULL, 80, b'0', NULL, 'APPROVED', 'Auto Grading', 'Chấm điểm tự động', 'grading, auto', NULL, NULL, UNIX_TIMESTAMP()*1000);

-- App 5: FPL UDPM Confess Poly 2
INSERT INTO `apps` (`id`, `created_at`, `last_modified_date`, `status`, `version`, `domain_id`, `name`, `sku`, `short_description`, `thumbnail`, `view_count`, `is_featured`, `homepage_sort_order`, `approval_status`, `meta_title`, `meta_description`, `meta_keywords`, `og_image`, `deleted_at`, `publish_at`) VALUES
('a0000000-0000-0000-0000-000000000005', UNIX_TIMESTAMP()*1000, UNIX_TIMESTAMP()*1000, 0, 0, 'd0000000-0000-0000-0000-000000000004', 'FPL Confess Poly 2', 'CFS-002', 'Cổng thông tin Confessions cho sinh viên Poly', NULL, 200, b'0', NULL, 'APPROVED', 'FPL Confess', 'Confessions Poly', 'confess, social', NULL, NULL, UNIX_TIMESTAMP()*1000);

-- App 6: FPL Hotel Management System
INSERT INTO `apps` (`id`, `created_at`, `last_modified_date`, `status`, `version`, `domain_id`, `name`, `sku`, `short_description`, `thumbnail`, `view_count`, `is_featured`, `homepage_sort_order`, `approval_status`, `meta_title`, `meta_description`, `meta_keywords`, `og_image`, `deleted_at`, `publish_at`) VALUES
('a0000000-0000-0000-0000-000000000006', UNIX_TIMESTAMP()*1000, UNIX_TIMESTAMP()*1000, 0, 0, 'd0000000-0000-0000-0000-000000000003', 'FPL Hotel Management', 'HTL-001', 'Hệ thống quản lý khách sạn', NULL, 60, b'0', NULL, 'APPROVED', 'Hotel Management', 'Quản lý khách sạn', 'hotel, management', NULL, NULL, UNIX_TIMESTAMP()*1000);

-- ----------------------------
-- 4. Insert App Details
-- ----------------------------
-- Schema: id, created_at, last_modified_date, status, app_id, long_description, demo_url, source_url, specifications
DELETE FROM `app_details` WHERE `id` IN (
    'ad000000-0000-0000-0000-000000000001',
    'ad000000-0000-0000-0000-000000000002',
    'ad000000-0000-0000-0000-000000000003',
    'ad000000-0000-0000-0000-000000000004',
    'ad000000-0000-0000-0000-000000000005',
    'ad000000-0000-0000-0000-000000000006'
);

INSERT INTO `app_details` (`id`, `created_at`, `last_modified_date`, `status`, `app_id`, `long_description`, `demo_url`, `source_url`, `specifications`) VALUES
('ad000000-0000-0000-0000-000000000001', UNIX_TIMESTAMP()*1000, UNIX_TIMESTAMP()*1000, 0, 'a0000000-0000-0000-0000-000000000001', 'Detailed description for Attendance System...', 'https://youtu.be/ffn-vYxPXlA', 'https://github.com/FPLHN-FACTORY/fpl-student-attendance.git', '{}'),
('ad000000-0000-0000-0000-000000000002', UNIX_TIMESTAMP()*1000, UNIX_TIMESTAMP()*1000, 0, 'a0000000-0000-0000-0000-000000000002', 'Detailed description for Scrum Helper...', 'https://youtu.be/QBcDDEY7Nrg', 'https://github.com/FPLHN-FACTORY/fpl-scrum-helper.git', '{}'),
('ad000000-0000-0000-0000-000000000003', UNIX_TIMESTAMP()*1000, UNIX_TIMESTAMP()*1000, 0, 'a0000000-0000-0000-0000-000000000003', 'Detailed description for Sell OpenSource...', 'https://youtu.be/HQPwbLLFaKE', 'https://github.com/FPLHN-FACTORY/fpl-sell-opensource.git', '{}'),
('ad000000-0000-0000-0000-000000000004', UNIX_TIMESTAMP()*1000, UNIX_TIMESTAMP()*1000, 0, 'a0000000-0000-0000-0000-000000000004', 'Detailed description for Auto Grading...', NULL, 'https://github.com/FPLHN-FACTORY/fpl-udpm-auto-grading-system.git', '{}'),
('ad000000-0000-0000-0000-000000000005', UNIX_TIMESTAMP()*1000, UNIX_TIMESTAMP()*1000, 0, 'a0000000-0000-0000-0000-000000000005', 'Detailed description for Confess Poly...', NULL, 'https://github.com/FPLHN-FACTORY/fpl-udpm-confess-poly2.git', '{}'),
('ad000000-0000-0000-0000-000000000006', UNIX_TIMESTAMP()*1000, UNIX_TIMESTAMP()*1000, 0, 'a0000000-0000-0000-0000-000000000006', 'Detailed description for Hotel Management...', NULL, 'https://github.com/FPLHN-FACTORY/fpl-hotel-management-system.git', '{}');

-- ----------------------------
-- 5. Insert App Technologies (Mapping)
-- ----------------------------
-- Schema: app_id, technology_id
DELETE FROM `app_technologies` WHERE `app_id` IN (
    'a0000000-0000-0000-0000-000000000001',
    'a0000000-0000-0000-0000-000000000002',
    'a0000000-0000-0000-0000-000000000003',
    'a0000000-0000-0000-0000-000000000004',
    'a0000000-0000-0000-0000-000000000005',
    'a0000000-0000-0000-0000-000000000006'
);

INSERT INTO `app_technologies` (`app_id`, `technology_id`) VALUES
('a0000000-0000-0000-0000-000000000001', 't0000000-0000-0000-0000-000000000001'),
('a0000000-0000-0000-0000-000000000001', 't0000000-0000-0000-0000-000000000002'),
('a0000000-0000-0000-0000-000000000001', 't0000000-0000-0000-0000-000000000004'),
('a0000000-0000-0000-0000-000000000002', 't0000000-0000-0000-0000-000000000001'),
('a0000000-0000-0000-0000-000000000002', 't0000000-0000-0000-0000-000000000003'),
('a0000000-0000-0000-0000-000000000003', 't0000000-0000-0000-0000-000000000002'),
('a0000000-0000-0000-0000-000000000003', 't0000000-0000-0000-0000-000000000003'),
('a0000000-0000-0000-0000-000000000004', 't0000000-0000-0000-0000-000000000001'),
('a0000000-0000-0000-0000-000000000005', 't0000000-0000-0000-0000-000000000002'),
('a0000000-0000-0000-0000-000000000006', 't0000000-0000-0000-0000-000000000002');

SET FOREIGN_KEY_CHECKS = 1;
