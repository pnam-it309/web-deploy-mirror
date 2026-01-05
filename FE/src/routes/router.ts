import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import { ROUTES_CONSTANTS } from '@/constants/path'

const routes: Array<RouteRecordRaw> = [
  // {
  //   path: ROUTES_CONSTANTS.LOGIN.path,
  //   name: ROUTES_CONSTANTS.LOGIN.name,
  //   component: () => import('@/pages/auth/OAuthCallback.vue'), // Assuming this is the login/callback handler
  // },
  // {
  //   path: ROUTES_CONSTANTS.SELECTION.path,
  //   name: ROUTES_CONSTANTS.SELECTION.name,
  //   // Creating a placeholder for selection since the file was not found, or pointing to existing if found later
  //   component: () => import('@/pages/auth/OAuthCallback.vue'), // Temporary fallback or correct if it handles selection too
  // },
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
    ],
  },
  {
    path: ROUTES_CONSTANTS.ADMIN.path,
    redirect: `${ROUTES_CONSTANTS.ADMIN.path}/${ROUTES_CONSTANTS.ADMIN.children.DASHBOARD.path}`,
  },
  {
    path: `${ROUTES_CONSTANTS.ADMIN.path}/${ROUTES_CONSTANTS.ADMIN.children.DASHBOARD.path}`,
    name: ROUTES_CONSTANTS.ADMIN.children.DASHBOARD.name,
    component: () => import('@/pages/admin/dashboard/AdminDashboardView.vue'),
  },

  {
    path: `${ROUTES_CONSTANTS.ADMIN.path}/${ROUTES_CONSTANTS.ADMIN.children.APPS.path}`,
    name: ROUTES_CONSTANTS.ADMIN.children.APPS.name,
    component: () => import('@/pages/admin/app/AdminAppList.vue'),
  },
  {
    path: `${ROUTES_CONSTANTS.ADMIN.path}/${ROUTES_CONSTANTS.ADMIN.children.APPS.path}/create`,
    name: 'admin-apps-create',
    component: () => import('@/pages/admin/app/AdminProductForm.vue'),
  },
  {
    path: `${ROUTES_CONSTANTS.ADMIN.path}/${ROUTES_CONSTANTS.ADMIN.children.APPS.path}/:id`,
    name: 'admin-apps-edit',
    component: () => import('@/pages/admin/app/AdminProductForm.vue'),
  },
  {
    path: `${ROUTES_CONSTANTS.ADMIN.path}/${ROUTES_CONSTANTS.ADMIN.children.DOMAINS.path}`,
    name: ROUTES_CONSTANTS.ADMIN.children.DOMAINS.name,
    component: () => import('@/pages/admin/domain/AdminDomainList.vue'),
  },
  {
    path: `${ROUTES_CONSTANTS.ADMIN.path}/${ROUTES_CONSTANTS.ADMIN.children.TECHNOLOGIES.path}`,
    name: ROUTES_CONSTANTS.ADMIN.children.TECHNOLOGIES.name,
    component: () => import('@/pages/admin/technology/AdminTechnologyList.vue'),
  },
  {
    path: `${ROUTES_CONSTANTS.ADMIN.path}/${ROUTES_CONSTANTS.ADMIN.children.FEATURES.path}`,
    name: ROUTES_CONSTANTS.ADMIN.children.FEATURES.name,
    component: () => import('@/pages/admin/feature/AdminFeatureList.vue'),
  },

  {
    path: ROUTES_CONSTANTS.REDIRECT.path,
    name: ROUTES_CONSTANTS.REDIRECT.name,
    component: () => import('@/routes/guard/Redirect.vue'),
  },

  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/pages/error/NotFoundPage.vue'),
  },
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
})

export default router
