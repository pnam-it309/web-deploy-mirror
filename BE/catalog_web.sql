-- Database: catalog_web
-- Generated based on JPA Entities

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles` (
  `id` varchar(36) NOT NULL,
  `created_at` bigint(20) DEFAULT NULL,
  `last_modified_date` bigint(20) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `name` varchar(50) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for permissions
-- ----------------------------
DROP TABLE IF EXISTS `permissions`;
CREATE TABLE `permissions` (
  `id` varchar(36) NOT NULL,
  `created_at` bigint(20) DEFAULT NULL,
  `last_modified_date` bigint(20) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `code` varchar(50) NOT NULL,
  `description` varchar(5000) DEFAULT NULL,
  `category` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_permissions_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for role_permissions
-- ----------------------------
DROP TABLE IF EXISTS `role_permissions`;
CREATE TABLE `role_permissions` (
  `id` varchar(36) NOT NULL,
  `created_at` bigint(20) DEFAULT NULL,
  `last_modified_date` bigint(20) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `role_id` varchar(36) NOT NULL,
  `permission_id` varchar(36) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_role_permissions` (`role_id`,`permission_id`),
  KEY `FK_role_permissions_role` (`role_id`),
  KEY `FK_role_permissions_permission` (`permission_id`),
  CONSTRAINT `FK_role_permissions_permission` FOREIGN KEY (`permission_id`) REFERENCES `permissions` (`id`),
  CONSTRAINT `FK_role_permissions_role` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for admins
-- ----------------------------
DROP TABLE IF EXISTS `admins`;
CREATE TABLE `admins` (
  `id` varchar(36) NOT NULL,
  `created_at` bigint(20) DEFAULT NULL,
  `last_modified_date` bigint(20) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `full_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_admins_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for admin_roles
-- ----------------------------
DROP TABLE IF EXISTS `admin_roles`;
CREATE TABLE `admin_roles` (
  `admin_id` varchar(36) NOT NULL,
  `role_id` varchar(36) NOT NULL,
  PRIMARY KEY (`admin_id`,`role_id`),
  KEY `FK_admin_roles_role` (`role_id`),
  CONSTRAINT `FK_admin_roles_admin` FOREIGN KEY (`admin_id`) REFERENCES `admins` (`id`),
  CONSTRAINT `FK_admin_roles_role` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for customers
-- ----------------------------
DROP TABLE IF EXISTS `customers`;
CREATE TABLE `customers` (
  `id` varchar(36) NOT NULL,
  `created_at` bigint(20) DEFAULT NULL,
  `last_modified_date` bigint(20) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `full_name` varchar(255) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_customers_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for customer_roles
-- ----------------------------
DROP TABLE IF EXISTS `customer_roles`;
CREATE TABLE `customer_roles` (
  `customer_id` varchar(36) NOT NULL,
  `role_id` varchar(36) NOT NULL,
  PRIMARY KEY (`customer_id`,`role_id`),
  KEY `FK_customer_roles_role` (`role_id`),
  CONSTRAINT `FK_customer_roles_customer` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`),
  CONSTRAINT `FK_customer_roles_role` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for domains
