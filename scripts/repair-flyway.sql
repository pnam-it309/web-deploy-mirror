-- Repair Flyway failed migrations
-- Run this script on production database when migration fails

USE catalog_web;

-- Check current flyway_schema_history status
SELECT version, description, type, script, checksum, installed_on, execution_time, success
FROM flyway_schema_history
ORDER BY installed_rank DESC
LIMIT 10;

-- Delete failed migration records (if any)
-- IMPORTANT: Only delete if you're SURE the migration should be re-run
DELETE FROM flyway_schema_history 
WHERE success = 0;

-- If you need to rollback to a specific version, delete newer versions
-- Example: To rollback to V1 and re-run V002 and later:
-- DELETE FROM flyway_schema_history WHERE version >= '002';

-- After repair, restart the backend application to re-run migrations
