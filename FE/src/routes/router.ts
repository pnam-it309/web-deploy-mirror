import {
  createRouter,
  createWebHistory,
  type RouteRecordRaw,
  type NavigationGuardNext,
  type RouteLocationNormalized,
} from 'vue-router'
import { useAuthStore } from '@/stores/auth'
// Using direct paths instead of constants to avoid potential circular dependencies

// Layouts
const AuthLayout = () => import('@/layouts/AuthLayout.vue')
const AdminLayout = () => import('@/layouts/AdminLayout.vue')
const CustomerLayout = () => import('@/layouts/CustomerLayout.vue')

// Public pages
const ProductDetailPage = () => import('@/pages/admin/product/product-detail/ProductDetailPage.vue')

// Admin pages
const AdminDashBoard = () => import('@/pages/admin/dashboard/AdminDashboardPage.vue')
const AdminProducts = () => import('@/pages/admin/product/ProductsPage.vue')
const AdminProductCreateModal = () => import('@/pages/admin/product/ProductCreateModal.vue')
const AdminProductDetailModal = () => import('@/pages/admin/product/ProductDetailModal.vue')
const AdminOrders = () => import('@/pages/admin/orders/OrderPage.vue')
const AdminOrderCreateModal = () => import('@/pages/admin/orders/OrderCreateModal.vue')
const AdminSettings = () => import('@/pages/admin/SettingsPage.vue')
// const AdminManageCustormers = () => import('@/pages/admin/manage_customer/CustomerPage.vue')
// const AdminManageCustomerCreateModal = () =>
  import('@/pages/admin/manage_customer/CustomerCreateModal.vue')

const AdminCategories = () => import('@/pages/admin/categories/CategoryPage.vue')
const AdminCategoryCreateModal = () => import('@/pages/admin/categories/CategoryCreateModal.vue')
// Admin customers
const AdminCustomerList = () => import('@/pages/admin/customers/CustomerList.vue')
const AdminCustomerCreate = () => import('@/pages/admin/customers/CustomerCreate.vue')
const AdminCustomerDetail = () => import('@/pages/admin/customers/CustomerDetail.vue')
const AdminBrand = () => import('@/pages/admin/brand/BrandPage.vue')
const AdminBrandCreateModal = () => import('@/pages/admin/brand/BrandCreateModal.vue')

// Customer pages
const CustomerDashboard = () => import('@/pages/customer/dashboard_cus/DashboardPage.vue')
const CustomerOrders = () => import('@/pages/customer/orders/OrdersPage.vue')

// Authentication guard - Check for valid JWT token using auth store
const requireAuth = async (
  to: RouteLocationNormalized,
  from: RouteLocationNormalized,
  next: NavigationGuardNext
) => {
  const authStore = useAuthStore()
  
  if (authStore.isAuthenticated) {
    next()
  } else {
    console.log('[AUTH] User not authenticated, redirecting to selection')
    next('/selection')
  }
}

// Role-based guard - Check user role permissions
const requireRole = (roles: string[]) => {
  return async (
    to: RouteLocationNormalized,
    from: RouteLocationNormalized,
    next: NavigationGuardNext
  ) => {
    const authStore = useAuthStore()
    
    // Check if we have a valid token
    if (!authStore.isAuthenticated) {
      console.log('[AUTH] No valid token found, redirecting to selection')
      next('/selection')
      return
    }

    try {
      // Get user role from the store
      const userRole = authStore.userRole
      
      if (!userRole) {
        console.warn('[AUTH] No user role found in auth store')
        next('/selection')
        return
      }

      const normalizedRoles = roles.map(role => role.toUpperCase())
      const normalizedUserRole = userRole.toUpperCase()

      console.log(`[AUTH] Checking access - User role: ${normalizedUserRole}, Required roles: ${normalizedRoles.join(', ')}`)

      if (normalizedRoles.includes(normalizedUserRole) || normalizedRoles.includes('*')) {
        console.log(`[AUTH] Access granted to ${to.path}`)
        next()
      } else {
        console.warn(`[AUTH] Access denied. User role: ${normalizedUserRole}, Required roles: ${normalizedRoles.join(', ')}`)
        next('/403')
      }
    } catch (error) {
      console.error('[AUTH] Error checking access:', error)
      next('/selection')
    }
  }
}

