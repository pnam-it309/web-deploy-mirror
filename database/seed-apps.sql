-- =====================================================
-- Script: Seed Data for FPLHN-FACTORY Applications
-- Database: FPL-UDPM-Catalog
-- Created: 2026-01-08
-- Description: Insert sample apps from FPLHN-FACTORY
-- =====================================================

USE `catalog_web`;

-- =====================================================
-- 1. Insert Domains (Categories)
-- =====================================================
INSERT INTO `domains` (`id`, `name`, `slug`, `description`, `icon`, `color`, `parent_id`, `sort_order`, `status`, `created_at`, `updated_at`)
VALUES
    (UUID(), 'Education & Learning', 'education-learning', 'Educational management and learning platforms', 'fas fa-graduation-cap', '#4F46E5', NULL, 1, 'ACTIVE', NOW(), NOW()),
    (UUID(), 'Social & Community', 'social-community', 'Social networking and community platforms', 'fas fa-users', '#EC4899', NULL, 2, 'ACTIVE', NOW(), NOW()),
    (UUID(), 'Development Tools', 'development-tools', 'Developer productivity and coding tools', 'fas fa-code', '#10B981', NULL, 3, 'ACTIVE', NOW(), NOW()),
    (UUID(), 'Business Management', 'business-management', 'Business and enterprise management systems', 'fas fa-briefcase', '#F59E0B', NULL, 4, 'ACTIVE', NOW(), NOW()),
    (UUID(), 'Data & Analytics', 'data-analytics', 'Data analysis and indicator tracking', 'fas fa-chart-line', '#8B5CF6', NULL, 5, 'ACTIVE', NOW(), NOW())
ON DUPLICATE KEY UPDATE `updated_at` = NOW();

-- Get domain IDs for reference
SET @domain_education = (SELECT id FROM domains WHERE slug = 'education-learning' LIMIT 1);
SET @domain_social = (SELECT id FROM domains WHERE slug = 'social-community' LIMIT 1);
SET @domain_devtools = (SELECT id FROM domains WHERE slug = 'development-tools' LIMIT 1);
SET @domain_business = (SELECT id FROM domains WHERE slug = 'business-management' LIMIT 1);
SET @domain_analytics = (SELECT id FROM domains WHERE slug = 'data-analytics' LIMIT 1);

-- =====================================================
-- 2. Insert Technologies
-- =====================================================
INSERT INTO `technologies` (`id`, `name`, `icon`, `status`, `created_at`, `updated_at`)
VALUES
    (UUID(), 'Spring Boot', 'https://cdn.jsdelivr.net/gh/devicons/devicon/icons/spring/spring-original.svg', 'ACTIVE', NOW(), NOW()),
    (UUID(), 'Vue.js', 'https://cdn.jsdelivr.net/gh/devicons/devicon/icons/vuejs/vuejs-original.svg', 'ACTIVE', NOW(), NOW()),
    (UUID(), 'React', 'https://cdn.jsdelivr.net/gh/devicons/devicon/icons/react/react-original.svg', 'ACTIVE', NOW(), NOW()),
    (UUID(), 'MySQL', 'https://cdn.jsdelivr.net/gh/devicons/devicon/icons/mysql/mysql-original.svg', 'ACTIVE', NOW(), NOW()),
    (UUID(), 'Docker', 'https://cdn.jsdelivr.net/gh/devicons/devicon/icons/docker/docker-original.svg', 'ACTIVE', NOW(), NOW()),
    (UUID(), 'Redis', 'https://cdn.jsdelivr.net/gh/devicons/devicon/icons/redis/redis-original.svg', 'ACTIVE', NOW(), NOW()),
    (UUID(), 'WebSocket', 'https://cdn.jsdelivr.net/gh/devicons/devicon/icons/socketio/socketio-original.svg', 'ACTIVE', NOW(), NOW()),
    (UUID(), 'Java', 'https://cdn.jsdelivr.net/gh/devicons/devicon/icons/java/java-original.svg', 'ACTIVE', NOW(), NOW()),
    (UUID(), 'TypeScript', 'https://cdn.jsdelivr.net/gh/devicons/devicon/icons/typescript/typescript-original.svg', 'ACTIVE', NOW(), NOW()),
    (UUID(), 'Tailwind CSS', 'https://cdn.jsdelivr.net/gh/devicons/devicon/icons/tailwindcss/tailwindcss-plain.svg', 'ACTIVE', NOW(), NOW())
