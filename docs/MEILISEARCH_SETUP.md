# Meilisearch Setup Guide

## 📦 What is Meilisearch?

Meilisearch is an **open-source, typo-tolerant search engine** that provides instant search results (<50ms). It's a FREE alternative to Algolia with self-hosting capability.

**Key Features:**
- ✅ Typo tolerance out of the box
- ✅ Instant search (<50ms)
- ✅ Faceted search & filtering
- ✅ Highlighting & snippeting
- ✅ **100% FREE** (self-hosted)

---

## 🚀 Quick Start

### 1. Start Meilisearch

```bash
# Start Meilisearch via Docker Compose
cd d:\FPL-UDPM-Catalog
docker-compose up -d meilisearch

# Check status
docker-compose logs meilisearch
```

Meilisearch will be available at: **http://localhost:7700**

### 2. Verify Installation

```powershell
# Run status check script
.\scripts\meilisearch-status.ps1
```

Expected output:
```
✅ Meilisearch is running!
Version: v1.6.x

📊 Current Indexes:
  - products: 0 documents (initially empty)

🎯 Meilisearch Admin UI: http://localhost:7700
```

---

## 🔧 Configuration

### Backend Configuration (Spring Boot)

File: `BE/src/main/resources/application.properties`

```properties
# Meilisearch Configuration
meilisearch.host=http://localhost:7700
meilisearch.apiKey=changeme_master_key_for_production
```

**Production Environment Variables:**
```bash
export MEILI_HOST=https://your-production-meilisearch.com
export MEILI_MASTER_KEY=your_secure_key_here
```

### Index Configuration

The `MeilisearchConfig` automatically initializes the index on startup with:

**Searchable Attributes (priority order):**
1. `name` → Product name (highest priority)
2. `summary` → Short description
3. `description` → Full description
4. `domain_name` → Domain/category
5. `technology_names` → Tech stack
6. `feature_names` → Product features

**Filterable Attributes:**
- `domain_id` → Filter by domain
- `technology_ids` → Filter by technologies
- `status` → Filter published/draft
- `is_featured` → Filter featured products

**Sortable Attributes:**
- `view_count` → Sort by popularity
- `published_at` → Sort by date

---

## 📖 Usage

### Backend API

#### 1. Search Endpoint

```http
GET /customer/search?q=vuejs&domainId=xxx&limit=20

Response:
{
  "data": {
    "hits": [
      {
        "id": "...",
        "name": "<em>VueJS</em> Dashboard",
        "summary": "Admin panel built with <em>Vue.js</em>",
        "domain_name": "Web Development",
        "technology_names": ["Vue 3", "TypeScript"],
        "view_count": 150,
        "is_featured": true
      }
    ],
    "query": "vuejs",
    "processingTimeMs": 2,
    "estimatedTotalHits": 5
  }
}
```

**Features:**
- ✅ Query với typo tolerance: `vuejs`, `vue.js`, `vu js` đều work
- ✅ Highlighting: `<em>` tags around matches
- ✅ Faceted filtering: Domain, Tech, Status
- ✅ Fast: <50ms response time

#### 2. Autocomplete/Suggest Endpoint

```http
GET /customer/search/suggest?q=vu

Response:
{
  "data": [
    { "name": "VueJS Dashboard", ... },
    { "name": "Vue E-commerce", ... }
  ]
}
```

### Frontend Component

```vue
<template>
  <InstantSearch />
</template>

<script setup>
import InstantSearch from '@/components/search/InstantSearch.vue'
</script>
```

**Features:**
- Debounced search (300ms)
- Highlighted matches
- Responsive grid layout
- Loading spinner
- Empty states

---

## 🔄 Auto-Sync Mechanism

Products are **automatically synced** to Meilisearch when:

1. **Product Created** → `ProductCreatedEvent` → Index product
2. **Product Updated** → `ProductUpdatedEvent` → Re-index product
3. **Product Deleted** → `ProductDeletedEvent` → Remove from index

**Implementation:**

```java
// AppServiceImpl.java
eventPublisher.publishEvent(
    new ProductCreatedEvent(savedApp)
);
```

**Listener:**

```java
@TransactionalEventListener
@Async
public void onProductCreated(ProductCreatedEvent event) {
    meilisearchService.indexProduct(event.getProduct());
}
```

---

## 🧪 Testing