const routes: RouteRecordRaw[] = [
  // Redirect root to selection page
  {
    path: '/',
    redirect: '/selection',
  },
  // Redirect login to selection page
  {
    path: '/login',
    redirect: '/selection',
  },
  {
    path: '/selection',
    name: 'selection',
    component: () => import('@/pages/log/SelectionPage.vue'),
    meta: { public: true },
  },
  {
    path: '/unauthorized',
    name: 'unauthorized',
    component: {
      template: `
        <div style="display: flex; align-items: center; justify-content: center; height: 100vh; text-align: center; background: #f5f5f5;">
          <div>
            <h1 style="color: #ff4d4f; margin-bottom: 16px;">🚫 Unauthorized</h1>
            <p style="color: #666; margin-bottom: 24px;">You don't have permission to access this page.</p>
            <a-button type="primary" @click="$router.push('/selection')" size="large">
              Go to Login
            </a-button>
          </div>
        </div>
      `
    },
    meta: { public: true },
  },
  {
    path: '/oauth2/callback',
    name: 'oauth-callback',
    component: () => import('@/pages/auth/OAuthCallback.vue'),
    meta: { public: true },
  },
  {
    path: '/oauth2/callback/google',
    name: 'oauth-callback-google',
    component: () => import('@/pages/auth/OAuthCallback.vue'),
    meta: { public: true },
  },

  // Auth routes (for internal auth if needed later)
  {
    path: '/auth',
    component: AuthLayout,
    meta: { public: true },
    children: [
      // These routes are kept for future internal auth if needed
      // {
      //   path: 'internal-login',
      //   name: 'internal-login',
      //   component: LoginPage,
      //   meta: { public: true },
      // },
      // {
      //   path: 'register',
      //   name: 'register',
      //   component: RegisterPage,
      //   meta: { public: true },
      // },
      // {
      //   path: 'forgot-password',
      //   name: 'forgot-password',
      //   component: ForgotPasswordPage,
      //   meta: { public: true },
      // },
    ],
  },

  // Admin routes with layout
  {
    path: '/admin',
    component: AdminLayout,
    beforeEnter: requireRole(['ADMIN']),
    children: [
      {
        path: '',
        redirect: '/admin/dashboard',
      },
      {
        path: 'dashboard',
        name: 'admin-dashboard',
        component: AdminDashBoard,
        meta: {
          title: 'Dashboard',
          apiEndpoint: '/api/admin/dashboard',
          requiresAuth: true
        },
      },
      {
        path: 'products',
        name: 'admin-products',
        component: AdminProducts,
        meta: {
          title: 'Quản lý sản phẩm',
          apiEndpoint: '/api/admin/products',
        },
        children: [
          { path: 'new',
            name: 'admin-products-new',
            component: AdminProductCreateModal
          },
          { path: ':id', name: 'admin-products-detail', component: AdminProductDetailModal }
        ],

      },

      {
        path: 'categories',
        name: 'admin-categories',
        component: AdminCategories,
        meta: {
          title: 'Quản lý danh mục sản phẩm',
          apiEndpoint: '/api/admin/categories',
        },
        children: [{ path: 'new', name: 'admin-categories-new', component: AdminCategoryCreateModal }],
      },
      {
        path: 'brand',
        name: 'admin-brand',
        component: AdminBrand,
        meta: {
          title: 'Quản lý thương hiệu',
          apiEndpoint: '/api/admin/brands',
        },
        children: [
          { path: 'new', name: 'admin-brand-new', component: AdminBrandCreateModal }
        ],
      },
      {
        path: 'orders',
        name: 'admin-orders',
        component: AdminOrders,
        meta: {
          title: 'Quản lý đơn hàng',
          apiEndpoint: '/api/admin/orders'
        },
        children: [
          { path: 'new', name: 'admin-orders-new', component: AdminOrderCreateModal},
        ]
      },
      {
        path: 'customers',
        name: 'admin-customers',
        component: AdminCustomerList,
        meta: {
          title: 'Quản lý khách hàng',
          apiEndpoint: '/api/admin/customers'
        }
      },
      {
        path: 'customers/new',
        name: 'admin-customers-new',
        component: AdminCustomerCreate,
        meta: { title: 'Thêm khách hàng' }
      },
      {
        path: 'customers/:id',
        name: 'admin-customers-detail',
        component: AdminCustomerDetail,
        meta: { title: 'Chi tiết khách hàng' }
      },
      {
        path: 'settings',
        name: 'admin-settings',
        component: AdminSettings,
        meta: {
          title: 'Cài đặt',
          apiEndpoint: '/api/admin/settings',
        },
      },
      // Redirect any unmatched admin routes to dashboard
      {
        path: ':catchAll(.*)*',
        redirect: { name: 'admin-dashboard' },
      },
    ],
  },

  // Customer routes with layout
  {
    path: '/customer',
    component: CustomerLayout,
    beforeEnter: requireRole(['CUSTOMER', 'USER']),
    children: [
      {
        path: 'dashboard',
        name: 'customer-dashboard',
        component: CustomerDashboard,
        meta: { title: 'My Dashboard' },
      },
      {
        path: 'orders',
        name: 'customer-orders',
        component: CustomerOrders,
        meta: { title: 'My Orders' },
      },
      // Redirect any unmatched customer routes to dashboard
      {
        path: ':catchAll(.*)*',
        redirect: { name: 'customer-dashboard' },
      },
    ],
  },

  // Error pages
  {
    path: '/403',
    name: 'forbidden',
    component: () => import('@/pages/403/Forbidden.vue'),
    meta: { public: true },
  },
  {
    path: '/404',
    name: 'not-found',
    component: () => import('@/pages/404/NotFound.vue'),
    meta: { public: true },
  },

  // 404 Not Found - Redirect to 404 page instead of selection
  {
    path: '/:catchAll(.*)*',
    redirect: '/404',
  },
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
  scrollBehavior() {
    return { top: 0, behavior: 'smooth' }
  },
})

