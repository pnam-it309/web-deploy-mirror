<template>
    <header
        class="h-16 bg-white/80 dark:bg-gray-900/80 backdrop-blur-xl border-b border-white/20 dark:border-gray-700/20 sticky top-0 z-50 transition-all duration-300 shadow-lg shadow-black/5 dark:shadow-black/20">
        <div class="container mx-auto px-4 h-full flex items-center justify-between">
            <!-- Logo -->
            <router-link :to="{ name: ROUTES_CONSTANTS.CUSTOMER.children.HOME.name }" class="flex items-center gap-2">
                <div
                    class="w-8 h-8 rounded bg-[#1e293b] dark:bg-blue-600 flex items-center justify-center text-white font-bold">
                    F
                </div>
                <!-- <img src="@/assets/images/logo.png" alt="Logo" class="w-8 h-8" /> -->
                <span class="font-bold text-xl text-gray-800 dark:text-white">FPL Catalog</span>
            </router-link>

            <!-- Navigation -->
            <nav class="hidden md:flex items-center gap-8">
                <router-link :to="{ name: ROUTES_CONSTANTS.CUSTOMER.children.HOME.name }"
                    class="text-gray-600 dark:text-gray-300 hover:text-[#1e293b] dark:hover:text-white font-medium transition-colors">
                    Trang chủ
                </router-link>
                <router-link :to="{ name: ROUTES_CONSTANTS.CUSTOMER.children.APPS.name }"
                    class="text-gray-600 dark:text-gray-300 hover:text-[#1e293b] dark:hover:text-white font-medium transition-colors">
                    Sản phẩm
                </router-link>

            </nav>

            <!-- Mobile Menu Button -->
            <button @click="toggleMobileMenu"
                class="md:hidden p-2 text-gray-600 dark:text-gray-300 hover:text-gray-900 dark:hover:text-white">
                <svg v-if="!isMobileMenuOpen" class="w-6 h-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16" />
                </svg>
                <svg v-else class="w-6 h-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
                </svg>
            </button>

            <!-- Actions (Search/Auth) -->
            <div class="flex items-center gap-4">
                <!-- Bookmarks Link -->
                <router-link :to="{ name: ROUTES_CONSTANTS.CUSTOMER.children.BOOKMARKS.name }"
                    class="relative group p-2 text-gray-600 dark:text-gray-300 hover:text-red-500 dark:hover:text-red-400 transition-colors">
                    <svg class="w-6 h-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                            d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" />
                    </svg>
                    <span v-if="bookmarkCount > 0"
                        class="absolute -top-1 -right-1 w-5 h-5 flex items-center justify-center text-xs font-bold text-white bg-red-500 rounded-full">
                        {{ bookmarkCount > 9 ? '9+' : bookmarkCount }}
                    </span>

                    <!-- Tooltip -->
                    <div
                        class="absolute right-0 top-full mt-2 px-3 py-1.5 bg-gray-900/95 text-white text-xs font-medium rounded-lg whitespace-nowrap opacity-0 group-hover:opacity-100 transition-all duration-200 pointer-events-none z-50 backdrop-blur-sm shadow-xl transform translate-y-1 group-hover:translate-y-0">
                        Sản phẩm yêu thích
                        <div class="absolute bottom-full right-3 border-[6px] border-transparent border-b-gray-900/95">
                        </div>
                    </div>
                </router-link>

                <!-- Theme Toggle -->
                <ThemeToggle />

                <!-- Profile Dropdown (If Logged In) -->
                <div v-if="isLoggedIn" class="relative" ref="dropdownRef">
                    <button @click="toggleDropdown"
                        class="flex items-center gap-2 cursor-pointer focus:outline-none pl-2 border-l border-gray-200 dark:border-gray-700">
                        <img :src="userInfo.avatar || 'https://ui-avatars.com/api/?name=' + userInfo.name" alt="User"
                            class="w-8 h-8 rounded-full border border-gray-200 dark:border-gray-600" />
                        <span class="hidden sm:block text-sm font-medium text-gray-700 dark:text-gray-200">{{
                            userInfo.name }}</span>
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
                            class="absolute right-0 mt-2 w-56 bg-white/90 dark:bg-gray-800/90 backdrop-blur-xl border border-white/20 dark:border-gray-700/20 rounded-xl shadow-2xl shadow-black/10 dark:shadow-black/30 py-1 z-50 origin-top-right">
                            <div class="px-4 py-3 border-b border-gray-100 dark:border-gray-700">
                                <p class="text-xs text-gray-500 dark:text-gray-400">Đăng nhập là</p>
                                <p class="text-sm font-medium text-gray-900 dark:text-white truncate">{{ userInfo.email
                                    }}</p>
                            </div>

                            <router-link v-if="isAdmin" :to="{ name: ROUTES_CONSTANTS.ADMIN.children.DASHBOARD.name }"
                                class="flex items-center gap-3 px-4 py-2.5 text-sm text-gray-700 dark:text-gray-300 hover:bg-gray-50 dark:hover:bg-gray-700/50 hover:text-[#1e293b] dark:hover:text-white transition-colors cursor-pointer border-b border-gray-100 dark:border-gray-700">
                                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                                    stroke-width="1.5" stroke="currentColor" class="w-5 h-5">
                                    <path stroke-linecap="round" stroke-linejoin="round"
                                        d="M10.5 6h9.75M10.5 6a1.5 1.5 0 11-3 0m3 0a1.5 1.5 0 10-3 0M3.75 6H7.5m3 12h9.75m-9.75 0a1.5 1.5 0 01-3 0m3 0a1.5 1.5 0 00-3 0m-3.75 0H7.5m9-6h3.75m-3.75 0a1.5 1.5 0 01-3 0m3 0a1.5 1.5 0 00-3 0m-9.75 0h9.75" />
                                </svg>
                                <span>Quản trị hệ thống</span>
                            </router-link>

                            <div @click="handleLogout"
                                class="flex items-center gap-3 px-4 py-2.5 text-sm text-red-600 hover:bg-red-50 dark:hover:bg-red-900/10 hover:text-red-700 transition-colors cursor-pointer">
                                <ArrowRightStartOnRectangleIcon class="w-5 h-5" />
                                <span>Đăng xuất</span>
                            </div>
                        </div>
                    </transition>
                </div>

                <!-- Apps/Login Button (If Not Logged In) -->
                <div v-if="!isLoggedIn" class="pl-2 border-l border-gray-200 dark:border-gray-700">
                    <button @click="openLoginModal"
                        class="px-4 py-2 rounded-lg bg-gray-900 dark:bg-white text-white dark:text-gray-900 text-sm font-medium hover:bg-gray-800 dark:hover:bg-gray-100 transition-colors">
                        Đăng ký / Đăng nhập
                    </button>
                </div>
            </div>
        </div>

        <!-- Mobile Menu Overlay -->
        <transition enter-active-class="transition ease-out duration-200" enter-from-class="opacity-0"
            enter-to-class="opacity-100" leave-active-class="transition ease-in duration-150"
            leave-from-class="opacity-100" leave-to-class="opacity-0">
            <div v-if="isMobileMenuOpen" @click="closeMobileMenu"
                class="fixed inset-0 bg-black bg-opacity-50 z-40 md:hidden"></div>
        </transition>

        <!-- Mobile Menu Drawer -->
        <transition enter-active-class="transition ease-out duration-300" enter-from-class="translate-x-full"
            enter-to-class="translate-x-0" leave-active-class="transition ease-in duration-200"
            leave-from-class="translate-x-0" leave-to-class="translate-x-full">
            <div v-if="isMobileMenuOpen"
                class="fixed top-0 right-0 h-full w-64 bg-white/95 dark:bg-gray-900/95 backdrop-blur-xl shadow-2xl shadow-black/20 dark:shadow-black/40 z-50 md:hidden overflow-y-auto">
                <div class="p-4">
                    <div class="flex justify-between items-center mb-6">
                        <span class="font-bold text-lg text-gray-800 dark:text-white">Menu</span>
                        <button @click="closeMobileMenu" class="p-2 text-gray-600 dark:text-gray-300">
                            <svg class="w-6 h-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M6 18L18 6M6 6l12 12" />
                            </svg>
                        </button>
                    </div>

                    <nav class="flex flex-col gap-4">
                        <router-link @click="closeMobileMenu"
                            :to="{ name: ROUTES_CONSTANTS.CUSTOMER.children.HOME.name }"
                            class="text-gray-700 dark:text-gray-300 hover:text-[#1e293b] dark:hover:text-white font-medium py-2 transition-colors">
                            Trang chủ
                        </router-link>
                        <router-link @click="closeMobileMenu"
                            :to="{ name: ROUTES_CONSTANTS.CUSTOMER.children.APPS.name }"
                            class="text-gray-700 dark:text-gray-300 hover:text-[#1e293b] dark:hover:text-white font-medium py-2 transition-colors">
                            Sản phẩm
                        </router-link>
                        <router-link @click="closeMobileMenu"
                            :to="{ name: ROUTES_CONSTANTS.CUSTOMER.children.BOOKMARKS.name }"
                            class="text-gray-700 dark:text-gray-300 hover:text-[#1e293b] dark:hover:text-white font-medium py-2 transition-colors flex items-center justify-between">
                            <span>Sản phẩm yêu thích</span>
                            <span v-if="bookmarkCount > 0"
                                class="px-2 py-1 text-xs font-bold text-white bg-red-500 rounded-full">{{ bookmarkCount
                                }}</span>
                        </router-link>
                        <div class="border-t border-gray-200 dark:border-gray-700 my-2"></div>
                        <template v-if="isLoggedIn">
                            <!-- Admin Link Mobile -->
                            <router-link v-if="isAdmin" @click="closeMobileMenu"
                                :to="{ name: ROUTES_CONSTANTS.ADMIN.children.DASHBOARD.name }"
                                class="text-left text-gray-700 dark:text-gray-300 hover:text-[#1e293b] dark:hover:text-white font-medium py-2 transition-colors flex items-center gap-2">
                                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                                    stroke-width="1.5" stroke="currentColor" class="w-5 h-5">
                                    <path stroke-linecap="round" stroke-linejoin="round"
                                        d="M10.5 6h9.75M10.5 6a1.5 1.5 0 11-3 0m3 0a1.5 1.5 0 10-3 0M3.75 6H7.5m3 12h9.75m-9.75 0a1.5 1.5 0 01-3 0m3 0a1.5 1.5 0 00-3 0m-3.75 0H7.5m9-6h3.75m-3.75 0a1.5 1.5 0 01-3 0m3 0a1.5 1.5 0 00-3 0m-9.75 0h9.75" />
                                </svg>
                                Quản trị hệ thống
                            </router-link>

                            <div class="flex items-center gap-3 py-2">
                                <img :src="userInfo.avatar || 'https://ui-avatars.com/api/?name=' + userInfo.name"
                                    alt="User" class="w-8 h-8 rounded-full border border-gray-200" />
                                <div class="flex flex-col">
                                    <span class="text-sm font-medium text-gray-900 dark:text-white">{{ userInfo.name
                                        }}</span>
                                    <span class="text-xs text-gray-500">{{ userInfo.email }}</span>
                                </div>
                            </div>
                            <button @click="handleLogout"
                                class="text-left text-red-600 hover:text-red-700 font-medium py-2 transition-colors">
                                Đăng xuất
                            </button>
                        </template>
                        <button v-else @click="onMobileLoginClick"
                            class="text-left text-gray-700 dark:text-gray-300 hover:text-[#1e293b] dark:hover:text-white font-medium py-2 transition-colors">
                            Đăng ký / Đăng nhập
                        </button>
                    </nav>
                </div>
            </div>
        </transition>

        <!-- Login Modal -->
        <LoginModal :isOpen="isLoginModalOpen" @close="closeLoginModal" @login-google="handleLoginGoogle" />

    </header>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, computed } from 'vue';

