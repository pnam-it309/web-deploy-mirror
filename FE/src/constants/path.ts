export const ROUTES_CONSTANTS = {
  // Authentication
  // Authentication
  // LOGIN: {
  //   path: '/login',
  //   name: 'login',
  // },
  // SELECTION: {
  //   path: '/selection',
  //   name: 'selection',
  // },
  // AUTH: {
  //   path: '/auth',
  //   name: 'auth',
  //   children: {
  //     CALLBACK: { path: 'callback', name: 'auth-callback' },
  //     LOGOUT: { path: 'logout', name: 'auth-logout' },
  //   },
  // },

  // Admin
  ADMIN: {
    path: '/admin',
    name: 'admin',
    children: {
      DASHBOARD: { path: 'dashboard', name: 'admin-dashboard' },
      APPS: { path: 'apps', name: 'admin-apps' },
      DOMAINS: { path: 'domains', name: 'admin-domains' },
      TECHNOLOGIES: { path: 'technologies', name: 'admin-technologies' },
      FEATURES: { path: 'features', name: 'admin-features' },
    },
  },

  // Customer
  CUSTOMER: {
    path: '/',
    name: 'customer',
    children: {
      HOME: { path: '', name: 'home' },
      APPS: { path: 'apps', name: 'client-app-list' },
      APP_DETAIL: { path: 'apps/:id', name: 'client-app-detail' },
      DOMAINS: { path: 'domains', name: 'client-domain-list' },
      TECHNOLOGIES: { path: 'technologies', name: 'client-technology-list' },
    },
  },

  // Error & System Pages
  FORBIDDEN: { path: '/error/403', name: 'forbidden' },
  UNAUTHORIZED: { path: '/error/401', name: 'unauthorized' },
  NOT_FOUND: { path: '/:catchAll(.*)*', name: 'not-found' },
  REDIRECT: { path: '/redirect', name: 'redirect' },
  MAINTENANCE: { path: '/maintenance', name: 'maintenance' },
  COMING_SOON: { path: '/coming-soon', name: 'coming-soon' },
}
