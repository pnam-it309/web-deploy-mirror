-- Phase 3A: Full-Text Search Support
-- This migration adds full-text indexes for fast, relevance-ranked search
-- Expected impact: 50x faster search queries with better relevance

-- ==========================================
-- APPS TABLE FULL-TEXT SEARCH
-- ==========================================

-- Add full-text index for product search
-- Includes name, SKU, and short_description for comprehensive search
DROP INDEX ft_app_search ON apps;
ALTER TABLE apps 
ADD FULLTEXT INDEX ft_app_search (name, sku, short_description);

-- Note: MySQL full-text search features:
-- - NATURAL LANGUAGE MODE: relevance ranking, stemming
-- - BOOLEAN MODE: +required -excluded "phrase"
-- - QUERY EXPANSION: finds related terms

-- Example queries:
-- SELECT * FROM apps 
-- WHERE MATCH(name, sku, short_description) AGAINST('react framework' IN NATURAL LANGUAGE MODE);
--
-- SELECT *, MATCH(name, sku, short_description) AGAINST('react') as relevance
-- FROM apps 
-- WHERE MATCH(name, sku, short_description) AGAINST('react' IN NATURAL LANGUAGE MODE)
-- ORDER BY relevance DESC;

-- ==========================================
-- CUSTOMERS TABLE FULL-TEXT SEARCH (optional)
-- ==========================================

-- Uncomment if you want full-text search for customers
-- ALTER TABLE customers 
-- ADD FULLTEXT INDEX ft_customer_search (email, full_name);

-- ==========================================
-- FEATURES TABLE FULL-TEXT SEARCH (optional)
-- ==========================================

-- Uncomment if you want full-text search within features
-- ALTER TABLE features 
-- ADD FULLTEXT INDEX ft_feature_search (title, description);

-- ==========================================
-- Full-Text Search Configuration Notes
-- ==========================================

-- Minimum word length (default is 4 for MyISAM, 3 for InnoDB in MySQL 5.7+)
-- To change: SET GLOBAL innodb_ft_min_token_size = 3;
-- Then rebuild the index: ALTER TABLE apps DROP INDEX ft_app_search;
--                         ALTER TABLE apps ADD FULLTEXT INDEX ft_app_search (name, sku, short_description);

-- Stop words: Common words like "the", "and" are ignored
-- To customize stop words list, modify innodb_ft_server_stopword_table

-- ==========================================
-- Testing Full-Text Search Performance
-- ==========================================

-- Test query without full-text (OLD way - slow):
-- SELECT * FROM apps 
-- WHERE LOWER(name) LIKE LOWER('%react%') 
--    OR LOWER(sku) LIKE LOWER('%react%') 
--    OR LOWER(short_description) LIKE LOWER('%react%');

-- Test query with full-text (NEW way - fast):
-- SELECT * FROM apps 
-- WHERE MATCH(name, sku, short_description) AGAINST('react' IN NATURAL LANGUAGE MODE);

-- Check query execution plan:
-- EXPLAIN SELECT * FROM apps 
-- WHERE MATCH(name, sku, short_description) AGAINST('react' IN NATURAL LANGUAGE MODE);
-- Should show: type = fulltext

-- ==========================================
-- Verify full-text indexes were created
-- Run this query to check:
-- SELECT TABLE_NAME, INDEX_NAME, COLUMN_NAME 
-- FROM INFORMATION_SCHEMA.STATISTICS 
-- WHERE TABLE_SCHEMA = 'catalog_web' 
--   AND INDEX_TYPE = 'FULLTEXT'
-- ORDER BY TABLE_NAME;
-- ==========================================
