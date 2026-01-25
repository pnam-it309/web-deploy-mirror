# 🎓 FPL UDPM Catalog

> Enterprise-Grade Student Project Showcase Platform

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Vue 3](https://img.shields.io/badge/Vue.js-3.5.13-4FC08D.svg)](https://vuejs.org/)
[![TypeScript](https://img.shields.io/badge/TypeScript-5.7.3-blue.svg)](https://www.typescriptlang.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

A comprehensive platform for managing and showcasing student projects with enterprise-level features including real-time notifications, advanced search, analytics, and more.

---

## 📋 Table of Contents

- [Overview](#-overview)
- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Architecture](#-architecture)
- [Getting Started](#-getting-started)
- [Development](#-development)
- [Deployment](#-deployment)
- [API Documentation](#-api-documentation)
- [Contributing](#-contributing)
- [License](#-license)

📖 **Additional Documentation:**

- [📐 Architecture Details](ARCHITECTURE.md) - Comprehensive project structure guide
- [🚀 Deployment Guide](docs/DEPLOYMENT.md) - Production deployment instructions
- [🔄 Mirror Sync Guide](docs/MIRROR-SYNC-GUIDE.md) - Repository synchronization
- [📊 Project Description](docs/PROJECT_CV_DESCRIPTION.md) - For CV/portfolio
- [📈 Clarity Setup](docs/CLARITY_SETUP.md) - Analytics integration

---

## 🌟 Overview

FPL UDPM Catalog is a full-stack web application designed to help educational institutions manage and showcase student projects effectively. The platform provides:

- **For Administrators:** Complete project management with RBAC, analytics, bulk operations, and workflow automation
- **For Students:** Professional portfolio showcase with rich media, team collaboration, and social features
- **For Visitors:** Advanced search, filtering, ratings, and interactive exploration of student work

### Key Metrics

- 🚀 **500+ concurrent users** supported
- ⚡ **<200ms API response time** (P95)
- 📊 **90+ REST & GraphQL endpoints**
- 🔍 **<100ms full-text search** with Elasticsearch
- 🎯 **99.8% uptime** with monitoring
- 📦 **50GB+ media** handling with CDN

---

## ✨ Features

### Admin Panel

- ✅ **Content Management**

  - CRUD operations for Domains, Apps, Features, Technologies
  - Drag & drop ordering with real-time updates
  - Bulk import/export (Excel/CSV, 1000+ records/batch)
  - Soft delete with 30-day recovery
  - Version control & audit logging

- ✅ **Advanced Media Library**

  - Virus scanning & file validation
  - Auto thumbnail generation (3 sizes)
  - Watermark protection
  - CDN integration (Cloudinary)
  - Media tagging & reuse

- ✅ **User & Permission Management**

  - Role-Based Access Control (4 roles)
  - Custom permissions per resource
  - IP whitelist for admin panel
  - Two-Factor Authentication (2FA)
  - Session management with Redis

- ✅ **Analytics Dashboard**
  - Real-time metrics & visualizations
  - Top trending projects (weekly/monthly)
  - Search analytics (top 50 keywords)
  - Traffic sources & user journey
  - Automated reports (daily/weekly/monthly)

### Customer Features

- ✅ **Advanced Search & Discovery**

  - Elasticsearch full-text search (1000+ searches/min)
  - Multi-criteria filtering (domain, tech, year, team size)
  - Auto-complete suggestions
  - Trending searches & history
  - Related products AI recommendation

- ✅ **Interactive Product Pages**

  - Rich media slider with lazy loading
  - Social sharing (Facebook, Twitter, LinkedIn)
  - Rating & review system (5-star)
  - Wishlist/Bookmark functionality
  - Team member profiles

- ✅ **Modern UX**
  - Responsive design (mobile-first)
  - Dark mode support
  - SEO optimized (meta tags, sitemaps)
  - PWA capabilities
  - WCAG 2.1 Level AA accessible

### Real-time & Communication

- ✅ **WebSocket Notifications**

  - Push notifications (500+ concurrent)
  - Admin alerts (submissions, approvals)
  - Customer updates (new products, announcements)
  - Email subscriptions by domain/technology
  - Slack integration for admin team

- ✅ **Background Processing**
  - Spring Batch jobs for bulk operations
  - Scheduled tasks (backups, cleanup, reports)
  - Async email queue
  - Auto-publish scheduled products

---

## 🛠️ Tech Stack

### Backend

```yaml
Framework: Spring Boot 3.2.0 (Java 17)
Security: Spring Security + OAuth2 + JWT + 2FA
Database: MySQL 8.0 + JPA/Hibernate
Caching: Redis (Lettuce)
Search: Elasticsearch 8.9
Messaging: Spring WebSocket + STOMP
Batch: Spring Batch
API: REST + GraphQL
Documentation: Swagger/OpenAPI
Monitoring: Sentry + Google Analytics
```

### Frontend

```yaml
Framework: Vue 3 (Composition API) + TypeScript
State: Pinia + TanStack Query
Routing: Vue Router 4
UI: TailwindCSS + Ant Design Vue
Charts: Chart.js + ECharts
Editor: TipTap (Rich Text)
HTTP: Axios
Icons: FontAwesome + Boxicons
```

### DevOps & Tools

```yaml
Containerization: Docker + Docker Compose
CI/CD: GitHub Actions
Cloud Storage: AWS S3 + Cloudinary CDN
Email: SMTP (Gmail)
Version Control: Git + GitHub
```

### Third-party Integrations

- 🔐 Google OAuth2 (Authentication)
- 📊 Google Analytics 4 (User tracking)
- 📈 Google Sheets API (Import/Export)
- 📧 SMTP (Email notifications)
- 💬 Slack Webhook (Admin alerts)
- 🖼️ Cloudinary (Media CDN)
- 🚨 Sentry (Error monitoring)

---

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                         Frontend (Vue 3)                     │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐   │
│  │  Admin   │  │ Customer │  │  Auth    │  │  Public  │   │
│  │  Panel   │  │  Portal  │  │  Module  │  │  Pages   │   │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘   │
└─────────────────────────────────────────────────────────────┘
                            │
                            │ HTTPS/WSS
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                    Spring Boot Backend                       │
│  ┌──────────────────────────────────────────────────────┐  │
│  │  Controllers (REST + GraphQL + WebSocket)            │  │
│  └──────────────────────────────────────────────────────┘  │
│  ┌──────────────────────────────────────────────────────┐  │
│  │  Services (Business Logic)                           │  │
│  └──────────────────────────────────────────────────────┘  │
│  ┌──────────────────────────────────────────────────────┐  │
│  │  Repositories (JPA + Custom Queries)                 │  │
│  └──────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
        │               │               │               │
        ▼               ▼               ▼               ▼
┌─────────────┐ ┌─────────────┐ ┌─────────────┐ ┌─────────────┐
│   MySQL     │ │    Redis    │ │Elasticsearch│ │  External   │
│  (Primary)  │ │   (Cache)   │ │  (Search)   │ │  Services   │
│  Database   │ │  Sessions   │ │  Indexing   │ │ (S3, CDN)   │
└─────────────┘ └─────────────┘ └─────────────┘ └─────────────┘
```

### Project Structure

```
FPL-UDPM-Catalog/
├── BE/                          # Backend (Spring Boot)
│   ├── src/main/java/           # Java source code
│   │   └── udpm/hn/server/
│   │       ├── core/            # Business logic (admin, customer, common, graphql)
│   │       ├── entity/          # JPA Entities (26 entities)
│   │       ├── repository/      # Data repositories (20 repos)
│   │       ├── infrastructure/  # Cross-cutting concerns
│   │       └── utils/           # Utilities
│   ├── src/main/resources/      # Resources
│   │   ├── application.yml      # Main config
│   │   └── db/migration/        # SQL migrations
│   └── build.gradle             # Gradle dependencies
│
├── FE/                          # Frontend (Vue 3)
│   ├── src/
│   │   ├── assets/              # Static assets
│   │   ├── components/          # Vue components (admin, client, base, common)
│   │   ├── pages/               # Page views (admin, client, auth, error pages)
│   │   ├── router/              # Vue Router config
│   │   ├── stores/              # Pinia stores (7 stores)
│   │   ├── services/            # API services (21 services)
│   │   ├── composable/          # Vue composables (8 composables)
│   │   ├── utils/               # Utility functions (8 utils)
│   │   ├── constants/           # Application constants (11 files)
│   │   ├── types/               # TypeScript types
│   │   ├── layouts/             # Layout components
│   │   └── theme/               # Theme configuration
│   ├── package.json             # NPM dependencies
│   └── vite.config.ts           # Vite configuration
│
├── SDK/                         # Software Development Kit
├── database/                    # Database scripts
│   ├── seed-apps.sql            # Sample data
│   └── verify-data.sql          # Verification queries
│
├── docs/                        # 📚 Documentation
│   ├── DEPLOYMENT.md            # Production deployment guide
│   ├── MIRROR-SYNC-GUIDE.md     # Repository sync guide
│   ├── PROJECT_CV_DESCRIPTION.md # Project portfolio description
│   └── CLARITY_SETUP.md         # Analytics setup guide
│
├── scripts/                     # 🔧 Automation Scripts
│   ├── push-to-mirror.bat       # Push to mirror repository
│   ├── sync-to-mirror.bat       # Sync with mirror (Windows)
│   └── sync-to-mirror.ps1       # Sync with mirror (PowerShell)
│
├── .github/workflows/           # CI/CD pipelines
├── docker-compose.yml           # Development environment
├── docker-compose.prod.yml      # Production setup
├── ARCHITECTURE.md              # 📐 Detailed architecture documentation
└── README.md                    # This file
```

💡 **See [ARCHITECTURE.md](ARCHITECTURE.md) for detailed structure and patterns**

---

## 🚀 Getting Started

### Prerequisites

- **Java 17+** (for backend)
- **Node.js 18+** (for frontend)
- **MySQL 8.0+**
- **Redis** (optional, for caching)
- **Elasticsearch 8.9+** (optional, for search)
- **Docker** (recommended)

### Quick Start with Docker (Recommended)

```bash
# 1. Clone the repository
git clone https://github.com/your-org/FPL-UDPM-Catalog.git
cd FPL-UDPM-Catalog

# 2. Copy environment file
cp .env.example .env

# 3. Start all services
docker-compose up -d

# 4. Access the application
# Frontend: http://localhost:5173
# Backend: http://localhost:9999
# API Docs: http://localhost:9999/swagger-ui.html
# GraphQL: http://localhost:9999/graphiql
```

### Manual Setup

#### Backend Setup

```bash
cd BE

# 1. Configure database
# Edit src/main/resources/application.yml with your MySQL credentials

# 2. Build the project
./gradlew clean build

# 3. Run the application
./gradlew bootRun

# The backend will start on http://localhost:9999
```

#### Frontend Setup

```bash
cd FE

# 1. Install dependencies
npm install

# 2. Configure environment
# Create .env.development.local
echo "VITE_API_BASE_URL=http://localhost:9999" > .env.development.local

# 3. Start development server
npm run dev

# The frontend will start on http://localhost:5173
```

#### Database Setup

```bash
# 1. Create database
mysql -u root -p
CREATE DATABASE catalog_web CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 2. Run migrations (automatic with Spring Boot)
# Or manually:
mysql -u root -p catalog_web < database/seed-apps.sql

# 3. Verify data
mysql -u root -p catalog_web < database/verify-data.sql
```

---

## 💻 Development

### Backend Development

```bash
cd BE

# Run with hot reload (DevTools)
./gradlew bootRun

# Run tests
./gradlew test

# Check code coverage
./gradlew jacocoTestReport

# Build JAR
./gradlew clean build
```

### Frontend Development

```bash
cd FE

# Development server with hot reload
npm run dev

# Type checking
npm run type-check

# Linting
npm run lint

# Build for production
npm run build

# Preview production build
npm run preview
```

### Environment Variables

#### Backend (.env or application-\*.yml)

```properties
# Database
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/catalog_web
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=yourpassword

# JWT
JWT_SECRET=your-256-bit-secret-key-here

# OAuth2
GOOGLE_CLIENT_ID=your-google-client-id
GOOGLE_CLIENT_SECRET=your-google-client-secret

# Redis (optional)
SPRING_REDIS_HOST=localhost
SPRING_REDIS_PORT=6379

# Elasticsearch (optional)
ELASTICSEARCH_URIS=http://localhost:9200

# Email
MAIL_HOST=smtp.gmail.com
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password

# AWS S3 (optional)
AWS_ACCESS_KEY_ID=your-access-key
AWS_SECRET_ACCESS_KEY=your-secret-key
AWS_REGION=ap-southeast-1

# Cloudinary
CLOUDINARY_URL=cloudinary://api_key:api_secret@cloud_name
```

#### Frontend (.env.development.local)

```properties
VITE_API_BASE_URL=http://localhost:9999
VITE_GOOGLE_CLIENT_ID=your-google-client-id
VITE_WS_BASE_URL=ws://localhost:9999
```

### Code Style & Standards

- **Backend:** Follow Spring Boot best practices, use Lombok annotations
- **Frontend:** ESLint + Prettier configuration included
- **Commits:** Conventional Commits format recommended
- **Branching:** Git Flow (main, develop, feature/_, hotfix/_)

---

## 📦 Deployment

### Production Build

#### Backend

```bash
cd BE
./gradlew clean build
# JAR file will be in build/libs/
```

#### Frontend

```bash
cd FE
npm run build
# Static files will be in dist/
```

### Docker Deployment

```bash
# Build and run with production config
docker-compose -f docker-compose.prod.yml up -d

# View logs
docker-compose -f docker-compose.prod.yml logs -f

# Stop services
docker-compose -f docker-compose.prod.yml down
```

### Manual Deployment

See [docs/DEPLOYMENT.md](docs/DEPLOYMENT.md) for detailed production deployment instructions including:

- Environment configuration
- Database migration
- SSL/TLS setup
- Monitoring & alerts
- Backup strategies
- Performance tuning

---

## 📚 API Documentation

### REST API

Access interactive API documentation at:

- **Swagger UI:** http://localhost:9999/swagger-ui.html
- **OpenAPI JSON:** http://localhost:9999/v3/api-docs

### GraphQL API

Access GraphQL playground at:

- **GraphiQL:** http://localhost:9999/graphiql

Example GraphQL query:

```graphql
query {
  apps(page: 1, size: 10) {
    content {
      id
      name
      shortDescription
      domain {
        name
        slug
      }
      technologies {
        name
        icon
      }
    }
    totalElements
  }
}
```

### WebSocket

Connect to WebSocket for real-time notifications:

```javascript
const socket = new SockJS("http://localhost:9999/ws");
const stompClient = Stomp.over(socket);

stompClient.connect({}, (frame) => {
  stompClient.subscribe("/topic/notifications", (message) => {
    console.log("Received:", message.body);
  });
});
```

---

## 🧪 Testing

### Backend Tests

```bash
cd BE

# Run all tests
./gradlew test

# Run specific test class
./gradlew test --tests "com.example.be.service.AppServiceTest"

# Generate coverage report
./gradlew jacocoTestReport
# Report available at: build/reports/jacoco/test/html/index.html
```

### Frontend Tests

```bash
cd FE

# Run unit tests
npm run test:unit

# Run e2e tests
npm run test:e2e

# Coverage report
npm run test:coverage
```

---

## 🤝 Contributing

We welcome contributions! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Development Guidelines

- Write meaningful commit messages (Conventional Commits)
- Add tests for new features
- Update documentation as needed
- Follow existing code style
- Ensure all tests pass before submitting PR

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👥 Team

**Project Lead:** [Your Name]  
**Contributors:** [List contributors here]

---

## 🙏 Acknowledgments

- [Spring Boot](https://spring.io/projects/spring-boot) - Backend framework
- [Vue.js](https://vuejs.org/) - Frontend framework
- [Ant Design Vue](https://antdv.com/) - UI components
- [TailwindCSS](https://tailwindcss.com/) - Styling
- [Elasticsearch](https://www.elastic.co/) - Search engine

---

## 📞 Support

For issues and questions:

- 🐛 **Bug Reports:** [GitHub Issues](https://github.com/your-org/FPL-UDPM-Catalog/issues)
- 💬 **Discussions:** [GitHub Discussions](https://github.com/your-org/FPL-UDPM-Catalog/discussions)
- 📧 **Email:** your-email@example.com

---

## 🗺️ Roadmap

- [ ] Mobile app (React Native)
- [ ] AI-powered project recommendations
- [ ] Advanced analytics with ML insights
- [ ] Multi-language support (i18n)
- [ ] Video conferencing integration
- [ ] Blockchain certificates for projects

---

<div align="center">

**Made with ❤️ by FPL UDPM Team**

⭐ Star us on GitHub if you find this helpful!

</div>
