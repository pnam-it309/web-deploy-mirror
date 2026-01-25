-- Phase 3A: Performance Indexes
-- This migration adds indexes to improve query performance across the application
-- Expected impact: 5-50x faster queries on filtered/sorted columns

-- ==========================================
-- APPS TABLE INDEXES
-- ==========================================

-- Index for filtering by status (very common filter)
-- Index for filtering by status (very common filter)
-- DROP INDEX IF EXISTS idx_app_status ON apps;
CREATE INDEX idx_app_status ON apps(status);

-- Index for filtering by domain (common in domain pages)
-- DROP INDEX IF EXISTS idx_app_domain ON apps;
CREATE INDEX idx_app_domain ON apps(domain_id) USING BTREE;

-- Index for sorting by view count (trending, popular products)
-- DROP INDEX IF EXISTS idx_app_view_count ON apps;
CREATE INDEX idx_app_view_count ON apps(view_count DESC);

-- Index for sorting by created date (newest products)
-- DROP INDEX IF EXISTS idx_app_created_at ON apps;
CREATE INDEX idx_app_created_at ON apps(created_at DESC);

-- Composite index for featured products (used together)
-- DROP INDEX IF EXISTS idx_app_featured ON apps;
CREATE INDEX idx_app_featured ON apps(is_featured, homepage_sort_order);

-- Index for featured videos
-- DROP INDEX IF EXISTS idx_app_featured_video ON apps;
CREATE INDEX idx_app_featured_video ON apps(is_featured_video, created_at DESC);

-- Composite index for active apps in a domain (common query pattern)
-- DROP INDEX IF EXISTS idx_app_domain_status ON apps;
CREATE INDEX idx_app_domain_status ON apps(domain_id, status, created_at DESC);

-- ==========================================
-- REVIEWS TABLE INDEXES
-- ==========================================

-- Composite index for app reviews (app_id + status together)
-- DROP INDEX IF EXISTS idx_review_app_status ON reviews;
CREATE INDEX idx_review_app_status ON reviews(app_id, moderation_status, created_at DESC);

-- Index for review moderation page
-- DROP INDEX IF EXISTS idx_review_moderation ON reviews;
CREATE INDEX idx_review_moderation ON reviews(moderation_status, created_at DESC);

-- Index for customer's reviews
-- DROP INDEX IF EXISTS idx_review_customer ON reviews;
CREATE INDEX idx_review_customer ON reviews(customer_id, created_at DESC);

-- Index for rating calculations
-- DROP INDEX IF EXISTS idx_review_rating ON reviews;
CREATE INDEX idx_review_rating ON reviews(app_id, rating);

-- ==========================================
-- AUDIT_LOGS TABLE INDEXES
-- ==========================================

-- Composite index for entity audit logs (most common query)
-- DROP INDEX IF EXISTS idx_audit_entity ON audit_logs;
CREATE INDEX idx_audit_entity ON audit_logs(entity_type, entity_id, created_at DESC);

-- Index for user activity tracking
-- DROP INDEX IF EXISTS idx_audit_user ON audit_logs;
CREATE INDEX idx_audit_user ON audit_logs(user_email, created_at DESC);

-- Index for recent audit logs (dashboard)
-- DROP INDEX IF EXISTS idx_audit_created ON audit_logs;
CREATE INDEX idx_audit_created ON audit_logs(created_at DESC);

-- Index for filtering by action type
-- DROP INDEX IF EXISTS idx_audit_action ON audit_logs;
CREATE INDEX idx_audit_action ON audit_logs(action, created_at DESC);

-- ==========================================
-- SEARCH_QUERIES TABLE INDEXES
-- ==========================================

-- Index for analytics time range queries
-- DROP INDEX IF EXISTS idx_search_created ON search_queries;
CREATE INDEX idx_search_created ON search_queries(created_at);

-- Index for grouping by query text (top keywords)
-- DROP INDEX IF EXISTS idx_search_text ON search_queries;
CREATE INDEX idx_search_text ON search_queries(query_text, created_at);

-- Index for no-result queries filtering
-- DROP INDEX IF EXISTS idx_search_no_results ON search_queries;
CREATE INDEX idx_search_no_results ON search_queries(has_results, created_at);

