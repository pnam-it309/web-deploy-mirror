import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'

// Import pages from current locations (phase 1). We can move them into features/* later.
const CatalogPage = () => import('../../pages/catalog/CatalogPage.vue')
const AdminUpload = () => import('../../pages/admin/dashboard/AdminUpload.vue')
const ProductDetailPage = () => import('../../pages/product-detail/ProductDetailPage.vue')

const routes: RouteRecordRaw[] = [
  { path: '/', name: 'catalog', component: CatalogPage },
  { path: '/product/:sku', name: 'product-detail', component: ProductDetailPage, props: true },
  { path: '/admin/upload', name: 'admin-upload', component: AdminUpload },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router
