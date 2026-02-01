<template>
    <div class="space-y-16 pb-16">
        <!-- Hero Section -->
        <section
            class="relative bg-gradient-to-br from-secondary to-secondary-800 text-white py-24 lg:py-32 overflow-hidden">
            <!-- Background Shapes (Decorative) -->
            <div class="absolute top-0 right-0 w-1/2 h-full bg-gradient-to-l from-primary/10 to-transparent skew-x-12 transform translate-x-1/4"></div>
            <div class="absolute bottom-0 left-0 w-1/3 h-1/2 bg-gradient-to-t from-primary/5 to-transparent rounded-full blur-3xl"></div>

            <div class="container mx-auto px-4 relative z-10 flex flex-col items-center text-center animate-fade-in">
                <div class="inline-block px-4 py-1.5 mb-6 text-xs font-bold tracking-widest uppercase bg-white/10 backdrop-blur-md rounded-full border border-white/10 text-primary-300">
                    FPL UDPM Catalog
                </div>
                
                <h1 class="text-5xl lg:text-7xl font-serif font-bold mb-8 tracking-tight leading-tight">
                    Khám phá Kho tàng <br/>
                    <span class="bg-clip-text text-transparent bg-gradient-to-r from-primary-400 to-primary-200">Sản phẩm Sinh viên</span>
                </h1>
                
                <p class="text-lg lg:text-xl text-gray-300 max-w-2xl mb-10 leading-relaxed font-light">
                    Nơi lưu trữ và trưng bày các dự án xuất sắc, đồ án tốt nghiệp và sản phẩm sáng tạo từ cộng đồng sinh
                    viên FPL UDPM.
                </p>

                <div class="flex flex-col sm:flex-row gap-4 w-full sm:w-auto">
                    <router-link to="/products"
                        class="px-8 py-4 bg-primary hover:bg-primary-hover text-white font-bold rounded-lg transition-all shadow-lg shadow-primary/30 hover:shadow-primary/50 transform hover:-translate-y-1">
                        Xem tất cả sản phẩm
                    </router-link>
                    <a href="#featured"
                        class="px-8 py-4 bg-white/5 hover:bg-white/10 text-white font-bold rounded-lg backdrop-blur-sm border border-white/10 transition-all hover:bg-white/15">
                        Sản phẩm nổi bật
                    </a>
                </div>
            </div>
        </section>

        <!-- Domains Section -->
        <section class="container mx-auto px-4">
            <div class="text-center mb-16">
                <h2 class="text-3xl lg:text-4xl font-bold font-serif text-gray-900 dark:text-white mb-4">Lĩnh vực hoạt động</h2>
                <div class="w-24 h-1.5 bg-primary mx-auto rounded-full"></div>
            </div>

            <div class="grid grid-cols-2 md:grid-cols-4 gap-6">
                <DomainCard 
                    v-for="domain in domains" 
                    :key="domain.id" 
                    :domain="domain" 
                    class="h-full" 
                    @click="navigateToCategory(domain.id)" 
                />
            </div>
        </section>

        <!-- Product Technologies Slider -->
        <TechnologySlider />

        <!-- Featured Products Section -->
        <section id="featured" class="container mx-auto px-4">
            <div class="flex items-end justify-between mb-12 border-b border-gray-100 dark:border-gray-800 pb-4">
                <div>
                    <h2 class="text-3xl lg:text-4xl font-bold font-serif text-gray-900 dark:text-white mb-3">Sản phẩm Nổi bật</h2>
                    <p class="text-gray-500 dark:text-gray-400 text-lg">Các dự án được đánh giá cao và có tính ứng dụng thực tế</p>
                </div>
                <router-link to="/products"
                    class="hidden md:flex items-center text-primary font-bold hover:text-primary-hover transition-colors gap-2 px-4 py-2 rounded-lg hover:bg-primary-50 dark:hover:bg-primary-900/20">
                    Xem thêm <span class="text-xl">&rarr;</span>
                </router-link>
            </div>

            <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
                <ProductCard v-for="product in featuredProducts" :key="product.id" :product="product" />
            </div>

            <!-- Mobile View More -->
            <div class="mt-8 text-center md:hidden">
                <router-link to="/products" class="inline-flex items-center text-blue-600 font-medium">
                    Xem tất cả sản phẩm &rarr;
                </router-link>
            </div>
        </section>

        <!-- Featured Videos Section -->
        <section v-if="hasFeaturedVideos" class="container mx-auto px-4">
            <div class="text-center mb-12">
                <h2 class="text-3xl font-bold text-gray-900 mb-4">Video Nổi Bật</h2>
                <p class="text-gray-500">Khám phá các demo video từ những dự án xuất sắc</p>
                <div class="w-20 h-1 bg-yellow-500 mx-auto rounded mt-4"></div>
            </div>

            <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
                <div v-for="video in featuredVideos" :key="video.id"
                    class="group cursor-pointer transform transition-all hover:-translate-y-2"
                    @click="$router.push(`/products/${video.id}`)">
                    <div
                        class="bg-white rounded-xl overflow-hidden shadow-lg border border-gray-100 hover:shadow-2xl transition-shadow">
                        <!-- Video Embed -->
                        <div class="relative aspect-video bg-gray-900">
                            <YouTubeEmbed :url="video.videoUrl!" :title="video.name" class="w-full h-full"
                                :autoplay="true" :mute="true" />
                            <!-- Overlay on hover -->
                            <div
                                class="absolute inset-0 bg-black/40 opacity-0 group-hover:opacity-100 transition-opacity flex items-center justify-center">
                                <div class="text-white text-center">
                                    <svg class="w-16 h-16 mx-auto mb-2" fill="currentColor" viewBox="0 0 20 20">
                                        <path
                                            d="M10 18a8 8 0 100-16 8 8 0 000 16zM9.555 7.168A1 1 0 008 8v4a1 1 0 001.555.832l3-2a1 1 0 000-1.664l-3-2z" />
                                    </svg>
                                    <span class="font-semibold">Xem chi tiết</span>
                                </div>
                            </div>
                        </div>

                        <!-- Video Info -->
                        <div class="p-6">
                            <div class="flex items-start justify-between mb-3">
                                <h3
                                    class="text-lg font-bold text-gray-900 group-hover:text-blue-600 transition-colors line-clamp-2">
                                    {{ video.name }}
                                </h3>
                            </div>

                            <p class="text-sm text-gray-500 mb-4 line-clamp-2">
                                {{ video.shortDescription || video.description }}
                            </p>

                            <!-- Meta Info -->
                            <div class="flex items-center justify-between text-xs text-gray-400">
                                <div class="flex items-center gap-1">
                                    <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                            d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                            d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
                                    </svg>
                                    <span>{{ video.viewCount || 0 }} lượt xem</span>
                                </div>
                                <span v-if="video.domainName"
                                    class="px-2 py-1 bg-blue-50 text-blue-600 rounded-full font-semibold">
                                    {{ video.domainName }}
                                </span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';
