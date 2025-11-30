<script setup lang="ts">
import { ref, onMounted, onUnmounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { useSidebarStore } from '@/stores/sidebar.store'
import { useTheme } from '@/composable/useTheme'

import Sidebar from '@/components/custom/Sidebar/Sidebar.vue'
// Import ButtonCustom
import ButtonCustom from '@/components/custom/Button/ButtonDefault.vue' 

import {
  Bars3Icon,
  BellIcon,
  UserCircleIcon,
  ArrowLeftOnRectangleIcon,
  MoonIcon,
  SunIcon
} from '@heroicons/vue/24/outline'

const router = useRouter()
const authStore = useAuthStore()
const sidebarStore = useSidebarStore()
const { isDark, toggleTheme } = useTheme()

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

onMounted(() => { document.addEventListener('click', handleClickOutside) })
onUnmounted(() => { document.removeEventListener('click', handleClickOutside) })
</script>

<template>
  <!-- SỬA: Dùng màu Gray-900 cho nền tối thay vì brand-dark-200 (đảm bảo hiện màu đen) -->
  <div v-if="isAuthenticated" class="flex h-screen bg-[#f8f9fa] dark:bg-gray-900 text-gray-800 dark:text-gray-100 font-sans overflow-hidden transition-colors duration-300">
    
    <Sidebar />

    <div class="flex flex-col flex-1 w-0 overflow-hidden">
      
      <!-- HEADER -->
      <!-- SỬA: Header dùng Gray-800 đậm hơn nền chính một chút -->
      <header class="relative z-10 flex items-center justify-between h-16 px-4 bg-white/80 dark:bg-gray-800/95 backdrop-blur-md border-b border-gray-200 dark:border-gray-700 shadow-sm sm:px-6 lg:px-8 flex-shrink-0 transition-colors duration-300">
        
        <!-- Left: Mobile Toggle -->
        <div class="flex items-center gap-3">
          <!-- SỬA: Nút toggle mobile -->
          <ButtonCustom
            @click="sidebarStore.toggleSidebar()"
            color="cream-soft"
            class="lg:hidden !p-2 !rounded-lg dark:bg-gray-700 dark:text-gray-200"
          >
            <Bars3Icon class="w-6 h-6" />
          </ButtonCustom>

          <!-- Tiêu đề trang: Màu đậm ở Light, màu sáng ở Dark -->
          <h1 class="text-lg font-bold text-brand-mocha dark:text-gray-100 tracking-wide transition-colors">
            {{ $route.meta.title }}
          </h1>
        </div>

        <!-- Right: Actions -->
        <div class="flex items-center space-x-3">
          
          <!-- Notification Btn -->
          <div class="relative">
            <!-- SỬA: Nút thông báo trên nền tối -->
            <ButtonCustom class="!p-2 !rounded-full relative bg-transparent hover:bg-gray-100 dark:hover:bg-gray-700 text-gray-500 dark:text-gray-300 border-none shadow-none">
              <BellIcon class="w-6 h-6" />
              <span class="absolute top-1.5 right-1.5 block h-2 w-2 rounded-full bg-red-500 ring-2 ring-white dark:ring-gray-800"></span>
            </ButtonCustom>
          </div>

          <!-- Profile Dropdown -->
          <div class="relative ml-2">
            <button
              id="user-menu-button"
              @click="isProfileOpen = !isProfileOpen"
              class="flex items-center max-w-xs text-sm rounded-full focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-brand-olive transition-shadow"
            >
              <!-- Avatar: Light nền kem, Dark nền xám -->
              <div class="w-9 h-9 rounded-full bg-brand-cream dark:bg-gray-700 flex items-center justify-center text-brand-mocha dark:text-white font-bold shadow-sm border border-brand-sage/50 dark:border-gray-600 transition-colors">
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
                class="absolute right-0 z-20 w-60 py-2 mt-2 origin-top-right bg-white dark:bg-gray-800 rounded-xl shadow-xl ring-1 ring-black ring-opacity-5 dark:ring-white/10 focus:outline-none divide-y divide-gray-100 dark:divide-gray-700 transition-colors"
              >
                <div class="px-4 py-3">
                    <p class="text-sm font-bold text-gray-800 dark:text-white truncate">{{ authStore.user?.fullName }}</p>
                    <p class="text-xs text-gray-500 dark:text-gray-400 truncate">{{ authStore.user?.email }}</p>
                </div>

                <div class="py-1">
                  <a href="#" class="group flex items-center px-4 py-2 text-sm text-gray-700 dark:text-gray-300 hover:bg-gray-50 dark:hover:bg-gray-700/50 transition-colors">
                      <UserCircleIcon class="mr-3 h-5 w-5 text-gray-400 group-hover:text-brand-olive dark:text-gray-500 dark:group-hover:text-gray-300" /> Hồ sơ
                  </a>
                  
                  <!-- Nút Chuyển Theme trong Dropdown -->
                  <button
                    @click="toggleTheme"
                    class="w-full group flex items-center justify-between px-4 py-2 text-sm text-gray-700 dark:text-gray-300 hover:bg-gray-50 dark:hover:bg-gray-700/50 transition-colors"
                  >
                    <div class="flex items-center">
                       <MoonIcon v-if="isDark" class="mr-3 h-5 w-5 text-yellow-400"/>
                       <SunIcon v-else class="mr-3 h-5 w-5 text-orange-500"/>
                       <span>{{ isDark ? 'Giao diện Tối' : 'Giao diện Sáng' }}</span>
                    </div>
                    <!-- Switch -->
                    <div class="w-8 h-4 rounded-full relative transition-colors duration-300" :class="isDark ? 'bg-brand-olive' : 'bg-gray-300'">
                        <div class="w-3 h-3 bg-white rounded-full shadow absolute top-0.5 left-0.5 transition-transform duration-300" :class="{'translate-x-4': isDark}"></div>
                    </div>
                  </button>
                </div>

                <div class="py-1">
                  <button
                    @click="logout"
                    class="w-full group flex items-center px-4 py-2 text-sm text-red-600 dark:text-red-400 hover:bg-red-50 dark:hover:bg-red-900/20 transition-colors"
                  >
                     <ArrowLeftOnRectangleIcon class="mr-3 h-5 w-5 text-red-400 group-hover:text-red-500"/> Đăng xuất
                  </button>
                </div>
              </div>
            </transition>
          </div>
        </div>
      </header>

      <!-- MAIN VIEW -->
      <!-- SỬA: Nền chính màu Gray-900 -->
      <main class="flex-1 overflow-y-auto bg-[#f9fafb] dark:bg-gray-900 p-4 sm:p-6 relative scroll-smooth transition-colors duration-300">
        <router-view v-slot="{ Component }">
             <transition name="fade" mode="out-in">
                <component :is="Component" />
             </transition>
        </router-view>
      </main>
    </div>
  </div>
  
  <!-- Loading -->
  <div v-else class="flex items-center justify-center h-screen bg-brand-cream/20 dark:bg-gray-900">
    <div class="text-center">
      <div class="animate-spin rounded-full h-12 w-12 border-b-4 border-brand-olive mx-auto mb-4"></div>
      <p class="text-lg font-medium text-brand-mocha dark:text-gray-200">Đang tải dữ liệu...</p>
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