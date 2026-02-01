<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';
import { useRoute } from 'vue-router';
import { getProductDetail, incrementViewCount, type ProductDetail } from '@/services/client/client.service';
import { decodeId, encodeId } from '@/utils';
import { getRelatedProducts } from '@/services/client/app.service';
import BaseButton from '@/components/base/BaseButton.vue';
import YouTubeEmbed from '@/components/common/YouTubeEmbed.vue';
import ShareButtons from '@/components/common/ShareButtons.vue';
import ReviewSection from '@/components/common/ReviewSection.vue';
import LikeButton from '@/components/common/LikeButton.vue';

const route = useRoute();
const product = ref<ProductDetail | null>(null);
const relatedProducts = ref<any[]>([]);
const isLoading = ref(true);

const fetchProduct = async () => {
    try {
        const id = decodeId(route.params.id as string);
        if (!id) return;
        const res = await getProductDetail(id);
        if (res) {
            product.value = res;
            // Increase view count
            incrementViewCount(id);
            // Fetch related products
            fetchRelatedProducts(id);
        }
    } catch (error) {
        console.error("Failed to load product", error);
    } finally {
        isLoading.value = false;
    }
};

const fetchRelatedProducts = async (productId: string) => {
    try {
        const related = await getRelatedProducts(productId);
        relatedProducts.value = related || [];
    } catch (error) {
        console.error("Failed to load related products", error);
        relatedProducts.value = [];
    }
};

onMounted(() => {
    fetchProduct();
});

const sortedFeatures = computed(() => {
    return product.value?.features || [];
});