import { useRouter } from 'vue-router'; // Import useRouter
import DomainCard from '@/components/client/domain/DomainCard.vue';
import ProductCard from '@/components/client/product/ProductCard.vue';
import TechnologySlider from '@/components/client/home/TechnologySlider.vue';
import YouTubeEmbed from '@/components/common/YouTubeEmbed.vue';
import { getPublicDomains, getPublicFeaturedProducts, getPublicFeaturedVideos, type PublicDomain, type PublicProduct } from '@/services/client/client.service';

const router = useRouter(); // Initialize router
const domains = ref<PublicDomain[]>([]);
const featuredProducts = ref<PublicProduct[]>([]);
const featuredVideos = ref<PublicProduct[]>([]);

const hasFeaturedVideos = computed(() => {
    return Array.isArray(featuredVideos.value) && featuredVideos.value.length > 0;
});

const navigateToCategory = (id: string) => {
    router.push({ path: '/apps', query: { domain: id } });
};

onMounted(async () => {
    try {
        const [domainsData, productsData, videosData] = await Promise.all([
            getPublicDomains(),
            getPublicFeaturedProducts({ sort: 'FEATURED' }),
            getPublicFeaturedVideos()
        ]);
        
        // Handle domains safety
        domains.value = domainsData || [];
        
        // Handle products safety - check if it's a paginated response or direct array
        if (productsData) {
            featuredProducts.value = (productsData as any).data || (productsData as any).content || (Array.isArray(productsData) ? productsData : []);
        } else {
            featuredProducts.value = [];
        }

        // Handle videos safety
        const videos = videosData || [];
        if (Array.isArray(videos)) {
            featuredVideos.value = videos.map((v: any) => ({
                ...v,
                videoUrl: v.demoUrl || v.videoUrl // Fallback if videoUrl already exists
            }));
        } else {
            featuredVideos.value = [];
        }
    } catch (error) {
        console.error("Failed to load home data", error);
        // Ensure refs are at least empty arrays on error
        domains.value = [];
        featuredProducts.value = [];
        featuredVideos.value = [];
    }
});
</script>