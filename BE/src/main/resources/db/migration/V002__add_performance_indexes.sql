-- Phase 3A: Performance Indexes
-- This migration adds indexes to improve query performance across the application
-- Expected impact: 5-50x faster queries on filtered/sorted columns

-- ===============================================================
-- SAFE DROP INDEX PROCDURE (Handle "IF EXISTS" manually)
-- ===============================================================
DROP PROCEDURE IF EXISTS create_index_if_not_exists;

DELIMITER $$
CREATE PROCEDURE create_index_if_not_exists(IN idx_name VARCHAR(255), IN tbl_name VARCHAR(255), IN idx_col_def VARCHAR(1000))
BEGIN
    DECLARE idx_exists INT DEFAULT 0;
    
    -- Check if index exists
    SELECT COUNT(*) INTO idx_exists
    FROM information_schema.statistics
    WHERE table_schema = DATABASE() 
    AND table_name = tbl_name 
    AND index_name = idx_name;

    -- Create if not exists
    IF idx_exists = 0 THEN
        SET @s = CONCAT('CREATE INDEX ', idx_name, ' ON ', tbl_name, ' ', idx_col_def);
        PREPARE stmt FROM @s;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
    END IF;
END $$
DELIMITER ;

-- ==========================================
-- APPS TABLE INDEXES
-- ==========================================

-- Index for filtering by status (very common filter)
CALL create_index_if_not_exists('idx_app_status', 'apps', '(status)');

-- Index for filtering by domain (common in domain pages)
-- NOTE: Skipped - domain_id already has an index from the FK constraint fk_app_domain
-- MySQL automatically creates an index for foreign key columns
-- CALL drop_index_if_exists('idx_app_domain', 'apps');
-- CREATE INDEX idx_app_domain ON apps(domain_id) USING BTREE;

-- Index for sorting by view count (trending, popular products)
CALL create_index_if_not_exists('idx_app_view_count', 'apps', '(view_count DESC)');

-- Index for sorting by created date (newest products)
CALL create_index_if_not_exists('idx_app_created_at', 'apps', '(created_at DESC)');

-- Composite index for featured products (used together)
CALL create_index_if_not_exists('idx_app_featured', 'apps', '(is_featured, homepage_sort_order)');

-- Index for featured videos
CALL create_index_if_not_exists('idx_app_featured_video', 'apps', '(is_featured_video, created_at DESC)');

-- Composite index for active apps in a domain (common query pattern)
CALL create_index_if_not_exists('idx_app_domain_status', 'apps', '(domain_id, status, created_at DESC)');

-- ==========================================
-- REVIEWS TABLE INDEXES
-- ==========================================

-- Composite index for app reviews (app_id + status together)
CALL create_index_if_not_exists('idx_review_app_status', 'reviews', '(app_id, moderation_status, created_at DESC)');

-- Index for review moderation page
CALL create_index_if_not_exists('idx_review_moderation', 'reviews', '(moderation_status, created_at DESC)');

-- Index for customer's reviews
CALL create_index_if_not_exists('idx_review_customer', 'reviews', '(customer_id, created_at DESC)');

-- Index for rating calculations
CALL create_index_if_not_exists('idx_review_rating', 'reviews', '(app_id, rating)');

-- ==========================================
-- AUDIT_LOGS TABLE INDEXES
-- ==========================================

-- Composite index for entity audit logs (most common query)
CALL create_index_if_not_exists('idx_audit_entity', 'audit_logs', '(entity_type, entity_id, created_at DESC)');

-- Index for user activity tracking
CALL create_index_if_not_exists('idx_audit_user', 'audit_logs', '(user_email, created_at DESC)');

-- Index for recent audit logs (dashboard)
CALL create_index_if_not_exists('idx_audit_created', 'audit_logs', '(created_at DESC)');

-- Index for filtering by action type
CALL create_index_if_not_exists('idx_audit_action', 'audit_logs', '(action, created_at DESC)');

-- ==========================================
-- SEARCH_QUERIES TABLE INDEXES
-- ==========================================

-- Index for analytics time range queries
CALL create_index_if_not_exists('idx_search_created', 'search_queries', '(created_at)');

-- Index for grouping by query text (top keywords)
CALL create_index_if_not_exists('idx_search_text', 'search_queries', '(query_text, created_at)');

-- Index for no-result queries filtering
CALL create_index_if_not_exists('idx_search_no_results', 'search_queries', '(has_results, created_at)');

-- ==========================================
-- FEATURES TABLE INDEXES
-- ==========================================

-- Index for app features
CALL create_index_if_not_exists('idx_feature_app', 'features', '(app_id, sort_order, status)');

-- Index for active features only
CALL create_index_if_not_exists('idx_feature_active', 'features', '(app_id, sort_order)');

-- ==========================================
-- APP_MEMBERS TABLE INDEXES
-- ==========================================

-- Index for finding team members
CALL create_index_if_not_exists('idx_app_member_app', 'app_members', '(app_id)');

-- Index for customer's projects
CALL create_index_if_not_exists('idx_app_member_customer', 'app_members', '(customer_id)');

-- ==========================================
-- APP_TECHNOLOGIES TABLE (junction table)
-- ==========================================

-- DROP INDEX idx_app_tech_technology ON app_technologies;
-- Renamed to avoid FK constraint conflict
CALL create_index_if_not_exists('idx_app_tech_technology_composite', 'app_technologies', '(technology_id, app_id)');

-- Reverse index for app's technologies
CALL create_index_if_not_exists('idx_app_tech_app', 'app_technologies', '(app_id, technology_id)');

-- ==========================================
-- MEDIA_LIBRARY TABLE INDEXES
-- ==========================================

-- Index for folder filtering
CALL create_index_if_not_exists('idx_media_folder', 'media_library', '(folder, created_at DESC)');

-- Index for file type filtering
CALL create_index_if_not_exists('idx_media_file_type', 'media_library', '(file_type, created_at DESC)');

-- ==========================================
-- PRODUCT_LIKES TABLE INDEXES
-- ==========================================

-- Composite index for like checks
CALL create_index_if_not_exists('idx_product_like_app_customer', 'product_likes', '(app_id, customer_id)');

-- Index for counting likes per app
CALL create_index_if_not_exists('idx_product_like_app', 'product_likes', '(app_id)');

-- ==========================================
-- CUSTOMERS TABLE INDEXES
-- ==========================================

-- Index for email lookups (login, OAuth)
CALL create_index_if_not_exists('idx_customer_email', 'customers', '(email)');

-- Index for status filtering
CALL create_index_if_not_exists('idx_customer_status', 'customers', '(status)');

-- ==========================================
-- EMAIL_SUBSCRIPTIONS TABLE INDEXES
-- ==========================================

-- Index for finding subscriptions by email
CALL create_index_if_not_exists('idx_email_sub_email', 'email_subscriptions', '(email, is_verified)');

-- Index for domain subscriptions
CALL create_index_if_not_exists('idx_email_sub_domain', 'email_subscriptions', '(domain_id, is_verified)');

-- ==========================================
-- PUSH_SUBSCRIPTIONS TABLE INDEXES
-- ==========================================

-- Index for finding push subscriptions
CALL create_index_if_not_exists('idx_push_sub_email', 'push_subscriptions', '(user_email)');

-- ==========================================
-- CLEANUP
-- ==========================================
DROP PROCEDURE IF EXISTS create_index_if_not_exists;
DROP PROCEDURE IF EXISTS drop_index_if_exists;
