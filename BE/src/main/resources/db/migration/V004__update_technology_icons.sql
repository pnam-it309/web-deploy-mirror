-- Phase 4: Fix Data - Update Technology Icons
-- Fix broken icon for Tailwind CSS

UPDATE technologies 
SET icon = 'https://upload.wikimedia.org/wikipedia/commons/d/d5/Tailwind_CSS_Logo.svg' 
WHERE name LIKE '%Tailwind%';
