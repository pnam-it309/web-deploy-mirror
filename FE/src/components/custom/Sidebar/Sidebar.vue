<script setup lang="ts">
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { useSidebarStore } from '@/stores/sidebar.store'
import SidebarItem from './SidebarItem.vue'
// Import Component ButtonCustom
import ButtonCustom from '@/components/custom/Button/ButtonDefault.vue'
import {
  HomeIcon,
  ShoppingBagIcon,
  FolderIcon,
  DocumentTextIcon,
  UsersIcon,
  XMarkIcon
} from '@heroicons/vue/24/outline'

const router = useRouter()
const authStore = useAuthStore()
const sidebarStore = useSidebarStore()

const navItems = [
  { label: 'Trang chủ', routeName: 'admin-dashboard', icon: HomeIcon },
  { label: 'Sản phẩm', routeName: 'admin-products', icon: ShoppingBagIcon },
  { label: 'Thương hiệu', routeName: 'admin-brand', icon: UsersIcon },
  { label: 'Danh mục', routeName: 'admin-categories', icon: FolderIcon },
  { label: 'Đơn hàng', routeName: 'admin-orders', icon: DocumentTextIcon },
  { label: 'Khách hàng', routeName: 'admin-customers', icon: UsersIcon },
]

const userInitials = computed(() => {
  if (!authStore.user?.fullName) return 'AD'
  return authStore.user.fullName.substring(0, 2).toUpperCase()
})
</script>

<template>
  <div>
    <!-- 1. DESKTOP SIDEBAR -->
    <aside
      class="hidden lg:flex flex-col w-64 h-screen sticky top-0 transition-colors duration-300 border-r shadow-sm bg-white border-gray-200 dark:bg-brand-dark-300 dark:border-brand-dark-200">

      <!-- Logo Header -->
      <div
        class="flex items-center justify-center h-16 border-b flex-shrink-0 bg-white border-gray-100 dark:bg-brand-dark-300 dark:border-brand-dark-200">
        <h1 class="text-2xl font-bold tracking-wider text-brand-mocha dark:text-brand-cream">
          <span class="text-brand-olive">U</span>DPM
        </h1>
      </div>

      <!-- Navigation Menu -->
      <nav class="flex-1 overflow-y-auto py-4 px-3 space-y-1 custom-scrollbar">
        <template v-for="(item, index) in navItems" :key="index">
          <SidebarItem :item="item" :index="index" :is-collapsed="false" />
        </template>
      </nav>

      <!-- Footer Sidebar -->
      <div
        class="p-4 border-t mt-auto border-gray-100 bg-[#fcfdf9]/50 dark:border-brand-dark-200 dark:bg-brand-dark-200/50">
        <div class="flex items-center gap-3 px-2 cursor-pointer hover:opacity-80 transition-opacity">
          <div
            class="w-8 h-8 rounded-full bg-brand-cream flex items-center justify-center text-brand-mocha font-bold text-xs shadow-sm dark:bg-brand-coffee dark:text-brand-cream">
            {{ userInitials }}
          </div>
          <div class="flex-1 min-w-0">
            <p class="text-sm font-bold text-brand-mocha dark:text-brand-cream truncate">{{ authStore.user?.fullName ||
              'Admin' }}</p>
            <p class="text-xs text-brand-coffee dark:text-brand-sage truncate">{{ authStore.user?.email }}</p>
          </div>
        </div>
      </div>
    </aside>

    <!-- 2. MOBILE SIDEBAR OVERLAY -->
    <div v-if="sidebarStore.isSidebarOpen" class="relative z-50 lg:hidden" role="dialog" aria-modal="true">
      <!-- Backdrop -->
      <div class="fixed inset-0 bg-brand-mocha/40 backdrop-blur-sm transition-opacity"
        @click="sidebarStore.toggleSidebar()"></div>

      <!-- Mobile Panel -->
      <div class="fixed inset-0 flex">
        <div
          class="relative flex w-full max-w-xs flex-1 flex-col bg-[#fffdf5] dark:bg-brand-dark-300 pb-4 pt-5 shadow-xl transition-transform duration-300 ease-in-out">

          <!-- Close Button (SỬA: Dùng ButtonCustom) -->
          <div class="absolute right-0 top-0 -mr-12 pt-2">
            <ButtonCustom color="cream-soft" @click="sidebarStore.toggleSidebar()"
              class="!rounded-full !p-2 !w-10 !h-10 flex items-center justify-center">
              <XMarkIcon class="h-6 w-6 text-white" />
            </ButtonCustom>
          </div>

          <!-- Logo Mobile -->
          <div
            class="flex flex-shrink-0 items-center justify-center px-4 border-b border-brand-cream pb-4 dark:border-brand-dark-200">
            <h1 class="text-2xl font-bold tracking-wider text-brand-mocha dark:text-brand-cream">MENU</h1>
          </div>

          <!-- Nav Mobile -->
          <nav class="mt-5 h-full overflow-y-auto px-2 space-y-1 custom-scrollbar">
            <template v-for="(item, index) in navItems" :key="index">
              <SidebarItem :item="item" :index="index" :is-collapsed="false"
                @item-click="sidebarStore.toggleSidebar()" />
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

:global(.dark) .custom-scrollbar::-webkit-scrollbar-thumb {
  background: #6c584c;
}
</style>