ON DUPLICATE KEY UPDATE `updated_at` = NOW();

-- Get technology IDs for reference
SET @tech_spring = (SELECT id FROM technologies WHERE name = 'Spring Boot' LIMIT 1);
SET @tech_vue = (SELECT id FROM technologies WHERE name = 'Vue.js' LIMIT 1);
SET @tech_react = (SELECT id FROM technologies WHERE name = 'React' LIMIT 1);
SET @tech_mysql = (SELECT id FROM technologies WHERE name = 'MySQL' LIMIT 1);
SET @tech_docker = (SELECT id FROM technologies WHERE name = 'Docker' LIMIT 1);
SET @tech_redis = (SELECT id FROM technologies WHERE name = 'Redis' LIMIT 1);
SET @tech_websocket = (SELECT id FROM technologies WHERE name = 'WebSocket' LIMIT 1);
SET @tech_java = (SELECT id FROM technologies WHERE name = 'Java' LIMIT 1);
SET @tech_typescript = (SELECT id FROM technologies WHERE name = 'TypeScript' LIMIT 1);
SET @tech_tailwind = (SELECT id FROM technologies WHERE name = 'Tailwind CSS' LIMIT 1);

-- =====================================================
-- 3. Insert Applications
-- =====================================================

-- App 1: FPL UDPM Confess Poly 2
SET @app_confess_id = UUID();
INSERT INTO `apps` (`id`, `domain_id`, `name`, `sku`, `short_description`, `thumbnail`, `view_count`, `is_featured`, `homepage_sort_order`, `approval_status`, `status`, `created_at`, `updated_at`)
VALUES
    (@app_confess_id, @domain_social, 'FPL UDPM Confess Poly 2', 'CONFESS-V2', 'Anonymous confession platform for FPT Polytechnic students to share thoughts, stories, and connect with peers safely.', 'https://raw.githubusercontent.com/FPLHN-FACTORY/fpl-udpm-confess-poly2/main/thumbnail.png', 0, TRUE, 1, 'APPROVED', 'ACTIVE', NOW(), NOW());

-- App 2: FPL Hotel Management System
SET @app_hotel_id = UUID();
INSERT INTO `apps` (`id`, `domain_id`, `name`, `sku`, `short_description`, `thumbnail`, `view_count`, `is_featured`, `homepage_sort_order`, `approval_status`, `status`, `created_at`, `updated_at`)
VALUES
    (@app_hotel_id, @domain_business, 'FPL Hotel Management System', 'HMS-2024', 'Comprehensive hotel management system for booking, check-in/out, room management, and customer service operations.', 'https://raw.githubusercontent.com/FPLHN-FACTORY/fpl-hotel-management-system/main/thumbnail.png', 0, TRUE, 2, 'APPROVED', 'ACTIVE', NOW(), NOW());

-- App 3: FPL Student Attendance
SET @app_attendance_id = UUID();
INSERT INTO `apps` (`id`, `domain_id`, `name`, `sku`, `short_description`, `thumbnail`, `view_count`, `is_featured`, `homepage_sort_order`, `approval_status`, `status`, `created_at`, `updated_at`)
VALUES
    (@app_attendance_id, @domain_education, 'FPL Student Attendance', 'ATTENDANCE-SYS', 'Smart attendance tracking system with QR code scanning, real-time monitoring, and automated reporting for classes.', 'https://raw.githubusercontent.com/FPLHN-FACTORY/fpl-student-attendance/main/thumbnail.png', 0, TRUE, 3, 'APPROVED', 'ACTIVE', NOW(), NOW());

