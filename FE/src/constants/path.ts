export const ROUTES_CONSTANTS = {
  // Authentication
  LOGIN: {
    path: '/login',
    name: 'login',
  },

  // Admin
  ADMIN: {
    path: '/admin',
    name: 'admin',
    children: {
      // Dashboard
      DASHBOARD: { path: 'dashboard', name: 'admin-dashboard' },
      //import
      IMPORT: { path: 'import', name: 'import_data' },

      // Product Management
      PRODUCTS: { path: 'products', name: 'admin-products' },
      PRODUCT_CREATE: { path: 'products/create', name: 'admin-product-create' },
      PRODUCT_EDIT: { path: 'products/:id/edit', name: 'admin-product-edit' },

      // Category Management
      CATEGORIES: { path: 'categories', name: 'admin-categories' },

      // Order Management
      ORDERS: { path: 'orders', name: 'admin-orders' },
      ORDER_DETAIL: { path: 'orders/:id', name: 'admin-order-detail' },

      // Customer Management
      CUSTOMERS: { path: 'customers', name: 'admin-customers' },
      CUSTOMER_DETAIL: { path: 'customers/:id', name: 'admin-customer-detail' },

      // Staff Management
      STAFF: { path: 'staff', name: 'admin-staff' },
      STAFF_CREATE: { path: 'staff/create', name: 'admin-staff-create' },
      STAFF_EDIT: { path: 'staff/:id/edit', name: 'admin-staff-edit' },

      // Settings & Profile
      SETTINGS: { path: 'settings', name: 'admin-settings' },
      PROFILE: { path: 'profile', name: 'admin-profile' },
    },
  },

  // Customer
  CUSTOMER: {
    path: '/',
    name: 'customer',
    children: {
      // Main Pages
      HOME: { path: '', name: 'customer-home' },
      PRODUCTS: { path: 'products', name: 'customer-products' },
      PRODUCT_DETAIL: { path: 'products/:id', name: 'customer-product-detail' },

      // User Account
      CART: { path: 'cart', name: 'customer-cart' },
      ORDERS: { path: 'orders', name: 'customer-orders' },
      ORDER_DETAIL: { path: 'orders/:id', name: 'customer-order-detail' },
      PROFILE: { path: 'profile', name: 'customer-profile' },
      ADDRESSES: { path: 'addresses', name: 'customer-addresses' },

      // Help & Support
      CONTACT: { path: 'contact', name: 'customer-contact' },
      FAQ: { path: 'faq', name: 'customer-faq' },
      ABOUT: { path: 'about', name: 'customer-about' },
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