import { ROUTES_CONSTANTS } from '@/constants/path';
import { ArrowRightStartOnRectangleIcon } from '@heroicons/vue/24/outline';
import ThemeToggle from '@/components/common/ThemeToggle.vue';
import LoginModal from '@/components/auth/LoginModal.vue';
import { cookieStorageAction } from '@/utils/storage';
import { SCREEN_COOKIE_NAME, ROLE_CUSTOMER } from '@/constants/cookie.constants';
import { useLikeStore } from '@/stores/like.store';
import { localStorageAction } from '@/utils/storage';
import { ACCESS_TOKEN_STORAGE_KEY } from '@/constants/storagekey';
import { authService } from '@/services/api/auth.service';
import { ROLES } from '@/constants/roles';
import { useAuthStore } from '@/stores/auth';

const likeStore = useLikeStore();
const bookmarkCount = ref(0);

// State for Login/User
const isLoggedIn = ref(false);
const userInfo = ref({
    name: 'User',
    email: 'user@example.com',
    avatar: '',
    roles: [] as string[]
});

const isAdmin = computed(() => userInfo.value.roles.includes(ROLES.ADMIN));

const isLoginModalOpen = ref(false);
const openLoginModal = () => isLoginModalOpen.value = true;
const closeLoginModal = () => isLoginModalOpen.value = false;