-- App 4: FPL UDPM LeetSync
SET @app_leetsync_id = UUID();
INSERT INTO `apps` (`id`, `domain_id`, `name`, `sku`, `short_description`, `thumbnail`, `view_count`, `is_featured`, `homepage_sort_order`, `approval_status`, `status`, `created_at`, `updated_at`)
VALUES
    (@app_leetsync_id, @domain_devtools, 'FPL UDPM LeetSync', 'LEETSYNC-2024', 'Automated LeetCode problem synchronization and tracking tool for competitive programming practice and progress monitoring.', 'https://raw.githubusercontent.com/FPLHN-FACTORY/fpl-udpm-leet-sync/main/thumbnail.png', 0, TRUE, 4, 'APPROVED', 'ACTIVE', NOW(), NOW());

-- App 5: UDPM Indicator Explanation
SET @app_indicator_id = UUID();
INSERT INTO `apps` (`id`, `domain_id`, `name`, `sku`, `short_description`, `thumbnail`, `view_count`, `is_featured`, `homepage_sort_order`, `approval_status`, `status`, `created_at`, `updated_at`)
VALUES
    (@app_indicator_id, @domain_analytics, 'UDPM Indicator Explanation', 'INDICATOR-EXP', 'Comprehensive data indicator explanation and visualization platform for academic and business metrics analysis.', 'https://raw.githubusercontent.com/FPLHN-FACTORY/udpm-indicator-explanation/main/thumbnail.png', 0, TRUE, 5, 'APPROVED', 'ACTIVE', NOW(), NOW());

-- =====================================================
-- 4. Insert App Details
-- =====================================================

-- App Detail 1: Confess Poly 2
INSERT INTO `app_details` (`id`, `app_id`, `long_description`, `demo_url`, `source_url`, `specifications`, `status`, `created_at`, `updated_at`)
VALUES
    (UUID(), @app_confess_id, 
    'FPL UDPM Confess Poly 2 is an anonymous confession platform designed specifically for FPT Polytechnic students. The platform provides a safe and moderated space for students to share their thoughts, experiences, and stories anonymously.\n\nKey Features:\n- Anonymous posting with privacy protection\n- Real-time confession feed with WebSocket\n- Comment and reaction system\n- Content moderation and reporting\n- Tag-based categorization\n- Search and filter functionality\n- Admin dashboard for moderation\n\nTechnical Highlights:\n- Built with modern web technologies for optimal performance\n- Secure authentication and authorization\n- Real-time updates using WebSocket\n- Responsive design for mobile and desktop\n- Scalable architecture with microservices',
    'https://confess-poly.fplhn.tech',
    'https://github.com/FPLHN-FACTORY/fpl-udpm-confess-poly2.git',
    JSON_OBJECT(
        'version', '2.0.0',
        'architecture', 'Microservices',
        'database', 'MySQL 8.0',
        'authentication', 'JWT + OAuth2',
        'deployment', 'Docker + CI/CD',
        'features', JSON_ARRAY('Anonymous Posting', 'Real-time Feed', 'Moderation System', 'React System', 'Search & Filter')
    ),
    'ACTIVE', NOW(), NOW());

-- App Detail 2: Hotel Management System
INSERT INTO `app_details` (`id`, `app_id`, `long_description`, `demo_url`, `source_url`, `specifications`, `status`, `created_at`, `updated_at`)
VALUES
    (UUID(), @app_hotel_id,
    'FPL Hotel Management System is a comprehensive solution for managing hotel operations from booking to checkout. The system streamlines all aspects of hotel management including reservations, room management, billing, and customer service.\n\nKey Features:\n- Online booking and reservation system\n- Room availability and pricing management\n- Check-in/check-out automation\n- Guest management and CRM\n- Billing and invoicing\n- Housekeeping management\n- Reporting and analytics\n- Multi-property support\n\nTechnical Highlights:\n- Enterprise-grade architecture\n- Payment gateway integration\n- Email and SMS notifications\n- Role-based access control\n- Audit logging and compliance',
    'https://hotel.fplhn.tech',
    'https://github.com/FPLHN-FACTORY/fpl-hotel-management-system.git',
    JSON_OBJECT(
        'version', '1.5.0',
        'architecture', 'Monolithic + Microservices',
        'database', 'MySQL 8.0 + Redis',
        'payment', 'VNPay Integration',
        'deployment', 'Docker Swarm',
        'features', JSON_ARRAY('Booking System', 'Room Management', 'Billing', 'Housekeeping', 'Reports', 'Multi-property')
    ),
    'ACTIVE', NOW(), NOW());

