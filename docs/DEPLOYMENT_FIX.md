# Hướng dẫn Fix Lỗi Production Deployment

## Vấn đề
- Server production đang chạy code cũ (chưa có fix EntityManagerFactory)
- Flyway migration V002 đã failed, cần repair database

## Giải pháp

### Bước 1: Repair Flyway Database

**Trên production server**, kết nối MySQL và chạy:

```sql
USE catalog_web;

-- Xem status các migrations
SELECT version, description, type, success, installed_on
FROM flyway_schema_history
ORDER BY installed_rank DESC;

-- Xóa các migrations failed
DELETE FROM flyway_schema_history WHERE success = 0;
```

**HOẶC** nếu cần reset hoàn toàn (⚠️ NGUY HIỂM - Mất data):

```sql
-- Drop toàn bộ database và tạo lại
DROP DATABASE IF EXISTS catalog_web;
CREATE DATABASE catalog_web CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### Bước 2: Pull Code Mới

**Trên production server**, chạy:

```bash
cd /path/to/FPL-UDPM-Catalog
git pull origin main
# Hoặc
git pull mirror main
```

Verify commit mới nhất:
```bash
git log --oneline -1
# Phải thấy: d903469 Fix: EntityManagerFactory startup error...
```

### Bước 3: Rebuild Docker Image

```bash
# Stop containers
docker-compose down

# Rebuild backend với --no-cache
docker-compose build --no-cache backend

# Start lại
docker-compose up -d
```

### Bước 4: Kiểm tra Logs

```bash
# Xem logs backend
docker logs -f udpm-backend

# Kiểm tra không còn lỗi:
# ✅ "Started BeApplication in X seconds"
# ❌ "jpaSharedEM_entityManagerFactory" (không được có)
# ❌ "Key column 'created_at' doesn't exist" (không được có)
```

## Fix đã thực hiện (commit d903469)

1. ✅ **BatchConfig.java** - Xóa custom transactionManager gây xung đột
2. ✅ **V005__insert_sample_data.sql** - Đổi tên từ V4 để tránh conflict
3. ✅ **docker-compose.yml** - Sửa Frontend API URL: `backend:9999` → `localhost:9999`

## Nếu vẫn lỗi

### Option A: Reset Database (Khuyến nghị nếu không có data quan trọng)

```bash
# Trong production environment
docker-compose exec udpm-mysql mysql -uroot -p123456 -e "DROP DATABASE IF EXISTS catalog_web; CREATE DATABASE catalog_web;"

# Restart backend để chạy migrations từ đầu
docker-compose restart backend
```

### Option B: Manual Check

```bash
# Kết nối MySQL
docker exec -it udpm-mysql mysql -uroot -p123456 catalog_web

# Kiểm tra schema
DESCRIBE apps;
# Phải có column: created_at BIGINT

# Nếu không có - bảng bị corrupt, cần recreate
```

## Checklist Hoàn tất

- [ ] Git pulled code mới nhất (commit d903469)
- [ ] Flyway schema_history đã clean (không có success=0)
- [ ] Docker images đã rebuilt
- [ ] Backend started successfully
- [ ] Không còn errors trong logs
- [ ] Frontend kết nối được API
