<script setup lang="ts">
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { useSidebarStore } from '@/stores/sidebar.store'
import SidebarItem from './SidebarItem.vue'
import ButtonCustom from '@/components/custom/Button/ButtonDefault.vue'
import {
  HomeIcon,
  ShoppingBagIcon,
  FolderIcon,
  DocumentTextIcon,
  UsersIcon,
  XMarkIcon,
  UserOutlined as UserIcon // Alias
} from '@heroicons/vue/24/outline'

const router = useRouter()
const authStore = useAuthStore()
const sidebarStore = useSidebarStore()

// Menu Config
const navItems = [
  { 
    label: 'Dashboard', 
    routeName: 'admin-dashboard', 
    icon: HomeIcon 
  },
  {
    label: 'Sản phẩm',
    icon: ShoppingBagIcon,
    children: [
      { label: 'Danh sách sản phẩm', routeName: 'admin-products' },
      { label: 'Thêm sản phẩm', routeName: 'admin-products-new' }
    ]
  },
  {
    label: 'Danh mục',
    routeName: 'admin-categories',
    icon: FolderIcon,
  },
  {
    label: 'Đơn hàng',
    routeName: 'admin-orders',
    icon: DocumentTextIcon,
  },
  {
    label: 'Khách hàng',
    routeName: 'admin-customers',
    icon: UsersIcon,
  },
  {
    label: 'Thương hiệu',
    routeName: 'admin-brand',
    icon: UsersIcon,
  },
]

const userInitials = computed(() => {
  if (!authStore.user?.fullName) return 'AD'
  const name = authStore.user.fullName.trim()
  const parts = name.split(/\s+/)
  if (parts.length > 1) {
    return (parts[0][0] + parts[parts.length - 1][0]).toUpperCase()
  }
  return name.substring(0, 2).toUpperCase()
})
</script>

<template>
  <div>
    <!-- 1. DESKTOP SIDEBAR -->
    <aside class="hidden lg:flex flex-col w-64 bg-white border-r border-gray-200 shadow-sm transition-all duration-300 h-screen sticky top-0">
      <!-- Logo Header -->
      <div class="flex items-center justify-center h-16 bg-white border-b border-gray-100 flex-shrink-0">
        <h1 class="text-2xl font-bold tracking-wider text-[#6c584c]">
          <span class="text-[#adc178]">U</span>DPM
        </h1>
      </div>

      <!-- Navigation Menu -->
      <nav class="flex-1 overflow-y-auto py-4 px-3 space-y-1 custom-scrollbar">
        <template v-for="(item, index) in navItems" :key="index">
          <SidebarItem 
            :item="item" 
            :index="index"
            :is-collapsed="false" 
          />
        </template>
      </nav>

      <!-- Footer Sidebar -->
      <div class="p-4 border-t border-gray-100 mt-auto bg-[#fcfdf9]/50">
        <div class="flex items-center gap-3 px-2 cursor-pointer hover:opacity-80 transition-opacity">
            <div class="w-8 h-8 rounded-full bg-[#f0ead2] flex items-center justify-center text-[#6c584c] font-bold text-xs shadow-sm">
                {{ userInitials }}
            </div>
            <div class="flex-1 min-w-0">
                <p class="text-sm font-bold text-[#5a483e] truncate">{{ authStore.user?.fullName || 'Quản trị viên' }}</p>
                <p class="text-xs text-[#8c7a6b] truncate">{{ authStore.user?.email || 'Xem hồ sơ' }}</p>
            </div>
        </div>
      </div>
    </aside>

    <!-- 2. MOBILE SIDEBAR OVERLAY -->
    <div v-if="sidebarStore.isSidebarOpen" class="relative z-50 lg:hidden" role="dialog" aria-modal="true">
      <!-- Backdrop -->
      <div class="fixed inset-0 bg-[#5a483e]/40 backdrop-blur-sm transition-opacity" @click="sidebarStore.toggleSidebar()"></div>

      <!-- Mobile Panel -->
      <div class="fixed inset-0 flex">
        <div class="relative flex w-full max-w-xs flex-1 flex-col bg-[#fffdf5] pb-4 pt-5 shadow-xl transition-transform duration-300 ease-in-out">
          <!-- Close Button -->
          <div class="absolute right-0 top-0 -mr-12 pt-2">
            <button 
              @click="sidebarStore.toggleSidebar()" 
              class="ml-1 flex h-10 w-10 items-center justify-center rounded-full focus:outline-none focus:ring-2 focus:ring-inset focus:ring-white"
            >
              <XMarkIcon class="h-6 w-6 text-white" />
            </button>
          </div>
          
          <!-- Logo Mobile -->
          <div class="flex flex-shrink-0 items-center justify-center px-4 border-b border-[#e6dfc0] pb-4">
             <h1 class="text-2xl font-bold tracking-wider text-[#6c584c]">MENU</h1>
          </div>
          
          <!-- Nav Mobile -->
          <nav class="mt-5 h-full overflow-y-auto px-2 space-y-1">
             <template v-for="(item, index) in navItems" :key="index">
                <SidebarItem 
                  :item="item" 
                  :index="index" 
                  :is-collapsed="false" 
                  @item-click="sidebarStore.toggleSidebar()" 
                />
             </template>
          </nav>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.custom-scrollbar::-webkit-scrollbar {
  width: 4px;
}
.custom-scrollbar::-webkit-scrollbar-track {
  background: transparent;
}
.custom-scrollbar::-webkit-scrollbar-thumb {
  background: #d1d5db;
  border-radius: 2px;
}
.custom-scrollbar::-webkit-scrollbar-thumb:hover {
  background: #9ca3af;
}
</style>