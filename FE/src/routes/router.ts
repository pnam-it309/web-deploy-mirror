import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router'
import { ROUTES_CONSTANTS } from '@/constants/path'
import { ROLES } from '@/constants/roles'
import { useAuthStore } from '@/stores/auth'

// Import the ProductsPage component
const ProductsPage = () => import('@/pages/admin/product/ProductsPage.vue')
// Import the CustomerList component
const CustomerList = () => import('@/pages/admin/customers/CustomerList.vue')

// Error pages
const ForbiddenPage = () => import('@/pages/403/Forbidden.vue')
const UnauthorizedPage = () => import('@/pages/401/Unauthorized.vue')
const NotFoundPage = () => import('@/pages/404/NotFound.vue')

// Admin components
const AdminLayout = () => import('@/layouts/AdminLayout.vue')
const AdminDashboard = () => import('@/pages/admin/dashboard/AdminDashboard.vue')
const WebSocketTest = () => import('@/pages/admin/websocket-test.vue')

// Customer components
const CustomerLayout = () => import('@/layouts/CustomerLayout.vue')
const HomePage = () => import('@/pages/customer/HomePage.vue')

// Selection Page
const SelectionPage = () => import('@/pages/log/SelectionPage.vue')

// Routes configuration
const ROUTES = {
  ...ROUTES_CONSTANTS,
  SELECTION: {
    path: '/',
    name: 'selection',
  },
  ADMIN: {
    ...ROUTES_CONSTANTS.ADMIN,
    name: 'admin',
    children: {
      ...ROUTES_CONSTANTS.ADMIN.children,
      DASHBOARD: {
        ...ROUTES_CONSTANTS.ADMIN.children.DASHBOARD,
        name: 'admin-dashboard'
      }
    }
  },
  CUSTOMER: {
    ...ROUTES_CONSTANTS.CUSTOMER,
    name: 'customer',
    children: {
      ...ROUTES_CONSTANTS.CUSTOMER.children,
      HOME: {
        ...ROUTES_CONSTANTS.CUSTOMER.children.HOME,
        name: 'customer-dashboard'
      }
    }
  }
}

const routes: RouteRecordRaw[] = [
  // Selection Page as root
  {
    path: ROUTES.SELECTION.path,
    name: ROUTES.SELECTION.name,
    component: SelectionPage,
    meta: {
      requiresAuth: false,
      title: 'Chọn chế độ đăng nhập',
      layout: 'default'
    }
  },

  // Admin routes
  {
    path: ROUTES.ADMIN.path,
    component: AdminLayout,
    meta: {
      requiresAuth: false, // Changed to false to bypass login
      requiresRole: ROLES.ADMIN,
      title: 'Admin'
    },
    children: [
      {
        path: '',
        redirect: { name: ROUTES.ADMIN.children.DASHBOARD.name }
      },
      {
        path: ROUTES.ADMIN.children.DASHBOARD.path,
        name: ROUTES.ADMIN.children.DASHBOARD.name,
        component: AdminDashboard,
        meta: { 
          title: 'Tổng quan',
          requiresAuth: true,
          requiresRole: ROLES.ADMIN
        }
      },
      {
        path: ROUTES.ADMIN.children.IMPORT.path,
        name: ROUTES.ADMIN.children.IMPORT.name,
        component: WebSocketTest,
        meta: { 
          title: 'Nhập dữ liệu',
          requiresAuth: true,
          requiresRole: ROLES.ADMIN
        }
      },
      {
        path: ROUTES.ADMIN.children.PRODUCTS.path,
        name: ROUTES.ADMIN.children.PRODUCTS.name,
        component: ProductsPage,
        meta: { 
          title: 'Quản lý sản phẩm',
          requiresAuth: true,
          requiresRole: ROLES.ADMIN
        }
      },
      {
        path: ROUTES.ADMIN.children.PRODUCT_CREATE.path,
        name: ROUTES.ADMIN.children.PRODUCT_CREATE.name,
        component: () => import('@/pages/admin/product/ProductCreateModal.vue'),
        meta: { 
          title: 'Thêm sản phẩm mới',
          requiresAuth: true,
          requiresRole: ROLES.ADMIN
        }
      },
      // Customers Management
      {
        path: ROUTES.ADMIN.children.CUSTOMERS.path,
        name: ROUTES.ADMIN.children.CUSTOMERS.name,
        component: CustomerList,
        meta: { 
          title: 'Quản lý khách hàng',
          requiresAuth: true,
          requiresRole: ROLES.ADMIN
        }
      }
    ]
  },

  // Customer routes
  {
    path: ROUTES.CUSTOMER.path,
    name: ROUTES.CUSTOMER.name,
    component: CustomerLayout,
    meta: {
      requiresAuth: true,
      requiresRole: ROLES.CUSTOMER,
      title: 'Trang chủ',
      layout: 'customer',
    },
    children: [
      {
        path: '',
        name: ROUTES.CUSTOMER.children.HOME.name,
        component: HomePage,
        meta: { title: 'Trang chủ' },
      },
    ],
  },

  // Error pages
  {
    path: ROUTES.NOT_FOUND.path,
    name: ROUTES.NOT_FOUND.name,
    component: NotFoundPage,
    meta: { layout: 'error' },
  },
  {
    path: ROUTES.FORBIDDEN.path,
    name: ROUTES.FORBIDDEN.name,
    component: ForbiddenPage,
    meta: { layout: 'error' },
  },
  {
    path: ROUTES.UNAUTHORIZED.path,
    name: ROUTES.UNAUTHORIZED.name,
    component: UnauthorizedPage,
    meta: { layout: 'error' },
  },

  // Catch all route - must be last
  {
    path: '/:pathMatch(.*)*',
    redirect: { name: ROUTES.NOT_FOUND.name },
  },
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition
    } else {
      return { top: 0 }
    }
  },
})

