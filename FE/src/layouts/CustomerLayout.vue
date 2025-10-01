<template>
  <div class="flex h-screen bg-gray-50">
    <!-- Sidebar -->
    <div class="hidden md:flex md:flex-shrink-0">
      <div class="flex flex-col w-64 bg-indigo-700">
        <!-- Logo -->
        <div class="flex items-center h-16 px-4 bg-indigo-800">
          <h1 class="text-xl font-bold text-white">Customer Portal</h1>
        </div>
        
        <!-- Navigation -->
        <nav class="flex-1 px-2 py-4 space-y-1 overflow-y-auto">
          <router-link 
            v-for="item in navItems" 
            :key="item.path" 
            :to="item.path"
            class="flex items-center px-4 py-3 text-sm font-medium text-indigo-100 rounded-md group hover:bg-indigo-600 hover:text-white"
            :class="[$route.path.startsWith(item.path) ? 'bg-indigo-800 text-white' : '']"
          >
            <component :is="item.icon" class="w-5 h-5 mr-3 text-indigo-300 group-hover:text-indigo-100" />
            {{ item.label }}
          </router-link>
        </nav>
        
        <!-- User Profile -->
        <div class="p-4 mt-auto border-t border-indigo-800">
          <div class="flex items-center">
            <div class="w-10 h-10 rounded-full bg-indigo-600 flex items-center justify-center text-white font-medium">
              {{ userInitials }}
            </div>
            <div class="ml-3">
              <p class="text-sm font-medium text-white">{{ userName }}</p>
              <button 
                @click="handleLogout"
                class="text-xs text-indigo-200 hover:text-white"
              >
                Sign out
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Main Content -->
    <div class="flex flex-col flex-1 overflow-hidden">
      <!-- Top Navigation -->
      <header class="bg-white shadow-sm">
        <div class="flex items-center justify-between px-4 py-3 sm:px-6 lg:px-8">
          <div class="flex items-center">
            <button @click="isSidebarOpen = !isSidebarOpen" class="p-1 text-gray-500 rounded-md hover:text-gray-600 focus:outline-none focus:ring-2 focus:ring-indigo-500 md:hidden">
              <Bars3Icon class="w-6 h-6" />
            </button>
            <h1 class="ml-2 text-lg font-semibold text-gray-900">{{ $route.meta.title || 'Dashboard' }}</h1>
          </div>
          
          <div class="flex items-center space-x-4">
            <button class="p-1 text-gray-500 rounded-full hover:text-gray-600 focus:outline-none focus:ring-2 focus:ring-indigo-500">
              <span class="sr-only">View notifications</span>
              <BellIcon class="w-6 h-6" />
            </button>
            
            <!-- Mobile profile dropdown -->
            <div class="relative ml-3 md:hidden">
              <div>
                <button @click="isProfileOpen = !isProfileOpen" type="button" class="flex items-center max-w-xs text-sm rounded-full focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
                  <span class="sr-only">Open user menu</span>
                  <div class="w-8 h-8 rounded-full bg-indigo-100 flex items-center justify-center text-indigo-600 font-medium">
                    {{ userInitials }}
                  </div>
                </button>
              </div>
              
              <!-- Dropdown menu -->
              <transition 
                enter-active-class="transition duration-100 ease-out transform"
                enter-from-class="opacity-0 scale-95"
                enter-to-class="opacity-100 scale-100"
                leave-active-class="transition duration-75 ease-in"
                leave-from-class="opacity-100 scale-100"
                leave-to-class="opacity-0 scale-95"
              >
                <div v-show="isProfileOpen" class="absolute right-0 z-10 w-48 py-1 mt-2 origin-top-right bg-white rounded-md shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none">
                  <button 
                    @click="handleLogout"
                    class="block w-full px-4 py-2 text-sm text-left text-gray-700 hover:bg-gray-100"
                  >
                    Sign out
                  </button>
                </div>
              </transition>
            </div>
          </div>
        </div>
      </header>

      <!-- Page Content -->
      <main class="flex-1 overflow-y-auto bg-gray-50">
        <div class="px-4 py-6 mx-auto max-w-7xl sm:px-6 lg:px-8">
          <router-view />
        </div>
      </main>
    </div>
    
    <!-- Mobile sidebar -->
    <div v-show="isSidebarOpen" class="fixed inset-0 z-40 md:hidden" role="dialog" aria-modal="true">
      <div class="fixed inset-0 bg-gray-600 bg-opacity-75" @click="isSidebarOpen = false"></div>
      <div class="fixed inset-y-0 left-0 flex flex-col w-64 max-w-xs bg-indigo-700">
        <div class="flex items-center h-16 px-4 bg-indigo-800">
          <h1 class="text-xl font-bold text-white">Customer Portal</h1>
        </div>
        <div class="flex-1 px-2 py-4 space-y-1 overflow-y-auto">
          <router-link 
            v-for="item in navItems" 
            :key="item.path" 
            :to="item.path"
            class="flex items-center px-4 py-3 text-sm font-medium text-indigo-100 rounded-md group hover:bg-indigo-600 hover:text-white"
            :class="[$route.path.startsWith(item.path) ? 'bg-indigo-800 text-white' : '']"
            @click="isSidebarOpen = false"
          >
            <component :is="item.icon" class="w-5 h-5 mr-3 text-indigo-300 group-hover:text-indigo-100" />
            {{ item.label }}
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/auth';
import { Bars3Icon, BellIcon } from '@heroicons/vue/24/outline';

