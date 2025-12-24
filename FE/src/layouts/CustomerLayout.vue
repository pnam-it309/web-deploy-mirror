<template>
  <div class="flex flex-col min-h-screen bg-gray-50">
    <!-- Top Navigation Bar -->
    <header class="fixed top-0 z-50 transition-all duration-300 w-full left-0 right-0" :class="[
      isNavbarVisible ? 'translate-y-0' : '-translate-y-full',
      lastScrollPosition > 0 ? 'bg-white shadow-md' : 'bg-white'
    ]">
      <div class="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
        <div class="flex h-16 justify-between items-center">
          <!-- Logo -->
          <div class="flex items-center">
            <h1 class="text-2xl font-bold text-indigo-600 cursor-pointer" @click="router.push('/customer/dashboard')">
              Customer Portal
            </h1>

            <!-- Desktop Navigation -->
            <nav class="hidden md:ml-10 md:flex md:space-x-8">
              <router-link v-for="item in navItems" :key="item.path" :to="item.path"
                class="inline-flex items-center px-1 pt-1 text-sm font-medium border-b-2 transition-colors duration-200"
                :class="[
                  $route.path.startsWith(item.path)
                    ? 'border-indigo-500 text-gray-900'
                    : 'border-transparent text-gray-500 hover:border-gray-300 hover:text-gray-700'
                ]">
                <component :is="item.icon" class="w-5 h-5 mr-1.5" :class="[
                  $route.path.startsWith(item.path)
                    ? 'text-indigo-500'
                    : 'text-gray-400 group-hover:text-gray-500'
                ]" />
                {{ item.label }}
              </router-link>
            </nav>
          </div>

          <!-- Right Section: Search Icon, User Profile -->
          <div class="flex items-center gap-4">
            <!-- Search Icon Toggle -->
            <button @click="isSearchOpen = true" class="p-2 text-gray-400 hover:text-gray-500 transition-colors">
              <span class="sr-only">Search</span>
              <MagnifyingGlassIcon class="h-6 w-6" />
            </button>
            <!-- Search (Optional) -->
            <!-- 
            <div class="hidden lg:block relative mr-2">
               <input 
                 type="text" 
                 placeholder="Search products..." 
                 class="w-64 rounded-full border-gray-300 pl-10 focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm"
                 @keydown.enter="handleSearch"
               >
               <MagnifyingGlassIcon class="absolute left-3 top-2.5 h-4 w-4 text-gray-400" />
            </div>
            -->

            <!-- Wishlist -->
            <!-- Icons (Cart, Wishlist, Bell) removed as requested -->
            <!-- 
            <router-link to="/customer/wishlist" ... > ... </router-link>
            <router-link to="/customer/orders" ... > ... </router-link>
            <button ... > ... </button>
            -->

            <!-- Profile Dropdown (Simple version for now) -->
            <div class="relative ml-3 hidden md:block group">
              <button
                class="flex rounded-full bg-white text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2">
                <span class="sr-only">Open user menu</span>
                <div
                  class="h-8 w-8 rounded-full bg-indigo-100 flex items-center justify-center text-indigo-600 font-bold border border-indigo-200">
                  {{ userInitials }}
                </div>
              </button>

              <!-- Dropdown Menu -->
              <div
                class="absolute right-0 z-10 mt-2 w-48 origin-top-right rounded-md bg-white py-1 shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none hidden group-hover:block hover:block">
                <div class="px-4 py-2 border-b border-gray-100">
                  <p class="text-sm font-medium text-gray-900 truncate">{{ userName }}</p>
                  <p class="text-xs text-gray-500 truncate">{{ authStore.user?.email }}</p>
                </div>
                <router-link to="/customer/profile" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">Your
                  Profile</router-link>
                <!-- <router-link to="/customer/settings" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">Settings</router-link> -->
                <button @click="handleLogout"
                  class="block w-full text-left px-4 py-2 text-sm text-red-600 hover:bg-gray-50">Sign out</button>
              </div>
            </div>

            <!-- Mobile menu button -->
            <div class="flex items-center md:hidden">
              <button @click="isMobileMenuOpen = !isMobileMenuOpen"
                class="inline-flex items-center justify-center rounded-md p-2 text-gray-400 hover:bg-gray-100 hover:text-gray-500 focus:outline-none focus:ring-2 focus:ring-inset focus:ring-indigo-500">
                <span class="sr-only">Open main menu</span>
                <Bars3Icon v-if="!isMobileMenuOpen" class="block h-6 w-6" aria-hidden="true" />
                <XMarkIcon v-else class="block h-6 w-6" aria-hidden="true" />
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- Mobile Menu -->
      <div v-if="isMobileMenuOpen" class="md:hidden border-t border-gray-200 bg-white">
        <div class="space-y-1 pb-3 pt-2">
          <router-link v-for="item in navItems" :key="item.path" :to="item.path" @click="isMobileMenuOpen = false"
            class="block border-l-4 py-2 pl-3 pr-4 text-base font-medium" :class="[
              $route.path.startsWith(item.path)
                ? 'border-indigo-500 bg-indigo-50 text-indigo-700'
                : 'border-transparent text-gray-500 hover:border-gray-300 hover:bg-gray-50 hover:text-gray-700'
            ]">
            <div class="flex items-center">
              <component :is="item.icon" class="mr-3 h-5 w-5 flex-shrink-0" />
              {{ item.label }}
            </div>
          </router-link>
        </div>
        <div class="border-t border-gray-200 pb-3 pt-4">
          <div class="flex items-center px-4">
            <div class="flex-shrink-0">
              <div
                class="h-10 w-10 rounded-full bg-indigo-100 flex items-center justify-center text-indigo-600 font-bold border border-indigo-200">
                {{ userInitials }}
              </div>
            </div>
            <div class="ml-3">
              <div class="text-base font-medium text-gray-800">{{ userName }}</div>
              <div class="text-sm font-medium text-gray-500">{{ authStore.user?.email }}</div>
            </div>
            <button type="button"
              class="ml-auto flex-shrink-0 rounded-full bg-white p-1 text-gray-400 hover:text-gray-500 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2">
              <span class="sr-only">View notifications</span>
              <BellIcon class="h-6 w-6" aria-hidden="true" />
            </button>
          </div>
          <div class="mt-3 space-y-1">
            <router-link to="/customer/profile" @click="isMobileMenuOpen = false"
              class="block px-4 py-2 text-base font-medium text-gray-500 hover:bg-gray-100 hover:text-gray-800">Your
              Profile</router-link>
            <!-- <router-link to="/customer/settings" @click="isMobileMenuOpen = false" class="block px-4 py-2 text-base font-medium text-gray-500 hover:bg-gray-100 hover:text-gray-800">Settings</router-link> -->
            <button @click="handleLogout"
              class="block w-full text-left px-4 py-2 text-base font-medium text-red-600 hover:bg-gray-100">Sign
              out</button>
          </div>
        </div>
      </div>
    </header>

    <!-- Page Content -->
    <main class="flex-1 bg-gray-50 pt-16">
      <div class="mx-auto max-w-7xl px-4 py-6 sm:px-6 lg:px-8">
        <router-view />
      </div>
    </main>

    <!-- Search Overlay -->
    <SearchOverlay :is-open="isSearchOpen" @close="isSearchOpen = false" />
  </div>
