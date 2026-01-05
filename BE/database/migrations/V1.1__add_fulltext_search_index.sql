-- =====================================================
-- Full-Text Search Index Migration
-- =====================================================
-- Purpose: Add full-text index for better search performance
-- Date: 2025-12-30
-- =====================================================

-- Add full-text index on apps table for name and short_description
ALTER TABLE apps ADD FULLTEXT INDEX ft_apps_search (name, short_description);

-- Optional: Add full-text index on description as well (if needed)
-- ALTER TABLE apps ADD FULLTEXT INDEX ft_apps_description (description);

-- Test the full-text search
-- SELECT * FROM apps WHERE MATCH(name, short_description) AGAINST ('java spring' IN NATURAL LANGUAGE MODE);

-- =====================================================
-- Notes:
-- - Full-text search is much faster than LIKE queries
-- - Supports natural language search
-- - Can search multiple columns at once
-- - MySQL full-text minimum word length is 4 by default
--   (can be changed in my.cnf: ft_min_word_len=3)
-- =====================================================
