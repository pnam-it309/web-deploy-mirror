-- Phase 2: Performance and Search Optimization
-- Merged from old V002 and V003

-- ===============================================================
-- HELPER PROCEDURES
-- ===============================================================
DROP PROCEDURE IF EXISTS create_index_if_not_exists;
DROP PROCEDURE IF EXISTS drop_index_if_exists;

DELIMITER $$

CREATE PROCEDURE create_index_if_not_exists(IN idx_name VARCHAR(255), IN tbl_name VARCHAR(255), IN idx_col_def VARCHAR(1000))
BEGIN
    DECLARE idx_exists INT DEFAULT 0;
    SELECT COUNT(*) INTO idx_exists FROM information_schema.statistics
    WHERE table_schema = DATABASE() AND table_name = tbl_name AND index_name = idx_name;
    IF idx_exists = 0 THEN
        SET @s = CONCAT('CREATE INDEX ', idx_name, ' ON ', tbl_name, ' ', idx_col_def);
        PREPARE stmt FROM @s; EXECUTE stmt; DEALLOCATE PREPARE stmt;
    END IF;
END $$

CREATE PROCEDURE drop_index_if_exists(IN idx_name VARCHAR(255), IN tbl_name VARCHAR(255))
BEGIN
    DECLARE idx_exists INT DEFAULT 0;
    DECLARE is_fk_index INT DEFAULT 0;
    SELECT COUNT(*) INTO idx_exists FROM information_schema.statistics
    WHERE table_schema = DATABASE() AND table_name = tbl_name AND index_name = idx_name;
    
    -- Check if index is part of a Foreign Key
    SELECT COUNT(*) INTO is_fk_index FROM information_schema.KEY_COLUMN_USAGE
    WHERE table_schema = DATABASE() AND table_name = tbl_name AND constraint_name = idx_name;

    IF idx_exists > 0 AND is_fk_index = 0 THEN
        SET @s = CONCAT('DROP INDEX ', idx_name, ' ON ', tbl_name);
        PREPARE stmt FROM @s; EXECUTE stmt; DEALLOCATE PREPARE stmt;
    END IF;
END $$

DELIMITER ;

-- ==========================================
-- 1. PERFORMANCE INDEXES
-- ==========================================
CALL create_index_if_not_exists('idx_app_status', 'apps', '(status)');
CALL create_index_if_not_exists('idx_app_view_count', 'apps', '(view_count DESC)');
CALL create_index_if_not_exists('idx_app_created_at', 'apps', '(created_at DESC)');
CALL create_index_if_not_exists('idx_app_featured', 'apps', '(is_featured, homepage_sort_order)');
CALL create_index_if_not_exists('idx_app_featured_video', 'apps', '(is_featured_video, created_at DESC)');
CALL create_index_if_not_exists('idx_app_domain_status', 'apps', '(domain_id, status, created_at DESC)');

CALL create_index_if_not_exists('idx_review_app_status', 'reviews', '(app_id, moderation_status, created_at DESC)');
CALL create_index_if_not_exists('idx_review_moderation', 'reviews', '(moderation_status, created_at DESC)');
CALL create_index_if_not_exists('idx_review_customer', 'reviews', '(customer_id, created_at DESC)');
CALL create_index_if_not_exists('idx_review_rating', 'reviews', '(app_id, rating)');

CALL create_index_if_not_exists('idx_audit_entity', 'audit_logs', '(entity_type, entity_id, created_at DESC)');
CALL create_index_if_not_exists('idx_audit_user', 'audit_logs', '(user_email, created_at DESC)');
CALL create_index_if_not_exists('idx_audit_created', 'audit_logs', '(created_at DESC)');

CALL create_index_if_not_exists('idx_app_tech_technology_composite', 'app_technologies', '(technology_id, app_id)');
CALL create_index_if_not_exists('idx_app_tech_app', 'app_technologies', '(app_id, technology_id)');

CALL create_index_if_not_exists('idx_customer_email', 'customers', '(email)');
CALL create_index_if_not_exists('idx_customer_status', 'customers', '(status)');

-- ==========================================
-- 2. FULL-TEXT SEARCH INDEXES
-- ==========================================
CALL drop_index_if_exists('ft_app_search', 'apps');
ALTER TABLE apps ADD FULLTEXT INDEX ft_app_search (name, sku, short_description);

-- ==========================================
-- CLEANUP
-- ==========================================
DROP PROCEDURE IF EXISTS create_index_if_not_exists;
DROP PROCEDURE IF EXISTS drop_index_if_exists;
