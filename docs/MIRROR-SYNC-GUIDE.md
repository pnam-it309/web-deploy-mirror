# Hướng dẫn đẩy code từ Catalog sang Mirror Repository

## 🎯 Tổng quan

Repository này có 2 remotes:

- **origin**: `https://github.com/FPLHN-FACTORY/FPL-UDPM-Catalog.git` (repo chính)
- **mirror**: `https://github.com/pnam-it309/web-deploy-mirror.git` (repo deploy)

## 🚀 Cách đẩy code sang Mirror

### Cách 1: Sử dụng script tự động (Khuyến nghị)

Chạy file `push-to-mirror.bat`:

```bash
.\push-to-mirror.bat
```

Script này sẽ:

- Push branch hiện tại lên mirror/main
- Tự động xử lý lỗi nếu có

### Cách 2: Thủ công

```bash
# Push branch main
git push mirror main

# Hoặc push branch hiện tại
git push mirror HEAD:main

# Force push (nếu cần)
git push mirror main --force
```

## ⚠️ Xử lý lỗi Push Protection

### Vấn đề: GitHub Secret Scanning chặn push

Nếu gặp lỗi:

```
remote: error: GH013: Repository rule violations found
remote: - Push cannot contain secrets
```

### Giải pháp:

1. **Đọc thông báo lỗi** để tìm các URL như:

   ```
   https://github.com/pnam-it309/web-deploy-mirror/security/secret-scanning/unblock-secret/xxxxx
   ```

2. **Mở từng URL** trong browser (phải đăng nhập GitHub)

3. **Click "Allow secret"** trên mỗi trang

4. **Chạy lại** lệnh push:
   ```bash
   .\push-to-mirror.bat
   ```

## 📝 Lưu ý quan trọng

### Files được bỏ qua (không push lên mirror):

- `BE/.env`
- `BE/src/main/resources/application.properties`
- `BE/src/main/resources/application-*.properties`
- `FE/.env`
- `FE/.env.local`
- `FE/.env.production`

### Files example được tạo sẵn:

- `BE/.env.example`
- `BE/src/main/resources/application.properties.example`
- `FE/.env.example`

## 🔒 Bảo mật

### Sau khi deploy lần đầu:

1. **Revoke** các Google OAuth credentials cũ trên Google Cloud Console
2. **Tạo mới** OAuth credentials cho production
3. **Cập nhật** vào file `.env` trên server (không commit vào git)

### Best practices:

- ❌ **KHÔNG BAO GIỜ** commit file `.env` hoặc `application.properties` có thật
- ✅ **LUÔN LUÔN** dùng file `.example` để làm template
- ✅ **LUÔN LUÔN** kiểm tra `.gitignore` trước khi commit

## 🔄 Workflow thông thường

```bash
# 1. Làm việc và commit trên branch origin
git add .
git commit -m "feat: your changes"
git push origin main

# 2. Khi cần deploy lên mirror
.\push-to-mirror.bat

# 3. Verify trên GitHub
# Mở: https://github.com/pnam-it309/web-deploy-mirror
```

## 📚 Scripts có sẵn

### `push-to-mirror.bat`

Script đơn giản để push nhanh, dùng khi không có thay đổi về file sensitive.

### `sync-to-mirror.bat`

Script đầy đủ để:

- Tạo branch tạm thời
- Remove các file sensitive
- Tạo file `.example`
- Push lên mirror

Dùng script này khi:

- Lần đầu setup mirror
- Có thay đổi lớn về cấu trúc config files
- Cần đảm bảo không có sensitive data trong lịch sử git

## ❓ Troubleshooting

### Lỗi: "failed to push some refs"

➜ Repository có branch protection rules, cần allow secrets (xem phần **Xử lý lỗi Push Protection**)

### Lỗi: "Updates were rejected"

```bash
# Force push (thận trọng!)
git push mirror main --force
```

### Lỗi: "Permission denied"

➜ Kiểm tra quyền truy cập vào repo `pnam-it309/web-deploy-mirror`

### Kiểm tra trạng thái mirror

```bash
# Xem commit mới nhất trên mirror
git ls-remote mirror main

# So sánh với local
git log --oneline -5
```

## 🎓 Tips & Tricks

### Xem danh sách remotes:

```bash
git remote -v
```

### Thêm remote mới (nếu chưa có):

```bash
git remote add mirror https://github.com/pnam-it309/web-deploy-mirror.git
```

### Xóa branch trên mirror:

```bash
git push mirror --delete branch-name
```

### Push tất cả branches lên mirror:

```bash
git push mirror --all
```

---

**Ngày tạo**: 2026-01-08  
**Lần cập nhật cuối**: 2026-01-08
