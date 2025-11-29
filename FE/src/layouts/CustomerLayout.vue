<template>
  <div class="flex bg-gray-50 min-h-screen">
    <!-- Sidebar -->
    <div class="w-64 bg-indigo-700 flex flex-col sticky top-0 h-screen flex-shrink-0">
      <!-- Logo -->
      <div class="flex items-center h-16 px-4 bg-indigo-800 flex-shrink-0">
        <h1 class="text-xl font-bold text-white">Customer Portal</h1>
      </div>

      <!-- Navigation -->
      <nav class="flex-1 px-2 py-4 space-y-1 overflow-y-auto">
        <router-link v-for="item in navItems" :key="item.path" :to="item.path"
          class="flex items-center px-4 py-3 text-sm font-medium text-indigo-100 rounded-md group hover:bg-indigo-600 hover:text-white"
          :class="[$route.path.startsWith(item.path) ? 'bg-indigo-800 text-white' : '']">
          <component :is="item.icon" class="w-5 h-5 mr-3 text-indigo-300 group-hover:text-indigo-100 flex-shrink-0" />
          {{ item.label }}
        </router-link>
      </nav>

      <!-- User Profile -->
      <div class="p-4 border-t border-indigo-800 flex-shrink-0">
        <div class="flex items-center">
          <div
            class="w-10 h-10 rounded-full bg-indigo-600 flex items-center justify-center text-white font-medium flex-shrink-0">
            {{ userInitials }}
          </div>
          <div class="ml-3 min-w-0">
            <p class="text-sm font-medium text-white truncate">{{ userName }}</p>
            <button @click="handleLogout" class="text-xs text-indigo-200 hover:text-white">
              Sign out
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Main Content -->
    <div class="flex flex-col flex-1 min-h-screen">
      <!-- Top Navigation -->
      <header class="bg-white shadow-sm flex-shrink-0">
        <div class="flex items-center justify-between px-4 py-3 sm:px-6 lg:px-8">
          <div class="flex items-center">
            <h1 class="ml-2 text-lg font-semibold text-gray-900">{{ $route.meta.title || 'Dashboard' }}</h1>
          </div>
          <div class="flex items-center space-x-4">
            <router-link to="/customer/wishlist" class="p-1 text-gray-400 hover:text-gray-500 relative">
              <span class="sr-only">Wishlist</span>
              <HeartIcon class="w-6 h-6" />
              <span v-if="wishlistCount > 0"
                class="absolute -top-1 -right-1 bg-red-500 text-white text-xs rounded-full h-4 w-4 flex items-center justify-center">
                {{ wishlistCount }}
              </span>
            </router-link>
            <router-link to="/customer/orders" class="p-1 text-gray-400 hover:text-gray-500 relative">
              <span class="sr-only">Cart</span>
              <ShoppingCartIcon class="w-6 h-6" />
            </router-link>
            <button @click="toast.info('No new notifications')"
              class="p-1 text-gray-400 hover:text-gray-500 focus:outline-none">
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
import { onMounted, onUnmounted, watch, computed, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/auth';
import { toast } from "vue3-toastify";
import "vue3-toastify/dist/index.css";
import { BellIcon, HomeIcon, ShoppingCartIcon, DocumentTextIcon, UserCircleIcon, Cog6ToothIcon, MagnifyingGlassIcon, HeartIcon } from '@heroicons/vue/24/outline';

const route = useRoute();
const router = useRouter();
const authStore = useAuthStore();

const wishlistCount = ref(0);

const updateWishlistCount = () => {
  const wishlist = JSON.parse(localStorage.getItem('wishlist') || '[]');
  wishlistCount.value = wishlist.length;
};

const handleSearch = (event: any) => {
  const query = event.target.value;
  router.push({ path: '/customer/products', query: { search: query } });
};

// Navigation items
const navItems = [
  { path: '/customer/dashboard', label: 'Dashboard', icon: HomeIcon },
  { path: '/customer/orders', label: 'My Orders', icon: ShoppingCartIcon },
  { path: '/customer/products', label: 'Sản phẩm', icon: DocumentTextIcon },
  { path: '/customer/wishlist', label: 'Sản phẩm yêu thích', icon: UserCircleIcon },
  { path: '/customer/settings', label: 'Settings', icon: Cog6ToothIcon },
];

// User initials
const userInitials = computed(() => {
  const user = authStore.user;
  if (!user) return 'U';
  return user.fullName
    ? user.fullName.split(' ').map(n => n[0]).join('').toUpperCase().substring(0, 2)
    : (user.email ? user.email[0].toUpperCase() : 'U');
});

// User name
const userName = computed(() => {
  return authStore.user?.fullName || authStore.user?.email?.split('@')[0] || 'User';
});

// Logout
const handleLogout = async () => {
  try {
    await authStore.logout();
    toast.success("Đăng xuất thành công!");
    router.push('/login');
  } catch (error) {
    toast.error("Đăng xuất thất bại!");
    console.error('Logout failed:', error);
  }
};

// Khi vừa load trang
onMounted(() => {
  toast.success("Chào mừng bạn đến với catalgo web");
  updateWishlistCount();
  window.addEventListener('wishlist-updated', updateWishlistCount);

  // Also update when storage changes (e.g. from another tab)
  window.addEventListener('storage', (e) => {
    if (e.key === 'wishlist') updateWishlistCount();
  });
});

onUnmounted(() => {
  window.removeEventListener('wishlist-updated', updateWishlistCount);
});

// Khi thay đổi route
// watch(() => route.fullPath, (newPath) => {
//   toast.info(`Đã chuyển đến ${route.meta.title || 'trang mới'}`);
// });
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
