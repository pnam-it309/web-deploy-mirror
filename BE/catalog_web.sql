/**
 * Database Schema Script for FPL-UDPM-Catalog
 * Generated based on JPA Entities
 */

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `id` varchar(36) NOT NULL,
  `created_date` bigint DEFAULT NULL,
  `last_modified_date` bigint DEFAULT NULL,
  `status` tinyint DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `picture` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles` (
  `id` varchar(36) NOT NULL,
  `created_date` bigint DEFAULT NULL,
  `last_modified_date` bigint DEFAULT NULL,
  `status` tinyint NOT NULL,
  `code` varchar(50) NOT NULL,
  `name` varchar(255) NOT NULL,
  `description` varchar(5000) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_roles_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for admin_role
-- ----------------------------
DROP TABLE IF EXISTS `admin_role`;
CREATE TABLE `admin_role` (
  `id` varchar(36) NOT NULL,
  `created_date` bigint DEFAULT NULL,
  `last_modified_date` bigint DEFAULT NULL,
  `status` tinyint DEFAULT NULL,
  `id_admin` varchar(36) DEFAULT NULL,
  `id_role` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_admin_role_admin` (`id_admin`),
  KEY `FK_admin_role_role` (`id_role`),
  CONSTRAINT `FK_admin_role_admin` FOREIGN KEY (`id_admin`) REFERENCES `admin` (`id`),
  CONSTRAINT `FK_admin_role_role` FOREIGN KEY (`id_role`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for brands
-- ----------------------------
DROP TABLE IF EXISTS `brands`;
CREATE TABLE `brands` (
  `id` varchar(36) NOT NULL,
  `code` varchar(50) NOT NULL,
  `name` varchar(100) NOT NULL,
  `slug` varchar(255) NOT NULL,
  `description` varchar(500) DEFAULT NULL,
  `logo_url` varchar(255) DEFAULT NULL,
  `status` varchar(50) NOT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `last_modified_date` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_brands_code` (`code`),
  UNIQUE KEY `UK_brands_slug` (`slug`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for categories
-- ----------------------------
DROP TABLE IF EXISTS `categories`;
CREATE TABLE `categories` (
  `id` varchar(36) NOT NULL,
  `created_date` bigint DEFAULT NULL,
  `last_modified_date` bigint DEFAULT NULL,
  `status` tinyint DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `slug` varchar(285) NOT NULL,
  `description` varchar(5000) DEFAULT NULL,
  `parent_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_categories_slug` (`slug`),
  KEY `FK_categories_parent` (`parent_id`),
  CONSTRAINT `FK_categories_parent` FOREIGN KEY (`parent_id`) REFERENCES `categories` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for customer
-- ----------------------------
DROP TABLE IF EXISTS `customer`;
CREATE TABLE `customer` (
  `id` varchar(36) NOT NULL,
  `created_date` bigint DEFAULT NULL,
  `last_modified_date` bigint DEFAULT NULL,
  `status` tinyint DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `picture` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for customer_role
-- ----------------------------
DROP TABLE IF EXISTS `customer_role`;
CREATE TABLE `customer_role` (
  `id` varchar(36) NOT NULL,
  `created_date` bigint DEFAULT NULL,
  `last_modified_date` bigint DEFAULT NULL,
  `status` tinyint DEFAULT NULL,
  `id_customer` varchar(36) DEFAULT NULL,
  `id_role` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_customer_role_customer` (`id_customer`),
  KEY `FK_customer_role_role` (`id_role`),
  CONSTRAINT `FK_customer_role_customer` FOREIGN KEY (`id_customer`) REFERENCES `customer` (`id`),
  CONSTRAINT `FK_customer_role_role` FOREIGN KEY (`id_role`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for products
-- ----------------------------
DROP TABLE IF EXISTS `products`;
CREATE TABLE `products` (
  `id` varchar(36) NOT NULL,
  `created_date` bigint DEFAULT NULL,
  `last_modified_date` bigint DEFAULT NULL,
  `status` tinyint DEFAULT NULL,
  `sku` varchar(50) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `slug` varchar(305) NOT NULL,
  `short_description` varchar(1000) DEFAULT NULL,
  `price` decimal(14,2) DEFAULT '0.00',
  `stock_quantity` int NOT NULL DEFAULT '0',
  `brand_id` varchar(36) DEFAULT NULL,
  `category_id` varchar(36) DEFAULT NULL,
  `unit` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_products_sku` (`sku`),
  UNIQUE KEY `UK_products_slug` (`slug`),
  KEY `FK_products_brand` (`brand_id`),
  KEY `FK_products_category` (`category_id`),
  CONSTRAINT `FK_products_brand` FOREIGN KEY (`brand_id`) REFERENCES `brands` (`id`),
  CONSTRAINT `FK_products_category` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for product_details
-- ----------------------------
DROP TABLE IF EXISTS `product_details`;
CREATE TABLE `product_details` (
  `id` varchar(36) NOT NULL,
  `created_date` bigint DEFAULT NULL,
  `last_modified_date` bigint DEFAULT NULL,
  `status` tinyint DEFAULT NULL,
  `product_id` varchar(36) NOT NULL,
  `long_description` varchar(5000) DEFAULT NULL,
  `specification` json DEFAULT NULL,
  `packaging` varchar(255) DEFAULT NULL,
  `weight` decimal(10,3) DEFAULT NULL,
  `dimensions` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_product_details_product` (`product_id`),
  CONSTRAINT `FK_product_details_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for images
-- ----------------------------
DROP TABLE IF EXISTS `images`;
CREATE TABLE `images` (
  `id` varchar(36) NOT NULL,
  `created_date` bigint DEFAULT NULL,
  `last_modified_date` bigint DEFAULT NULL,
  `status` tinyint DEFAULT NULL,
  `product_detail_id` varchar(36) DEFAULT NULL,
  `url` varchar(2000) NOT NULL,
  `alt_text` varchar(255) DEFAULT NULL,
  `sort_order` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `FK_images_product_detail` (`product_detail_id`),
  CONSTRAINT `FK_images_product_detail` FOREIGN KEY (`product_detail_id`) REFERENCES `product_details` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
  `id` varchar(36) NOT NULL,
  `created_date` bigint DEFAULT NULL,
  `last_modified_date` bigint DEFAULT NULL,
  `status` varchar(255) NOT NULL, -- Overridden in Entity as STRING
  `order_code` varchar(20) NOT NULL,
  `customer_name` varchar(100) NOT NULL,
  `customer_phone` varchar(20) NOT NULL,
  `customer_email` varchar(100) DEFAULT NULL,
  `customer_address` text NOT NULL,
  `total_amount` decimal(14,2) NOT NULL,
  `order_status` varchar(255) NOT NULL,
  `payment_status` varchar(255) NOT NULL,
  `payment_method` varchar(50) DEFAULT NULL,
  `estimated_delivery_date` date DEFAULT NULL,
  `actual_delivery_date` date DEFAULT NULL,
  `cancellation_reason` text,
  `notes` text,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_orders_code` (`order_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for order_items
-- ----------------------------
DROP TABLE IF EXISTS `order_items`;
CREATE TABLE `order_items` (
  `id` varchar(36) NOT NULL,
  `created_date` bigint DEFAULT NULL,
  `last_modified_date` bigint DEFAULT NULL,
  `status` tinyint DEFAULT NULL,
  `order_id` varchar(36) NOT NULL,
  `product_id` varchar(36) NOT NULL,
  `quantity` int NOT NULL,
  `unit_price` decimal(10,2) NOT NULL,
  `total_price` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_order_items_order` (`order_id`),
  KEY `FK_order_items_product` (`product_id`),
  CONSTRAINT `FK_order_items_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
  CONSTRAINT `FK_order_items_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for quote_requests
-- ----------------------------
DROP TABLE IF EXISTS `quote_requests`;
CREATE TABLE `quote_requests` (
  `id` varchar(36) NOT NULL,
  `created_date` bigint DEFAULT NULL,
  `last_modified_date` bigint DEFAULT NULL,
  `status` tinyint NOT NULL,
  `customer_name` varchar(255) NOT NULL,
  `customer_email` varchar(255) DEFAULT NULL,
  `customer_phone` varchar(50) DEFAULT NULL,
  `customer_company` varchar(255) DEFAULT NULL,
  `notes` varchar(5000) DEFAULT NULL,
  `total_estimated` decimal(14,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for quote_items
-- ----------------------------
DROP TABLE IF EXISTS `quote_items`;
CREATE TABLE `quote_items` (
  `id` varchar(36) NOT NULL,
  `created_date` bigint DEFAULT NULL,
  `last_modified_date` bigint DEFAULT NULL,
  `status` tinyint DEFAULT NULL,
  `quote_request_id` varchar(36) NOT NULL,
  `product_id` varchar(36) DEFAULT NULL,
  `product_name_snapshot` varchar(255) NOT NULL,
  `unit_price` decimal(14,2) DEFAULT NULL,
  `quantity` int NOT NULL DEFAULT '1',
  `unit_snapshot` varchar(50) DEFAULT NULL,
  `notes` varchar(5000) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_quote_items_request` (`quote_request_id`),
  KEY `FK_quote_items_product` (`product_id`),
  CONSTRAINT `FK_quote_items_request` FOREIGN KEY (`quote_request_id`) REFERENCES `quote_requests` (`id`),
  CONSTRAINT `FK_quote_items_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for wishlists
-- ----------------------------
DROP TABLE IF EXISTS `wishlists`;
CREATE TABLE `wishlists` (
  `id` varchar(36) NOT NULL,
  `created_date` bigint DEFAULT NULL,
  `last_modified_date` bigint DEFAULT NULL,
  `status` tinyint DEFAULT NULL,
  `user_id` varchar(255) NOT NULL,
  `product_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

SET FOREIGN_KEY_CHECKS = 1;