### 1. Manual Testing

```bash
#Step 1: Khởi động Meilisearch
docker-compose up -d meilisearch

# Step 2: Khởi động Backend
cd BE
./gradlew bootRun

# Step 3: Create a product via Admin UI
# → Automatic indexing happens via event listener

# Step 4: Test search
curl "http://localhost:9999/customer/search?q=test"
```

### 2. Index All Existing Products

```java
// Admin API: Bulk re-index (to be implemented)
@PostMapping("/admin/search/reindex")
public ResponseEntity<?> reindexAll() {
    List<App> apps = appRepository.findAll();
    meilisearchService.indexProducts(apps);
    return ResponseEntity.ok("Indexed " + apps.size() + " products");
}
```

### 3. Frontend Testing

```bash
cd FE
npm run dev

# Navigate to search page
```

**Expected behavior:**
1. Type "vuejs" → See results instantly
2. Type "vu js" (typo) → Still finds "VueJS" products
3. Filter by domain → Results update
4. Highlighted matches → `<mark>` tags visible

---

## 📊 Monitoring

### View Index Stats

```powershell
# PowerShell
$meiliUrl = "http://localhost:7700"
$headers = @{ "Authorization" = "Bearer changeme_master_key_for_production" }

# Get all indexes
Invoke-RestMethod -Uri "$meiliUrl/indexes" -Headers $headers | ConvertTo-Json

# Get specific index stats
Invoke-RestMethod -Uri "$meiliUrl/indexes/products/stats" -Headers $headers
```

**Metrics to monitor:**
- `numberOfDocuments` → Total indexed products
- `isIndexing` → Currently indexing status
- `fieldDistribution` → Field usage statistics

---

## 🐛 Troubleshooting

### Problem: Meilisearch not connecting

**Solution:**
```bash
# Check if Meilisearch is running
docker ps | grep meilisearch

# Check logs
docker-compose logs meilisearch

# Restart Meilisearch
docker-compose restart meilisearch
```

### Problem: Products not appearing in search

**Possible causes:**
1. **Index not initialized** → Check backend logs for "✅ Meilisearch index initialized"
2. **Product status != PUBLISHED** → Only published products are searchable
3. **Event not triggered** → Check event listener logs

**Debug:**
```bash
# Check index document count
curl "http://localhost:7700/indexes/products/stats"

# Manual re-index
curl -X POST "http://localhost:9999/admin/search/reindex"
```

### Problem: Slow search performance

**Optimization tips:**
1. **Reduce displayed attributes** → Only return necessary fields
2. **Limit search results** → Default limit=20, max=100
3. **Use filters** → Narrow down search scope
4. **Enable caching** → Redis caching layer (already configured)

---

## 🚀 Production Deployment

### Using Managed Meilisearch Cloud (FREE TIER)

Meilisearch offers a FREE cloud tier:
- **meilisearch.com** → Sign up & deploy
- 100K searches/month FREE
- Auto-scaling & backups

**Configuration:**
```properties
# application-prod.properties
meilisearch.host=https://your-app.meilisearch.io
meilisearch.apiKey=YOUR_SEARCH_API_KEY
```

### Self-Hosting on VPS

**Deploy on $5/month VPS:**

```bash
# Install Meilisearch
curl -L https://install.meilisearch.com | sh

# Run as systemd service
sudo systemctl enable meilisearch
sudo systemctl start meilisearch
```

**Nginx reverse proxy:**
```nginx
location /search {
    proxy_pass http://127.0.0.1:7700;
}
```

---

## 📚 Additional Resources

- **Official Docs:** https://www.meilisearch.com/docs
- **GitHub:** https://github.com/meilisearch/meilisearch
- **Community Discord:** https://discord.meilisearch.com
- **API Reference:** https://www.meilisearch.com/docs/reference/api

---

## ✅ Next Steps

After Meilisearch is working, consider:

1. **Phase 1.1: Content-Based Recommendations**
   - Use TF-IDF similarity for "Similar Products"
   - Implement collaborative filtering

2. **Phase 1.3: AI Content Assistant**
   - Hugging Face API for SEO generation
   - Grammar checking with LanguageTool

3. **Phase 4: Analytics**
   - Track search queries & click-through rates
   - Popular keywords dashboard

---

**Need help?** Check the troubleshooting section or open an issue in the project repository.
