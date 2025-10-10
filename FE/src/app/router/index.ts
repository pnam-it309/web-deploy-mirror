import {
  createRouter,
  createWebHistory,
  type RouteRecordRaw,
  type NavigationGuardNext,
  type RouteLocationNormalized,
} from 'vue-router'
import { ROUTES_CONSTANTS } from '@/constants/path'
import { h } from 'vue'

// Layouts
const AuthLayout = () => import('@/layouts/AuthLayout.vue')
const AdminLayout = () => import('@/layouts/AdminLayout.vue')
const CustomerLayout = () => import('@/layouts/CustomerLayout.vue')

// Public pages
const LoginPage = () => import('@/pages/auth/LoginPage.vue')
const RegisterPage = () => import('@/pages/auth/RegisterPage.vue')
const ForgotPasswordPage = () => import('@/pages/auth/ForgotPasswordPage.vue')
const CatalogPage = () => import('@/pages/catalog/CatalogPage.vue')
const ProductDetailPage = () => import('@/pages/product-detail/ProductDetailPage.vue')

// Admin pages
const AdminDashboard = () => import('@/pages/admin/DashboardPage.vue')
const AdminDashBoard2 = () => import('@/pages/admin/dashboard/DashboardPage.vue')
const AdminUsers = () => import('@/pages/admin/UsersPage.vue')
const AdminProducts = () => import('@/pages/admin/product/ProductsPage.vue')
const AdminProductCreateModal = () => import('@/pages/admin/product/ProductCreateModal.vue')
const AdminProductDetailModal = () => import('@/pages/admin/product/ProductDetailModal.vue')
const AdminOrders = () => import('@/pages/admin/OrdersPage.vue')
const AdminSettings = () => import('@/pages/admin/SettingsPage.vue')
const AdminManageCustormers = () => import('@/pages/admin/manage_customer/CustomerPage.vue')
const AdminManageCustomerCreateModal = () =>
  import('@/pages/admin/manage_customer/CustomerCreateModal.vue')

const AdminCategories = () => import('@/pages/admin/categories/CategoryPage.vue')
const AdminCategoryCreateModal = () => import('@/pages/admin/categories/CategoryCreateModal.vue')
const AdminBrand = () => import('@/pages/admin/brand/BrandPage.vue')
const AdminBrandCreateModal = () => import('@/pages/admin/brand/BrandCreateModal.vue')

// Customer pages
const CustomerDashboard = () => import('@/pages/customer/DashboardPage.vue')
const CustomerOrders = () => import('@/pages/customer/OrdersPage.vue')
const CustomerProfile = () => import('@/pages/customer/ProfilePage.vue')
const CustomerSettings = () => import('@/pages/customer/SettingsPage.vue')

// Authentication guard - Completely bypassed
const requireAuth = (
  to: RouteLocationNormalized,
  from: RouteLocationNormalized,
  next: NavigationGuardNext
) => {
  next()
}

// Role-based guard - Completely bypassed
const requireRole = (roles: string[]) => {
  return (
    to: RouteLocationNormalized,
    from: RouteLocationNormalized,
    next: NavigationGuardNext
  ) => {
    next()
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
    path: ROUTES_CONSTANTS.LOGIN.path,
    redirect: '/selection',
  },
  {
    path: '/selection',
    name: 'selection',
    component: () => import('@/pages/SelectionPage.vue'),
    meta: { public: true },
  },
  {
    path: '/catalog',
    name: 'catalog',
    component: CatalogPage,
    meta: { public: true },
  },
  {
    path: '/product/:id',
    name: 'product-detail',
    component: ProductDetailPage,
    meta: { public: true },
  },

  // Auth routes (for internal auth if needed later)
  {
    path: '/auth',
    component: AuthLayout,
    meta: { public: true },
    children: [
      // These routes are kept for future internal auth if needed
      {
        path: 'internal-login',
        name: 'internal-login',
        component: LoginPage,
        meta: { public: true },
      },
      {
        path: 'register',
        name: 'register',
        component: RegisterPage,
        meta: { public: true },
      },
      {
        path: 'forgot-password',
        name: 'forgot-password',
        component: ForgotPasswordPage,
        meta: { public: true },
      },
    ],
  },

  // Admin routes with layout
  {
    path: '/admin',
    component: AdminLayout,
    children: [
      {
        path: '',
        name: 'admin-dashboard',
        component: { render: () => h(AdminDashboard) },
        meta: {
          title: 'Dashboard',
          requiresAuth: true,
          apiEndpoint: '/api/admin/dashboard',
        },
      },
      {
        path: 'dashboard',
        name: 'admin-dashboard2',
        component: AdminDashBoard2,
        meta: {
          title: 'Dashboard',
          apiEndpoint: '/api/admin/dashboard2',
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
        path: 'orders',
        name: 'admin-orders',
        component: AdminOrders,
        meta: {
          title: 'Quản lý đơn hàng',
          apiEndpoint: '/api/admin/orders',
        },
      },
      {
        path: 'brand',
        name: 'admin-brand',
        component: AdminBrand,
        meta: {
          title: 'Quản lý brand',
          apiEndpoint: '/api/admin/brand',
        },
        children: [{ path: 'new', name: 'admin-brand-new', component: AdminBrandCreateModal }],
      },
      {
        path: '/admin/customers',
        name: 'admin-customers',
        component: AdminManageCustormers,
        children: [
          {
            path: 'new',
            name: 'admin-customers-new',
            component: AdminManageCustomerCreateModal,
          },
        ],
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
      {
        path: 'users',
        name: 'admin-users',
        component: AdminUsers,
        meta: {
          title: 'Quản lý người dùng',
          apiEndpoint: '/api/admin/users',
        },
      },
      // Redirect any unmatched admin routes to dashboard
      {
        path: ':pathMatch(.*)*',
        redirect: { name: 'admin-dashboard' },
      },
    ],
  },

  // Customer routes with layout
  {
    path: '/customer',
    component: CustomerLayout,
    beforeEnter: requireRole(['customer', 'user']),
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
      {
        path: 'profile',
        name: 'customer-profile',
        component: CustomerProfile,
        meta: { title: 'My Profile' },
      },
      // {
      //   path: 'settings',
      //   name: 'customer-settings',
      //   meta: { title: 'Account Settings' }
      // },
      // Redirect any unmatched customer routes to dashboard
      {
        path: ':pathMatch(.*)*',
        redirect: { name: 'customer-dashboard' },
      },
    ],
  },

  // 404 Not Found - Redirect to selection page
  {
    path: '/:pathMatch(.*)*',
    redirect: '/selection',
  },
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
  scrollBehavior() {
    return { top: 0, behavior: 'smooth' }
  },
})

// Navigation guard - Allow all routes
router.beforeEach((to, from, next) => {
  // Redirect root to admin dashboard
  if (to.path === '/') {
    next('/admin')
  } else {
    next()
  }
})

export default router
