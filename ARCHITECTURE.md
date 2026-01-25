# 🏗️ FPL-UDPM-Catalog - Architecture Documentation

## 📁 Project Structure Overview

```
FPL-UDPM-Catalog/
├── BE/                     # Backend - Spring Boot Application
├── FE/                     # Frontend - Vue 3 + TypeScript
├── SDK/                    # Software Development Kit
├── database/               # Database scripts & migrations
├── docs/                   # 📚 Documentation files
├── scripts/                # 🔧 Automation scripts
├── .github/                # GitHub Actions workflows
├── docker-compose.yml      # Development docker setup
├── docker-compose.prod.yml # Production docker setup
└── README.md              # Main project documentation
```

---

## 🎯 Backend Architecture (BE)

### Directory Structure

```
BE/src/main/java/udpm/hn/server/
├── BeApplication.java           # Spring Boot entry point
├── core/                        # 🎯 Business Logic Layer
│   ├── admin/                   # Admin module (18 sub-modules)
│   │   ├── analytics/           # Search & usage analytics
│   │   ├── announcement/        # System announcements
│   │   ├── app/                 # Application CRUD
│   │   ├── audit/               # Audit logging
│   │   ├── customer/            # Customer management
│   │   ├── dashboard/           # Admin dashboard & homepage config
│   │   ├── domain/              # Domain management (with drag-drop ordering)
│   │   ├── export/              # Data export functionality
│   │   ├── feature/             # Feature management (with drag-drop ordering)
│   │   ├── media/               # Media library
│   │   ├── moderation/          # Content moderation
│   │   ├── preview/             # App preview
│   │   ├── role/                # Role & permission management
│   │   ├── security/            # Security settings
│   │   ├── subscription/        # Subscription management
│   │   ├── technology/          # Technology stack management
│   │   ├── trash/               # Soft delete management
│   │   └── webhook/             # Webhook configuration
│   ├── customer/                # Customer-facing module
│   │   ├── app/                 # App browsing & search
│   │   ├── auth/                # Authentication (OAuth2, JWT)
│   │   └── profile/             # User profile
│   ├── common/                  # Shared business logic
│   │   └── auth/                # Common auth utilities
│   └── graphql/                 # GraphQL API endpoint
├── entity/                      # 🗃️ JPA Entities (26 entities)
├── repository/                  # 📊 Data Access Layer (20 repositories)
├── infrastructure/              # 🔧 Cross-cutting Concerns
│   ├── config/                  # Spring configurations
│   ├── security/                # Security configurations (JWT, OAuth2)
│   ├── constant/                # Application constants
│   ├── exception/               # Global exception handling
│   └── listener/                # Event listeners
└── utils/                       # 🛠️ Utility Classes
```

### Module Pattern

Each business module follows this structure:

```
<module>/
├── controller/                  # REST Controllers (@RestController)
├── service/                     # Business Logic (@Service)
│   └── impl/                    # Service implementations
├── model/                       # Domain models (if needed)
│   ├── request/                 # Request DTOs
│   └── response/                # Response DTOs
└── dto/                         # Alternative to model/ in some modules
    ├── request/
    └── response/
```

### Key Technologies

- **Framework**: Spring Boot 3.x
- **Security**: Spring Security + JWT + OAuth2
- **Database**: JPA/Hibernate + PostgreSQL
- **API**: REST + GraphQL
- **Validation**: Hibernate Validator

---

## 🎨 Frontend Architecture (FE)

### Directory Structure

