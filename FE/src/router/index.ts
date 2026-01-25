import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import { ROUTES_CONSTANTS } from '@/constants/path'

const routes: Array<RouteRecordRaw> = [
  {
    path: ROUTES_CONSTANTS.CUSTOMER.path,
    component: () => import('@/layouts/public/PublicLayout.vue'),
    children: [
      {
        path: ROUTES_CONSTANTS.CUSTOMER.children.HOME.path,
        name: ROUTES_CONSTANTS.CUSTOMER.children.HOME.name,
        component: () => import('@/pages/client/home/HomePage.vue'),
      },
      {
        path: ROUTES_CONSTANTS.CUSTOMER.children.APPS.path,
        name: ROUTES_CONSTANTS.CUSTOMER.children.APPS.name,
        component: () => import('@/pages/client/app/ProductListPage.vue'),
      },
      {
        path: ROUTES_CONSTANTS.CUSTOMER.children.APP_DETAIL.path,
        name: ROUTES_CONSTANTS.CUSTOMER.children.APP_DETAIL.name,
        component: () => import('@/pages/client/app/ProductDetailPage.vue'),
      },
      {
        path: ROUTES_CONSTANTS.CUSTOMER.children.DOMAINS.path,
        name: ROUTES_CONSTANTS.CUSTOMER.children.DOMAINS.name,
        component: () => import('@/pages/client/domain/DomainListPage.vue'),
      },
      {
        path: ROUTES_CONSTANTS.CUSTOMER.children.TECHNOLOGIES.path,
        name: ROUTES_CONSTANTS.CUSTOMER.children.TECHNOLOGIES.name,
        component: () => import('@/pages/client/technology/TechnologyListPage.vue'),
      },
      {
        path: ROUTES_CONSTANTS.CUSTOMER.children.BOOKMARKS.path,
        name: ROUTES_CONSTANTS.CUSTOMER.children.BOOKMARKS.name,
        component: () => import('@/pages/client/bookmarks/BookmarksPage.vue'),
      },
      {
        path: '/preview/:token',
        name: 'PreviewPage',
        component: () => import('@/pages/client/preview/PreviewPage.vue'),
      },
    ],
  },

  {
    path: ROUTES_CONSTANTS.ADMIN.path,
    component: () => import('@/layouts/admin/AdminLayout.vue'),
    redirect: { name: ROUTES_CONSTANTS.ADMIN.children.DASHBOARD.name },
    children: [
      {
        path: ROUTES_CONSTANTS.ADMIN.children.DASHBOARD.path,
        name: ROUTES_CONSTANTS.ADMIN.children.DASHBOARD.name,
        component: () => import('@/pages/admin/dashboard/AdminDashboardView.vue'),
        meta: { title: 'Tổng quan hệ thống' },
      },
      {
        path: 'homepage-config',
        name: 'AdminHomepageConfig',
        component: () => import('@/pages/admin/dashboard/AdminHomepageConfig.vue'),
        meta: { title: 'Cấu hình Trang chủ' },
      },
      {
        path: ROUTES_CONSTANTS.ADMIN.children.APPS.path,
        name: ROUTES_CONSTANTS.ADMIN.children.APPS.name,
        component: () => import('@/pages/admin/app/AdminAppPage.vue'),
        meta: { title: 'Sản phẩm phần mềm' },
      },
      {
        path: 'apps/create',
        name: 'AdminAppCreate',
        component: () => import('@/pages/admin/app/AppModify.vue'),
        meta: { title: 'Tạo ứng dụng mới' },
      },
      {
        path: 'apps/:id',
        name: 'AdminAppEdit',
        component: () => import('@/pages/admin/app/AppModify.vue'),
        meta: { title: 'Cập nhật ứng dụng' },
      },
      {
        path: ROUTES_CONSTANTS.ADMIN.children.DOMAINS.path,
        name: ROUTES_CONSTANTS.ADMIN.children.DOMAINS.name,
        component: () => import('@/pages/admin/domain/AdminDomainIndex.vue'), // Đổi tên file List
        meta: { title: 'Quản lý Lĩnh vực' },
      },
      {
        path: 'domains/create',
        name: 'AdminDomainCreate',
        component: () => import('@/pages/admin/domain/DomainModify.vue'), // File mới
        meta: { title: 'Thêm Lĩnh vực' },
      },
      {
        path: 'domains/:id',
        name: 'AdminDomainEdit',
        component: () => import('@/pages/admin/domain/DomainModify.vue'), // File mới
        meta: { title: 'Sửa Lĩnh vực' },
      },
      {
        path: ROUTES_CONSTANTS.ADMIN.children.TECHNOLOGIES.path,
        name: ROUTES_CONSTANTS.ADMIN.children.TECHNOLOGIES.name,
        component: () => import('@/pages/admin/technology/AdminTechnologyIndex.vue'),
        meta: { title: 'Quản lý Công nghệ' },
      },
      {
        path: 'technologies/create',
        name: 'AdminTechnologyCreate',
        component: () => import('@/pages/admin/technology/TechnologyModify.vue'),
        meta: { title: 'Thêm Công nghệ' },
      },
      {
        path: 'technologies/:id',
        name: 'AdminTechnologyEdit',
        component: () => import('@/pages/admin/technology/TechnologyModify.vue'),
        meta: { title: 'Sửa Công nghệ' },
      },
      {
        path: ROUTES_CONSTANTS.ADMIN.children.FEATURES.path,
        name: ROUTES_CONSTANTS.ADMIN.children.FEATURES.name,
        component: () => import('@/pages/admin/feature/AdminFeatureIndex.vue'),
        meta: { title: 'Quản lý Chức năng' },
      },
      {
        path: 'features/create',
        name: 'AdminFeatureCreate',
        component: () => import('@/pages/admin/feature/FeatureModify.vue'),
        meta: { title: 'Thêm Chức năng' },
      },
      {
        path: 'features/:id',
        name: 'AdminFeatureEdit',
        component: () => import('@/pages/admin/feature/FeatureModify.vue'),
        meta: { title: 'Sửa Chức năng' },
      },
      {
        path: 'audit-logs',
        name: 'AdminAuditLogs',
        component: () => import('@/pages/admin/audit/AdminAuditLogPage.vue'),
        meta: { title: 'Lịch sử thay đổi' },
      },
      {
        path: 'trash',
        name: 'AdminTrash',
        component: () => import('@/pages/admin/trash/AdminTrashPage.vue'),
        meta: { title: 'Thùng rác' },
      },
      {
        path: 'media-library',
        name: 'AdminMediaLibrary',
        component: () => import('@/pages/admin/media/AdminMediaLibraryPage.vue'),
        meta: { title: 'Thư viện Media' },
      },
      {
        path: 'webhooks',
        name: 'AdminWebhooks',
        component: () => import('@/pages/admin/webhook/AdminWebhookPage.vue'),
        meta: { title: 'Quản lý Webhooks' },
      },
      {
        path: 'subscriptions',
        name: 'AdminSubscriptions',
        component: () => import('@/pages/admin/subscription/AdminSubscriptionPage.vue'),
        meta: { title: 'Quản lý đăng ký Email' },
      },
      {
        path: 'ip-whitelist',
        name: 'AdminIpWhitelist',
        component: () => import('@/pages/admin/security/AdminIpWhitelistPage.vue'),
        meta: { title: 'IP Whitelist' },
      },
      {
        path: 'moderation',
        name: 'AdminModeration',
        component: () => import('@/pages/admin/moderation/AdminReviewModeration.vue'),
        meta: { title: 'Kiểm duyệt nội dung' },
      },
      {
        path: 'roles',
        name: 'AdminRoles',
        component: () => import('@/pages/admin/role/AdminRoleManagement.vue'),
        meta: { title: 'Quản lý Vai trò & Quyền hạn' },
      },
      {
        path: 'analytics/search',
        name: 'AdminSearchAnalytics',
        component: () => import('@/pages/admin/analytics/AdminSearchAnalytics.vue'),
        meta: { title: 'Search Analytics' },
      },
    ],
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: ROUTES_CONSTANTS.CUSTOMER.path,
  },
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
  scrollBehavior(_to, _from, savedPosition) {
    if (savedPosition) {
      return savedPosition
    } else {
      return { top: 0 }
    }
  },
})

export default router