-- App Detail 3: Student Attendance
INSERT INTO `app_details` (`id`, `app_id`, `long_description`, `demo_url`, `source_url`, `specifications`, `status`, `created_at`, `updated_at`)
VALUES
    (UUID(), @app_attendance_id,
    'FPL Student Attendance is a smart attendance tracking system that revolutionizes how educational institutions manage student attendance. Using QR code technology and real-time monitoring, it provides accurate and efficient attendance management.\n\nKey Features:\n- QR code-based attendance marking\n- Real-time attendance tracking\n- GPS location verification\n- Automated attendance reports\n- Student and teacher portals\n- Absence notification system\n- Analytics and insights dashboard\n- Export to Excel/PDF\n\nTechnical Highlights:\n- Mobile-first responsive design\n- QR code generation and validation\n- Real-time data synchronization\n- Offline capability\n- Integration with academic systems',
    'https://attendance.fplhn.tech',
    'https://github.com/FPLHN-FACTORY/fpl-student-attendance.git',
    JSON_OBJECT(
        'version', '3.2.0',
        'architecture', 'Progressive Web App',
        'database', 'MySQL 8.0',
        'qr_technology', 'ZXing Library',
        'deployment', 'Docker + Nginx',
        'features', JSON_ARRAY('QR Scanning', 'GPS Verification', 'Real-time Sync', 'Reports', 'Notifications', 'Offline Mode')
    ),
    'ACTIVE', NOW(), NOW());

-- App Detail 4: LeetSync
INSERT INTO `app_details` (`id`, `app_id`, `long_description`, `demo_url`, `source_url`, `specifications`, `status`, `created_at`, `updated_at`)
VALUES
    (UUID(), @app_leetsync_id,
    'FPL UDPM LeetSync is an automated tool for synchronizing and tracking LeetCode problem-solving progress. It helps students and professionals maintain their competitive programming practice and monitor their improvement over time.\n\nKey Features:\n- Automatic LeetCode submission synchronization\n- Progress tracking and statistics\n- Problem categorization by difficulty and topic\n- Personal coding streak tracking\n- Submission history and analysis\n- Performance insights and recommendations\n- Study plan generation\n- Integration with GitHub\n\nTechnical Highlights:\n- LeetCode API integration\n- Automated data scraping and parsing\n- GitHub repository synchronization\n- Data visualization and charts\n- Scheduled background jobs',
    'https://leetsync.fplhn.tech',
    'https://github.com/FPLHN-FACTORY/fpl-udpm-leet-sync.git',
    JSON_OBJECT(
        'version', '1.8.0',
        'architecture', 'Serverless + Scheduler',
        'database', 'MySQL 8.0 + MongoDB',
        'sync_frequency', '15 minutes',
        'deployment', 'AWS Lambda + CloudWatch',
        'features', JSON_ARRAY('Auto Sync', 'Progress Tracking', 'Statistics', 'GitHub Integration', 'Study Plans', 'Insights')
    ),
    'ACTIVE', NOW(), NOW());

