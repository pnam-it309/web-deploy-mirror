<template>
  <header
    class="h-16 bg-white dark:bg-gray-800 border-b border-gray-100 dark:border-gray-700 flex items-center justify-between px-6 lg:px-10 z-30 sticky top-0 transition-colors duration-300">
    <!-- Left: Branding or Toggle (Mobile) -->
    <div class="flex items-center gap-4">
      <button @click="sidebarStore.toggleMobileSidebar" class="lg:hidden p-2 text-gray-500 hover:bg-gray-100 dark:hover:bg-gray-700 rounded-lg transition-colors">
        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor"
          class="w-6 h-6 text-gray-700 dark:text-gray-200">
          <path stroke-linecap="round" stroke-linejoin="round" d="M3.75 6.75h16.5M3.75 12h16.5m-16.5 5.25h16.5" />
        </svg>
      </button>
      <!-- Breadcrumbs or Page Title could go here -->
      <div class="hidden md:block">
        <h1 class="text-xl font-bold text-gray-800 dark:text-white font-serif tracking-tight">
          Hello, <span class="text-blue-600 dark:text-blue-400">{{ userInfo.name }}</span> 👋
        </h1>
        <p class="text-xs text-gray-500 dark:text-gray-400 font-medium mt-0.5">Welcome back to the dashboard</p>
      </div>
    </div>

    <!-- Right: Actions & Profile -->
    <div class="flex items-center gap-3 sm:gap-6">
      <button @click="themeStore.toggleTheme" class="p-2.5 text-gray-400 hover:text-yellow-500 dark:text-gray-400 dark:hover:text-yellow-400 hover:bg-yellow-50 dark:hover:bg-yellow-900/20 rounded-xl transition-all">
          <SunIcon v-if="themeStore.theme === 'dark'" class="w-6 h-6" />
          <MoonIcon v-else class="w-6 h-6" />
      </button>

      <div class="relative" ref="notificationRef">
        <button @click="toggleNotification" class="p-2.5 text-gray-400 hover:text-blue-600 dark:text-gray-400 dark:hover:text-blue-400 hover:bg-blue-50 dark:hover:bg-blue-900/20 rounded-xl transition-all relative group">
          <BellIcon class="w-6 h-6 transition-transform group-hover:rotate-12" />
          <span
            v-if="notificationStore.unreadCount > 0"
            class="absolute top-2.5 right-3 block h-2.5 w-2.5 rounded-full bg-red-500 ring-2 ring-white dark:ring-gray-800 transform translate-x-1/2 -translate-y-1/2 shadow-sm animate-pulse"></span>
        </button>

        <!-- Notification Dropdown -->
        <transition enter-active-class="transition ease-out duration-100"
          enter-from-class="transform opacity-0 scale-95" enter-to-class="transform opacity-100 scale-100"
          leave-active-class="transition ease-in duration-75" leave-from-class="transform opacity-100 scale-100"
          leave-to-class="transform opacity-0 scale-95">
          <div v-if="isNotificationOpen"
            class="absolute right-0 mt-4 w-80 sm:w-96 bg-white dark:bg-gray-800 border border-gray-100 dark:border-gray-700 rounded-2xl shadow-xl shadow-gray-200/50 dark:shadow-black/50 overflow-hidden z-50 origin-top-right ring-1 ring-black/5">
            
            <div class="p-4 border-b border-gray-100 dark:border-gray-700 flex justify-between items-center bg-gray-50/50 dark:bg-gray-900/50 backdrop-blur-sm">
              <h3 class="font-bold text-gray-900 dark:text-white">Thông báo</h3>
              <button 
                v-if="notificationStore.unreadCount > 0"
                @click.stop="notificationStore.markAllAsRead" 
                class="text-xs font-semibold text-blue-600 hover:text-blue-700 dark:text-blue-400 hover:underline"
              >
                Đánh dấu tất cả đã đọc
              </button>
            </div>

            <div class="max-h-[400px] overflow-y-auto custom-scrollbar">
              <div v-if="notificationStore.notifications.length === 0" class="p-8 text-center text-gray-500 dark:text-gray-400">
                <BellIcon class="w-10 h-10 mx-auto mb-2 opacity-50" />
                <p class="text-sm">Không có thông báo nào</p>
              </div>

              <div 
                v-for="item in notificationStore.notifications" 
                :key="item.id"
                class="group relative p-4 hover:bg-gray-50 dark:hover:bg-gray-700/50 transition-colors border-b border-gray-50 dark:border-gray-700/50 last:border-0 cursor-pointer"
                :class="{ 'bg-blue-50/30 dark:bg-blue-900/10': !item.read }"
                @click="notificationStore.markAsRead(item.id)"
              >
                <div class="flex gap-3">
                  <div class="shrink-0 mt-0.5">
                    <span 
                      class="w-8 h-8 flex items-center justify-center rounded-full shadow-sm"
                      :class="{
                        'bg-green-100 text-green-600 dark:bg-green-900/30 dark:text-green-400': item.type === 'success',
                        'bg-red-100 text-red-600 dark:bg-red-900/30 dark:text-red-400': item.type === 'error',
                        'bg-yellow-100 text-yellow-600 dark:bg-yellow-900/30 dark:text-yellow-400': item.type === 'warning',
                        'bg-blue-100 text-blue-600 dark:bg-blue-900/30 dark:text-blue-400': item.type === 'info',
                      }"
                    >
                       <CheckCircleIcon v-if="item.type === 'success'" class="w-5 h-5" />
                       <XCircleIcon v-if="item.type === 'error'" class="w-5 h-5" />
                       <ExclamationCircleIcon v-if="item.type === 'warning'" class="w-5 h-5" />
                       <InformationCircleIcon v-if="item.type === 'info'" class="w-5 h-5" />
                    </span>
                  </div>
                  <div class="flex-1 min-w-0">
                    <p class="text-sm text-gray-900 dark:text-gray-100 mb-1 leading-snug break-words" :class="{ 'font-semibold': !item.read }">
                      {{ item.message }}
                    </p>
                    <p class="text-xs text-gray-400 dark:text-gray-500 font-medium">
                      {{ new Date(item.timestamp).toLocaleString('vi-VN') }}
                    </p>
                  </div>
                  <div v-if="!item.read" class="shrink-0 self-center">
                    <span class="w-2 h-2 bg-blue-600 rounded-full block ring-2 ring-white dark:ring-gray-800"></span>
                  </div>
                </div>
              </div>
            </div>
            
             <div class="p-2 bg-gray-50 dark:bg-gray-900/50 border-t border-gray-100 dark:border-gray-700 text-center flex justify-center">
               <button @click.stop="notificationStore.clearAll" class="text-xs text-gray-500 hover:text-red-500 transition-colors w-full py-1 flex items-center justify-center gap-1 group">
                 <TrashIcon class="w-3 h-3 group-hover:scale-110 transition-transform"/>
                 Xóa tất cả lịch sử
               </button>
             </div>
          </div>
        </transition>
      </div>

      <div class="h-8 w-px bg-gray-200 dark:bg-gray-700 hidden sm:block"></div>

      <div class="relative" ref="dropdownRef">
        <div class="flex items-center gap-3 pl-2 cursor-pointer group" @click="toggleDropdown">
          <div class="relative">
             <AdminAvatar :src="userInfo.avatar || 'https://ui-avatars.com/api/?name=' + userInfo.name"
            :alt="userInfo.name" size="md" status="online" class="ring-2 ring-transparent group-hover:ring-blue-100 dark:group-hover:ring-blue-900 transition-all rounded-full" />
          </div>
         
          <div class="hidden sm:block text-left">
            <p class="text-sm font-bold text-gray-900 dark:text-white leading-none select-none group-hover:text-blue-600 dark:group-hover:text-blue-400 transition-colors">{{ userInfo.name }}</p>
            <p class="text-xs text-gray-500 dark:text-gray-400 font-medium leading-none mt-1 group-hover:text-gray-700 dark:group-hover:text-gray-300">Administrator</p>
          </div>
          <ChevronDownIcon class="w-4 h-4 text-gray-400 group-hover:text-gray-600 dark:group-hover:text-gray-300 hidden sm:block transition-transform duration-200"
            :class="{ 'rotate-180': isDropdownOpen }" />
        </div>

        <!-- Dropdown Menu -->
        <transition enter-active-class="transition ease-out duration-100"
          enter-from-class="transform opacity-0 scale-95" enter-to-class="transform opacity-100 scale-100"
          leave-active-class="transition ease-in duration-75" leave-from-class="transform opacity-100 scale-100"
          leave-to-class="transform opacity-0 scale-95">
          <div v-if="isDropdownOpen"
            class="absolute right-0 mt-4 w-72 bg-white dark:bg-gray-800 border border-gray-100 dark:border-gray-700 rounded-2xl shadow-xl shadow-gray-200/50 dark:shadow-black/50 py-2 z-50 origin-top-right overflow-hidden">
            
            <div class="px-6 py-5 border-b border-gray-100 dark:border-gray-700 bg-gray-50/50 dark:bg-gray-800">
               <div class="flex items-center gap-3">
                  <AdminAvatar :src="userInfo.avatar || 'https://ui-avatars.com/api/?name=' + userInfo.name"
                  :alt="userInfo.name" size="md" />
                  <div>
                    <p class="text-sm font-bold text-gray-900 dark:text-white">{{ userInfo.name }}</p>
                    <p class="text-xs text-gray-500 dark:text-gray-400 font-medium">{{ userInfo.email }}</p>
                  </div>
               </div>
            </div>

            <div class="p-2">
                <router-link :to="{ name: ROUTES_CONSTANTS.CUSTOMER.children.HOME.name }"
                class="flex items-center gap-3 px-4 py-3 text-sm font-bold text-gray-600 dark:text-gray-300 hover:bg-gray-50 dark:hover:bg-gray-700 hover:text-blue-600 dark:hover:text-blue-400 rounded-xl transition-all group">
                <div class="p-2 bg-gray-100 dark:bg-gray-700 group-hover:bg-blue-100 dark:group-hover:bg-blue-900/30 text-gray-500 dark:text-gray-400 group-hover:text-blue-600 dark:group-hover:text-blue-400 rounded-lg transition-colors">
                     <ArrowRightStartOnRectangleIcon class="w-5 h-5" />
                </div>
                <span>Chuyển sang giao diện người dùng</span>
                </router-link>
            </div>
          </div>
        </transition>
      </div>
    </div>
  </header>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue';