-- ==========================================
-- FEATURES TABLE INDEXES
-- ==========================================

-- Index for app features
-- DROP INDEX IF EXISTS idx_feature_app ON features;
CREATE INDEX idx_feature_app ON features(app_id, sort_order, status);

-- Index for active features only
-- DROP INDEX IF EXISTS idx_feature_active ON features;
CREATE INDEX idx_feature_active ON features(app_id, sort_order);

-- ==========================================
-- APP_MEMBERS TABLE INDEXES
-- ==========================================

-- Index for finding team members
-- DROP INDEX IF EXISTS idx_app_member_app ON app_members;
CREATE INDEX idx_app_member_app ON app_members(app_id);

-- Index for customer's projects
-- DROP INDEX IF EXISTS idx_app_member_customer ON app_members;
CREATE INDEX idx_app_member_customer ON app_members(customer_id);

-- ==========================================
-- APP_TECHNOLOGIES TABLE (junction table)
-- ==========================================

-- Composite index for technology filtering (most important for many-to-many)
-- DROP INDEX IF EXISTS idx_app_tech_technology ON app_technologies;
CREATE INDEX idx_app_tech_technology ON app_technologies(technology_id, app_id);

-- Reverse index for app's technologies
-- DROP INDEX IF EXISTS idx_app_tech_app ON app_technologies;
CREATE INDEX idx_app_tech_app ON app_technologies(app_id, technology_id);

-- ==========================================
-- MEDIA_LIBRARY TABLE INDEXES
-- ==========================================

-- Index for folder filtering
-- DROP INDEX IF EXISTS idx_media_folder ON media_library;
CREATE INDEX idx_media_folder ON media_library(folder, created_at DESC);

-- Index for file type filtering
-- DROP INDEX IF EXISTS idx_media_file_type ON media_library;
CREATE INDEX idx_media_file_type ON media_library(file_type, created_at DESC);

-- ==========================================
-- PRODUCT_LIKES TABLE INDEXES
-- ==========================================

-- Composite index for like checks
-- DROP INDEX IF EXISTS idx_product_like_app_customer ON product_likes;
CREATE INDEX idx_product_like_app_customer ON product_likes(app_id, customer_id);

-- Index for counting likes per app
-- DROP INDEX IF EXISTS idx_product_like_app ON product_likes;
CREATE INDEX idx_product_like_app ON product_likes(app_id);

-- ==========================================
-- CUSTOMERS TABLE INDEXES
-- ==========================================

-- Index for email lookups (login, OAuth)
-- DROP INDEX IF EXISTS idx_customer_email ON customers;
CREATE INDEX idx_customer_email ON customers(email);

-- Index for status filtering
-- DROP INDEX IF EXISTS idx_customer_status ON customers;
CREATE INDEX idx_customer_status ON customers(status);

-- ==========================================
-- EMAIL_SUBSCRIPTIONS TABLE INDEXES
-- ==========================================

-- Index for finding subscriptions by email
-- DROP INDEX IF EXISTS idx_email_sub_email ON email_subscriptions;
CREATE INDEX idx_email_sub_email ON email_subscriptions(email, is_verified);

-- Index for domain subscriptions
-- DROP INDEX IF EXISTS idx_email_sub_domain ON email_subscriptions;
CREATE INDEX idx_email_sub_domain ON email_subscriptions(domain_id, is_verified);

-- ==========================================
-- PUSH_SUBSCRIPTIONS TABLE INDEXES
-- ==========================================

-- Index for finding push subscriptions
-- DROP INDEX IF EXISTS idx_push_sub_email ON push_subscriptions;
CREATE INDEX idx_push_sub_email ON push_subscriptions(user_email);

-- ==========================================
-- Verify indexes were created
-- Run this query to check:
-- SELECT TABLE_NAME, INDEX_NAME, COLUMN_NAME 
-- FROM INFORMATION_SCHEMA.STATISTICS 
-- WHERE TABLE_SCHEMA = 'catalog_web' 
-- ORDER BY TABLE_NAME, INDEX_NAME;
-- ==========================================
