-- Phase 3A: Performance Indexes
-- This migration adds indexes to improve query performance across the application
-- Expected impact: 5-50x faster queries on filtered/sorted columns

-- ===============================================================
-- SAFE DROP INDEX PROCDURE (Handle "IF EXISTS" manually)
-- ===============================================================
DROP PROCEDURE IF EXISTS drop_index_if_exists;

DELIMITER $$
CREATE PROCEDURE drop_index_if_exists(IN idx_name VARCHAR(255), IN tbl_name VARCHAR(255))
BEGIN
    DECLARE idx_exists INT DEFAULT 0;
    
    SELECT COUNT(*) INTO idx_exists
    FROM information_schema.statistics
    WHERE table_schema = DATABASE() 
    AND table_name = tbl_name 
    AND index_name = idx_name;

    IF idx_exists > 0 THEN
        SET @s = CONCAT('DROP INDEX ', idx_name, ' ON ', tbl_name);
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
CALL drop_index_if_exists('idx_app_status', 'apps');
CREATE INDEX idx_app_status ON apps(status);

-- Index for filtering by domain (common in domain pages)
CALL drop_index_if_exists('idx_app_domain', 'apps');
CREATE INDEX idx_app_domain ON apps(domain_id) USING BTREE;

-- Index for sorting by view count (trending, popular products)
CALL drop_index_if_exists('idx_app_view_count', 'apps');
CREATE INDEX idx_app_view_count ON apps(view_count DESC);

-- Index for sorting by created date (newest products)
CALL drop_index_if_exists('idx_app_created_at', 'apps');
CREATE INDEX idx_app_created_at ON apps(created_at DESC);

-- Composite index for featured products (used together)
CALL drop_index_if_exists('idx_app_featured', 'apps');
CREATE INDEX idx_app_featured ON apps(is_featured, homepage_sort_order);

-- Index for featured videos
CALL drop_index_if_exists('idx_app_featured_video', 'apps');
CREATE INDEX idx_app_featured_video ON apps(is_featured_video, created_at DESC);

-- Composite index for active apps in a domain (common query pattern)
CALL drop_index_if_exists('idx_app_domain_status', 'apps');
CREATE INDEX idx_app_domain_status ON apps(domain_id, status, created_at DESC);

-- ==========================================
-- REVIEWS TABLE INDEXES
-- ==========================================

-- Composite index for app reviews (app_id + status together)
CALL drop_index_if_exists('idx_review_app_status', 'reviews');
CREATE INDEX idx_review_app_status ON reviews(app_id, moderation_status, created_at DESC);

-- Index for review moderation page
CALL drop_index_if_exists('idx_review_moderation', 'reviews');
CREATE INDEX idx_review_moderation ON reviews(moderation_status, created_at DESC);

-- Index for customer's reviews
CALL drop_index_if_exists('idx_review_customer', 'reviews');
CREATE INDEX idx_review_customer ON reviews(customer_id, created_at DESC);

-- Index for rating calculations
CALL drop_index_if_exists('idx_review_rating', 'reviews');
CREATE INDEX idx_review_rating ON reviews(app_id, rating);

-- ==========================================
-- AUDIT_LOGS TABLE INDEXES
-- ==========================================

-- Composite index for entity audit logs (most common query)
CALL drop_index_if_exists('idx_audit_entity', 'audit_logs');
CREATE INDEX idx_audit_entity ON audit_logs(entity_type, entity_id, created_at DESC);

-- Index for user activity tracking
CALL drop_index_if_exists('idx_audit_user', 'audit_logs');
CREATE INDEX idx_audit_user ON audit_logs(user_email, created_at DESC);

-- Index for recent audit logs (dashboard)
CALL drop_index_if_exists('idx_audit_created', 'audit_logs');
CREATE INDEX idx_audit_created ON audit_logs(created_at DESC);

-- Index for filtering by action type
CALL drop_index_if_exists('idx_audit_action', 'audit_logs');
CREATE INDEX idx_audit_action ON audit_logs(action, created_at DESC);

-- ==========================================
-- SEARCH_QUERIES TABLE INDEXES
-- ==========================================

-- Index for analytics time range queries
CALL drop_index_if_exists('idx_search_created', 'search_queries');
CREATE INDEX idx_search_created ON search_queries(created_at);

