<template>
  <div class="flex bg-gray-50 min-h-screen">
    <!-- Sidebar - Sticky sidebar -->
    <div class="w-64 bg-indigo-700 flex flex-col sticky top-0 h-screen flex-shrink-0">
      <!-- Logo -->
      <div class="flex items-center h-16 px-4 bg-indigo-800 flex-shrink-0">
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
          <component :is="item.icon" class="w-5 h-5 mr-3 text-indigo-300 group-hover:text-indigo-100 flex-shrink-0" />
          {{ item.label }}
        </router-link>
      </nav>

      <!-- User Profile -->
      <div class="p-4 border-t border-indigo-800 flex-shrink-0">
        <div class="flex items-center">
          <div class="w-10 h-10 rounded-full bg-indigo-600 flex items-center justify-center text-white font-medium flex-shrink-0">
            {{ userInitials }}
          </div>
          <div class="ml-3 min-w-0">
            <p class="text-sm font-medium text-white truncate">{{ userName }}</p>
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

    <!-- Main Content - Flexible -->
    <div class="flex flex-col flex-1 min-h-screen">
      <!-- Top Navigation -->
      <header class="bg-white shadow-sm flex-shrink-0">
        <div class="flex items-center justify-between px-4 py-3 sm:px-6 lg:px-8">
          <div class="flex items-center">
            <h1 class="ml-2 text-lg font-semibold text-gray-900">{{ $route.meta.title || 'Dashboard' }}</h1>
          </div>

          <div class="flex items-center space-x-4">
            <button class="p-1 text-gray-500 rounded-full hover:text-gray-600 focus:outline-none focus:ring-2 focus:ring-indigo-500">
              <span class="sr-only">View notifications</span>
              <BellIcon class="w-6 h-6" />
            </button>
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
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/auth';
import { BellIcon } from '@heroicons/vue/24/outline';

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
  }
};
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