```
FE/src/
├── main.ts                      # Application entry point
├── App.vue                      # Root component
├── assets/                      # 🎨 Static assets
│   ├── images/
│   ├── css/
│   └── fonts/
├── components/                  # 🧩 Vue Components
│   ├── admin/                   # Admin-specific components (22 components)
│   │   ├── dashboard/
│   │   ├── domain/              # DomainTable with drag-drop
│   │   ├── feature/             # FeatureTable with drag-drop
│   │   ├── app/
│   │   └── ...
│   ├── client/                  # Customer-facing components (10 components)
│   │   ├── home/
│   │   ├── product/
│   │   └── ...
│   ├── base/                    # Reusable base components (15 components)
│   │   ├── BaseButton.vue
│   │   ├── BaseInput.vue
│   │   ├── BaseModal.vue
│   │   └── ...
│   ├── common/                  # Shared components (15 components)
│   │   ├── Header.vue
│   │   ├── Footer.vue
│   │   └── ...
│   └── auth/                    # Authentication components
├── pages/                       # 📄 Page Components (Routing)
│   ├── admin/                   # Admin pages (20 pages)
│   │   ├── dashboard/
│   │   ├── domain/
│   │   ├── feature/
│   │   ├── app/
│   │   └── ...
│   ├── client/                  # Customer pages (7 pages)
│   │   ├── HomePage.vue
│   │   ├── ProductDetailPage.vue
│   │   └── ...
│   ├── auth/                    # Auth pages
│   ├── 401/                     # Unauthorized page
│   ├── 403/                     # Forbidden page
│   └── 404/                     # Not Found page
├── router/                      # 🛣️ Vue Router
│   ├── index.ts                 # Main router config
│   ├── admin.routes.ts          # Admin routes
│   └── client.routes.ts         # Client routes
├── stores/                      # 🗄️ Pinia State Management (7 stores)
│   ├── auth.store.ts
│   ├── app.store.ts
│   ├── cart.store.ts
│   └── ...
├── services/                    # 🌐 API Services (21 services)
│   ├── api/
│   │   ├── admin/
│   │   └── customer/
│   ├── request.ts               # Axios instance & interceptors
│   └── ...
├── composable/                  # 🎣 Vue Composables (Reactive Logic)
│   ├── useBookmarks.ts          # Bookmark functionality
│   ├── useCompare.ts            # Product comparison
│   ├── useGoogleAnalytics.ts   # GA4 integration
│   ├── usePushNotifications.ts # Firebase notifications
│   ├── useTableAnimations.ts   # Table animations
│   ├── useTheme.ts              # Theme switching
│   ├── useWebSocket.ts          # WebSocket connection
│   └── usetableHeight.ts        # Dynamic table height
├── utils/                       # 🛠️ Pure Utility Functions
│   ├── commom.helper.ts         # Common helpers
│   ├── eventBus.ts              # Event bus
│   ├── filters.ts               # Vue filters
│   ├── slug.ts                  # URL slug generation
│   ├── storage.ts               # LocalStorage wrapper
│   ├── store.ts                 # Store utilities
│   ├── token.helper.ts          # JWT token handling
│   └── urlActivityHelper.ts     # URL tracking
├── constants/                   # 📋 Application Constants (11 files)
│   ├── routes.ts
│   ├── api.ts
│   ├── storagekey.ts
│   └── ...
├── types/                       # 📐 TypeScript Type Definitions
├── layouts/                     # 🏛️ Layout Components
│   ├── AdminLayout.vue
│   ├── ClientLayout.vue
│   └── AuthLayout.vue
├── theme/                       # 🎨 Theme Configuration
│   ├── colors.ts
│   └── variables.ts
└── firebase.ts                  # Firebase configuration
```

### Key Technologies

- **Framework**: Vue 3 (Composition API)
- **Language**: TypeScript
- **State**: Pinia
- **Routing**: Vue Router
- **HTTP**: Axios
- **UI/UX**: Custom components + drag-drop (vuedraggable)
- **Analytics**: Google Analytics 4
- **Notifications**: Firebase Cloud Messaging

### Component Architecture

**Base Components**: Generic, highly reusable

- No business logic
- Props-driven
- Used across all modules

**Common Components**: Shared but with some context

- Header, Footer, Navigation
- Used by both admin & client

**Feature Components**: Domain-specific

- Admin components (tables, forms, dashboards)
- Client components (product cards, filters)

---

## 🔄 Data Flow

### Customer Flow

```
User → Client Page → Composable (state) → Service (API) → Backend Controller
                                                              ↓
User ← Client Page ← Store (Pinia) ←────────────────────── Response
```

### Admin Flow

```
Admin → Admin Page → Component (form/table) → Service → Backend Controller
                                                           ↓
Admin ← Admin Page ← Component ← ─────────────────────── Response
```

### Drag & Drop Ordering Flow

```
User drags item → VueDraggable emits @change → handleReorder()
                                                    ↓
                                          API: POST /api/admin/{module}/reorder
                                                    ↓
                                          Backend updates order_index
                                                    ↓
                                          Success ← UI reflects new order
```

---

## 🗂️ Naming Conventions

### Backend

