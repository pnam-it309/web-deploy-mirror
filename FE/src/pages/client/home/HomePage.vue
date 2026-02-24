<template>
    <div class="space-y-0 pb-16">

        <!-- ═══════════════════════════════════════════════════════════
             1. FLOATING MESH GRADIENT HERO SECTION
        ═══════════════════════════════════════════════════════════ -->
        <section class="relative min-h-[92vh] flex items-center justify-center overflow-hidden" ref="heroRef">

            <!-- Mesh Gradient Background -->
            <div class="absolute inset-0 bg-[#0a0a1a]">
                <div class="mesh-blob mesh-blob-1"></div>
                <div class="mesh-blob mesh-blob-2"></div>
                <div class="mesh-blob mesh-blob-3"></div>
                <div class="mesh-blob mesh-blob-4"></div>
                <!-- Noise texture overlay -->
                <div class="absolute inset-0 opacity-[0.03]" style="background-image: url('data:image/svg+xml,%3Csvg xmlns=%22http://www.w3.org/2000/svg%22 width=%22300%22 height=%22300%22%3E%3Cfilter id=%22noise%22%3E%3CfeTurbulence type=%22fractalNoise%22 baseFrequency=%220.65%22 numOctaves=%223%22 stitchTiles=%22stitch%22/%3E%3CfeColorMatrix type=%22saturate%22 values=%220%22/%3E%3C/filter%3E%3Crect width=%22300%22 height=%22300%22 filter=%22url(%23noise)%22 opacity=%221%22/%3E%3C/svg%3E');
  background-repeat: repeat;"></div>
                <!-- Grid overlay -->
                <div class="absolute inset-0"
                    style="background-image: linear-gradient(rgba(255,255,255,0.03) 1px, transparent 1px), linear-gradient(90deg, rgba(255,255,255,0.03) 1px, transparent 1px); background-size: 60px 60px;">
                </div>
            </div>

            <!-- Floating particles -->
            <div class="absolute inset-0 overflow-hidden pointer-events-none">
                <div v-for="i in 20" :key="i" class="particle"
                    :style="`left:${(i * 37) % 100}%; top:${(i * 53) % 100}%; animation-delay:${i * 0.4}s; animation-duration:${8 + (i % 5) * 2}s`">
                </div>
            </div>

            <!-- Hero Content with Parallax -->
            <div class="container mx-auto px-4 relative z-10 text-center" ref="heroContentRef">

                <!-- Badge -->
                <div class="inline-flex items-center gap-2 px-5 py-2 mb-8 text-xs font-bold tracking-widest uppercase bg-white/5 backdrop-blur-md rounded-full border border-white/10 text-purple-300 hero-fade-in"
                    style="animation-delay: 0.1s">
                    <span class="w-2 h-2 bg-purple-400 rounded-full animate-pulse"></span>
                    FPL UDPM · Student Project Catalog
                </div>

                <!-- Main Heading with Floating Animation -->
                <h1 class="text-5xl sm:text-6xl lg:text-8xl font-bold mb-8 tracking-tight leading-[1.05] hero-fade-in text-white floating-text"
                    style="animation-delay: 0.2s">
                    Khám phá Kho tàng
                    <span
                        class="block bg-clip-text text-transparent bg-gradient-to-r from-violet-400 via-purple-300 to-blue-400 mt-2">
                        Sản phẩm Sinh viên
                    </span>
                </h1>

                <!-- Description -->
                <p class="text-lg lg:text-xl text-gray-400 max-w-2xl mx-auto mb-12 leading-relaxed hero-fade-in"
                    style="animation-delay: 0.35s">
                    Nơi lưu trữ và trưng bày các dự án xuất sắc, đồ án tốt nghiệp và sản phẩm sáng tạo
                    từ cộng đồng sinh viên FPL UDPM.
                </p>

                <!-- CTAs -->
                <div class="flex flex-col sm:flex-row items-center justify-center gap-4 hero-fade-in"
                    style="animation-delay: 0.5s">
                    <router-link :to="{ name: ROUTES_CONSTANTS.CUSTOMER.children.APPS.name }"
                        class="group px-8 py-4 bg-gradient-to-r from-violet-600 to-purple-600 hover:from-violet-500 hover:to-purple-500 text-white font-bold rounded-xl transition-all shadow-2xl shadow-purple-500/30 hover:shadow-purple-500/50 transform hover:-translate-y-1 flex items-center gap-2">
                        Xem tất cả sản phẩm
                        <svg class="w-4 h-4 transition-transform group-hover:translate-x-1" fill="none"
                            viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                d="M13 7l5 5m0 0l-5 5m5-5H6" />
                        </svg>
                    </router-link>
                    <a href="#featured"
                        class="px-8 py-4 bg-white/5 hover:bg-white/10 text-white font-bold rounded-xl backdrop-blur-sm border border-white/10 transition-all hover:border-white/20 flex items-center gap-2">
                        <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                d="M11.049 2.927c.3-.921 1.603-.921 1.902 0l1.519 4.674a1 1 0 00.95.69h4.915c.969 0 1.371 1.24.588 1.81l-3.976 2.888a1 1 0 00-.363 1.118l1.518 4.674c.3.922-.755 1.688-1.538 1.118l-3.976-2.888a1 1 0 00-1.176 0l-3.976 2.888c-.783.57-1.838-.197-1.538-1.118l1.518-4.674a1 1 0 00-.363-1.118l-3.976-2.888c-.784-.57-.38-1.81.588-1.81h4.914a1 1 0 00.951-.69l1.519-4.674z" />
                        </svg>
                        Sản phẩm nổi bật
                    </a>
                </div>

                <!-- Stats Bar -->
                <div class="mt-16 flex items-center justify-center gap-12 hero-fade-in" style="animation-delay: 0.65s">
                    <div class="text-center">
                        <div class="text-3xl font-bold text-white">{{ featuredProducts.length }}+</div>
                        <div class="text-xs text-gray-500 uppercase tracking-widest mt-1">Sản phẩm</div>
                    </div>
                    <div class="w-px h-10 bg-white/10"></div>
                    <div class="text-center">
                        <div class="text-3xl font-bold text-white">{{ domains.length }}</div>
                        <div class="text-xs text-gray-500 uppercase tracking-widest mt-1">Lĩnh vực</div>
                    </div>
                    <div class="w-px h-10 bg-white/10"></div>
                    <div class="text-center">
                        <div class="text-3xl font-bold text-white">100+</div>
                        <div class="text-xs text-gray-500 uppercase tracking-widest mt-1">Sinh viên</div>
                    </div>
                </div>
            </div>

            <!-- Scroll indicator -->
            <div
                class="absolute bottom-8 left-1/2 -translate-x-1/2 flex flex-col items-center gap-2 text-white/30 animate-bounce">
                <span class="text-xs tracking-widest">SCROLL</span>
                <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" />
                </svg>
            </div>
        </section>

        <!-- ═══════════════════════════════════════════════════════════
             2. DOMAIN FILTER CHIPS (Gradient)
        ═══════════════════════════════════════════════════════════ -->
        <section class="py-20 bg-gray-50 dark:bg-gray-900 transition-colors">
            <div class="container mx-auto px-4">
                <div class="text-center mb-12">
                    <h2 class="text-3xl lg:text-4xl font-bold text-gray-900 dark:text-white mb-4">Lĩnh vực hoạt động
                    </h2>
                    <div class="w-24 h-1 bg-gradient-to-r from-violet-500 to-purple-500 mx-auto rounded-full"></div>
                    <p class="text-gray-500 dark:text-gray-400 mt-4">Chọn lĩnh vực để khám phá các dự án liên quan</p>
                </div>

                <!-- Filter Chips with Gradient -->
                <div class="flex flex-wrap justify-center gap-3 mb-12">
                    <button @click="selectedDomain = null" class="domain-chip group"
                        :class="selectedDomain === null ? 'domain-chip-active' : 'domain-chip-inactive'">
                        <span class="text-sm font-bold">Tất cả</span>
                    </button>
                    <button v-for="domain in domains" :key="domain.id" @click="navigateToCategory(domain.id)"
                        class="domain-chip group domain-chip-inactive">
                        <i v-if="domain.icon && !domain.icon.startsWith('http')" :class="domain.icon"
                            class="text-base"></i>
                        <img v-else-if="domain.icon" :src="domain.icon" class="w-4 h-4 object-contain" />
                        <span class="text-sm font-bold">{{ domain.name }}</span>
                        <span class="text-xs opacity-60 ml-1">({{ domain.productCount || 0 }})</span>
                    </button>
                </div>

                <!-- Domain Cards Grid -->
                <div class="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-4">
                    <DomainCard v-for="domain in domains" :key="domain.id" :domain="domain" class="h-full"
                        @click="navigateToCategory(domain.id)" />
                </div>
            </div>
        </section>

        <!-- ═══════════════════════════════════════════════════════════
             3. TECH MARQUEE (Enhanced)
        ═══════════════════════════════════════════════════════════ -->
        <TechnologySlider />

        <!-- ═══════════════════════════════════════════════════════════
             4. DYNAMIC BENTO GRID — Featured Products
        ═══════════════════════════════════════════════════════════ -->
        <section id="featured" class="py-20 bg-white dark:bg-gray-950 transition-colors">
            <div class="container mx-auto px-4">
                <div class="flex items-end justify-between mb-12">
                    <div>
                        <span
                            class="inline-block text-xs font-bold tracking-widest uppercase text-violet-500 mb-2">Featured</span>
                        <h2 class="text-3xl lg:text-4xl font-bold text-gray-900 dark:text-white">Sản phẩm Nổi bật</h2>
                        <p class="text-gray-500 dark:text-gray-400 mt-2">Các dự án được đánh giá cao, có tính ứng dụng
                            thực tế</p>
                    </div>
                    <router-link :to="{ name: ROUTES_CONSTANTS.CUSTOMER.children.APPS.name }"
                        class="hidden md:flex items-center gap-2 text-violet-600 dark:text-violet-400 font-bold hover:gap-3 transition-all px-4 py-2 rounded-lg hover:bg-violet-50 dark:hover:bg-violet-900/20">
                        Xem thêm <span>→</span>
                    </router-link>
                </div>

                <!-- BENTO GRID LAYOUT -->
                <div v-if="featuredProducts.length > 0" class="bento-grid">
                    <!-- Hero card (large 2x2) – first product -->
                    <div class="bento-hero" v-if="featuredProducts[0]">
                        <BentoCard :product="featuredProducts[0]" :large="true" />
                    </div>

                    <!-- Small cards (1x1) -->
                    <div class="bento-small-1" v-if="featuredProducts[1]">
                        <BentoCard :product="featuredProducts[1]" />
                    </div>
                    <div class="bento-small-2" v-if="featuredProducts[2]">
                        <BentoCard :product="featuredProducts[2]" />
                    </div>
                    <div class="bento-small-3" v-if="featuredProducts[3]">
                        <BentoCard :product="featuredProducts[3]" />
                    </div>
                    <div class="bento-small-4" v-if="featuredProducts[4]">
                        <BentoCard :product="featuredProducts[4]" />
                    </div>
                </div>

                <!-- Remaining products in standard grid -->
                <div v-if="featuredProducts.length > 5"
                    class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 mt-6">
                    <ProductCard v-for="product in featuredProducts.slice(5)" :key="product.id" :product="product" />
                </div>

                <!-- Empty state -->
                <div v-if="featuredProducts.length === 0" class="text-center py-20 text-gray-400 dark:text-gray-600">
                    <div class="text-6xl mb-4">✨</div>
                    <p class="text-lg">Chưa có sản phẩm nổi bật</p>
                </div>

                <!-- Mobile View More -->
                <div class="mt-8 text-center md:hidden">
                    <router-link :to="{ name: ROUTES_CONSTANTS.CUSTOMER.children.APPS.name }"
                        class="inline-flex items-center gap-2 text-violet-600 font-semibold">
                        Xem tất cả sản phẩm →
                    </router-link>
                </div>
            </div>
        </section>

        <!-- ═══════════════════════════════════════════════════════════
             5. FEATURED VIDEOS (Enhanced)
        ═══════════════════════════════════════════════════════════ -->
        <section v-if="hasFeaturedVideos" class="py-20 bg-gray-50 dark:bg-gray-900 transition-colors">
            <div class="container mx-auto px-4">
                <div class="text-center mb-12">
                    <span class="inline-block text-xs font-bold tracking-widest uppercase text-amber-500 mb-2">Video
                        Demo</span>
                    <h2 class="text-3xl font-bold text-gray-900 dark:text-white mb-3">Video Nổi Bật</h2>
                    <p class="text-gray-500 dark:text-gray-400">Khám phá các demo video từ những dự án xuất sắc</p>
                    <div class="w-20 h-1 bg-gradient-to-r from-amber-400 to-orange-500 mx-auto rounded mt-4"></div>
                </div>

                <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
                    <div v-for="video in featuredVideos" :key="video.id"
                        class="group cursor-pointer transform transition-all hover:-translate-y-2"
                        @click="$router.push({ name: ROUTES_CONSTANTS.CUSTOMER.children.APP_DETAIL.name, params: { id: encodeId(video.id) } })">
                        <div
                            class="bg-white dark:bg-gray-800 rounded-2xl overflow-hidden shadow-lg border border-gray-100 dark:border-gray-700 hover:shadow-2xl hover:shadow-amber-500/10 transition-all">
                            <div class="relative aspect-video bg-gray-900">
                                <YouTubeEmbed :url="video.videoUrl!" :title="video.name" class="w-full h-full"
                                    :autoplay="true" :mute="true" />
                                <div
                                    class="absolute inset-0 bg-gradient-to-t from-black/60 to-transparent opacity-0 group-hover:opacity-100 transition-opacity flex items-end justify-start p-4">
                                    <span class="text-white font-bold text-sm">Xem chi tiết →</span>
                                </div>
                            </div>
                            <div class="p-5">
                                <h3
                                    class="text-base font-bold text-gray-900 dark:text-white group-hover:text-amber-500 transition-colors line-clamp-1">
                                    {{ video.name }}
                                </h3>
                                <p class="text-sm text-gray-500 dark:text-gray-400 mt-1 line-clamp-2">
                                    {{ video.shortDescription || video.description }}
                                </p>
                                <div class="flex items-center justify-between mt-3 text-xs text-gray-400">
                                    <span class="flex items-center gap-1">
                                        <svg class="w-3.5 h-3.5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                                d="M15 12a3 3 0 11-6 0 3 3 0 016 0zM2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
                                        </svg>
                                        {{ video.viewCount || 0 }} lượt xem
                                    </span>
                                    <span v-if="video.domainName"
                                        class="px-2 py-0.5 bg-amber-50 dark:bg-amber-900/20 text-amber-600 dark:text-amber-400 rounded-full font-semibold">
                                        {{ video.domainName }}
                                    </span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed, onUnmounted } from 'vue';