// Navigation guard
router.beforeEach(async (to, from, next) => {
  const authStore = useAuthStore()
  const isAuthenticated = authStore.isAuthenticated
  const userRole = authStore.userRole || authStore.user?.roleScreen
  const requiresAuth = to.matched.some(record => record.meta.requiresAuth)
  const requiresRole = to.meta.requiresRole as string | undefined
  
  // Set page title
  document.title = to.meta.title
    ? `${to.meta.title} | ${import.meta.env.VITE_APP_NAME || 'FPL UDPM Catalog'}`
    : import.meta.env.VITE_APP_NAME || 'FPL UDPM Catalog'
  
  // Skip auth check for admin routes
  if (to.path.startsWith('/admin')) {
    return next()
  }
  
  // Redirect to login if not authenticated and route requires auth
  if (requiresAuth && !isAuthenticated) {
    return next({
      name: ROUTES.LOGIN.name,
      query: { redirect: to.fullPath },
    })
  }
  
  // Check if user has required role
  if (requiresRole && userRole !== requiresRole) {
    return next({ name: ROUTES.FORBIDDEN.name })
  }
  
  // Continue with the navigation
  next()
  if (isAuthenticated && !userRole && to.name !== 'select-role') {
    return next({ name: 'select-role' })
  }

  // Redirect to appropriate dashboard if already authenticated and trying to access auth pages
  const authPages = [ROUTES.LOGIN.name, ROUTES.REGISTER.name]
  if (authPages.includes(String(to.name)) && isAuthenticated) {
    if (userRole === ROLES.ADMIN) {
      return next({ name: ROUTES.ADMIN.children.DASHBOARD.name })
    } else {
      return next({ name: ROUTES.CUSTOMER.children.HOME.name })
    }
  }

  // Check role-based access
  if (requiresRole && userRole !== requiresRole) {
    if (userRole === ROLES.ADMIN) {
      return next({ name: ROUTES.ADMIN.children.DASHBOARD.name })
    } else {
      return next({ name: ROUTES.CUSTOMER.children.HOME.name })
    }
  }

  // Only allow admin and customer roles
  if (isAuthenticated && userRole && !['admin', 'customer'].includes(userRole.toLowerCase())) {
    await authStore.logout()
    return next({
      name: ROUTES.LOGIN.name,
      query: { error: 'unauthorized_role' },
    })
  }

  next()
})

export default router