-- Index for grouping by query text (top keywords)
CALL drop_index_if_exists('idx_search_text', 'search_queries');
CREATE INDEX idx_search_text ON search_queries(query_text, created_at);

-- Index for no-result queries filtering
CALL drop_index_if_exists('idx_search_no_results', 'search_queries');
CREATE INDEX idx_search_no_results ON search_queries(has_results, created_at);

-- ==========================================
-- FEATURES TABLE INDEXES
-- ==========================================

-- Index for app features
CALL drop_index_if_exists('idx_feature_app', 'features');
CREATE INDEX idx_feature_app ON features(app_id, sort_order, status);

-- Index for active features only
CALL drop_index_if_exists('idx_feature_active', 'features');
CREATE INDEX idx_feature_active ON features(app_id, sort_order);

-- ==========================================
-- APP_MEMBERS TABLE INDEXES
-- ==========================================

-- Index for finding team members
CALL drop_index_if_exists('idx_app_member_app', 'app_members');
CREATE INDEX idx_app_member_app ON app_members(app_id);

-- Index for customer's projects
CALL drop_index_if_exists('idx_app_member_customer', 'app_members');
CREATE INDEX idx_app_member_customer ON app_members(customer_id);

-- ==========================================
-- APP_TECHNOLOGIES TABLE (junction table)
-- ==========================================

-- DROP INDEX idx_app_tech_technology ON app_technologies;
-- Renamed to avoid FK constraint conflict
CALL drop_index_if_exists('idx_app_tech_technology_composite', 'app_technologies');
CREATE INDEX idx_app_tech_technology_composite ON app_technologies(technology_id, app_id);

-- Reverse index for app's technologies
CALL drop_index_if_exists('idx_app_tech_app', 'app_technologies');
CREATE INDEX idx_app_tech_app ON app_technologies(app_id, technology_id);

-- ==========================================
-- MEDIA_LIBRARY TABLE INDEXES
-- ==========================================

-- Index for folder filtering
CALL drop_index_if_exists('idx_media_folder', 'media_library');
CREATE INDEX idx_media_folder ON media_library(folder, created_at DESC);

-- Index for file type filtering
CALL drop_index_if_exists('idx_media_file_type', 'media_library');
CREATE INDEX idx_media_file_type ON media_library(file_type, created_at DESC);

-- ==========================================
-- PRODUCT_LIKES TABLE INDEXES
-- ==========================================

-- Composite index for like checks
CALL drop_index_if_exists('idx_product_like_app_customer', 'product_likes');
CREATE INDEX idx_product_like_app_customer ON product_likes(app_id, customer_id);

-- Index for counting likes per app
CALL drop_index_if_exists('idx_product_like_app', 'product_likes');
CREATE INDEX idx_product_like_app ON product_likes(app_id);

-- ==========================================
-- CUSTOMERS TABLE INDEXES
-- ==========================================

-- Index for email lookups (login, OAuth)
CALL drop_index_if_exists('idx_customer_email', 'customers');
CREATE INDEX idx_customer_email ON customers(email);

-- Index for status filtering
CALL drop_index_if_exists('idx_customer_status', 'customers');
CREATE INDEX idx_customer_status ON customers(status);

-- ==========================================
-- EMAIL_SUBSCRIPTIONS TABLE INDEXES
-- ==========================================

-- Index for finding subscriptions by email
CALL drop_index_if_exists('idx_email_sub_email', 'email_subscriptions');
CREATE INDEX idx_email_sub_email ON email_subscriptions(email, is_verified);

-- Index for domain subscriptions
CALL drop_index_if_exists('idx_email_sub_domain', 'email_subscriptions');
CREATE INDEX idx_email_sub_domain ON email_subscriptions(domain_id, is_verified);

-- ==========================================
-- PUSH_SUBSCRIPTIONS TABLE INDEXES
-- ==========================================

-- Index for finding push subscriptions
CALL drop_index_if_exists('idx_push_sub_email', 'push_subscriptions');
CREATE INDEX idx_push_sub_email ON push_subscriptions(user_email);

-- ==========================================
-- CLEANUP
-- ==========================================
DROP PROCEDURE IF EXISTS drop_index_if_exists;