import { useRouter } from 'vue-router';
import { ROUTES_CONSTANTS } from '@/constants/path';
import DomainCard from '@/components/client/domain/DomainCard.vue';
import ProductCard from '@/components/client/product/ProductCard.vue';
import BentoCard from '@/components/client/home/BentoCard.vue';
import TechnologySlider from '@/components/client/home/TechnologySlider.vue';
import YouTubeEmbed from '@/components/common/YouTubeEmbed.vue';
import { getPublicDomains, getPublicFeaturedProducts, getPublicFeaturedVideos, type PublicDomain, type PublicProduct } from '@/services/client/client.service';
import { encodeId } from '@/utils';

const router = useRouter();
const domains = ref<PublicDomain[]>([]);
const featuredProducts = ref<PublicProduct[]>([]);
const featuredVideos = ref<PublicProduct[]>([]);
const selectedDomain = ref<string | null>(null);
const heroContentRef = ref<HTMLElement | null>(null);

const hasFeaturedVideos = computed(() => {
    return Array.isArray(featuredVideos.value) && featuredVideos.value.length > 0;
});

const navigateToCategory = (id: string) => {
    router.push({ name: ROUTES_CONSTANTS.CUSTOMER.children.APPS.name, query: { domain: id } });
};

// Parallax scroll effect
const handleScroll = () => {
    if (!heroContentRef.value) return;
    const scrollY = window.scrollY;
    heroContentRef.value.style.transform = `translateY(${scrollY * 0.35}px)`;
    heroContentRef.value.style.opacity = String(Math.max(0, 1 - scrollY / 600));
};

