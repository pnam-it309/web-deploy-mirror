<template>
  <header
    class="h-16 bg-white border-2 border-[#1e293b] rounded-3xl flex items-center justify-between px-4 lg:px-8 z-40 mx-6 mt-4">
    <!-- Left: Branding or Toggle (Mobile) -->
    <div class="flex items-center gap-4">
      <button class="lg:hidden p-2 text-gray-500 hover:bg-gray-100 rounded-md">
        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor"
          class="w-6 h-6">
          <path stroke-linecap="round" stroke-linejoin="round" d="M3.75 6.75h16.5M3.75 12h16.5m-16.5 5.25h16.5" />
        </svg>
      </button>
      <!-- Optional: Breadcrumbs or Page Title could go here -->
      <div class="hidden md:block">
        <h1 class="text-xl font-bold text-gray-800">
          Welcome, Admin
        </h1>
      </div>
    </div>

    <!-- Right: Actions & Profile -->
    <div class="flex items-center gap-3 sm:gap-4">
      <button class="p-2 text-gray-400 hover:text-gray-600 hover:bg-gray-50 rounded-full transition-colors relative">
        <BellIcon class="w-6 h-6" />
        <span
          class="absolute top-2 right-2.5 block h-2 w-2 rounded-full bg-red-500 ring-2 ring-white transform translate-x-1/2 -translate-y-1/2"></span>
      </button>

      <div class="h-8 w-px bg-gray-200 hidden sm:block"></div>

      <div class="relative" ref="dropdownRef">
        <div class="flex items-center gap-3 pl-2 cursor-pointer" @click="toggleDropdown">
          <AdminAvatar
            src="https://images.unsplash.com/photo-1494790108377-be9c29b29330?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=facearea&facepad=2&w=256&h=256&q=80"
            alt="Admin User" size="md" status="online" />
          <div class="hidden sm:block text-left">
            <p class="text-sm font-semibold text-gray-900 leading-none select-none">Annisa Salma</p>
          </div>
          <ChevronDownIcon class="w-4 h-4 text-gray-400 hidden sm:block transition-transform duration-200"
            :class="{ 'rotate-180': isDropdownOpen }" />
        </div>

        <!-- Dropdown Menu -->
        <transition enter-active-class="transition ease-out duration-100"
          enter-from-class="transform opacity-0 scale-95" enter-to-class="transform opacity-100 scale-100"
          leave-active-class="transition ease-in duration-75" leave-from-class="transform opacity-100 scale-100"
          leave-to-class="transform opacity-0 scale-95">
          <div v-if="isDropdownOpen"
            class="absolute right-0 mt-2 w-64 bg-white border border-gray-100 rounded-xl shadow-lg py-2 z-50 origin-top-right">
            <div class="px-4 py-3 border-b border-gray-100 mb-1">
              <p class="text-sm font-semibold text-gray-900">Annisa Salma</p>
              <p class="text-xs text-gray-500">admin@fpt.edu.vn</p>
            </div>

            <router-link :to="{ name: ROUTES_CONSTANTS.CUSTOMER.children.HOME.name }"
              class="flex items-center gap-3 px-4 py-2.5 text-sm text-gray-600 hover:bg-gray-50 hover:text-indigo-600 transition-colors">
              <ArrowRightStartOnRectangleIcon class="w-5 h-5" />
              <span>Chuyển sang giao diện người dùng</span>
            </router-link>
          </div>
        </transition>
      </div>
    </div>
  </header>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue';
import AdminInput from '@/components/admin/AdminInput.vue';
import AdminAvatar from '@/components/admin/AdminAvatar.vue';
import { MagnifyingGlassIcon, BellIcon, ChevronDownIcon, ArrowRightStartOnRectangleIcon } from '@heroicons/vue/24/outline';
import { ROUTES_CONSTANTS } from '@/constants/path';

const isDropdownOpen = ref(false);
const dropdownRef = ref<HTMLElement | null>(null);

const toggleDropdown = () => {
  isDropdownOpen.value = !isDropdownOpen.value;
};

const closeDropdown = (event: MouseEvent) => {
  if (dropdownRef.value && !dropdownRef.value.contains(event.target as Node)) {
    isDropdownOpen.value = false;
  }
};

onMounted(() => {
  document.addEventListener('click', closeDropdown);
});

onUnmounted(() => {
  document.removeEventListener('click', closeDropdown);
});
</script>