import { useSidebarStore } from '@/stores/sidebar.store';
import { useThemeStore } from '@/stores/theme.store';
import { useNotificationStore } from '@/stores/notification.store';
import AdminAvatar from '@/components/admin/AdminAvatar.vue';
import { BellIcon, ChevronDownIcon, ArrowRightStartOnRectangleIcon, SunIcon, MoonIcon, CheckCircleIcon, XCircleIcon, ExclamationCircleIcon, InformationCircleIcon, TrashIcon } from '@heroicons/vue/24/outline';
import { ROUTES_CONSTANTS } from '@/constants/path';
import { authService } from '@/services/api/auth.service';

const sidebarStore = useSidebarStore();
const themeStore = useThemeStore();
const notificationStore = useNotificationStore();

const isDropdownOpen = ref(false);
const dropdownRef = ref<HTMLElement | null>(null);

const isNotificationOpen = ref(false);
const notificationRef = ref<HTMLElement | null>(null);

const userInfo = ref({
  name: 'Admin',
  email: '',
  avatar: ''
});

const toggleDropdown = () => {
  isDropdownOpen.value = !isDropdownOpen.value;
  if (isDropdownOpen.value) {
     isNotificationOpen.value = false;
  }
};

const toggleNotification = () => {
  isNotificationOpen.value = !isNotificationOpen.value;
  if (isNotificationOpen.value) {
     isDropdownOpen.value = false;
  }
};

const closeDropdowns = (event: MouseEvent) => {
  const target = event.target as Node;
  
  if (dropdownRef.value && !dropdownRef.value.contains(target)) {
    isDropdownOpen.value = false;
  }
  
  if (notificationRef.value && !notificationRef.value.contains(target)) {
    isNotificationOpen.value = false;
  }
};

onMounted(async () => {
  document.addEventListener('click', closeDropdowns);

  try {
    const res = await authService.getCurrentUser();
    if (res.data) {
      userInfo.value = {
        name: res.data.name || 'Admin',
        email: res.data.email || '',
        avatar: res.data.avatar || ''
      };
    }
  } catch (error) {
    console.error('Failed to fetch admin info', error);
  }
});

onUnmounted(() => {
  document.removeEventListener('click', closeDropdowns);
});
</script>
