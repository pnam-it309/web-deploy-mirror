import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router'
import { ROUTES_CONSTANTS } from '@/constants/path'
import { ROLES } from '@/constants/roles'
import { useAuthStore } from '@/stores/auth'

// Lazy load components for better performance
const LoginPage = () => import('@/pages/auth/LoginPage.vue')
const RegisterPage = () => import('@/pages/auth/RegisterPage.vue')
const RoleSelection = () => import('@/pages/auth/RoleSelection.vue')

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


// Add REGISTER to ROUTES_CONSTANTS
const ROUTES = {
  ...ROUTES_CONSTANTS,
  REGISTER: {
    path: '/register',
    name: 'register',
  },
}

const routes: RouteRecordRaw[] = [
  // Root redirect to login
  {
    path: '/',
    redirect: { name: ROUTES.LOGIN.name },
  },

  // Auth routes
  {
    path: ROUTES.LOGIN.path,
    name: ROUTES.LOGIN.name,
    component: LoginPage,
    meta: {
      requiresAuth: false,
      title: 'Đăng nhập',
      layout: 'auth',
    },
  },
  {
    path: ROUTES.REGISTER.path,
    name: ROUTES.REGISTER.name,
    component: RegisterPage,
    meta: {
      requiresAuth: false,
      title: 'Đăng ký',
      layout: 'auth',
    },
  },
  {
    path: '/select-role',
    name: 'select-role',
    component: RoleSelection,
    meta: {
      requiresAuth: true,
      title: 'Chọn vai trò',
      layout: 'auth',
    },
  },

  // Admin routes
  {
    path: ROUTES.ADMIN.path,
    name: ROUTES.ADMIN.name,
    component: AdminLayout,
    meta: {
      requiresAuth: true,
      requiresRole: ROLES.ADMIN,
      title: 'Admin Dashboard',
      layout: 'admin',
    },
    children: [
      {
        path: '',
        redirect: { name: ROUTES.ADMIN.children.DASHBOARD.name },
      },
      {
        path: ROUTES.ADMIN.children.DASHBOARD.path,
        name: ROUTES.ADMIN.children.DASHBOARD.name,
        component: AdminDashboard,
        meta: { title: 'Tổng quan' },
      },
      {
        path: '/admin/websocket-test',
        name: 'websocket-test',
        component: WebSocketTest,
        meta: { title: 'WebSocket Test' },
      },
    ],
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
  const requiresAuth = to.matched.some((record) => record.meta.requiresAuth)
  const requiresRole = to.meta.requiresRole as string | undefined
  const isAuthenticated = authStore.isAuthenticated
  const userRole = authStore.userRole || authStore.user?.roleSwitch

  // Set page title if available
  document.title = to.meta.title
    ? `${to.meta.title} | ${import.meta.env.VITE_APP_NAME || 'FPL UDPM Catalog'}`
    : import.meta.env.VITE_APP_NAME || 'FPL UDPM Catalog'

  // Redirect to login if not authenticated and route requires auth
  if (requiresAuth && !isAuthenticated) {
    return next({
      name: ROUTES.LOGIN.name,
      query: { redirect: to.fullPath },
    })
  }

  // Redirect to role selection if authenticated but no role is set
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
