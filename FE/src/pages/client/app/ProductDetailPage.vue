<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';
import { useRoute } from 'vue-router';
import { getProductDetail, incrementViewCount, type ProductDetail } from '@/services/client/client.service';
import BaseButton from '@/components/base/BaseButton.vue';

const route = useRoute();
const product = ref<ProductDetail | null>(null);
const isLoading = ref(true);

const fetchProduct = async () => {
    try {
        const id = route.params.id as string;
        if (!id) return;
        const res = await getProductDetail(id);
        if (res) {
            product.value = res;
            // Increase view count
            incrementViewCount(id);
        }
    } catch (error) {
        console.error("Failed to load product", error);
    } finally {
        isLoading.value = false;
    }
};

onMounted(() => {
    fetchProduct();
});

const sortedFeatures = computed(() => {
    return product.value?.features || [];
});

const isVideo = (url?: string) => {
    if (!url) return false;
    return /\.(mp4|webm|ogg|mov)$/i.test(url);
};
</script>

<template>
    <div v-if="isLoading" class="min-h-screen flex items-center justify-center">
        <div class="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-yellow-500"></div>
    </div>

    <div v-else-if="product" class="min-h-screen bg-white pb-20">
        <!-- Hero Section -->
        <section class="relative bg-[#1A1A1A] text-white pt-24 pb-16 lg:pt-32 lg:pb-24 overflow-hidden">
            <div class="absolute inset-0 z-0">
                <!-- Background Blur/Overlay -->
                <div class="absolute inset-0 bg-gradient-to-r from-[#1A1A1A] to-[#1A1A1A]/80 z-10"></div>
                <img v-if="product.thumbnail" :src="product.thumbnail" class="w-full h-full object-cover opacity-30" />
            </div>

            <div class="container mx-auto px-4 relative z-20">
                <div class="max-w-4xl mx-auto text-center">
                    <div v-if="product.domainName"
                        class="inline-block px-3 py-1 mb-4 text-xs font-bold tracking-wider uppercase bg-yellow-500 text-[#1A1A1A] rounded-full">
                        {{ product.domainName }}
                    </div>

                    <h1 class="text-3xl md:text-5xl lg:text-6xl font-serif font-bold mb-6 leading-tight">
                        {{ product.name }}
                    </h1>

                    <p class="text-lg md:text-xl text-gray-300 mb-8 max-w-2xl mx-auto leading-relaxed">
                        {{ product.shortDescription || product.description }}
                    </p>

                    <div class="flex flex-wrap items-center justify-center gap-4">
                        <a v-if="product.demoUrl" :href="product.demoUrl" target="_blank"
                            class="inline-flex items-center justify-center px-8 py-3 text-base font-bold text-[#1A1A1A] bg-yellow-500 rounded-sm hover:bg-yellow-400 transition-all transform hover:-translate-y-1 shadow-lg hover:shadow-yellow-500/50">
                            View Demo
                            <svg class="w-5 h-5 ml-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M10 6H6a2 2 0 00-2 2v10a2 2 0 002 2h10a2 2 0 002-2v-4M14 4h6m0 0v6m0-6L10 14" />
                            </svg>
                        </a>
                        <a v-if="product.sourceUrl" :href="product.sourceUrl" target="_blank"
                            class="inline-flex items-center justify-center px-8 py-3 text-base font-bold text-white border border-white/20 rounded-sm hover:bg-white/10 transition-all backdrop-blur-sm">
                            <svg class="w-5 h-5 mr-2" fill="currentColor" viewBox="0 0 24 24">
                                <path fill-rule="evenodd"
                                    d="M12 2C6.477 2 2 6.484 2 12.017c0 4.425 2.865 8.18 6.839 9.504.5.092.682-.217.682-.483 0-.237-.008-.868-.013-1.703-2.782.605-3.369-1.343-3.369-1.343-.454-1.158-1.11-1.466-1.11-1.466-.908-.62.069-.608.069-.608 1.003.07 1.531 1.032 1.531 1.032.892 1.53 2.341 1.088 2.91.832.092-.647.35-1.088.636-1.338-2.22-.253-4.555-1.113-4.555-4.951 0-1.093.39-1.988 1.029-2.688-.103-.253-.446-1.272.098-2.65 0 0 .84-.27 2.75 1.026A9.564 9.564 0 0112 6.844c.85.004 1.705.115 2.504.337 1.909-1.296 2.747-1.027 2.747-1.027.546 1.379.202 2.398.1 2.651.64.7 1.028 1.595 1.028 2.688 0 3.848-2.339 4.695-4.566 4.943.359.309.678.92.678 1.855 0 1.338-.012 2.419-.012 2.747 0 .268.18.58.688.482A10.019 10.019 0 0022 12.017C22 6.484 17.522 2 12 2z"
                                    clip-rule="evenodd"></path>
                            </svg>
                            Source Code
                        </a>
                    </div>

                    <!-- Tech Stack -->
                    <div class="mt-8 flex flex-wrap justify-center gap-3">
                        <div v-for="tech in product.technologies" :key="tech.id"
                            class="flex items-center gap-2 px-3 py-1.5 bg-white/5 rounded-full border border-white/10 text-sm text-gray-300">
                            <!-- We can assume tech.icon is a URL or handle default -->
                            <img v-if="tech.icon" :src="tech.icon" class="w-4 h-4 object-contain" />
                            <span class="font-medium">{{ tech.name }}</span>
                        </div>
                    </div>
                </div>
            </div>
        </section>

        <!-- Main Content -->
        <main class="container mx-auto px-4 -mt-10 relative z-30">
            <div class="grid grid-cols-1 lg:grid-cols-3 gap-8">
                <!-- Left Column (Overview & Features) -->
                <div class="lg:col-span-2 space-y-12">

                    <!-- Overview -->
                    <div class="bg-white rounded-xl shadow-xl p-8 border border-gray-100">
                        <h2 class="text-2xl font-bold text-gray-900 font-serif mb-6 flex items-center gap-3">
                            <span class="w-8 h-1 bg-yellow-500 block rounded-full"></span>
                            Tổng quan
                        </h2>
                        <div class="prose prose-lg text-gray-600 max-w-none whitespace-pre-line">
                            {{ product.longDescription || product.description }}
                        </div>

                        <!-- Specifications (JSON) Render -->
                        <div v-if="product.specifications" class="mt-8 pt-8 border-t border-gray-100">
                            <h3 class="text-lg font-bold text-gray-900 mb-4">Thông số kỹ thuật</h3>
                            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                                <!-- Naive rendering provided spec is simple object -->
                                <div v-for="(val, key) in product.specifications" :key="key"
                                    class="flex justify-between border-b border-gray-50 pb-2">
                                    <span class="text-gray-500 font-medium capitalize">{{ key }}</span>
                                    <span class="text-gray-900 font-semibold">{{ val }}</span>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Features -->
                    <div v-if="sortedFeatures.length > 0">
                        <h2 class="text-2xl font-bold text-gray-900 font-serif mb-8 flex items-center gap-3">
                            <span class="w-8 h-1 bg-yellow-500 block rounded-full"></span>
                            Chức năng nổi bật
                        </h2>

                        <div class="space-y-12">
                            <div v-for="(feature, idx) in sortedFeatures" :key="feature.id || idx"
                                class="flex flex-col gap-6 md:gap-8 bg-white rounded-2xl p-6 shadow-sm border border-gray-100 transition-all hover:shadow-lg"
                                :class="idx % 2 !== 0 ? 'md:flex-row-reverse' : 'md:flex-row'">

                                <!-- Text Side -->
                                <div class="flex-1 flex flex-col justify-center">
                                    <div class="inline-flex items-center gap-2 mb-3">
                                        <span
                                            class="px-2 py-0.5 rounded bg-blue-50 text-blue-600 text-[10px] font-bold tracking-wider uppercase">Feature
                                            {{ idx + 1 }}</span>
                                    </div>
                                    <h3 class="text-2xl font-bold text-gray-900 mb-3">{{ feature.name }}</h3>
                                    <p class="text-gray-500 leading-relaxed mb-6">{{ feature.description }}</p>

                                    <div>
                                        <!-- Mock Action -->
                                        <button
                                            class="px-4 py-2 bg-gray-50 hover:bg-gray-100 text-gray-900 text-sm font-bold rounded-sm transition-colors border border-gray-200">
                                            Xem chi tiết
                                        </button>
                                    </div>
                                </div>

                                <!-- Media Side -->
                                <div class="flex-1">
                                    <div
                                        class="rounded-xl overflow-hidden shadow-md border border-gray-100 group aspect-video bg-gray-50 flex items-center justify-center relative">
                                        <video v-if="feature.videoUrl" :src="feature.videoUrl" controls
                                            class="w-full h-full object-contain bg-black"></video>
                                        <img v-else-if="feature.imagePreview" :src="feature.imagePreview"
                                            class="w-full h-full object-cover transition-transform duration-700 group-hover:scale-105" />
                                        <div v-else
                                            class="text-gray-300 flex flex-col items-center p-8 bg-gray-100 w-full h-full justify-center">
                                            <svg class="w-12 h-12 mb-2" fill="none" viewBox="0 0 24 24"
                                                stroke="currentColor">
                                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5"
                                                    d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
                                            </svg>
                                            <span class="text-xs font-medium">No Preview</span>
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
                    <div class="bg-white rounded-xl shadow-lg p-6 border border-gray-100 sticky top-24">
                        <h3 class="text-lg font-bold text-gray-900 mb-6 border-b border-gray-50 pb-3">Thành viên thực
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
                                    <h4 class="text-sm font-bold text-gray-900">{{ member.fullName }}</h4>
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

                        <div class="mt-6 pt-6 border-t border-gray-50">
                            <h3 class="text-sm font-bold text-gray-900 mb-3">Thông tin thêm</h3>
                            <div class="flex justify-between text-sm mb-2">
                                <span class="text-gray-500">Lượt xem</span>
                                <span class="font-bold text-gray-900">{{ product.viewCount || 0 }}</span>
                            </div>
                            <div class="flex justify-between text-sm mb-2">
                                <span class="text-gray-500">Mã dự án</span>
                                <span class="font-bold text-gray-900">{{ product.sku || 'N/A' }}</span>
                            </div>
                        </div>

                        <!-- Share Buttons (Mock) -->
                        <div class="mt-6">
                            <button
                                class="w-full py-2 border border-gray-200 rounded text-sm font-semibold text-gray-600 hover:bg-gray-50 transition-colors">
                                Chia sẻ dự án
                            </button>
                        </div>
                    </div>

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