// Navigation guard - Handle authentication and public routes
router.beforeEach(async (to, from, next) => {
  console.log(`[ROUTER] Navigating from ${from.path} to ${to.path}`)
  
  const authStore = useAuthStore()
  
  // Handle root path redirection
  if (to.path === '/') {
    if (authStore.isAuthenticated) {
      const userRole = authStore.userRole?.toUpperCase()
      
      if (userRole === 'ADMIN') {
        next('/admin/dashboard')
      } else if (userRole === 'CUSTOMER' || userRole === 'USER') {
        next('/customer/dashboard')
      } else {
        next('/selection')
      }
    } else {
      next('/selection')
    }
    return
  }

  // Allow access to public routes
  if (to.meta?.public) {
    next()
    return
  }

  // For protected routes, check authentication
  if (!authStore.isAuthenticated) {
    console.log('[ROUTER] User not authenticated, redirecting to selection')
    next('/selection')
    return
  }

  // Handle authenticated users trying to access auth pages
  if (to.path === '/selection' || to.path === '/login') {
    const userRole = authStore.userRole?.toUpperCase()
    
    if (userRole === 'ADMIN') {
      next('/admin/dashboard')
    } else if (userRole === 'CUSTOMER' || userRole === 'USER') {
      next('/customer/dashboard')
    } else {
      next('/selection')
    }
    return
  }

  next()
})

export default router