const isYouTubeUrl = (url?: string) => {
    if (!url) return false;
    return /(?:youtube\.com\/watch\?v=|youtu\.be\/|youtube\.com\/embed\/)([^&\n?#]+)/.test(url);
};

const isVideo = (url?: string) => {
    if (!url) return false;
    return /\.(mp4|webm|ogg|mov)$/i.test(url);
};
</script>

<template>
    <div v-if="isLoading" class="min-h-screen flex items-center justify-center">
        <div class="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-yellow-500"></div>
    </div>

    <div v-else-if="product" class="min-h-screen bg-white dark:bg-gray-900 pb-20 transition-colors duration-300">
        <!-- Hero Section -->
        <section class="relative bg-secondary text-white pt-24 pb-16 lg:pt-32 lg:pb-24 overflow-hidden">
            <div class="absolute inset-0 z-0">
                <!-- Background Blur/Overlay -->
                <div class="absolute inset-0 bg-gradient-to-r from-secondary to-secondary/90 z-10"></div>
                <img v-if="product.thumbnail" :src="product.thumbnail" class="w-full h-full object-cover opacity-20 blur-sm" />
            </div>

            <div class="container mx-auto px-4 relative z-20">
                <div class="max-w-4xl mx-auto text-center animate-fade-in">
                    <div v-if="product.domainName"
                        class="inline-block px-4 py-1.5 mb-6 text-xs font-bold tracking-widest uppercase bg-primary text-white rounded-full shadow-lg shadow-primary/30">
                        {{ product.domainName }}
                    </div>

                    <h1 class="text-4xl md:text-6xl lg:text-7xl font-serif font-bold mb-6 leading-tight tracking-tight">
                        {{ product.name }}
                    </h1>

                    <p class="text-lg md:text-xl text-gray-300 mb-10 max-w-2xl mx-auto leading-relaxed font-light">
                        {{ product.shortDescription || product.description }}
                    </p>

                    <div class="flex flex-wrap items-center justify-center gap-4">
                        <a v-if="product.demoUrl" :href="product.demoUrl" target="_blank"
                            class="inline-flex items-center justify-center px-8 py-4 text-base font-bold text-white bg-primary rounded-lg hover:bg-primary-hover transition-all transform hover:-translate-y-1 shadow-lg hover:shadow-primary/50">
                            View Demo
                            <svg class="w-5 h-5 ml-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M10 6H6a2 2 0 00-2 2v10a2 2 0 002 2h10a2 2 0 002-2v-4M14 4h6m0 0v6m0-6L10 14" />
                            </svg>
                        </a>
                        <a v-if="product.sourceUrl" :href="product.sourceUrl" target="_blank"
                            class="inline-flex items-center justify-center px-8 py-4 text-base font-bold text-white border border-white/20 rounded-lg hover:bg-white/10 transition-all backdrop-blur-sm">
                            <svg class="w-5 h-5 mr-2" fill="currentColor" viewBox="0 0 24 24">
                                <path fill-rule="evenodd"
                                    d="M12 2C6.477 2 2 6.484 2 12.017c0 4.425 2.865 8.18 6.839 9.504.5.092.682-.217.682-.483 0-.237-.008-.868-.013-1.703-2.782.605-3.369-1.343-3.369-1.343-.454-1.158-1.11-1.466-1.11-1.466-.908-.62.069-.608.069-.608 1.003.07 1.531 1.032 1.531 1.032.892 1.53 2.341 1.088 2.91.832.092-.647.35-1.088.636-1.338-2.22-.253-4.555-1.113-4.555-4.951 0-1.093.39-1.988 1.029-2.688-.103-.253-.446-1.272.098-2.65 0 0 .84-.27 2.75 1.026A9.564 9.564 0 0112 6.844c.85.004 1.705.115 2.504.337 1.909-1.296 2.747-1.027 2.747-1.027.546 1.379.202 2.398.1 2.651.64.7 1.028 1.595 1.028 2.688 0 3.848-2.339 4.695-4.566 4.943.359.309.678.92.678 1.855 0 1.338-.012 2.419-.012 2.747 0 .268.18.58.688.482A10.019 10.019 0 0022 12.017C22 6.484 17.522 2 12 2z"
                                    clip-rule="evenodd"></path>
                            </svg>
                            Source Code
                        </a>
                        <LikeButton v-if="product.id" :product-id="product.id" :show-count="true" class="!px-8 !py-4" />
                    </div>

                    <!-- Tech Stack -->
                    <div class="mt-10 flex flex-wrap justify-center gap-3">
                        <div v-for="tech in product.technologies" :key="tech.id"
                            class="flex items-center gap-2 px-4 py-2 bg-white/5 rounded-full border border-white/10 text-sm text-gray-300 hover:bg-white/10 transition-colors">
                            <img v-if="tech.icon" :src="tech.icon" class="w-5 h-5 object-contain" />
                            <span class="font-medium">{{ tech.name }}</span>
                        </div>
                    </div>

                    <!-- Share Buttons -->
                    <div class="mt-10 flex justify-center opacity-80 hover:opacity-100 transition-opacity">
                        <ShareButtons :title="product.name" :description="product.shortDescription"
                            :show-label="true" />
                    </div>
                </div>
            </div>
        </section>

        <!-- Main Content -->
        <main class="container mx-auto px-4 -mt-20 relative z-30">
            <!-- Main Video Embed -->
            <div v-if="product.demoUrl && isYouTubeUrl(product.demoUrl)"
                class="mb-16 rounded-2xl overflow-hidden shadow-2xl shadow-black/50 border-4 border-white dark:border-gray-800 bg-black aspect-video max-w-5xl mx-auto">
                <YouTubeEmbed :url="product.demoUrl" :title="product.name" class="w-full h-full" />
            </div>

            <div class="grid grid-cols-1 lg:grid-cols-3 gap-10">
                <!-- Left Column (Overview & Features) -->
                <div class="lg:col-span-2 space-y-12">

                    <!-- Overview -->
                    <div class="bg-white dark:bg-dark-light rounded-2xl shadow-xl p-8 border border-gray-100 dark:border-gray-700">
                        <h2 class="text-3xl font-bold text-gray-900 dark:!text-white font-serif mb-6 flex items-center gap-3">
                            <span class="w-10 h-1.5 bg-primary block rounded-full"></span>
                            Tổng quan
                        </h2>
                        <div class="prose prose-lg dark:prose-invert prose-blue max-w-none whitespace-pre-line text-gray-600 dark:text-gray-300">
                            {{ product.longDescription || product.description }}
                        </div>

                        <!-- Specifications (JSON) Render -->
                        <div v-if="product.specifications" class="mt-10 pt-10 border-t border-gray-100 dark:border-gray-700">
                            <h3 class="text-xl font-bold text-gray-900 dark:text-white mb-6">Thông số kỹ thuật</h3>
                            <div class="grid grid-cols-1 md:grid-cols-2 gap-x-8 gap-y-4">
                                <div v-for="(val, key) in product.specifications" :key="key"
                                    class="flex justify-between border-b border-gray-50 dark:border-gray-700 pb-3">
                                    <span class="text-gray-500 dark:text-gray-400 font-medium capitalize">{{ key }}</span>
                                    <span class="text-gray-900 dark:text-white font-semibold">{{ val }}</span>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Features -->
                    <div v-if="sortedFeatures.length > 0">
                        <h2 class="text-3xl font-bold text-gray-900 dark:!text-white font-serif mb-10 flex items-center gap-3">
                            <span class="w-10 h-1.5 bg-primary block rounded-full"></span>
                            Chức năng nổi bật
                        </h2>

                        <div class="space-y-16">
                            <div v-for="(feature, idx) in sortedFeatures" :key="feature.id || idx"
                                class="flex flex-col gap-8 md:gap-12 bg-white dark:bg-dark-light rounded-2xl p-8 shadow-sm border border-gray-100 dark:border-gray-700 transition-all hover:shadow-xl hover:border-primary/30"
                                :class="idx % 2 !== 0 ? 'md:flex-row-reverse' : 'md:flex-row'">

                                <!-- Text Side -->
                                <div class="flex-1 flex flex-col justify-center">
                                    <div class="inline-flex items-center gap-2 mb-4">
                                        <span
                                            class="px-3 py-1 rounded-full bg-blue-50 dark:bg-blue-900/30 text-primary text-xs font-bold tracking-widest uppercase">Feature
                                            {{ idx + 1 }}</span>
                                    </div>
                                    <h3 class="text-2xl font-bold text-gray-900 dark:!text-white mb-4">{{ feature.name }}</h3>
                                    <p class="text-gray-500 dark:text-gray-400 leading-relaxed text-lg mb-6">{{ feature.description }}</p>

                                    <div>
                                        <!-- Mock Action -->
                                        <button
                                            class="px-6 py-2.5 bg-gray-50 dark:bg-gray-800 hover:bg-gray-100 dark:hover:bg-gray-700 text-gray-900 dark:text-white text-sm font-bold rounded-lg transition-colors border border-gray-200 dark:border-gray-600">
                                            Xem chi tiết
                                        </button>
                                    </div>
                                </div>

                                <!-- Media Side -->
                                <div class="flex-1">
                                    <div
                                        class="rounded-xl overflow-hidden shadow-lg border border-gray-100 dark:border-gray-700 group aspect-video bg-gray-50 dark:bg-gray-800 flex items-center justify-center relative">
                                        <!-- YouTube Embed -->
                                        <YouTubeEmbed v-if="feature.videoUrl && isYouTubeUrl(feature.videoUrl)"
                                            :url="feature.videoUrl" :title="feature.name" class="w-full h-full" />
                                        <!-- Direct Video File -->
                                        <video v-else-if="feature.videoUrl && isVideo(feature.videoUrl)"
                                            :src="feature.videoUrl" controls
                                            class="w-full h-full object-contain bg-black"></video>
                                        <!-- Image Preview -->
                                        <img v-else-if="feature.imagePreview" :src="feature.imagePreview"
                                            class="w-full h-full object-cover transition-transform duration-700 group-hover:scale-105" />
                                        <!-- Placeholder -->
                                        <div v-else
                                            class="text-gray-300 dark:text-gray-600 flex flex-col items-center p-8 bg-gray-100 dark:bg-gray-700 w-full h-full justify-center">
                                            <svg class="w-16 h-16 mb-2" fill="none" viewBox="0 0 24 24"
                                                stroke="currentColor">
                                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5"
                                                    d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
                                            </svg>
                                            <span class="text-sm font-medium">No Preview</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                </div>

                <!-- Right Column (Team, Meta, Share) -->
                <div class="space-y-8">

                    <!-- Team Card -->
                    <div class="bg-white dark:bg-dark-light rounded-xl shadow-lg p-6 border border-gray-100 dark:border-gray-700 sticky top-24">
                        <h3 class="text-lg font-bold text-gray-900 dark:!text-white mb-6 border-b border-gray-50 dark:border-gray-800 pb-3">Thành viên thực
                            hiện
                        </h3>

                        <div class="space-y-4">
                            <div v-for="member in product.teamMembers" :key="member.id" class="flex items-center gap-4">
                                <div
                                    class="w-10 h-10 rounded-full bg-gray-100 flex items-center justify-center font-bold text-gray-500 text-sm overflow-hidden">
                                    <!-- Avatar placeholder -->
                                    {{ member.fullName.charAt(0) }}
                                </div>
                                <div>
                                    <h4 class="text-sm font-bold text-gray-900 dark:text-white">{{ member.fullName }}</h4>
                                    <p class="text-xs"
                                        :class="member.role === 'LEADER' ? 'text-yellow-600 font-bold' : 'text-gray-500'">
                                        {{ member.role === 'LEADER' ? 'Project Leader' : 'Member' }}
                                    </p>
                                </div>
                            </div>
                            <div v-if="!product.teamMembers || product.teamMembers.length === 0"
                                class="text-gray-400 text-sm italic">
                                Chưa cập nhật thành viên
                            </div>
                        </div>

                        <div class="mt-6 pt-6 border-t border-gray-50 dark:border-gray-800">
                            <h3 class="text-sm font-bold text-gray-900 dark:text-white mb-3">Thông tin thêm</h3>
                            <div class="flex justify-between text-sm mb-2">
                                <span class="text-gray-500 dark:text-gray-400">Lượt xem</span>
                                <span class="font-bold text-gray-900 dark:text-white">{{ product.viewCount || 0 }}</span>
                            </div>
                            <div class="flex justify-between text-sm mb-2">
                                <span class="text-gray-500 dark:text-gray-400">Mã dự án</span>
                                <span class="font-bold text-gray-900 dark:text-white">{{ product.sku || 'N/A' }}</span>
                            </div>
                        </div>

                        <!-- Share Buttons (Mock) -->
                        <div class="mt-6">
                            <button
                                class="w-full py-2 border border-gray-200 dark:border-gray-700 rounded text-sm font-semibold text-gray-600 dark:text-gray-400 hover:bg-gray-50 dark:hover:bg-gray-800 transition-colors">
                                Chia sẻ dự án
                            </button>
                        </div>
                    </div>

                </div>
            </div>

            <!-- Reviews Section -->
            <div class="max-w-4xl mx-auto mt-12 px-4">
                <ReviewSection :app-id="product.id" />
            </div>

            <!-- Related Products Section -->
            <div v-if="relatedProducts.length > 0" class="max-w-6xl mx-auto mt-24 px-4 pb-16">
                <h2 class="text-3xl font-bold text-gray-900 dark:!text-white font-serif mb-12 text-center flex items-center justify-center gap-3">
                    <span class="w-12 h-1 bg-primary block rounded-full"></span>
                    Sản phẩm liên quan
                    <span class="w-12 h-1 bg-primary block rounded-full"></span>
                </h2>

                <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                    <router-link v-for="relatedProduct in relatedProducts" :key="relatedProduct.id"
                        :to="`/apps/${encodeId(relatedProduct.id)}`"
                        class="group bg-white dark:bg-dark-light rounded-xl shadow-md hover:shadow-xl transition-all duration-300 overflow-hidden border border-gray-100 dark:border-gray-700 hover:border-yellow-500">
                        <div class="aspect-video bg-gray-100 overflow-hidden relative">
                            <img v-if="relatedProduct.thumbnail" :src="relatedProduct.thumbnail"
                                :alt="relatedProduct.name"
                                class="w-full h-full object-cover group-hover:scale-110 transition-transform duration-500" />
                            <div v-else class="w-full h-full flex items-center justify-center text-gray-400">
                                <svg class="w-12 h-12" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5"
                                        d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
                                </svg>
                            </div>
                            <div v-if="relatedProduct.isFeatured"
                                class="absolute top-3 right-3 bg-yellow-500 text-[#1A1A1A] px-2 py-1 rounded-full text-xs font-bold">
                                Featured
                            </div>
                        </div>

                        <div class="p-5">
                            <div v-if="relatedProduct.domainName"
                                class="text-xs font-bold text-yellow-600 mb-2 uppercase tracking-wider">
                                {{ relatedProduct.domainName }}
                            </div>
                            <h3
                                class="text-lg font-bold text-gray-900 dark:text-white mb-2 line-clamp-2 group-hover:text-yellow-600 transition-colors">
                                {{ relatedProduct.name }}
                            </h3>
                            <p class="text-sm text-gray-500 dark:text-gray-400 line-clamp-2 mb-4">
                                {{ relatedProduct.shortDescription }}
                            </p>

                            <div class="flex items-center justify-between">
                                <div class="flex items-center gap-2 text-xs text-gray-400">
                                    <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                            d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                            d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
                                    </svg>
                                    <span>{{ relatedProduct.viewCount || 0 }}</span>
                                </div>
                                <div
                                    class="text-yellow-600 font-bold text-sm group-hover:translate-x-1 transition-transform">
                                    Xem thêm →
                                </div>
                            </div>
                        </div>
                    </router-link>
                </div>
            </div>
        </main>
    </div>

    <!-- 404 / Not Found State -->
    <div v-else class="min-h-screen flex flex-col items-center justify-center text-center">
        <h2 class="text-2xl font-bold text-gray-900 mb-2">Không tìm thấy sản phẩm</h2>
        <p class="text-gray-500 mb-6">Sản phẩm này có thể đã bị xoá hoặc không tồn tại.</p>
        <BaseButton @click="$router.push('/')" variant="primary">Về trang chủ</BaseButton>
    </div>
</template>