</template>

<script setup lang="ts">
import { onMounted, onUnmounted, computed, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { toast } from 'vue3-toastify'
import 'vue3-toastify/dist/index.css'
import {
  BellIcon,
  HomeIcon,
  ShoppingCartIcon,
  DocumentTextIcon,
  UserCircleIcon,
  HeartIcon,
  Bars3Icon,
  XMarkIcon,
  MagnifyingGlassIcon
} from '@heroicons/vue/24/outline'
import SearchOverlay from '@/components/custom/SearchOverlay.vue'

const isNavbarVisible = ref(true)
const lastScrollPosition = ref(0)
const isMobileMenuOpen = ref(false)
const isSearchOpen = ref(false)

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()

const wishlistCount = ref(0)

const updateWishlistCount = () => {
  const wishlist = JSON.parse(localStorage.getItem('wishlist') || '[]')
  wishlistCount.value = wishlist.length
}

const handleScroll = () => {
  const currentScrollPosition = window.scrollY
  if (currentScrollPosition < 0) return

  // Show navbar if scrolling up or at the top
  if (Math.abs(currentScrollPosition - lastScrollPosition.value) < 60) return

  isNavbarVisible.value = currentScrollPosition < lastScrollPosition.value || currentScrollPosition < 50
  lastScrollPosition.value = currentScrollPosition
}

const handleSearch = (event: any) => {
  const query = event.target.value
  router.push({ path: '/customer/products', query: { search: query } })
  isMobileMenuOpen.value = false
}

// Navigation items
const navItems = [
  { path: '/customer/dashboard', label: 'Trang chủ', icon: HomeIcon },
  { path: '/customer/products', label: 'Sản phẩm', icon: DocumentTextIcon },
  { path: '/customer/orders', label: 'Đơn hàng', icon: ShoppingCartIcon },
  { path: '/customer/wishlist', label: 'Sản phẩm yêu thích', icon: HeartIcon },
]

// User initials
const userInitials = computed(() => {
  const user = authStore.user
  if (!user) return 'U'
  return user.fullName
    ? user.fullName
      .split(' ')
      .map((n) => n[0])
      .join('')
      .toUpperCase()
      .substring(0, 2)
    : user.email
      ? user.email[0].toUpperCase()
      : 'U'
})

// User name
const userName = computed(() => {
  return authStore.user?.fullName || authStore.user?.email?.split('@')[0] || 'User'
})

// Logout
const handleLogout = async () => {
  try {
    await authStore.logout()
    toast.success('Đăng xuất thành công!')
    router.push('/login')
  } catch (error) {
    toast.error('Đăng xuất thất bại!')
    console.error('Logout failed:', error)
  }
}

// Khi vừa load trang
onMounted(() => {
  // toast.success('Chào mừng bạn đến với catalgo web')
  updateWishlistCount()
  window.addEventListener('wishlist-updated', updateWishlistCount)
  window.addEventListener('scroll', handleScroll)

  // Also update when storage changes (e.g. from another tab)
  window.addEventListener('storage', (e) => {
    if (e.key === 'wishlist') updateWishlistCount()
  })
})

onUnmounted(() => {
  window.removeEventListener('wishlist-updated', updateWishlistCount)
  window.removeEventListener('scroll', handleScroll)
})
</script>

<style scoped>
/* Scrollbar styling */
::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}

::-webkit-scrollbar-track {
  background: rgba(0, 0, 0, 0.1);
  border-radius: 3px;
}

::-webkit-scrollbar-thumb {
  background: rgba(255, 255, 255, 0.2);
  border-radius: 3px;
}

::-webkit-scrollbar-thumb:hover {
  background: rgba(255, 255, 255, 0.3);
}
</style>
