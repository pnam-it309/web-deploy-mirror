import {
  createRouter,
  createWebHistory,
  type RouteRecordRaw,
  type NavigationGuardNext,
  type RouteLocationNormalized,
} from 'vue-router'

// Layouts
const AuthLayout = () => import('@/layouts/AuthLayout.vue')
const AdminLayout = () => import('@/layouts/AdminLayout.vue')
const CustomerLayout = () => import('@/layouts/CustomerLayout.vue')

// Public pages
const ProductDetailPage = () => import('@/pages/admin/product/product-detail/ProductDetailPage.vue')

// Admin pages
const AdminDashBoard = () => import('@/pages/admin/dashboard/AdminDashboardPage.vue')

// -- Product Pages --
const AdminProducts = () => import('@/pages/admin/product/ProductPage.vue')
const AdminProductCreateModal = () => import('@/pages/admin/product/ProductCreateModal.vue')
const AdminProductDetailModal = () => import('@/pages/admin/product/ProductDetailModal.vue') // <-- FILE MỚI

// -- Order Pages --
const AdminOrders = () => import('@/pages/admin/orders/OrderPage.vue')
const AdminOrderCreateModal = () => import('@/pages/admin/orders/OrderCreateModal.vue')

const AdminSettings = () => import('@/pages/admin/SettingsPage.vue')

// -- Category Pages --
const AdminCategories = () => import('@/pages/admin/categories/CategoryPage.vue')
const AdminCategoryCreateModal = () => import('@/pages/admin/categories/CategoryCreateModal.vue')

// -- Customer Pages --
const AdminCustomerList = () => import('@/pages/admin/customers/CustomerList.vue')
const AdminCustomerCreate = () => import('@/pages/admin/customers/CustomerCreate.vue')
const AdminCustomerDetail = () => import('@/pages/admin/customers/CustomerDetail.vue')

// -- Brand Pages --
const AdminBrand = () => import('@/pages/admin/brand/BrandPage.vue')
const AdminBrandCreateModal = () => import('@/pages/admin/brand/BrandCreateModal.vue')

// Customer pages
const CustomerDashboard = () => import('@/pages/customer/dashboard_cus/DashboardPage.vue')
const CustomerOrders = () => import('@/pages/customer/orders/OrdersPage.vue')

// Guards
const requireAuth = (to: RouteLocationNormalized, from: RouteLocationNormalized, next: NavigationGuardNext) => next()
const requireRole = (roles: string[]) => (to: RouteLocationNormalized, from: RouteLocationNormalized, next: NavigationGuardNext) => next()

const routes: RouteRecordRaw[] = [
  { path: '/', redirect: '/selection' },
  { path: '/login', redirect: '/selection' },
  {
    path: '/selection',
    name: 'selection',
    component: () => import('@/pages/log/SelectionPage.vue'),
    meta: { public: true },
  },
  {
    path: '/product/:id',
    name: 'product-detail',
    component: ProductDetailPage,
    meta: { public: true },
  },
  {
    path: '/auth',
    component: AuthLayout,
    meta: { public: true },
    children: [],
  },

  // --- ADMIN ROUTES ---
  {
    path: '/admin',
    component: AdminLayout,
    children: [
      { path: '', redirect: '/admin/dashboard' },
      {
        path: 'dashboard',
        name: 'admin-dashboard',
        component: AdminDashBoard,
        meta: { title: 'Dashboard', apiEndpoint: '/api/v1/admin/dashboard' },
      },
      {
        path: 'products',
        name: 'admin-products',
        component: AdminProducts,
        meta: { title: 'Quản lý sản phẩm', apiEndpoint: '/api/v1/admin/products' },
        children: [
          // Lưu ý: Các route con này chỉ hoạt động nếu ProductPage có <router-view>
          // Hiện tại chúng ta dùng Modal state nội bộ, nhưng giữ lại route để mở rộng sau này
          { path: 'new', name: 'admin-products-new', component: AdminProductCreateModal },
          { path: ':id', name: 'admin-products-detail', component: AdminProductDetailModal }
        ],
      },
      {
        path: 'categories',
        name: 'admin-categories',
        component: AdminCategories,
        meta: { title: 'Quản lý danh mục', apiEndpoint: '/api/v1/admin/categories' },
        children: [{ path: 'new', name: 'admin-categories-new', component: AdminCategoryCreateModal }],
      },
      {
        path: 'brand',
        name: 'admin-brand',
        component: AdminBrand,
        meta: { title: 'Quản lý thương hiệu', apiEndpoint: '/api/v1/admin/brands' },
        children: [{ path: 'new', name: 'admin-brand-new', component: AdminBrandCreateModal }],
      },
      {
        path: 'orders',
        name: 'admin-orders',
        component: AdminOrders,
        meta: { title: 'Quản lý đơn hàng', apiEndpoint: '/api/v1/admin/orders' },
        children: [{ path: 'new', name: 'admin-orders-new', component: AdminOrderCreateModal }],
      },
      {
        path: 'customers',
        name: 'admin-customers',
        component: AdminCustomerList,
        meta: { title: 'Quản lý khách hàng', apiEndpoint: '/api/v1/admin/customers' }
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
        meta: { title: 'Cài đặt', apiEndpoint: '/api/v1/admin/settings' },
      },
      { path: ':catchAll(.*)*', redirect: { name: 'admin-dashboard' } },
    ],
  },

  // --- CUSTOMER ROUTES ---
  {
    path: '/customer',
    component: CustomerLayout,
    beforeEnter: requireRole(['customer', 'user']),
    children: [
      { path: 'dashboard', name: 'customer-dashboard', component: CustomerDashboard, meta: { title: 'My Dashboard' } },
      { path: 'orders', name: 'customer-orders', component: CustomerOrders, meta: { title: 'My Orders' } },
      { path: ':catchAll(.*)*', redirect: { name: 'customer-dashboard' } },
    ],
  },

  { path: '/:catchAll(.*)*', redirect: '/selection' },
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
  scrollBehavior() { return { top: 0, behavior: 'smooth' } },
})

router.beforeEach((to, from, next) => {
  if (to.path === '/') {
    next('/admin')
  } else {
    next()
  }
})

export default router
