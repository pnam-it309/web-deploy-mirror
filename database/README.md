# Database Seed Scripts

## 📋 Mô tả

Thư mục này chứa các script SQL để seed (khởi tạo) dữ liệu mẫu cho database **FPL-UDPM-Catalog**.

## 📁 Files

### `seed-apps.sql`

Script chính để insert dữ liệu cho 5 ứng dụng từ FPLHN-FACTORY:

1. **FPL UDPM Confess Poly 2** - Nền tảng confession ẩn danh
2. **FPL Hotel Management System** - Hệ thống quản lý khách sạn
3. **FPL Student Attendance** - Hệ thống điểm danh sinh viên
4. **FPL UDPM LeetSync** - Công cụ đồng bộ LeetCode
5. **UDPM Indicator Explanation** - Nền tảng phân tích chỉ số

## 🚀 Cách sử dụng

### Option 1: Chạy từ MySQL Command Line

```bash
mysql -u root -p catalog_db < database/seed-apps.sql
```

### Option 2: Chạy từ MySQL Workbench

1. Mở MySQL Workbench
2. Connect đến database server
3. Mở file `seed-apps.sql`
4. Click **Execute** (⚡) hoặc nhấn `Ctrl + Shift + Enter`
5. Kiểm tra kết quả ở phần **Output**

### Option 3: Chạy từ Terminal với Docker

Nếu database đang chạy trong Docker:

```bash
# Copy file vào container
docker cp database/seed-apps.sql mysql-container:/tmp/

# Execute script
docker exec -i mysql-container mysql -u root -pYOUR_PASSWORD catalog_db < /tmp/seed-apps.sql

# Hoặc một lệnh:
cat database/seed-apps.sql | docker exec -i mysql-container mysql -u root -pYOUR_PASSWORD catalog_db
```

### Option 4: Chạy từ Spring Boot Application

Đặt file vào `src/main/resources/data.sql` hoặc tạo custom script loader:

```java
@Component
public class DatabaseSeeder implements ApplicationRunner {

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Resource resource = resourceLoader.getResource("classpath:database/seed-apps.sql");
        String sql = new String(Files.readAllBytes(Paths.get(resource.getURI())));
        jdbcTemplate.execute(sql);
    }
}
```

## 📊 Dữ liệu được tạo

### Domains (5 categories)

- Education & Learning
- Social & Community
- Development Tools
- Business Management
- Data & Analytics

### Technologies (10 công nghệ)

- Spring Boot
- Vue.js
- React
- MySQL
- Docker
- Redis
- WebSocket
- Java
- TypeScript
- Tailwind CSS

### Applications (5 apps)

Mỗi app bao gồm:

- ✅ Thông tin cơ bản (name, SKU, description)
- ✅ Chi tiết đầy đủ (long description, URLs, specifications)
- ✅ Liên kết với domain
- ✅ Công nghệ sử dụng
- ✅ Hình ảnh mẫu (2 images/app)

### App Details với JSON Specifications

Mỗi app có:

- Version
- Architecture
- Database
- Deployment method
- Features list
- Custom fields (payment, QR technology, etc.)

## 🔍 Verification Queries

Script tự động chạy 2 queries để verify:

### 1. Kiểm tra Apps

```sql
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
```

### 2. Kiểm tra App Details

```sql
SELECT
    a.name AS app_name,
    ad.demo_url,
    ad.source_url,
    JSON_EXTRACT(ad.specifications, '$.version') AS version
FROM apps a
JOIN app_details ad ON a.id = ad.app_id
WHERE a.sku IN ('CONFESS-V2', 'HMS-2024', 'ATTENDANCE-SYS', 'LEETSYNC-2024', 'INDICATOR-EXP')
ORDER BY a.homepage_sort_order;
```

## ⚠️ Lưu ý

### Before Running

1. **Backup database** trước khi chạy script (nếu có data quan trọng)
2. **Đảm bảo database tồn tại**: `catalog_db`
3. **Kiểm tra quyền**: User MySQL cần có quyền INSERT, UPDATE
4. **Thay đổi database name** nếu dùng tên khác (dòng 8)

### Duplicate Handling

Script sử dụng `ON DUPLICATE KEY UPDATE` cho:

- Domains
- Technologies

Để tránh lỗi khi chạy lại script nhiều lần.

### UUID Generation

Script sử dụng `UUID()` function của MySQL để generate IDs tự động.

## 🔄 Reset Database

Nếu muốn xóa tất cả data và chạy lại từ đầu:

```sql
-- Xóa data (giữ lại cấu trúc tables)
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE app_images;
TRUNCATE TABLE app_members;
TRUNCATE TABLE app_technologies;
TRUNCATE TABLE feature_media;
TRUNCATE TABLE features;
TRUNCATE TABLE app_details;
TRUNCATE TABLE apps;
TRUNCATE TABLE technologies;
TRUNCATE TABLE domains;
SET FOREIGN_KEY_CHECKS = 1;

-- Sau đó chạy lại seed script
SOURCE database/seed-apps.sql;
```

## 📝 Customization

### Thay đổi Thumbnail URLs

Tìm và thay thế placeholder URLs:

```sql
-- From:
'https://raw.githubusercontent.com/FPLHN-FACTORY/.../thumbnail.png'

-- To:
'https://your-cdn.com/images/app-thumbnail.png'
```

### Thay đổi Demo/Source URLs

Tìm `demo_url` và `source_url` trong phần App Details và cập nhật:

```sql
'demo_url', 'https://your-demo-url.com',
'source_url', 'https://github.com/your-org/your-repo.git',
```

### Thêm Custom Specifications

Trong JSON specifications, bạn có thể thêm fields tùy ý:

```sql
JSON_OBJECT(
    'version', '1.0.0',
    'custom_field', 'custom_value',
    'array_field', JSON_ARRAY('item1', 'item2'),
    'nested_object', JSON_OBJECT('key', 'value')
)
```

## 🐛 Troubleshooting

### Lỗi: "Unknown database 'catalog_db'"

```sql
CREATE DATABASE IF NOT EXISTS catalog_db;
USE catalog_db;
```

### Lỗi: "Table doesn't exist"

Chạy migrations trước:

```bash
./gradlew bootRun
# Hoặc
mvn spring-boot:run
```

### Lỗi: "Duplicate entry"

- Xóa data cũ hoặc
- Sửa constraint UNIQUE trong entity
- Sử dụng `INSERT IGNORE` thay vì `INSERT`

### Lỗi: "Cannot add foreign key constraint"

Kiểm tra:

1. Referenced table tồn tại
2. Referenced column có cùng data type
3. Foreign key chưa vi phạm referential integrity

## 📚 Related Files

- `schema.sql` - Database schema (nếu có)
- `migration/` - Flyway/Liquibase migrations
- `application.properties` - Database configuration

## 🎯 Testing

Sau khi seed data, test các endpoints:

```bash
# Get all apps
curl http://localhost:8080/api/customer/apps

# Get app by ID
curl http://localhost:8080/api/customer/apps/{id}

# Get apps by domain
curl http://localhost:8080/api/customer/apps?domain=education-learning

# Search apps
curl http://localhost:8080/api/customer/apps?search=attendance
```

---

**Created**: 2026-01-08  
**Last Updated**: 2026-01-08
