# Meilisearch Admin Script
# Run this after starting docker-compose to initialize the search index

# 1. Start Meilisearch (if not already running)
docker-compose up -d meilisearch

# 2. Wait for Meilisearch to be ready
Write-Host "Waiting for Meilisearch to start..." -ForegroundColor Yellow
Start-Sleep -Seconds 5

# 3. Test Meilisearch health
$meilisearchUrl = "http://localhost:7700"
try {
    $health = Invoke-RestMethod -Uri "$meilisearchUrl/health" -Method Get
    Write-Host "✅ Meilisearch is running!" -ForegroundColor Green
    Write-Host "Version: $($health.version)" -ForegroundColor Cyan
} catch {
    Write-Host "❌ Failed to connect to Meilisearch at $meilisearchUrl" -ForegroundColor Red
    Write-Host "Please check docker-compose logs: docker-compose logs meilisearch" -ForegroundColor Yellow
    exit 1
}

# 4. Get index stats
try {
    $indexes = Invoke-RestMethod -Uri "$meilisearchUrl/indexes" -Method Get -Headers @{
        "Authorization" = "Bearer changeme_master_key_for_production"
    }
    
    Write-Host "`n📊 Current Indexes:" -ForegroundColor Cyan
    $indexes.results | ForEach-Object {
        Write-Host "  - $($_.uid): $($_.numberOfDocuments) documents" -ForegroundColor White
    }
} catch {
    Write-Host "⚠️ Could not fetch index stats" -ForegroundColor Yellow
}

Write-Host "`n🎯 Meilisearch Admin UI: http://localhost:7700" -ForegroundColor Green
Write-Host "📚 Docs: https://www.meilisearch.com/docs" -ForegroundColor Cyan