-- App Detail 5: Indicator Explanation
INSERT INTO `app_details` (`id`, `app_id`, `long_description`, `demo_url`, `source_url`, `specifications`, `status`, `created_at`, `updated_at`)
VALUES
    (UUID(), @app_indicator_id,
    'UDPM Indicator Explanation is a comprehensive platform for explaining, tracking, and visualizing various data indicators used in academic and business contexts. It provides clear explanations, formulas, and real-time calculations of complex metrics.\n\nKey Features:\n- Comprehensive indicator database\n- Interactive formula explanations\n- Real-time calculation tools\n- Data visualization dashboards\n- Comparative analysis\n- Export and reporting\n- Custom indicator creation\n- API access for integrations\n\nTechnical Highlights:\n- Advanced data analytics engine\n- Interactive charting libraries\n- RESTful API architecture\n- Data caching for performance\n- Multi-language support',
    'https://indicators.fplhn.tech',
    'https://github.com/FPLHN-FACTORY/udpm-indicator-explanation.git',
    JSON_OBJECT(
        'version', '2.3.0',
        'architecture', 'API-First',
        'database', 'MySQL 8.0',
        'charting', 'Chart.js + D3.js',
        'deployment', 'Kubernetes',
        'features', JSON_ARRAY('Indicator Database', 'Formula Calc', 'Visualizations', 'Comparisons', 'Custom Indicators', 'API Access')
    ),
    'ACTIVE', NOW(), NOW());

-- =====================================================
-- 5. Link Technologies to Apps
-- =====================================================

-- Confess Poly 2 Technologies
INSERT INTO `app_technologies` (`app_id`, `technology_id`)
VALUES
    (@app_confess_id, @tech_spring),
    (@app_confess_id, @tech_react),
    (@app_confess_id, @tech_mysql),
    (@app_confess_id, @tech_websocket),
    (@app_confess_id, @tech_redis),
    (@app_confess_id, @tech_docker);

-- Hotel Management Technologies
INSERT INTO `app_technologies` (`app_id`, `technology_id`)
VALUES
    (@app_hotel_id, @tech_spring),
    (@app_hotel_id, @tech_vue),
    (@app_hotel_id, @tech_mysql),
    (@app_hotel_id, @tech_redis),
    (@app_hotel_id, @tech_docker);

-- Student Attendance Technologies
INSERT INTO `app_technologies` (`app_id`, `technology_id`)
VALUES
    (@app_attendance_id, @tech_spring),
    (@app_attendance_id, @tech_vue),
    (@app_attendance_id, @tech_mysql),
    (@app_attendance_id, @tech_docker),
    (@app_attendance_id, @tech_java);

-- LeetSync Technologies
INSERT INTO `app_technologies` (`app_id`, `technology_id`)
VALUES
    (@app_leetsync_id, @tech_spring),
    (@app_leetsync_id, @tech_react),
    (@app_leetsync_id, @tech_mysql),
    (@app_leetsync_id, @tech_docker),
    (@app_leetsync_id, @tech_typescript);

-- Indicator Explanation Technologies
INSERT INTO `app_technologies` (`app_id`, `technology_id`)
VALUES
    (@app_indicator_id, @tech_spring),
    (@app_indicator_id, @tech_vue),
    (@app_indicator_id, @tech_mysql),
    (@app_indicator_id, @tech_docker),
    (@app_indicator_id, @tech_java);

-- =====================================================
-- 6. Insert Sample App Images (Optional)
-- =====================================================

-- Confess Poly 2 Images
INSERT INTO `app_images` (`id`, `app_id`, `url`, `alt_text`, `sort_order`, `is_primary`, `status`, `created_at`, `updated_at`)
VALUES
    (UUID(), @app_confess_id, 'https://via.placeholder.com/1920x1080/4F46E5/FFFFFF?text=Confess+Poly+2+Screenshot+1', 'Confession Feed Interface', 1, TRUE, 'ACTIVE', NOW(), NOW()),
    (UUID(), @app_confess_id, 'https://via.placeholder.com/1920x1080/4F46E5/FFFFFF?text=Confess+Poly+2+Screenshot+2', 'Post Creation UI', 2, FALSE, 'ACTIVE', NOW(), NOW());