-- ----------------------------
DROP TABLE IF EXISTS `domains`;
CREATE TABLE `domains` (
  `id` varchar(36) NOT NULL,
  `created_at` bigint(20) DEFAULT NULL,
  `last_modified_date` bigint(20) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `version` bigint(20) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `slug` varchar(255) DEFAULT NULL,
  `description` text,
  `icon` varchar(500) DEFAULT NULL,
  `color` varchar(255) DEFAULT NULL,
  `parent_id` varchar(36) DEFAULT NULL,
  `sort_order` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_domain_slug` (`slug`),
  KEY `idx_domain_name` (`name`),
  KEY `FK_domains_parent` (`parent_id`),
  CONSTRAINT `FK_domains_parent` FOREIGN KEY (`parent_id`) REFERENCES `domains` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for technologies
-- ----------------------------
DROP TABLE IF EXISTS `technologies`;
CREATE TABLE `technologies` (
  `id` varchar(36) NOT NULL,
  `created_at` bigint(20) DEFAULT NULL,
  `last_modified_date` bigint(20) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `version` bigint(20) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `icon` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_technologies_name` (`name`),
  KEY `idx_technology_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for apps
-- ----------------------------
DROP TABLE IF EXISTS `apps`;
CREATE TABLE `apps` (
  `id` varchar(36) NOT NULL,
  `created_at` bigint(20) DEFAULT NULL,
  `last_modified_date` bigint(20) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `version` bigint(20) DEFAULT NULL,
  `domain_id` varchar(36) NOT NULL,
  `name` varchar(255) NOT NULL,
  `sku` varchar(50) DEFAULT NULL,
  `short_description` varchar(5000) DEFAULT NULL,
  `thumbnail` varchar(500) DEFAULT NULL,
  `view_count` bigint(20) DEFAULT 0,
  `is_featured` bit(1) DEFAULT b'0',
  `homepage_sort_order` int(11) DEFAULT NULL,
  `approval_status` varchar(255) DEFAULT NULL,
  `meta_title` varchar(100) DEFAULT NULL,
  `meta_description` varchar(300) DEFAULT NULL,
  `meta_keywords` varchar(200) DEFAULT NULL,
  `og_image` varchar(500) DEFAULT NULL,
  `deleted_at` bigint(20) DEFAULT NULL,
  `publish_at` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_app_name` (`name`),
  KEY `idx_app_domain` (`domain_id`),
  KEY `idx_app_featured` (`is_featured`),
  KEY `idx_app_status` (`approval_status`),
  CONSTRAINT `FK_apps_domain` FOREIGN KEY (`domain_id`) REFERENCES `domains` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for app_details
-- ----------------------------
DROP TABLE IF EXISTS `app_details`;
CREATE TABLE `app_details` (
  `id` varchar(36) NOT NULL,
  `created_at` bigint(20) DEFAULT NULL,
  `last_modified_date` bigint(20) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `app_id` varchar(36) NOT NULL,
  `long_description` longtext,
  `demo_url` varchar(500) DEFAULT NULL,
  `source_url` varchar(500) DEFAULT NULL,
  `specifications` json DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_app_details_app` (`app_id`),
  CONSTRAINT `FK_app_details_app` FOREIGN KEY (`app_id`) REFERENCES `apps` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for app_images
-- ----------------------------
DROP TABLE IF EXISTS `app_images`;
CREATE TABLE `app_images` (
  `id` varchar(36) NOT NULL,
  `created_at` bigint(20) DEFAULT NULL,
  `last_modified_date` bigint(20) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `app_id` varchar(36) NOT NULL,
  `url` varchar(500) NOT NULL,
  `is_main` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_app_images_app` (`app_id`),
  CONSTRAINT `FK_app_images_app` FOREIGN KEY (`app_id`) REFERENCES `apps` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for app_member
-- ----------------------------
DROP TABLE IF EXISTS `app_member`;
CREATE TABLE `app_member` (
  `id` varchar(36) NOT NULL,
  `created_at` bigint(20) DEFAULT NULL,
  `last_modified_date` bigint(20) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `app_id` varchar(36) DEFAULT NULL,
  `customer_id` varchar(36) DEFAULT NULL,
  `member_email` varchar(255) DEFAULT NULL,
  `member_name` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_app_member_app` (`app_id`),
  KEY `FK_app_member_customer` (`customer_id`),
  CONSTRAINT `FK_app_member_app` FOREIGN KEY (`app_id`) REFERENCES `apps` (`id`),
  CONSTRAINT `FK_app_member_customer` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for app_technologies
-- ----------------------------
DROP TABLE IF EXISTS `app_technologies`;
CREATE TABLE `app_technologies` (
  `app_id` varchar(36) NOT NULL,
  `technology_id` varchar(36) NOT NULL,
  PRIMARY KEY (`app_id`,`technology_id`),
  KEY `FK_app_technologies_technology` (`technology_id`),
  CONSTRAINT `FK_app_technologies_app` FOREIGN KEY (`app_id`) REFERENCES `apps` (`id`),
  CONSTRAINT `FK_app_technologies_technology` FOREIGN KEY (`technology_id`) REFERENCES `technologies` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for features
-- ----------------------------
DROP TABLE IF EXISTS `features`;
CREATE TABLE `features` (
  `id` varchar(36) NOT NULL,
  `created_at` bigint(20) DEFAULT NULL,
  `last_modified_date` bigint(20) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `version` bigint(20) DEFAULT NULL,
  `app_id` varchar(36) NOT NULL,
  `name` varchar(255) NOT NULL,
  `description` text,
  `image_preview` varchar(500) DEFAULT NULL,
  `video_url` varchar(500) DEFAULT NULL,
  `sort_order` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_feature_app` (`app_id`),
  CONSTRAINT `FK_features_app` FOREIGN KEY (`app_id`) REFERENCES `apps` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for feature_media
-- ----------------------------
DROP TABLE IF EXISTS `feature_media`;
CREATE TABLE `feature_media` (
  `id` varchar(36) NOT NULL,
  `created_at` bigint(20) DEFAULT NULL,
  `last_modified_date` bigint(20) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `feature_id` varchar(36) NOT NULL,
  `url` varchar(500) DEFAULT NULL,
  `type` varchar(50) DEFAULT NULL,
  `sort_order` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_feature_media_feature` (`feature_id`),
  CONSTRAINT `FK_feature_media_feature` FOREIGN KEY (`feature_id`) REFERENCES `features` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for email_subscription
-- ----------------------------
DROP TABLE IF EXISTS `email_subscription`;
CREATE TABLE `email_subscription` (
  `id` varchar(36) NOT NULL,
  `created_at` bigint(20) DEFAULT NULL,
  `last_modified_date` bigint(20) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `is_verified` bit(1) DEFAULT b'0',
  `verification_token` varchar(255) DEFAULT NULL,
  `unsubscribe_token` varchar(255) DEFAULT NULL,
  `preferences` varchar(255) DEFAULT NULL,
  `last_email_sent_at` bigint(20) DEFAULT NULL,
  `subscribed_at` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_email_subscription_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for review
-- ----------------------------
DROP TABLE IF EXISTS `review`;
CREATE TABLE `review` (
  `id` varchar(36) NOT NULL,
  `created_at` bigint(20) DEFAULT NULL,
  `last_modified_date` bigint(20) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `rating` int(11) NOT NULL,
  `comment` varchar(1000) DEFAULT NULL,
  `user_email` varchar(255) NOT NULL,
  `user_name` varchar(255) DEFAULT NULL,
  `app_id` varchar(36) NOT NULL,
  `moderation_status` varchar(255) DEFAULT 'PENDING',
  `moderated_at` bigint(20) DEFAULT NULL,
  `moderated_by` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_review_app` (`app_id`),
  CONSTRAINT `FK_review_app` FOREIGN KEY (`app_id`) REFERENCES `apps` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for product_likes
-- ----------------------------
DROP TABLE IF EXISTS `product_likes`;
CREATE TABLE `product_likes` (
  `id` varchar(36) NOT NULL,
  `created_at` bigint(20) NOT NULL,
  `last_modified_date` bigint(20) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `app_id` varchar(36) NOT NULL,
  `customer_id` varchar(36) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_like_composite` (`app_id`,`customer_id`),
  KEY `idx_like_app` (`app_id`),
  KEY `idx_like_customer` (`customer_id`),
  CONSTRAINT `FK_product_likes_app` FOREIGN KEY (`app_id`) REFERENCES `apps` (`id`),
  CONSTRAINT `FK_product_likes_customer` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for audit_log
-- ----------------------------
DROP TABLE IF EXISTS `audit_log`;
CREATE TABLE `audit_log` (
  `id` varchar(36) NOT NULL,
  `created_at` bigint(20) DEFAULT NULL,
  `last_modified_date` bigint(20) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `entity_type` varchar(255) NOT NULL,
  `entity_id` varchar(255) NOT NULL,
  `action` varchar(255) NOT NULL,
  `user_email` varchar(255) DEFAULT NULL,
  `user_name` varchar(255) DEFAULT NULL,
  `old_value` text,
  `new_value` text,
  `ip_address` varchar(255) DEFAULT NULL,
  `user_agent` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for ip_whitelist
-- ----------------------------
DROP TABLE IF EXISTS `ip_whitelist`;
CREATE TABLE `ip_whitelist` (
  `id` varchar(36) NOT NULL,
  `created_at` bigint(20) DEFAULT NULL,
  `last_modified_date` bigint(20) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `ip_address` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `is_active` bit(1) DEFAULT b'1',
  `added_by` varchar(255) DEFAULT NULL,
  `expires_at` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for media_library
-- ----------------------------
DROP TABLE IF EXISTS `media_library`;
CREATE TABLE `media_library` (
  `id` varchar(36) NOT NULL,
  `created_at` bigint(20) DEFAULT NULL,
  `last_modified_date` bigint(20) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `file_name` varchar(255) NOT NULL,
  `original_name` varchar(255) NOT NULL,
  `file_url` varchar(500) NOT NULL,
  `file_type` varchar(255) NOT NULL,
  `mime_type` varchar(255) DEFAULT NULL,
  `file_size` bigint(20) DEFAULT NULL,
  `width` int(11) DEFAULT NULL,
  `height` int(11) DEFAULT NULL,
  `thumbnail_url` varchar(500) DEFAULT NULL,
  `tags` varchar(500) DEFAULT NULL,
  `folder` varchar(255) DEFAULT NULL,
  `uploaded_by` varchar(255) DEFAULT NULL,
  `usage_count` int(11) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for push_subscription
-- ----------------------------
DROP TABLE IF EXISTS `push_subscription`;
CREATE TABLE `push_subscription` (
  `id` varchar(36) NOT NULL,
  `created_at` bigint(20) DEFAULT NULL,
  `last_modified_date` bigint(20) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `endpoint` varchar(1000) NOT NULL,
  `p256dh_key` varchar(500) DEFAULT NULL,
  `auth_key` varchar(500) DEFAULT NULL,
  `user_email` varchar(255) DEFAULT NULL,
  `user_agent` varchar(500) DEFAULT NULL,
  `is_active` bit(1) DEFAULT b'1',
  `last_used_at` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for search_queries
-- ----------------------------
DROP TABLE IF EXISTS `search_queries`;
CREATE TABLE `search_queries` (
  `id` varchar(36) NOT NULL,
  `created_at` bigint(20) DEFAULT NULL,
  `last_modified_date` bigint(20) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `query_text` varchar(255) NOT NULL,
  `user_id` varchar(36) DEFAULT NULL,
  `result_count` int(11) DEFAULT NULL,
  `category` varchar(50) DEFAULT NULL,
  `has_results` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_query_text` (`query_text`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

SET FOREIGN_KEY_CHECKS = 1;