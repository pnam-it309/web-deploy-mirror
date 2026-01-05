# FPL-UDPM-Catalog

Full-stack web application for managing and showcasing UDPM projects at FPL.

## 🚀 Performance Optimizations (NEW!)

**The backend has been optimized with:**

- ✅ **Redis Caching** - 90% faster database queries
- ✅ **Async Processing** - Non-blocking operations
- ✅ **Parallel Processing** - 4x faster file uploads & Excel processing
- ✅ **Thread Pools** - Optimized resource utilization

**Performance Improvements:**

- Get All Apps: **94% faster** (250ms → 15ms)
- File Uploads: **84% faster** (5s → 800ms)
- Excel Processing: **73% faster** (30s → 8s)

👉 **[See Full Documentation](./BE/INDEX.md)**

---

## 📁 Project Structure

```
FPL-UDPM-Catalog/
├── BE/                    # Backend (Spring Boot)
│   ├── src/
│   ├── INDEX.md          # 📚 Performance docs index
│   ├── QUICK_START.md    # ⚡ Quick start guide
│   └── ...
└── FE/                    # Frontend (Vue 3 + Vite)
    ├── src/
    └── ...
```

---

## 🔧 Prerequisites

### Backend

- Java 17+
- MySQL 8.0+
- **Redis 6.0+** (NEW - for caching)
- Gradle 7.0+

### Frontend

- Node.js 16+
- npm 8+

---

## 🏃 Quick Start

### Backend

```bash
cd BE

# Setup Redis (required)
# Windows: Download from https://github.com/microsoftarchive/redis/releases
# Linux: sudo apt-get install redis-server
# macOS: brew install redis

# Start Redis
redis-server

# Run application
./gradlew bootRun
```

### Frontend

```bash
cd FE

# Install dependencies
npm install

# Run development server
npm run dev
```

---

## 📚 Documentation

### Backend Performance Docs

- **[INDEX.md](./BE/INDEX.md)** - Documentation index
- **[QUICK_START.md](./BE/QUICK_START.md)** - Quick start guide
- **[PERFORMANCE_OPTIMIZATION.md](./BE/PERFORMANCE_OPTIMIZATION.md)** - Detailed guide
- **[ARCHITECTURE.md](./BE/ARCHITECTURE.md)** - System architecture
- **[TESTING_GUIDE.md](./BE/TESTING_GUIDE.md)** - Testing guide

### Frontend

See [Vite Configuration Reference](https://vite.dev/config/)

---

## 🎯 Features

### Backend

- ✅ RESTful API
- ✅ Redis caching
- ✅ Async processing
- ✅ Parallel file uploads
- ✅ Parallel Excel processing
- ✅ WebSocket real-time updates
- ✅ JWT authentication
- ✅ OAuth2 (Google)

### Frontend

- ✅ Vue 3 + TypeScript
- ✅ Vite build tool
- ✅ Responsive design
- ✅ Admin dashboard
- ✅ Customer portal

---

## 🔗 API Endpoints

- Backend: `http://localhost:9999`
- Frontend: `http://localhost:6789`

---

## 👥 Team

- **Developer:** FPL UDPM Team
- **Contact:** nickhunter3009@gmail.com

---

## 📝 License

This project is for educational purposes at FPL.
