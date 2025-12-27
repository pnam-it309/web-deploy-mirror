<template>
    <header class="h-16 bg-white border-b border-gray-100 sticky top-0 z-50">
        <div class="container mx-auto px-4 h-full flex items-center justify-between">
            <!-- Logo -->
            <router-link :to="{ name: ROUTES_CONSTANTS.CUSTOMER.children.HOME.name }" class="flex items-center gap-2">
                <div class="w-8 h-8 rounded bg-[#1e293b] flex items-center justify-center text-white font-bold">
                    F
                </div>
                <span class="font-bold text-xl text-gray-800">FPL Catalog</span>
            </router-link>

            <!-- Navigation -->
            <nav class="hidden md:flex items-center gap-8">
                <router-link :to="{ name: ROUTES_CONSTANTS.CUSTOMER.children.HOME.name }"
                    class="text-gray-600 hover:text-[#1e293b] font-medium transition-colors">
                    Trang chủ
                </router-link>
                <router-link :to="{ name: ROUTES_CONSTANTS.CUSTOMER.children.DOMAINS.name }"
                    class="text-gray-600 hover:text-[#1e293b] font-medium transition-colors">
                    Lĩnh vực
                </router-link>
                <router-link :to="{ name: ROUTES_CONSTANTS.CUSTOMER.children.APPS.name }"
                    class="text-gray-600 hover:text-[#1e293b] font-medium transition-colors">
                    Sản phẩm
                </router-link>
                <router-link :to="{ name: ROUTES_CONSTANTS.CUSTOMER.children.TECHNOLOGIES.name }"
                    class="text-gray-600 hover:text-[#1e293b] font-medium transition-colors">
                    Công nghệ
                </router-link>
            </nav>

            <!-- Actions (Search/Auth) -->
            <div class="flex items-center gap-4">
                <!-- Search Bar -->
                <div class="relative hidden md:block">
                    <input type="text" v-model="searchQuery" @keyup.enter="handleSearch" placeholder="Tìm kiếm..."
                        class="bg-gray-100 hover:bg-white focus:bg-white border-transparent focus:border-blue-500 rounded-lg px-4 py-2 pl-10 text-sm transition-all focus:ring-4 focus:ring-blue-100 w-64 border">
                    <svg xmlns="http://www.w3.org/2000/svg"
                        class="h-4 w-4 absolute left-3 top-1/2 -translate-y-1/2 text-gray-500" fill="none"
                        viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                            d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
                    </svg>
                </div>

                <button class="md:hidden p-2 text-gray-500 hover:text-[#1e293b] transition-colors">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24"
                        stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                            d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
                    </svg>
                </button>
                <!-- Profile Dropdown (Mocked for now) -->
                <div class="relative" ref="dropdownRef">
                    <button @click="toggleDropdown"
                        class="flex items-center gap-2 cursor-pointer focus:outline-none pl-2 border-l border-gray-200">
                        <img src="https://images.unsplash.com/photo-1494790108377-be9c29b29330?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=facearea&facepad=2&w=256&h=256&q=80"
                            alt="User" class="w-8 h-8 rounded-full border border-gray-200" />
                        <span class="hidden sm:block text-sm font-medium text-gray-700">Annisa Salma</span>
                        <svg xmlns="http://www.w3.org/2000/svg"
                            class="h-4 w-4 text-gray-400 transition-transform duration-200"
                            :class="{ 'rotate-180': isDropdownOpen }" fill="none" viewBox="0 0 24 24"
                            stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" />
                        </svg>
                    </button>

                    <!-- Dropdown Menu -->
                    <transition enter-active-class="transition ease-out duration-100"
                        enter-from-class="transform opacity-0 scale-95" enter-to-class="transform opacity-100 scale-100"
                        leave-active-class="transition ease-in duration-75"
                        leave-from-class="transform opacity-100 scale-100"
                        leave-to-class="transform opacity-0 scale-95">
                        <div v-if="isDropdownOpen"
                            class="absolute right-0 mt-2 w-56 bg-white border border-gray-100 rounded-xl shadow-lg py-1 z-50 origin-top-right">
                            <router-link :to="{ name: ROUTES_CONSTANTS.ADMIN.children.DASHBOARD.name }"
                                class="flex items-center gap-3 px-4 py-2.5 text-sm text-gray-600 hover:bg-gray-50 hover:text-indigo-600 transition-colors">
                                <ArrowRightStartOnRectangleIcon class="w-5 h-5" />
                                <span>Đăng nhập Admin</span>
                            </router-link>
                        </div>
                    </transition>
                </div>
            </div>
        </div>
    </header>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue';
import { useRouter } from 'vue-router';
import { ROUTES_CONSTANTS } from '@/constants/path';
import { ArrowRightStartOnRectangleIcon } from '@heroicons/vue/24/outline';

const router = useRouter();
const searchQuery = ref('');
const isDropdownOpen = ref(false);
const dropdownRef = ref<HTMLElement | null>(null);

const handleSearch = () => {
    if (searchQuery.value.trim()) {
        router.push({
            name: ROUTES_CONSTANTS.CUSTOMER.children.APPS.name,
            query: { q: searchQuery.value }
        });
    }
};

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