// Icons for navigation items
import {
  HomeIcon,
  ShoppingCartIcon,
  DocumentTextIcon,
  UserCircleIcon,
  Cog6ToothIcon
} from '@heroicons/vue/24/outline';

const route = useRoute();
const router = useRouter();
const authStore = useAuthStore();

const isSidebarOpen = ref(false);
const isProfileOpen = ref(false);

// Navigation items for customer
const navItems = [
  { path: '/customer/dashboard', label: 'Dashboard', icon: HomeIcon },
  { path: '/customer/orders', label: 'My Orders', icon: ShoppingCartIcon },
  { path: '/customer/documents', label: 'Documents', icon: DocumentTextIcon },
  { path: '/customer/account', label: 'My Account', icon: UserCircleIcon },
  { path: '/customer/settings', label: 'Settings', icon: Cog6ToothIcon },
];

// Get user initials for avatar
const userInitials = computed(() => {
  const user = authStore.user;
  if (!user) return 'U';
  return user.fullName
    ? user.fullName.split(' ').map(n => n[0]).join('').toUpperCase().substring(0, 2)
    : (user.email ? user.email[0].toUpperCase() : 'U');
});

// Get user name for display
const userName = computed(() => {
  return authStore.user?.fullName || authStore.user?.email?.split('@')[0] || 'User';
});

// Handle logout
const handleLogout = async () => {
  try {
    await authStore.logout();
    router.push('/login');
  } catch (error) {
    console.error('Logout failed:', error);
  } finally {
    isProfileOpen.value = false;
  }
};

// Close dropdown when clicking outside
const handleClickOutside = (event: MouseEvent) => {
  const target = event.target as HTMLElement;
  if (!target.closest('.relative')) {
    isProfileOpen.value = false;
  }
};

// Close sidebar on route change
const unwatch = router.afterEach(() => {
  isSidebarOpen.value = false;
});

// Add event listeners
onMounted(() => {
  document.addEventListener('click', handleClickOutside);
});

// Clean up
onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside);
  unwatch();
});
</script>

<style scoped>
/* Smooth transitions */
.transition-all {
  transition-property: all;
  transition-timing-function: cubic-bezier(0.4, 0, 0.2, 1);
  transition-duration: 150ms;
}

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
