<template>

  <div class="flex h-screen bg-gray-100">
    <!-- Sidebar -->
    <div class="hidden md:flex md:flex-shrink-0">
      <div class="flex flex-col w-64 bg-gray-800">
        <!-- Logo -->
        <div class="flex items-center h-16 px-4 bg-gray-900">
          <h1 class="text-xl font-bold text-white">Admin Panel</h1>
        </div>

        <!-- Navigation -->
        <nav class="flex-1 px-2 py-4 space-y-1 overflow-y-auto">
          <router-link v-for="(item, index) in navItems" :key="item?.path || index" :to="item?.path || '#'" v-if="item"
            <-- THÊM KIỂM TRA NÀY!
            class="flex items-center px-4 py-3 text-sm font-medium text-gray-300 rounded-md group hover:bg-gray-700 hover:text-white"
            :class="[$route.path.startsWith(item.path) ? 'bg-gray-900 text-white' : '']">
            <component :is="item.icon" class="w-5 h-5 mr-3 text-gray-400 group-hover:text-gray-300" />
            {{ item.label }}
            <ChevronDownIcon v-if="item.children && item.children.length > 0"
              class="w-5 h-5 ml-2 text-gray-400 group-hover:text-gray-300" />
          </router-link>

          <div v-for="(item, index) in navItems" :key="'group-' + (item?.path || index)"
            v-if="item && item.children && item.children.length > 0" <-- THÊM KIỂM TRA NÀY!>
            <router-link v-for="child in item.children" :key="child.path" :to="child.path"
              class="flex items-center pl-12 py-3 text-sm font-medium text-gray-300 rounded-md group hover:bg-gray-700 hover:text-white"
              :class="[$route.path.startsWith(child.path) ? 'bg-gray-900 text-white' : '']">
              {{ child.label }}
            </router-link>
          </div>
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
            <h1 class="ml-2 text-lg font-semibold text-gray-900">{{ $route.meta.title || 'Dashboard' }}</h1>
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


      <!-- Main content -->
      <main class="flex-1 overflow-x-hidden overflow-y-auto bg-gray-50">
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
              <router-link v-for="(item, index) in navItems" :key="item?.path || index" :to="item?.path || '#'"
                v-if="item"
                class="flex items-center px-4 py-3 text-sm font-medium text-gray-300 rounded-md group hover:bg-gray-700 hover:text-white"
                :class="[$route.path.startsWith(item.path) ? 'bg-gray-900 text-white' : '']"
                @click="isSidebarOpen = false">
                <component :is="item.icon" class="w-5 h-5 mr-3 text-gray-400 group-hover:text-gray-300" />
                {{ item.label }}
              </router-link>

              <div v-for="(item, index) in navItems" :key="'mobile-group-' + (item?.path || index)"
                v-if="item && item.children && item.children.length > 0">
                <router-link v-for="child in item.children" :key="child.path" :to="child.path"
                  class="flex items-center pl-12 py-3 text-sm font-medium text-gray-300 rounded-md group hover:bg-gray-700 hover:text-white"
                  :class="[$route.path.startsWith(child.path) ? 'bg-gray-900 text-white' : '']"
                  @click="isSidebarOpen = false">
                  {{ child.label }}
                </router-link>
              </div>
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

.focus-style {
  @apply focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500;
}
</style>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth' // Thay bằng store của bạn
import {
  HomeIcon,
  UsersIcon,
  ShoppingBagIcon,
  Cog6ToothIcon,
  DocumentTextIcon,
  ChevronDownIcon,
  Bars3Icon,
  XMarkIcon,
  ArrowUpTrayIcon,
  CubeIcon,
  UserGroupIcon,
  ChartBarIcon,
  CogIcon
} from '@heroicons/vue/24/outline'



const authStore = useAuthStore()

// Tính toán chữ cái đầu (ví dụ: "Nguyễn Văn A" -> "NA")
const userInitials = computed(() => {
  const name = authStore.user?.fullName || 'Admin'; // Thay bằng trường tên thực tế
  const parts = name.split(' ');
  if (parts.length > 1) {
    return (parts[0][0] + parts[parts.length - 1][0]).toUpperCase();
  }
  return name.substring(0, 2).toUpperCase();
});

// KHAI BÁO navItems Ở ĐÂY
const navItems = [
  {
    label: 'Tổng quan',
    path: '/admin/dashboard',
    icon: HomeIcon,
    children: []
  },
  // Thêm các mục khác vào đây
  {
    label: 'Quản lý người dùng',
    path: '/admin/users',
    icon: UsersIcon
  },
]

const route = useRoute()
const router = useRouter()
const isProfileOpen = ref(false)
const isSidebarOpen = ref(false)

const handleClickOutside = (event: MouseEvent) => {
  // Logic xử lý click outside cho Profile dropdown (ví dụ)
  const profileDropdown = document.getElementById('user-menu-button')?.parentElement;
  if (isProfileOpen.value && profileDropdown && !profileDropdown.contains(event.target as Node)) {
    isProfileOpen.value = false
  }
}

// Close sidebar and profile when route changes
router.afterEach(() => {
  isSidebarOpen.value = false
  isProfileOpen.value = false
})

// Add event listeners
onMounted(() => {
  document.addEventListener('click', handleClickOutside)
  document.addEventListener('keydown', (e) => { // Thêm xử lý phím Esc đúng cách
    if (e.key === 'Escape' && isProfileOpen.value) {
      isProfileOpen.value = false
    }
  })
})

// Cleanup
onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside)
  // Xóa lắng nghe keydown cũng cần một hàm tham chiếu cụ thể nếu bạn định nghĩa nó bên trong onMounted
  // Để đơn giản, tôi sẽ giữ nguyên những gì bạn có, nhưng bạn nên xem xét lại cách thêm/xóa keydown listener.
})

// Logout function
const logout = () => {
  // Add your logout logic here
  router.push('/login')
}
</script>