onMounted(async () => {
    window.addEventListener('scroll', handleScroll, { passive: true });

    try {
        const [domainsData, productsData, videosData] = await Promise.all([
            getPublicDomains(),
            getPublicFeaturedProducts({ sort: 'FEATURED' }),
            getPublicFeaturedVideos()
        ]);

        domains.value = domainsData || [];

        if (productsData) {
            featuredProducts.value = (productsData as any).data || (productsData as any).content || (Array.isArray(productsData) ? productsData : []);
        } else {
            featuredProducts.value = [];
        }

        const videos = videosData || [];
        if (Array.isArray(videos)) {
            featuredVideos.value = videos.map((v: any) => ({
                ...v,
                videoUrl: v.demoUrl || v.videoUrl
            }));
        } else {
            featuredVideos.value = [];
        }
    } catch (error) {
        console.error("Failed to load home data", error);
        domains.value = [];
        featuredProducts.value = [];
        featuredVideos.value = [];
    }
});

onUnmounted(() => {
    window.removeEventListener('scroll', handleScroll);
});
</script>

<style scoped>
/* ─── Mesh Gradient Hero ─────────────────────────────── */
.mesh-blob {
    position: absolute;
    border-radius: 50%;
    filter: blur(80px);
    opacity: 0.55;
    animation: float-blob linear infinite;
}