const onMobileLoginClick = () => {
    closeMobileMenu();
    openLoginModal();
}

const authStore = useAuthStore();

const handleLoginGoogle = async () => {
    cookieStorageAction.set(SCREEN_COOKIE_NAME, ROLE_CUSTOMER, 60 * 5); // 5 mins
    await authStore.loginWithGoogle();
    closeLoginModal();
};

const handleLogout = async () => {
    localStorageAction.remove(ACCESS_TOKEN_STORAGE_KEY);
    isLoggedIn.value = false;
    window.location.reload();
};


const isDropdownOpen = ref(false);
const dropdownRef = ref<HTMLElement | null>(null);

const toggleDropdown = () => {
    isDropdownOpen.value = !isDropdownOpen.value;
};

const isMobileMenuOpen = ref(false);

const toggleMobileMenu = () => {
    isMobileMenuOpen.value = !isMobileMenuOpen.value;
};

const closeMobileMenu = () => {
    isMobileMenuOpen.value = false;
};


const closeDropdown = (event: MouseEvent) => {
    if (dropdownRef.value && !dropdownRef.value.contains(event.target as Node)) {
        isDropdownOpen.value = false;
    }
};

const fetchLikeCount = async () => {
    if (isLoggedIn.value) {
        try {
            const likedProducts = await likeStore.getLikedProducts();
            if (Array.isArray(likedProducts)) {
                bookmarkCount.value = likedProducts.length;
            }
        } catch (error) {
            console.error('Failed to fetch like count', error);
        }
    } else {
        bookmarkCount.value = 0;
    }
};

