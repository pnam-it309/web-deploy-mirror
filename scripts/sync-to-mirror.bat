@echo off
REM Script to sync code from Catalog to Mirror repository
REM This script removes sensitive files before pushing to mirror

echo ========================================
echo   Sync to Mirror Repository
echo ========================================
echo.

REM Step 1: Reset to clean state
echo [1/8] Resetting to clean state...
git reset --hard origin/main
if errorlevel 1 goto error

REM Step 2: Create temporary branch
echo [2/8] Creating temporary branch...
git branch -D mirror-sync-temp 2>nul
git checkout -b mirror-sync-temp
if errorlevel 1 goto error

REM Step 3: Remove sensitive files
echo [3/8] Removing sensitive files...
git rm --cached BE/.env 2>nul
git rm --cached "BE/src/main/resources/application.properties" 2>nul
git rm --cached "BE/src/main/resources/application-dev.properties" 2>nul
git rm --cached "BE/src/main/resources/application-prod.properties" 2>nul
git rm --cached FE/.env 2>nul
git rm --cached FE/.env.local 2>nul
git rm --cached FE/.env.production 2>nul

REM Step 4: Create example files
echo [4/8] Creating example configuration files...

REM Create BE/.env.example
echo # Database Configuration> BE\.env.example
echo DB_HOST=localhost>> BE\.env.example
echo DB_PORT=3306>> BE\.env.example
echo DB_NAME=catalog_db>> BE\.env.example
echo DB_USERNAME=root>> BE\.env.example
echo DB_PASSWORD=your_password>> BE\.env.example
echo.>> BE\.env.example
echo # JWT Configuration>> BE\.env.example
echo JWT_SECRET=your_jwt_secret_key_here>> BE\.env.example
echo JWT_EXPIRATION=86400000>> BE\.env.example

git add BE/.env.example

REM Create application.properties.example
echo # Spring Application Configuration> BE\src\main\resources\application.properties.example
echo spring.application.name=catalog-backend>> BE\src\main\resources\application.properties.example
echo.>> BE\src\main\resources\application.properties.example
echo # Database>> BE\src\main\resources\application.properties.example
echo spring.datasource.url=jdbc:mysql://localhost:3306/catalog_db>> BE\src\main\resources\application.properties.example
echo spring.datasource.username=root>> BE\src\main\resources\application.properties.example
echo spring.datasource.password=password>> BE\src\main\resources\application.properties.example

git add BE/src/main/resources/application.properties.example

REM Create FE/.env.example
echo # API Configuration> FE\.env.example
echo VITE_API_BASE_URL=http://localhost:8080/api>> FE\.env.example
echo VITE_API_TIMEOUT=30000>> FE\.env.example

git add FE/.env.example

REM Step 5: Update .gitignore
echo [5/8] Updating .gitignore...
echo.>> .gitignore
echo # Sensitive configuration files>> .gitignore
echo *.env>> .gitignore
echo !*.env.example>> .gitignore
echo application.properties>> .gitignore
echo application-*.properties>> .gitignore
echo !*.properties.example>> .gitignore

git add .gitignore

REM Step 6: Commit changes
echo [6/8] Committing changes...
git commit -m "chore: remove sensitive files for mirror deployment"
if errorlevel 1 (
    echo No changes to commit, continuing...
)

REM Step 7: Show what will be pushed
echo.
echo [7/8] Recent commits:
echo ----------------------------------------
git log --oneline -5
echo ----------------------------------------
echo.

REM Step 8: Push to mirror
echo [8/8] Ready to push to mirror repository
echo.
echo WARNING: This will FORCE PUSH to mirror/main
echo.
set /p confirm="Continue with push? (y/n): "

if /i "%confirm%"=="y" (
    echo.
    echo Pushing to mirror...
    git push mirror mirror-sync-temp:main --force
    if errorlevel 1 goto push_error
    
    echo.
    echo ========================================
    echo   SUCCESS! Code synced to mirror
    echo ========================================
    goto cleanup
) else (
    echo Push cancelled.
    goto cleanup
)

:push_error
echo.
echo ========================================
echo   PUSH FAILED!
echo ========================================
echo.
echo The mirror repository may have protection rules.
echo Please check:
echo 1. Repository settings on GitHub
echo 2. Branch protection rules
echo 3. File path protection rules
echo.
goto cleanup

:error
echo.
echo ========================================
echo   ERROR OCCURRED!
echo ========================================
goto cleanup

:cleanup
echo.
echo Cleaning up...
git checkout main
git branch -D mirror-sync-temp 2>nul
echo.
echo Done!
pause