.mesh-blob-1 {
    width: 700px;
    height: 700px;
    background: radial-gradient(circle, #7c3aed, transparent 70%);
    top: -200px;
    left: -150px;
    animation-duration: 18s;
    animation-delay: 0s;
}

.mesh-blob-2 {
    width: 600px;
    height: 600px;
    background: radial-gradient(circle, #2563eb, transparent 70%);
    top: 40%;
    right: -100px;
    animation-duration: 22s;
    animation-delay: -6s;
}

.mesh-blob-3 {
    width: 500px;
    height: 500px;
    background: radial-gradient(circle, #a855f7, transparent 70%);
    bottom: -150px;
    left: 30%;
    animation-duration: 16s;
    animation-delay: -10s;
}

.mesh-blob-4 {
    width: 400px;
    height: 400px;
    background: radial-gradient(circle, #06b6d4, transparent 70%);
    top: 20%;
    left: 50%;
    animation-duration: 25s;
    animation-delay: -4s;
}

@keyframes float-blob {

    0%,
    100% {
        transform: translate(0, 0) scale(1);
    }

    25% {
        transform: translate(40px, -60px) scale(1.05);
    }

    50% {
        transform: translate(-40px, 40px) scale(0.95);
    }

    75% {
        transform: translate(60px, 60px) scale(1.03);
    }
}

/* ─── Floating text ──────────────────────────────────── */
.floating-text {
    animation: gentle-float 6s ease-in-out infinite;
}

@keyframes gentle-float {

    0%,
    100% {
        transform: translateY(0px);
    }

    50% {
        transform: translateY(-10px);
    }
}

/* ─── Particles ──────────────────────────────────────── */
.particle {
    position: absolute;
    width: 2px;
    height: 2px;
    background: rgba(167, 139, 250, 0.6);
    border-radius: 50%;
    animation: particle-rise linear infinite;
}

@keyframes particle-rise {
    0% {
        transform: translateY(100px) scale(0);
        opacity: 0;
    }

    10% {
        opacity: 1;
    }

    90% {
        opacity: 0.5;
    }

    100% {
        transform: translateY(-200px) scale(1.5);
        opacity: 0;
    }
}

/* ─── Hero fade-in ──────────────────────────────────── */
.hero-fade-in {
    opacity: 0;
    transform: translateY(30px);
    animation: fade-up 0.8s cubic-bezier(0.22, 1, 0.36, 1) forwards;
}

@keyframes fade-up {
    to {
        opacity: 1;
        transform: translateY(0);
    }
}

/* ─── Domain Filter Chips ───────────────────────────── */
.domain-chip {
    display: inline-flex;
    align-items: center;
    gap: 6px;
    padding: 8px 18px;
    border-radius: 9999px;
    border: 1px solid;
    font-weight: 600;
    transition: all 0.25s ease;
    cursor: pointer;
}

.domain-chip-active {
    background: linear-gradient(135deg, #7c3aed, #6d28d9);
    border-color: #7c3aed;
    color: white;
    box-shadow: 0 0 20px rgba(124, 58, 237, 0.4);
}

.domain-chip-inactive {
    background: white;
    border-color: #e5e7eb;
    color: #374151;
}

:global(.dark) .domain-chip-inactive {
    background: transparent;
    border-color: #374151;
    color: #d1d5db;
}

.domain-chip-inactive:hover {
    border-color: #7c3aed;
    color: #7c3aed;
    background: #f5f3ff;
    box-shadow: 0 0 15px rgba(124, 58, 237, 0.15);
}

:global(.dark) .domain-chip-inactive:hover {
    background: rgba(124, 58, 237, 0.1);
    border-color: #7c3aed;
    color: #a78bfa;
}

/* ─── Bento Grid ─────────────────────────────────────── */
.bento-grid {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    grid-template-rows: auto auto;
    gap: 16px;
}

.bento-hero {
    grid-column: 1 / 3;
    grid-row: 1 / 3;
}

.bento-small-1 {
    grid-column: 3 / 4;
    grid-row: 1 / 2;
}

.bento-small-2 {
    grid-column: 4 / 5;
    grid-row: 1 / 2;
}

.bento-small-3 {
    grid-column: 3 / 4;
    grid-row: 2 / 3;
}

.bento-small-4 {
    grid-column: 4 / 5;
    grid-row: 2 / 3;
}

@media (max-width: 1024px) {
    .bento-grid {
        grid-template-columns: 1fr 1fr;
    }

    .bento-hero {
        grid-column: 1 / 3;
        grid-row: 1 / 2;
    }

    .bento-small-1 {
        grid-column: 1 / 2;
        grid-row: 2 / 3;
    }

    .bento-small-2 {
        grid-column: 2 / 3;
        grid-row: 2 / 3;
    }

    .bento-small-3 {
        grid-column: 1 / 2;
        grid-row: 3 / 4;
    }

    .bento-small-4 {
        grid-column: 2 / 3;
        grid-row: 3 / 4;
    }
}

@media (max-width: 640px) {
    .bento-grid {
        grid-template-columns: 1fr;
    }

    .bento-hero,
    .bento-small-1,
    .bento-small-2,
    .bento-small-3,
    .bento-small-4 {
        grid-column: 1;
        grid-row: auto;
    }
}
</style>