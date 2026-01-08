-- =====================================================
-- Verification Script - Check Seeded Data
-- =====================================================

USE `catalog_web`;

-- 1. Summary Statistics
SELECT '=== SUMMARY STATISTICS ===' AS '';

SELECT 
    'Domains' AS entity,
    COUNT(*) AS total,
    SUM(CASE WHEN status = 'ACTIVE' THEN 1 ELSE 0 END) AS active_count
FROM domains
UNION ALL
SELECT 
    'Technologies' AS entity,
    COUNT(*) AS total,
    SUM(CASE WHEN status = 'ACTIVE' THEN 1 ELSE 0 END) AS active_count
FROM technologies
UNION ALL
SELECT 
    'Apps' AS entity,
    COUNT(*) AS total,
    SUM(CASE WHEN status = 'ACTIVE' THEN 1 ELSE 0 END) AS active_count
FROM apps
UNION ALL
SELECT 
    'App Details' AS entity,
    COUNT(*) AS total,
    SUM(CASE WHEN status = 'ACTIVE' THEN 1 ELSE 0 END) AS active_count
FROM app_details
UNION ALL
SELECT 
    'App Images' AS entity,
    COUNT(*) AS total,
    SUM(CASE WHEN status = 'ACTIVE' THEN 1 ELSE 0 END) AS active_count
FROM app_images
UNION ALL
SELECT 
    'App-Technology Links' AS entity,
    COUNT(*) AS total,
    NULL AS active_count
FROM app_technologies;

-- 2. Detailed Apps View
SELECT '=== APPS DETAILS ===' AS '';

SELECT 
    a.sku,
    a.name AS app_name,
    d.name AS domain,
    a.short_description,
    a.is_featured,
    a.approval_status,
    a.status,
    COUNT(DISTINCT at.technology_id) AS tech_count,
    COUNT(DISTINCT ai.id) AS image_count,
    a.created_at
FROM apps a
LEFT JOIN domains d ON a.domain_id = d.id
LEFT JOIN app_technologies at ON a.id = at.app_id
LEFT JOIN app_images ai ON a.id = ai.app_id
GROUP BY a.id
ORDER BY a.homepage_sort_order;

-- 3. Technologies per App
SELECT '=== TECHNOLOGIES PER APP ===' AS '';

SELECT 
    a.name AS app_name,
    GROUP_CONCAT(t.name ORDER BY t.name SEPARATOR ', ') AS technologies
FROM apps a
LEFT JOIN app_technologies at ON a.id = at.app_id
LEFT JOIN technologies t ON at.technology_id = t.id
GROUP BY a.id
ORDER BY a.homepage_sort_order;

-- 4. App Details with URLs
SELECT '=== APP URLS ===' AS '';

SELECT 
    a.name AS app_name,
    ad.demo_url,
    ad.source_url,
    JSON_EXTRACT(ad.specifications, '$.version') AS version,
    JSON_EXTRACT(ad.specifications, '$.architecture') AS architecture
FROM apps a
JOIN app_details ad ON a.id = ad.app_id
ORDER BY a.homepage_sort_order;

-- 5. Featured Apps
SELECT '=== FEATURED APPS ===' AS '';

SELECT 
    homepage_sort_order AS position,
    name,
    sku,
    is_featured,
    approval_status
FROM apps
WHERE is_featured = TRUE
ORDER BY homepage_sort_order;

-- 6. Domains with App Count
SELECT '=== DOMAINS WITH APP COUNT ===' AS '';

SELECT 
    d.name AS domain_name,
    d.slug,
    d.color,
    COUNT(a.id) AS app_count
FROM domains d
LEFT JOIN apps a ON d.id = a.domain_id AND a.status = 'ACTIVE'
GROUP BY d.id
ORDER BY d.sort_order;

-- 7. Most Used Technologies
SELECT '=== MOST USED TECHNOLOGIES ===' AS '';

SELECT 
    t.name AS technology,
    COUNT(at.app_id) AS usage_count,
    GROUP_CONCAT(a.name ORDER BY a.name SEPARATOR ', ') AS used_in_apps
FROM technologies t
LEFT JOIN app_technologies at ON t.id = at.technology_id
LEFT JOIN apps a ON at.app_id = a.id
GROUP BY t.id
ORDER BY usage_count DESC, t.name;

-- 8. Check JSON Specifications
SELECT '=== JSON SPECIFICATIONS SAMPLE ===' AS '';

SELECT 
    a.name AS app_name,
    JSON_PRETTY(ad.specifications) AS specifications
FROM apps a
JOIN app_details ad ON a.id = ad.app_id
LIMIT 2;

-- =====================================================
-- VERIFICATION COMPLETE
-- =====================================================
