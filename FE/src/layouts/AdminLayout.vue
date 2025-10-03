<template>
  <div class="flex h-screen bg-gray-900 text-gray-200">
    <!-- sidebar layout -->
    <div :class="{
      'translate-x-0 ease-out': isSidebarOpen,
      '-translate-x-full ease-in': !isSidebarOpen,
    }"
      class="fixed z-30 inset-y-0 left-0 w-64 transition duration-300 transform bg-gray-800 md:translate-x-0 md:static md:inset-0">
      <div class="flex flex-col h-full">
        <div class="flex items-center justify-center h-16 bg-gray-900/50 shadow-lg border-b border-gray-700/50">
          <h1 class="text-xl font-bold text-pink-500 tracking-wider">
            <span class="text-3xl font-serif">S.G</span>
          </h1>
        </div>

        <nav class="flex-1 px-2 py-4 space-y-1 overflow-y-auto custom-scrollbar">
          <template v-for="(item, index) in navItems" :key="item?.path || index">
            <router-link v-if="item && (!item.children || item.children.length === 0)" :to="item.path"
              class="flex items-center px-4 py-3 text-sm font-medium rounded-md transition duration-200 hover:bg-gray-700/70"
              :class="[
                $route.path === item.path
                  ? 'bg-gray-700 text-white border-l-4 border-pink-500'
                  : 'text-gray-400 hover:text-white',
              ]" @click="isSidebarOpen = false">
              <component :is="item.icon" class="w-5 h-5 mr-3 flex-shrink-0" />
              {{ item.label }}
            </router-link>

            <button v-else-if="item && item.children && item.children.length > 0" @click="toggleSubmenu(item)"
              class="flex items-center w-full px-4 py-3 text-sm font-medium rounded-md transition duration-200 hover:bg-gray-700/70 text-left"
              :class="[
                item.open || $route.path.startsWith(item.path)
                  ? 'bg-gray-700 text-white border-l-4 border-pink-500'
                  : 'text-gray-400 hover:text-white',
              ]">
              <component :is="item.icon" class="w-5 h-5 mr-3 flex-shrink-0" />
              {{ item.label }}
              <ChevronDownIcon :class="{ 'rotate-180': item.open }"
                class="w-4 h-4 ml-auto transform transition duration-200" />
            </button>

            <div v-show="item && item.open && item.children" class="space-y-1 py-1 pl-4">
              <router-link v-for="child in item.children" :key="child.path" :to="child.path"
                class="flex items-center pl-8 py-2 text-sm font-medium rounded-md transition duration-200 hover:bg-gray-700/70"
                :class="[
                  $route.path === child.path
                    ? 'text-pink-400 bg-gray-700/50'
                    : 'text-gray-400 hover:text-white',
                ]" @click="isSidebarOpen = false">
                <div class="w-1 h-1 mr-3 rounded-full bg-current"></div>
                {{ child.label }}
              </router-link>
            </div>
          </template>
        </nav>
      </div>
    </div>

    <!-- Main Content -->
    <div class="flex flex-col flex-1 overflow-hidden">
      <!-- Top Navigation -->
      <header class="bg-white shadow-sm">
        <div class="flex items-center justify-between px-4 py-3 sm:px-6 lg:px-8">
          <div class="flex items-center">
            <button @click="isSidebarOpen = !isSidebarOpen"
              class="p-1 text-gray-500 rounded-md hover:text-gray-600 focus:outline-none focus:ring-2 focus:ring-indigo-500 md:hidden">
              <Bars3Icon class="w-6 h-6" />
            </button>
            <h1 class="ml-2 text-lg font-semibold text-gray-900">
              {{ $route.meta.title || 'Dashboard' }}
            </h1>
          </div>

          <div class="flex items-center space-x-4">
            <button
              class="p-1 text-gray-500 rounded-full hover:text-gray-600 focus:outline-none focus:ring-2 focus:ring-indigo-500">
              <span class="sr-only">View notifications</span>
              <BellIcon class="w-6 h-6" />
            </button>

            <!-- Profile dropdown -->
            <div class="relative ml-3">
              <div>
                <button @click="isProfileOpen = !isProfileOpen" type="button"
                  class="flex items-center max-w-xs text-sm rounded-full focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                  id="user-menu-button">
                  <span class="sr-only">Open user menu</span>
                  <div
                    class="w-8 h-8 rounded-full bg-indigo-100 flex items-center justify-center text-indigo-600 font-medium">
                    {{ userInitials }}
                  </div>
                </button>
              </div>

              <!-- Dropdown menu -->
              <transition enter-active-class="transition duration-100 ease-out transform"
                enter-from-class="opacity-0 scale-95" enter-to-class="opacity-100 scale-100"
                leave-active-class="transition duration-75 ease-in" leave-from-class="opacity-100 scale-100"
                leave-to-class="opacity-0 scale-95">
                <div v-show="isProfileOpen"
                  class="absolute right-0 z-10 w-48 py-1 mt-2 origin-top-right bg-white rounded-md shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none"
                  role="menu" aria-orientation="vertical" aria-labelledby="user-menu-button" tabindex="-1">
                  <a href="#" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100" role="menuitem"
                    tabindex="-1" id="user-menu-item-0">Your Profile</a>
                  <a href="#" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100" role="menuitem"
                    tabindex="-1" id="user-menu-item-1">Settings</a>
                  <button @click="logout"
                    class="block w-full px-4 py-2 text-sm text-left text-red-600 hover:bg-gray-100" role="menuitem"
                    tabindex="-1" id="user-menu-item-2">
                    Sign out
                  </button>
                </div>
              </transition>
            </div>
          </div>
        </div>
      </header>

      <!-- Main content area -->
      <main class="flex-1 overflow-y-auto focus:outline-none bg-gray-50">
        <div class="p-4 sm:p-6">
          <router-view />
        </div>
      </main>

      <!-- Mobile sidebar -->
      <div v-show="isSidebarOpen" class="md:hidden">
        <div class="fixed inset-0 z-40 flex">
          <div class="fixed inset-0 bg-gray-600 bg-opacity-75" @click="isSidebarOpen = false"></div>
          <div class="relative flex flex-col flex-1 w-full max-w-xs bg-gray-800">
            <div class="absolute top-0 right-0 p-1 -mr-14">
              <button @click="isSidebarOpen = false"
                class="flex items-center justify-center w-12 h-12 rounded-full focus:outline-none focus:ring-2 focus:ring-white">
                <XMarkIcon class="w-6 h-6 text-white" />
                <span class="sr-only">Close sidebar</span>
              </button>
            </div>
            <div class="flex-1 px-4 py-4 space-y-1 overflow-y-auto">
              <template v-for="(navItem, index) in navItems" :key="navItem?.path || index">
                <router-link v-if="navItem && (!navItem.children || navItem.children.length === 0)" 
                  :to="navItem.path"
                  class="flex items-center px-4 py-3 text-sm font-medium text-gray-300 rounded-md group hover:bg-gray-700 hover:text-white"
                  :class="{'bg-gray-900 text-white': $route.path === navItem.path}"
                  @click="isSidebarOpen = false">
                  <component :is="navItem.icon" class="w-5 h-5 mr-3 text-gray-400 group-hover:text-gray-300" />
                  {{ navItem.label }}
                </router-link>

                <div v-else-if="navItem && navItem.children && navItem.children.length > 0" class="space-y-1">
                  <button @click="toggleSubmenu(navItem)"
                    class="flex items-center w-full px-4 py-3 text-sm font-medium text-left text-gray-300 rounded-md group hover:bg-gray-700 hover:text-white"
                    :class="{'bg-gray-900 text-white': $route.path.startsWith(navItem.path)}">
                    <component :is="navItem.icon" class="w-5 h-5 mr-3 text-gray-400 group-hover:text-gray-300" />
                    <span class="flex-1">{{ navItem.label }}</span>
                    <ChevronDownIcon :class="{'rotate-180': navItem.open}" class="w-4 h-4 transform transition-transform" />
                  </button>

                  <div v-show="navItem.open" class="pl-4 space-y-1">
                    <router-link v-for="child in navItem.children" :key="child.path" :to="child.path"
                      class="flex items-center px-4 py-2 text-sm font-medium text-gray-300 rounded-md group hover:bg-gray-700 hover:text-white"
                      :class="{'bg-gray-800 text-white': $route.path === child.path}"
                      @click="isSidebarOpen = false">
                      <span class="w-1.5 h-1.5 mr-3 rounded-full bg-current"></span>
                      {{ child.label }}
                    </router-link>
                  </div>
                </div>
              </template>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