- **Entities**: PascalCase (e.g., `App`, `Domain`, `Feature`)
- **DTOs**: `<Entity><Type>Request/Response` (e.g., `AppCreateRequest`)
- **Services**: `<Entity>Service` + `impl/` for implementations
- **Controllers**: `<Entity>Controller`
- **Repositories**: `<Entity>Repository`

### Frontend

- **Components**: PascalCase (e.g., `DomainTable.vue`, `BaseButton.vue`)
- **Pages**: `<Feature>Page.vue` (e.g., `AdminDomainIndex.vue`)
- **Composables**: `use<Feature>.ts` (e.g., `useBookmarks.ts`)
- **Services**: `<feature>.service.ts` (e.g., `domain.service.ts`)
- **Stores**: `<feature>.store.ts` (e.g., `auth.store.ts`)

---

## 📚 Documentation Files (in `/docs`)

- **CLARITY_SETUP.md**: Setup guide for Clarity Analytics
- **DEPLOYMENT.md**: Production deployment instructions
- **MIRROR-SYNC-GUIDE.md**: Guide for syncing with mirror repository
- **PROJECT_CV_DESCRIPTION.md**: Project description for CV/portfolio

---

## 🔧 Scripts (in `/scripts`)

- **push-to-mirror.bat**: Push changes to mirror repo
- **sync-to-mirror.bat**: Sync with mirror repo (Windows)
- **sync-to-mirror.ps1**: Sync with mirror repo (PowerShell)

---

## 🐳 Docker Setup

### Development

- `docker-compose.yml`: PostgreSQL + pgAdmin

### Production

- `docker-compose.prod.yml`:
  - PostgreSQL
  - Backend service
  - Frontend service (Nginx)

---

## 🔐 Security Architecture

### Authentication Methods

1. **Username/Password**: Traditional login
2. **Google OAuth2**: Social login
3. **JWT Tokens**: Stateless authentication

### Authorization

- **Role-Based Access Control (RBAC)**
  - ADMIN role: Full access
  - CUSTOMER role: Limited access
- **Permission-based**: Fine-grained permissions per endpoint

### Security Layers

- CORS configuration
- JWT token validation
- Refresh token mechanism
- OAuth2 authentication flow
- XSS protection
- CSRF protection (for cookies)

---

## 🎯 Key Features by Module

### Admin Features

✅ Domain Management (CRUD + ordering)
✅ Feature Management (CRUD + ordering)
✅ App Management (CRUD + media)
✅ Technology Stack Management
✅ Homepage Configuration (drag-drop featured apps)
✅ Media Library (upload, validation, thumbnails)
✅ Search Analytics
✅ Role & Permission Management
✅ Content Moderation
✅ Audit Logging

### Customer Features

✅ App Browsing & Search
✅ Product Detail View
✅ Related Products
✅ Bookmarks
✅ Product Comparison
✅ Google OAuth2 Login
✅ Firebase Push Notifications

---

## 📊 Database

See `/database` folder for:

- Schema migrations
- Seed data (`data.sql`)
- ER diagrams (if available)

---

## 🚀 Getting Started

See main [README.md](../README.md) for:

- Prerequisites
- Installation steps
- Running locally
- Building for production

---

## 📝 Notes

### Why separate `composable/` and `utils/`?

- **composable/**: Vue-specific reactive logic using Composition API
  - Has access to Vue lifecycle, refs, computed, watch
  - Example: `useBookmarks()` returns reactive bookmark state
- **utils/**: Pure JavaScript/TypeScript functions
  - No Vue dependencies
  - Can be used anywhere (even in non-Vue code)
  - Example: `slugify()` just transforms strings

**📖 For detailed guidelines with examples, see [FE/FRONTEND-ORGANIZATION-GUIDE.md](FE/FRONTEND-ORGANIZATION-GUIDE.md)**

### Why keep `theme/` separate from `assets/`?

- **theme/**: Configuration files (colors, variables)
  - Imported in JS/TS files
  - Used programmatically
- **assets/**: Static files (images, fonts, CSS)
  - Processed by build tools
  - Referenced in templates

**📖 For theme organization best practices, see [FE/FRONTEND-ORGANIZATION-GUIDE.md](FE/FRONTEND-ORGANIZATION-GUIDE.md)**

---

**Last Updated**: 2026-01-13
**Maintained By**: Development Team
