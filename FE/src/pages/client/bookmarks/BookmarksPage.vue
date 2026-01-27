<template>
    <div class="min-h-screen bg-gray-50 dark:bg-gray-900 py-12 transition-colors duration-300">
        <div class="container mx-auto px-4">
            <!-- Header -->
            <div class="mb-12 text-center">
                <h1 class="text-4xl font-bold font-serif text-gray-900 dark:text-white mb-3">
                    Sản phẩm yêu thích
                </h1>
                <p class="text-gray-500 dark:text-gray-400 text-lg">
                    {{ bookmarks.length }} sản phẩm trong danh sách yêu thích của bạn
                </p>
            </div>

            <!-- Empty State -->
            <div v-if="bookmarks.length === 0" class="text-center py-20 bg-white dark:bg-dark-light rounded-2xl shadow-sm border border-gray-100 dark:border-gray-700 max-w-2xl mx-auto">
                <div
                    class="w-24 h-24 mx-auto mb-6 rounded-full bg-gray-50 dark:bg-gray-800 flex items-center justify-center">
                    <svg class="w-12 h-12 text-gray-300 dark:text-gray-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5"
                            d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" />
                    </svg>
                </div>
                <h2 class="text-xl font-bold text-gray-900 dark:text-white mb-2">
                    Chưa có sản phẩm nào
                </h2>
                <p class="text-gray-500 dark:text-gray-400 mb-8 max-w-sm mx-auto leading-relaxed">
                    Hãy khám phá và lưu các sản phẩm bạn yêu thích để xem lại sau!
                </p>
                <router-link :to="{ name: ROUTES_CONSTANTS.CUSTOMER.children.APPS.name }"
                    class="inline-flex items-center px-8 py-3 bg-primary text-white font-bold rounded-lg hover:bg-primary-hover transition-all shadow-lg hover:shadow-primary/30">
                    Khám phá sản phẩm
                    <svg class="w-5 h-5 ml-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                            d="M13 7l5 5m0 0l-5 5m5-5H6" />
                    </svg>
                </router-link>
            </div>

            <!-- Bookmarked Products Grid -->
            <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
                <div v-for="item in bookmarks" :key="item.id"
                    class="group bg-white dark:bg-gray-800 rounded-xl border border-gray-100 dark:border-gray-700 overflow-hidden hover:shadow-lg transition-all duration-300">
                    <!-- Thumbnail -->
                    <div class="relative aspect-video bg-gray-100 dark:bg-gray-700 overflow-hidden">
                        <img :src="item.thumbnail || 'https://placehold.co/640x360?text=No+Image'" :alt="item.name"
                            class="w-full h-full object-cover group-hover:scale-105 transition-transform duration-500" />
                        <!-- Remove Button -->
                        <button @click="handleRemoveLike(item.id)"
                            class="absolute top-3 right-3 p-2 rounded-full bg-red-500 text-white hover:bg-red-600 transition-colors"
                            title="Bỏ thích">
                            <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M6 18L18 6M6 6l12 12" />
                            </svg>
                        </button>
                    </div>

                    <!-- Content -->
                    <div class="p-4">
                        <span v-if="item.domainName"
                            class="inline-block px-2 py-1 text-xs font-semibold text-blue-600 dark:text-blue-400 bg-blue-50 dark:bg-blue-900/30 rounded-full mb-2">
                            {{ item.domainName }}
                        </span>
                        <router-link
                            :to="{ name: ROUTES_CONSTANTS.CUSTOMER.children.APP_DETAIL.name, params: { id: item.id } }"
                            class="block text-lg font-bold text-gray-900 dark:text-white hover:text-blue-600 dark:hover:text-blue-400 transition-colors line-clamp-1">
                            {{ item.name }}
                        </router-link>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useLikeStore } from '@/stores/like.store';
import { ROUTES_CONSTANTS } from '@/constants/path';
import { toast } from 'vue3-toastify';

const likeStore = useLikeStore();
const bookmarks = ref<any[]>([]);

const fetchBookmarks = async () => {
    bookmarks.value = await likeStore.getLikedProducts();
};

const handleRemoveLike = async (id: string) => {
    try {
        await likeStore.toggleLike(id);
        toast.success("Đã bỏ thích sản phẩm");
        await fetchBookmarks();
    } catch (error) {
        toast.error("Có lỗi xảy ra");
    }
};

onMounted(() => {
    fetchBookmarks();
});
</script>