/* Custom scrollbar for sidebar */
::-webkit-scrollbar {
  width: 6px;
}

::-webkit-scrollbar-track {
  background: #2d3748;
}

::-webkit-scrollbar-thumb {
  background: #4a5568;
  border-radius: 3px;
}

::-webkit-scrollbar-thumb:hover {
  background: #718096;
}

/* Smooth transitions */
.smooth-transition {
  transition: all 0.3s ease-in-out;
}

/* Focus styles */
.focus-style {
  outline: none;
  --tw-ring-offset-shadow: var(--tw-ring-inset) 0 0 0 var(--tw-ring-offset-width) var(--tw-ring-offset-color);
  --tw-ring-shadow: var(--tw-ring-inset) 0 0 0 calc(2px + var(--tw-ring-offset-width)) var(--tw-ring-color);
  box-shadow: var(--tw-ring-offset-shadow), var(--tw-ring-shadow), var(--tw-shadow, 0 0 #0000);
  --tw-ring-opacity: 1;
  --tw-ring-color: rgb(99 102 241 / var(--tw-ring-opacity));
  --tw-ring-offset-width: 2px;
}
</style>
<script setup lang="ts">
import { ref, onMounted, onUnmounted, computed, markRaw, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '@/stores/auth' // Thay bằng store của bạn
import {
  Bars3Icon,
  XMarkIcon,
  BellIcon,
  ChevronDownIcon,
  HomeIcon,
  ShoppingBagIcon,
  DocumentTextIcon,
  ArrowPathIcon,
  UserGroupIcon,
  ChartBarIcon,
  UsersIcon,
  PlusCircleIcon,
  TagIcon,
  UserPlusIcon
} from '@heroicons/vue/24/outline'

const authStore = useAuthStore()

// Tính toán chữ cái đầu (ví dụ: "Nguyễn Văn A" -> "NA")
const userInitials = computed(() => {
  const name = authStore.user?.fullName || 'Admin'
  const parts = name.split(' ').filter((p) => p.length > 0)
  if (parts.length > 1) {
    return (parts[0][0] + parts[parts.length - 1][0]).toUpperCase()
  }
  return name.substring(0, 2).toUpperCase()
})

// Define NavItem interface
interface NavItem {
  label: string;
  path: string;
  icon: any;
}

// Define NavItem interface
interface NavItem {
  label: string;
  path: string;
  icon: any;
  children: NavItem[];
  open: boolean;
}

// KHAI BÁO navItems (Đã sửa thành ref)
const navItems = ref<NavItem[]>([
  { 
    label: 'Dashboard', 
    path: '/admin', 
    icon: markRaw(HomeIcon),
    children: [], 
    open: false
  },
  {
    label: 'Sản phẩm',
    path: '/admin/products',
    icon: markRaw(ShoppingBagIcon),
    open: false,
    children: [
      { 
        label: 'Danh sách sản phẩm', 
        path: '/admin/products',
        icon: markRaw(ShoppingBagIcon),
        children: [],
        open: false
      },
      { 
        label: 'Thêm sản phẩm', 
        path: '/admin/products/new',
        icon: markRaw(PlusCircleIcon),
        children: [],
        open: false
      },
      { 
        label: 'Quản lý Danh mục', 
        path: '/admin/products/categories',
        icon: markRaw(TagIcon),
        children: [],
        open: false
      },
    ],
  },
  {
    label: 'Đơn hàng',
    path: '/admin/orders',
    icon: markRaw(DocumentTextIcon),
    open: false,
    children: [
      { 
        label: 'Đơn hàng mới', 
        path: '/admin/orders/new',
        icon: markRaw(PlusCircleIcon),
        children: [],
        open: false
      },
      { 
        label: 'Quản lý tất cả', 
        path: '/admin/orders/all',
        icon: markRaw(DocumentTextIcon),
        children: [],
        open: false
      },
      { 
        label: 'Thống kê', 
        path: '/admin/orders/stats',
        icon: markRaw(ChartBarIcon),
        children: [],
        open: false
      },
    ],
  },
  {
    label: 'Quản lý Khách hàng',
    path: '/admin/customers',
    icon: markRaw(UsersIcon),
    open: false,
    children: [
      {
        label: 'Danh sách khách hàng',
        path: '/admin/customers',
        icon: markRaw(UsersIcon),
        children: [],
        open: false
      },
      {
        label: 'Thêm khách hàng',
        path: '/admin/customers/new',
        icon: markRaw(UserPlusIcon),
        children: [],
        open: false
      }
    ]
  },
  {
    label: 'Giao dịch',
    path: '/admin/transactions',
    icon: markRaw(ArrowPathIcon),
    open: false,
    children: [
      {
        label: 'Báo cáo giao dịch',
        path: '/admin/transactions/report',
        icon: markRaw(DocumentTextIcon),
        children: [],
        open: false
      }
    ]
  },
  {
    label: 'Đối tác',
    path: '/admin/partners',
    icon: markRaw(UserGroupIcon),
    open: false,
    children: [
      {
        label: 'Danh sách đối tác',
        path: '/admin/partners',
        icon: markRaw(UserGroupIcon),
        children: [],
        open: false
      }
    ]
  },
  {
    label: 'Bán hàng',
    path: '/admin/sales',
    icon: markRaw(ChartBarIcon),
    open: false,
    children: [
      {
        label: 'Thống kê bán hàng',
        path: '/admin/sales/stats',
        icon: markRaw(ChartBarIcon),
        children: [],
        open: false
      }
    ]
  },
])

// Using router for navigation
const router = useRouter()
const route = useRoute()

// Log route changes
watch(() => route, (newRoute) => {
  console.log('🔵 ROUTE CHANGED TO:', {
    path: newRoute.path,
    fullPath: newRoute.fullPath,
    name: newRoute.name,
    params: newRoute.params,
    query: newRoute.query
  })
}, { deep: true, immediate: true })

// Log available routes
console.log('🟢 AVAILABLE ROUTES:', router.getRoutes())
console.log('🔵 CURRENT ROUTE:', {
  path: route.path,
  fullPath: route.fullPath,
  name: route.name,
  params: route.params,
  query: route.query
})
const isProfileOpen = ref(false)
const isSidebarOpen = ref(false)

// HÀM QUAN TRỌNG ĐỂ MỞ/ĐÓNG MENU CON
const toggleSubmenu = (clickedItem: any) => {
  if (clickedItem.children && clickedItem.children.length > 0) {
    // Đóng tất cả các menu khác
    navItems.value.forEach((item) => {
      if (item !== clickedItem && item.children && item.children.length > 0) {
        item.open = false
      }
    })
    // Mở/đóng mục hiện tại
    clickedItem.open = !clickedItem.open
  } else {
    isSidebarOpen.value = false
  }
}

const handleClickOutside = (event: MouseEvent) => {
  const profileButton = document.getElementById('user-menu-button')
  const profileDropdown = profileButton?.nextElementSibling

  if (isProfileOpen.value && profileButton && profileDropdown) {
    if (
      !profileButton.contains(event.target as Node) &&
      !profileDropdown.contains(event.target as Node)
    ) {
      isProfileOpen.value = false
    }
  }
}

// Close sidebar and profile when route changes
router.afterEach((to, from) => {
  console.log('Route changed from:', from.path, 'to:', to.path)
  isSidebarOpen.value = false
  isProfileOpen.value = false
})

// Add event listeners
onMounted(() => {
  document.addEventListener('click', handleClickOutside)
  document.addEventListener('keydown', (e) => {
    if (e.key === 'Escape' && isProfileOpen.value) {
      isProfileOpen.value = false
    }
  })
})

// Cleanup
</script>

<style scoped>
/* Custom scrollbar matching the dark theme */
.custom-scrollbar::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}

.custom-scrollbar::-webkit-scrollbar-track {
  background: #2d3748;
}

.custom-scrollbar::-webkit-scrollbar-thumb {
  background: #4a5568;
  border-radius: 3px;
}

.custom-scrollbar::-webkit-scrollbar-thumb:hover {
  background: #718096;
}

.custom-scrollbar::-webkit-scrollbar-thumb:hover {
  background: #718096;
}
</style>