onMounted(async () => {
    document.addEventListener('click', closeDropdown);

    const token = localStorageAction.get(ACCESS_TOKEN_STORAGE_KEY);
    if (token) {
        try {
            const res = await authService.getCurrentUser();
            if (res.data) {
                // Ensure we have a valid user ID, otherwise treat as guest
                if (!res.data.id) {
                    isLoggedIn.value = false;
                    localStorageAction.remove(ACCESS_TOKEN_STORAGE_KEY);
                    return;
                }

                isLoggedIn.value = true;
                const roles = Array.isArray(res.data.roles) ? res.data.roles : (res.data.roles ? [res.data.roles] : []);

                userInfo.value = {
                    name: res.data.name,
                    email: res.data.email,
                    avatar: res.data.avatar || 'https://ui-avatars.com/api/?name=' + res.data.name,
                    roles: roles as string[]
                };

                // Fetch like count after login calculation
                await fetchLikeCount();
            }
        } catch (error) {
            console.error('Failed to fetch user info', error);
            isLoggedIn.value = false;
            // Optional: remove token on error if 401
            // localStorageAction.remove(ACCESS_TOKEN_STORAGE_KEY);
        }
    }
});

onUnmounted(() => {
    document.removeEventListener('click', closeDropdown);
});
</script>