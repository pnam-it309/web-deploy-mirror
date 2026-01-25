# 🎨 Frontend (FE) - Vue 3 Application

## 📖 Important Documentation

### 🎯 [FRONTEND-ORGANIZATION-GUIDE.md](FRONTEND-ORGANIZATION-GUIDE.md)

**⭐ Đọc guide này trước khi code!**

Guide chi tiết về cách tổ chức code trong Frontend:

- 📂 Cách phân biệt `theme/`, `composable/`, và `utils/`
- ✅ Best practices và quy tắc đặt tên
- 📝 Ví dụ cụ thể từ dự án
- 🎯 Decision tree để biết đặt code ở đâu
- 🚀 Migration plan để refactor code hiện tại

---

## 🚀 Quick Start

### Development

```bash
# Install dependencies
npm install

# Start dev server
npm run dev

# App will run on http://localhost:5173
```

### Build for Production

```bash
# Build
npm run build

# Preview production build
npm run preview
```

### Linting & Type Checking

```bash
# Type check
npm run type-check

# Lint
npm run lint

# Lint and fix
npm run lint:fix
```

---

## 📁 Project Structure

```
FE/src/
├── main.ts                    # Entry point
├── App.vue                    # Root component
├── components/                # Vue components
│   ├── admin/                # Admin components
│   ├── client/               # Client components
│   ├── base/                 # Base reusable components
│   └── common/               # Shared components
├── pages/                     # Page components (routes)
│   ├── admin/                # Admin pages
│   ├── client/               # Client pages
│   └── auth/                 # Auth pages
├── composable/                # Vue composables (reactive logic)
│   ├── useBookmarks.ts
│   ├── useTheme.ts
│   └── ...
├── utils/                     # Pure utility functions
│   ├── format/
│   ├── storage/
│   └── ...
├── theme/                     # Design system & theme config
│   ├── colors.ts
│   ├── themeConfig.ts
│   └── index.css
├── stores/                    # Pinia stores
├── services/                  # API services
├── router/                    # Vue Router config
├── constants/                 # Application constants
├── types/                     # TypeScript types
└── layouts/                   # Layout components
```

**📖 For detailed structure explanation, see [FRONTEND-ORGANIZATION-GUIDE.md](FRONTEND-ORGANIZATION-GUIDE.md)**

---

## 🎨 Tech Stack

- **Framework**: Vue 3.5.13 (Composition API)
- **Language**: TypeScript 5.7.3
- **Build Tool**: Vite 6.0.11
- **State Management**: Pinia 2.3.0
- **Router**: Vue Router 4.5.0
- **HTTP Client**: Axios 1.7.9
- **UI Components**: Ant Design Vue 4.3.2
- **Styling**: TailwindCSS 4.0.0-alpha
- **Icons**: FontAwesome + @ant-design/icons-vue
- **Drag & Drop**: vuedraggable 4.1.0
- **Analytics**: Firebase + Google Analytics 4
- **Notifications**: Firebase Cloud Messaging

---

## 📋 Environment Variables

Create `.env.development.local` for local development:

```env
VITE_API_BASE_URL=http://localhost:9999
VITE_GOOGLE_CLIENT_ID=your-google-client-id
VITE_WS_BASE_URL=ws://localhost:9999
```

---

## 🎯 Code Organization Rules

### When to use `theme/`

- ✅ Color definitions
- ✅ Font configurations
- ✅ Spacing/sizing values
- ✅ Design tokens
- ❌ KHÔNG có logic hoặc functions

### When to use `composable/`

- ✅ Vue reactive state (`ref`, `reactive`)
- ✅ Vue lifecycle hooks
- ✅ Vue Router/Pinia usage
- ✅ Side effects (API calls, DOM)
- ❌ KHÔNG phải pure functions

### When to use `utils/`

- ✅ Pure functions
- ✅ No Vue dependencies
- ✅ Stateless
- ✅ Easy to test
- ❌ KHÔNG có reactive state

**📖 See [FRONTEND-ORGANIZATION-GUIDE.md](FRONTEND-ORGANIZATION-GUIDE.md) for detailed rules and examples**

---

## 💻 Recommended IDE Setup

- [VSCode](https://code.visualstudio.com/)
- [Volar](https://marketplace.visualstudio.com/items?itemName=Vue.volar) (and disable Vetur)
- [TypeScript Vue Plugin](https://marketplace.visualstudio.com/items?itemName=Vue.vscode-typescript-vue-plugin)

---

## 🧪 Testing

```bash
# Run unit tests
npm run test:unit

# Run e2e tests
npm run test:e2e

# Coverage report
npm run test:coverage
```

---

## 📚 Additional Resources

- [Frontend Organization Guide](FRONTEND-ORGANIZATION-GUIDE.md) - **Start here!**
- [Main Architecture Docs](../ARCHITECTURE.md) - Overall project architecture
- [Project README](../README.md) - Project overview
- [Deployment Guide](../docs/DEPLOYMENT.md) - Production deployment
- [Vite Configuration Reference](https://vite.dev/config/) - For build config

---

## 🤝 Contributing

Before contributing, please:

1. ⭐ Read [FRONTEND-ORGANIZATION-GUIDE.md](FRONTEND-ORGANIZATION-GUIDE.md)
2. Follow naming conventions
3. Write tests for new features
4. Run linter before committing

---

**Last Updated:** 2026-01-13  
**Framework Version:** Vue 3.5.13  
**Maintained By:** Frontend Team
