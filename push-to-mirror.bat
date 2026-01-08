@echo off
echo.
echo ========================================
echo   Quick Push to Mirror
echo ========================================
echo.
echo This will push the current branch to mirror/main
echo.

git push mirror HEAD:main --force

if errorlevel 1 (
    echo.
    echo ========================================
    echo   PUSH FAILED!
    echo ========================================
    echo.
    echo If you see "push protection" errors:
    echo 1. Open the URLs shown in the error message
    echo 2. Click "Allow secret" on each page
    echo 3. Run this script again
    echo.
) else (
    echo.
    echo ========================================
    echo   SUCCESS! Synced to mirror
    echo ========================================
    echo.
)

pause
