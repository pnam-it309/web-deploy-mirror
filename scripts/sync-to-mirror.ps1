# Script to sync code from Catalog to Mirror repository
# This script removes sensitive files before pushing to mirror

param(
    [string]$Branch = "main"
)

Write-Host "Starting sync to mirror repository..." -ForegroundColor Cyan
Write-Host "Branch: $Branch" -ForegroundColor Yellow

# Define sensitive files to remove
$sensitiveFiles = @(
    "BE/.env",
    "BE/src/main/resources/application.properties",
    "BE/src/main/resources/application-dev.properties",
    "BE/src/main/resources/application-prod.properties",
    "FE/.env",
    "FE/.env.local",
    "FE/.env.production"
)

try {
    # Step 1: Check current git status
    Write-Host "`nChecking current repository status..." -ForegroundColor Cyan
    $status = git status --porcelain
    if ($status) {
        Write-Host "Warning: You have uncommitted changes" -ForegroundColor Yellow
        git status --short
    }

    # Step 2: Fetch latest changes
    Write-Host "`nFetching latest changes from origin..." -ForegroundColor Cyan
    git fetch origin $Branch
    
    # Step 3: Pull latest changes
    Write-Host "Pulling latest changes..." -ForegroundColor Cyan
    git checkout $Branch
    git pull origin $Branch

    # Step 4: Create a temporary branch
    $mirrorBranch = "mirror-sync-temp"
    Write-Host "`nCreating temporary branch: $mirrorBranch" -ForegroundColor Cyan
    
    # Delete if exists
    git branch -D $mirrorBranch 2>$null
    
    git checkout -b $mirrorBranch

    # Step 5: Remove sensitive files
    Write-Host "`nRemoving sensitive files..." -ForegroundColor Cyan
    $removedFiles = @()
    foreach ($file in $sensitiveFiles) {
        if (Test-Path $file) {
            git rm --cached $file -f 2>$null
            if ($LASTEXITCODE -eq 0) {
                $removedFiles += $file
                Write-Host "  Removed: $file" -ForegroundColor Green
            }
        }
    }

    # Step 6: Create .gitignore entries
    Write-Host "`nUpdating .gitignore..." -ForegroundColor Cyan
    $gitignorePath = ".gitignore"
    $gitignoreContent = ""
    
    if (Test-Path $gitignorePath) {
        $gitignoreContent = Get-Content $gitignorePath -Raw
    }

    $patternsToAdd = @"

# Sensitive configuration files
*.env
!*.env.example
application.properties
application-*.properties
!*.properties.example
"@

    if (-not ($gitignoreContent -match "Sensitive configuration files")) {
        Add-Content -Path $gitignorePath -Value $patternsToAdd -Encoding UTF8
        git add $gitignorePath
        Write-Host "  Updated .gitignore" -ForegroundColor Green
    }

    # Step 7: Commit changes
    if ($removedFiles.Count -gt 0) {
        Write-Host "`nCommitting changes..." -ForegroundColor Cyan
        git commit -m "chore: remove sensitive files for mirror deployment" 2>$null
    }

    # Step 8: Show what will be pushed
    Write-Host "`nChanges to be pushed:" -ForegroundColor Cyan
    git log --oneline -5

    # Step 9: Push to mirror
    Write-Host "`nPushing to mirror repository..." -ForegroundColor Cyan
    Write-Host "This will FORCE PUSH to mirror/$Branch" -ForegroundColor Yellow
    $confirm = Read-Host "Continue? (y/n)"
    
    if ($confirm -eq 'y') {
        git push mirror $mirrorBranch:$Branch --force
        if ($LASTEXITCODE -eq 0) {
            Write-Host "`nSuccessfully synced to mirror repository!" -ForegroundColor Green
        } else {
            throw "Failed to push to mirror"
        }
    } else {
        Write-Host "Push cancelled" -ForegroundColor Red
    }

    # Step 10: Cleanup
    Write-Host "`nCleaning up..." -ForegroundColor Cyan
    git checkout $Branch
    git branch -D $mirrorBranch 2>$null
    Write-Host "Deleted temporary branch: $mirrorBranch" -ForegroundColor Green

    Write-Host "`nSync process completed!" -ForegroundColor Green

} catch {
    Write-Host "`nError: $_" -ForegroundColor Red
    Write-Host "Attempting to return to original branch..." -ForegroundColor Yellow
    git checkout $Branch 2>$null
    git branch -D $mirrorBranch 2>$null
    exit 1
}
