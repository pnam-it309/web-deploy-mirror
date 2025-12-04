<script setup lang="ts">
import { ref, onMounted, onUnmounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { useSidebarStore } from '@/stores/sidebar.store'

// Import Component Sidebar đã tách
import Sidebar from '@/components/custom/Sidebar/Sidebar.vue' 

// Import Icons cho Header
import {
  Bars3Icon,
  BellIcon,
  UserCircleIcon,
  ArrowLeftOnRectangleIcon,
  MoonIcon,
  SunIcon
} from '@heroicons/vue/24/outline'
import { useTheme } from '@/composable/useTheme'

const router = useRouter()
const authStore = useAuthStore()
const sidebarStore = useSidebarStore()
const { isDark, toggleTheme } = useTheme()

// State cho Header
const isProfileOpen = ref(false)

const userInitials = computed(() => {
  if (!authStore.user?.fullName) return 'AD'
  const name = authStore.user.fullName.trim()
  const parts = name.split(/\s+/)
  if (parts.length > 1) {
    return (parts[0][0] + parts[parts.length - 1][0]).toUpperCase()
  }
  return name.substring(0, 2).toUpperCase()
})

const isAuthenticated = computed(() => authStore.isAuthenticated && authStore.user !== null)

// Click outside to close profile dropdown
const handleClickOutside = (event: MouseEvent) => {
  const profileButton = document.getElementById('user-menu-button')
  const profileDropdown = document.getElementById('user-menu-dropdown')
  
  if (isProfileOpen.value && profileButton && profileDropdown) {
    if (!profileButton.contains(event.target as Node) && !profileDropdown.contains(event.target as Node)) {
      isProfileOpen.value = false
    }
  }
}

const logout = () => {
  authStore.logout()
  router.push('/login')
}

onMounted(() => {
  document.addEventListener('click', handleClickOutside)
})

onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside)
})
</script>

<template>
  <!-- Layout Container -->
  <div v-if="isAuthenticated" class="flex h-screen bg-[#f8f9fa] text-gray-800 font-sans overflow-hidden">
    
    <!-- 1. SIDEBAR COMPONENT -->
    <!-- Sidebar tự lo việc hiển thị và mobile toggle -->
    <Sidebar />

    <!-- 2. MAIN CONTENT AREA -->
    <div class="flex flex-col flex-1 w-0 overflow-hidden">
      
      <!-- Top Header -->
      <header class="relative z-10 flex items-center justify-between h-16 px-4 bg-white/80 backdrop-blur-md border-b border-gray-200 shadow-sm sm:px-6 lg:px-8 flex-shrink-0">
        <!-- Left: Toggle Button (Mobile) & Title -->
        <div class="flex items-center">
          <button
            @click="sidebarStore.toggleSidebar()"
            class="lg:hidden p-2 -ml-2 text-gray-500 rounded-md hover:text-gray-900 focus:outline-none focus:ring-2 focus:ring-inset focus:ring-indigo-500"
          >
            <Bars3Icon class="w-6 h-6" />
          </button>
          <h1 class="ml-2 text-lg font-semibold text-[#5a483e]">
            {{ $route.meta.title || 'Dashboard' }}
          </h1>
        </div>

        <!-- Right: Actions & Profile -->
        <div class="flex items-center space-x-4">
          <!-- Notification Btn -->
          <button class="p-1 text-gray-400 hover:text-gray-500 transition-colors relative">
            <span class="sr-only">View notifications</span>
            <BellIcon class="w-6 h-6" />
            <span class="absolute top-0 right-0 block h-2 w-2 rounded-full bg-red-500 ring-2 ring-white"></span>
          </button>

          <!-- Profile Dropdown -->
          <div class="relative ml-3">
            <button
              id="user-menu-button"
              @click="isProfileOpen = !isProfileOpen"
              class="flex items-center max-w-xs text-sm rounded-full focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-[#adc178] transition-shadow"
            >
              <span class="sr-only">Open user menu</span>
              <div class="w-8 h-8 rounded-full bg-[#dde5b6] flex items-center justify-center text-[#386641] font-bold shadow-inner">
                {{ userInitials }}
              </div>
            </button>

            <!-- Dropdown Panel -->
            <transition
              enter-active-class="transition ease-out duration-100"
              enter-from-class="transform opacity-0 scale-95"
              enter-to-class="transform opacity-100 scale-100"
              leave-active-class="transition ease-in duration-75"
              leave-from-class="transform opacity-100 scale-100"
              leave-to-class="transform opacity-0 scale-95"
            >
              <div
                id="user-menu-dropdown"
                v-show="isProfileOpen"
                class="absolute right-0 z-20 w-48 py-1 mt-2 origin-top-right bg-white rounded-md shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none"
              >
                <div class="px-4 py-2 border-b border-gray-100">
                    <p class="text-sm text-gray-900 font-medium truncate">{{ authStore.user?.fullName }}</p>
                    <p class="text-xs text-gray-500 truncate">{{ authStore.user?.email }}</p>
                </div>
                <a href="#" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-50 flex items-center">
                    <UserCircleIcon class="w-4 h-4 mr-2"/> Hồ sơ
                </a>
                <a href="#" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-50 flex items-center">
                    <span class="w-4 h-4 mr-2">⚙️</span> Cài đặt
                </a>
                <button
                  @click="toggleTheme"
                  class="block w-full text-left px-4 py-2 text-sm text-gray-700 hover:bg-gray-50 flex items-center justify-between"
                >
                   <div class="flex items-center">
                     <MoonIcon v-if="isDark" class="w-4 h-4 mr-2"/>
                     <SunIcon v-else class="w-4 h-4 mr-2"/>
                     {{ isDark ? 'Chế độ sáng' : 'Chế độ tối' }}
                   </div>
                   <!-- Simple Toggle Switch UI -->
                   <div class="w-8 h-4 bg-gray-200 rounded-full relative transition-colors" :class="{'bg-indigo-600': isDark}">
                      <div class="w-4 h-4 bg-white rounded-full shadow absolute top-0 left-0 transition-transform" :class="{'translate-x-4': isDark}"></div>
                   </div>
                </button>
                <button
                  @click="logout"
                  class="block w-full text-left px-4 py-2 text-sm text-red-600 hover:bg-red-50 flex items-center"
                >
                   <ArrowLeftOnRectangleIcon class="w-4 h-4 mr-2"/> Đăng xuất
                </button>
              </div>
            </transition>
          </div>
        </div>
      </header>

      <!-- Main View (Scrollable) -->
      <main class="flex-1 overflow-y-auto bg-[#f9fafb] p-4 sm:p-6 relative scroll-smooth">
        <router-view v-slot="{ Component }">
             <transition name="fade" mode="out-in">
                <component :is="Component" />
             </transition>
        </router-view>
      </main>
    </div>
  </div>
  
  <!-- Loading State -->
  <div v-else class="flex items-center justify-center h-screen bg-gray-50">
    <div class="text-center">
      <div class="animate-spin rounded-full h-12 w-12 border-b-4 border-[#adc178] mx-auto mb-4"></div>
      <p class="text-lg font-medium text-gray-600">Đang tải dữ liệu...</p>
    </div>
  </div>
</template>

<style scoped>
/* Transition cho Router View */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>