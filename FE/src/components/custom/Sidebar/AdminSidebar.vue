<script setup lang="ts">
import { ROUTES_CONSTANTS } from '@/constants/path'
import { ref } from 'vue'
import SidebarItem from './SidebarItem.vue'
import SidebarDropdown from './SidebarDropDown.vue'
import { 
  DashboardOutlined, 
  ShoppingOutlined, 
  UnorderedListOutlined, 
  FileTextOutlined,
  TeamOutlined,
  UserOutlined,
  ImportOutlined,
  SettingOutlined,
  ProfileOutlined
} from '@ant-design/icons-vue'

const isCollapsed = ref(false)
const isMobileMenuOpen = ref(false)

const menuItems = [
  {
    icon: DashboardOutlined,
    label: 'Dashboard',
    routeName: ROUTES_CONSTANTS.ADMIN.children.DASHBOARD.name,
  },
  {
    icon: ShoppingOutlined,
    label: 'Sản phẩm',
    routeName: ROUTES_CONSTANTS.ADMIN.children.PRODUCTS.name,
    children: [
      { label: 'Danh sách sản phẩm', routeName: ROUTES_CONSTANTS.ADMIN.children.PRODUCTS.name },
      { label: 'Thêm sản phẩm', routeName: ROUTES_CONSTANTS.ADMIN.children.PRODUCT_CREATE.name }
    ]
  },
  {
    icon: UnorderedListOutlined,
    label: 'Danh mục',
    routeName: ROUTES_CONSTANTS.ADMIN.children.CATEGORIES.name,
  },
  {
    icon: FileTextOutlined,
    label: 'Đơn hàng',
    routeName: ROUTES_CONSTANTS.ADMIN.children.ORDERS.name,
  },
  {
    icon: TeamOutlined,
    label: 'Khách hàng',
    routeName: ROUTES_CONSTANTS.ADMIN.children.CUSTOMERS.name,
  },
  {
    icon: UserOutlined,
    label: 'Nhân viên',
    routeName: ROUTES_CONSTANTS.ADMIN.children.STAFF.name,
    children: [
      { label: 'Danh sách nhân viên', routeName: ROUTES_CONSTANTS.ADMIN.children.STAFF.name },
      { label: 'Thêm nhân viên', routeName: ROUTES_CONSTANTS.ADMIN.children.STAFF_CREATE.name }
    ]
  },
  {
    icon: ImportOutlined,
    label: 'Nhập dữ liệu',
    routeName: ROUTES_CONSTANTS.ADMIN.children.IMPORT.name,
  },
  {
    icon: SettingOutlined,
    label: 'Cài đặt',
    routeName: ROUTES_CONSTANTS.ADMIN.children.SETTINGS.name,
  },
  {
    icon: ProfileOutlined,
    label: 'Hồ sơ',
    routeName: ROUTES_CONSTANTS.ADMIN.children.PROFILE.name,
  },
]
</script>

<template>
  <!-- Mobile menu button -->
  <button
    v-if="!isMobileMenuOpen"
    @click="isMobileMenuOpen = true"
    class="fixed top-4 left-4 z-50 flex items-center justify-center w-10 h-10 rounded-full bg-blue-600 hover:bg-blue-700 text-white shadow-lg transition transform hover:scale-105 active:scale-95 lg:hidden"
  >
    <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16" />
    </svg>
  </button>

  <!-- Sidebar -->
  <aside
    class="fixed inset-0 lg:inset-y-0 lg:left-0 z-40 flex h-screen flex-col bg-white shadow-lg transition-all duration-300 ease-in-out lg:relative lg:translate-x-0"
    :class="{
      'hidden lg:flex': !isMobileMenuOpen,
      'translate-x-0 w-[80%]': isMobileMenuOpen,
      'w-20': isCollapsed && !isMobileMenuOpen,
      'w-64': !isCollapsed && !isMobileMenuOpen,
    }"
  >
    <!-- Logo and collapse button -->
    <div class="flex items-center justify-between px-4 py-4 bg-white border-b border-gray-200">
      <img
        v-if="!isCollapsed"
        src="/src/assets/images/logo-udpm-dark.png"
        alt="Logo"
        class="h-8 transition-all duration-300"
      />
      
      <button
        v-if="!isMobileMenuOpen"
        @click="isCollapsed = !isCollapsed"
        class="hidden lg:flex items-center justify-center w-8 h-8 rounded-full hover:bg-gray-100 text-gray-600"
      >
        <svg
          class="w-5 h-5 transition-transform duration-300"
          :class="{ 'rotate-180': isCollapsed }"
          fill="none"
          stroke="currentColor"
          viewBox="0 0 24 24"
        >
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7" />
        </svg>
      </button>

      <button
        v-if="isMobileMenuOpen"
        @click="isMobileMenuOpen = false"
        class="lg:hidden flex items-center justify-center w-8 h-8 text-gray-600 hover:text-gray-900"
      >
        <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
        </svg>
      </button>
    </div>

    <!-- Navigation -->
    <div class="flex-1 overflow-y-auto py-4">
      <ul>
        <li v-for="(item, index) in menuItems" :key="index">
          <template v-if="!item.children">
            <SidebarItem 
              :item="item" 
              :index="index" 
              :is-collapsed="isCollapsed"
              @item-click="isMobileMenuOpen = false"
            />
          </template>
          <template v-else>
            <SidebarDropdown 
              :items="item.children" 
              :page="item"
              :is-collapsed="isCollapsed"
              @item-click="isMobileMenuOpen = false"
            />
          </template>
        </li>
      </ul>
    </div>

    <!-- User Profile -->
    <div 
      v-if="!isCollapsed" 
      class="p-4 border-t border-gray-200"
    >
      <div class="flex items-center">
        <div class="w-10 h-10 rounded-full bg-blue-100 flex items-center justify-center text-blue-600">
          <UserOutlined class="text-lg" />
        </div>
        <div class="ml-3">
          <p class="text-sm font-medium text-gray-900">Quản trị viên</p>
          <p class="text-xs text-gray-500">Xem hồ sơ</p>
        </div>
      </div>
    </div>
  </aside>

  <!-- Overlay for mobile -->
  <div 
    v-if="isMobileMenuOpen" 
    class="fixed inset-0 bg-black bg-opacity-50 z-30 lg:hidden"
    @click="isMobileMenuOpen = false"
  ></div>
</template>

<style scoped>
.router-link-active {
  background-color: #f0f9ff;
  color: #2563eb;
}

.router-link-active :deep(svg) {
  color: #2563eb;
}

/* Smooth transition for sidebar */
aside {
  transition: width 0.3s ease, transform 0.3s ease;
  will-change: width, transform;
  z-index: 40;
}
</style>