-- Hotel Management Images
INSERT INTO `app_images` (`id`, `app_id`, `url`, `alt_text`, `sort_order`, `is_primary`, `status`, `created_at`, `updated_at`)
VALUES
    (UUID(), @app_hotel_id, 'https://via.placeholder.com/1920x1080/F59E0B/FFFFFF?text=Hotel+Dashboard', 'Hotel Management Dashboard', 1, TRUE, 'ACTIVE', NOW(), NOW()),
    (UUID(), @app_hotel_id, 'https://via.placeholder.com/1920x1080/F59E0B/FFFFFF?text=Booking+System', 'Booking System Interface', 2, FALSE, 'ACTIVE', NOW(), NOW());

-- Student Attendance Images
INSERT INTO `app_images` (`id`, `app_id`, `url`, `alt_text`, `sort_order`, `is_primary`, `status`, `created_at`, `updated_at`)
VALUES
    (UUID(), @app_attendance_id, 'https://via.placeholder.com/1920x1080/10B981/FFFFFF?text=Attendance+QR+Scan', 'QR Code Scanning Interface', 1, TRUE, 'ACTIVE', NOW(), NOW()),
    (UUID(), @app_attendance_id, 'https://via.placeholder.com/1920x1080/10B981/FFFFFF?text=Attendance+Report', 'Attendance Report Dashboard', 2, FALSE, 'ACTIVE', NOW(), NOW());

-- LeetSync Images
INSERT INTO `app_images` (`id`, `app_id`, `url`, `alt_text`, `sort_order`, `is_primary`, `status`, `created_at`, `updated_at`)
VALUES
    (UUID(), @app_leetsync_id, 'https://via.placeholder.com/1920x1080/10B981/FFFFFF?text=LeetSync+Dashboard', 'LeetSync Progress Dashboard', 1, TRUE, 'ACTIVE', NOW(), NOW()),
    (UUID(), @app_leetsync_id, 'https://via.placeholder.com/1920x1080/10B981/FFFFFF?text=Problem+Stats', 'Problem Statistics View', 2, FALSE, 'ACTIVE', NOW(), NOW());

-- Indicator Explanation Images
INSERT INTO `app_images` (`id`, `app_id`, `url`, `alt_text`, `sort_order`, `is_primary`, `status`, `created_at`, `updated_at`)
VALUES
    (UUID(), @app_indicator_id, 'https://via.placeholder.com/1920x1080/8B5CF6/FFFFFF?text=Indicator+Dashboard', 'Indicator Analysis Dashboard', 1, TRUE, 'ACTIVE', NOW(), NOW()),
    (UUID(), @app_indicator_id, 'https://via.placeholder.com/1920x1080/8B5CF6/FFFFFF?text=Data+Visualization', 'Data Visualization Interface', 2, FALSE, 'ACTIVE', NOW(), NOW());

-- =====================================================
-- VERIFICATION QUERIES
-- =====================================================

-- Check inserted apps
SELECT 
    a.name AS app_name,
    d.name AS domain_name,
    a.sku,
    a.approval_status,
    COUNT(DISTINCT at.technology_id) AS tech_count,
    COUNT(DISTINCT ai.id) AS image_count
FROM apps a
LEFT JOIN domains d ON a.domain_id = d.id
LEFT JOIN app_technologies at ON a.id = at.app_id
LEFT JOIN app_images ai ON a.id = ai.app_id
WHERE a.sku IN ('CONFESS-V2', 'HMS-2024', 'ATTENDANCE-SYS', 'LEETSYNC-2024', 'INDICATOR-EXP')
GROUP BY a.id
ORDER BY a.homepage_sort_order;

-- Check app details
SELECT 
    a.name AS app_name,
    ad.demo_url,
    ad.source_url,
    JSON_EXTRACT(ad.specifications, '$.version') AS version
FROM apps a
JOIN app_details ad ON a.id = ad.app_id
WHERE a.sku IN ('CONFESS-V2', 'HMS-2024', 'ATTENDANCE-SYS', 'LEETSYNC-2024', 'INDICATOR-EXP')
ORDER BY a.homepage_sort_order;

-- =====================================================
-- COMPLETED SUCCESSFULLY
-- =====================================================
