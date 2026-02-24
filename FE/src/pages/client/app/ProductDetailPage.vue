<script setup lang="ts">
import { ref, onMounted, computed, onUnmounted } from 'vue';
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
            incrementViewCount(id);
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
        relatedProducts.value = [];
    }
};

// ── Feature Detail Modal ──
const selectedFeature = ref<{
    id: string;
    name: string;
    description: string;
    imagePreview?: string;
    videoUrl?: string;
} | null>(null);

const openFeatureModal = (feature: any) => {
    if (!feature) return;
    selectedFeature.value = feature;
    document.body.style.overflow = 'hidden';
};

const closeFeatureModal = () => {
    selectedFeature.value = null;
    document.body.style.overflow = '';
};

const handleEsc = (e: KeyboardEvent) => {
    if (e.key === 'Escape') closeFeatureModal();
};

onMounted(() => {
    fetchProduct();
    window.addEventListener('keydown', handleEsc);
});

onUnmounted(() => {
    window.removeEventListener('keydown', handleEsc);
    document.body.style.overflow = '';
});

const sortedFeatures = computed(() => product.value?.features || []);
const isYouTubeUrl = (url?: string) => {
    if (!url) return false;
    return /(?:youtube\.com\/watch\?v=|youtu\.be\/|youtube\.com\/embed\/)([^&\n?#]+)/.test(url);
};
const isVideo = (url?: string) => {
    if (!url) return false;
    return /\.(mp4|webm|ogg|mov)$/i.test(url);
};

// Tech badge color
const techColors: Record<string, string> = {
    'Java': '#f89820',
    'Spring Boot': '#6db33f',
    'Spring': '#6db33f',
    'Vue': '#42b883',
    'Vue.js': '#42b883',
    'React': '#61dafb',
    'Angular': '#dd0031',
    'MySQL': '#4479a1',
    'PostgreSQL': '#336791',
    'MongoDB': '#4ea94b',
    'Docker': '#2496ed',
    'Node.js': '#339933',
    'TypeScript': '#3178c6',
    'JavaScript': '#f7df1e',
    'Python': '#3776ab',
    'C#': '#239120',
    '.NET': '#512bd4',
    'Redis': '#dc382d',
};
const getTechColor = (name: string) => techColors[name] || '#6366f1';
</script>

<template>
    <div class="product-detail-page-wrapper">
        <!-- Loading -->
        <div v-if="isLoading" class="min-h-screen flex items-center justify-center bg-[#0a0a1a]">
            <div class="relative">
                <div class="w-16 h-16 rounded-full border-4 border-violet-500/20 border-t-violet-500 animate-spin">
                </div>
                <div class="absolute inset-0 flex items-center justify-center text-violet-400 text-xs font-bold">...
                </div>
            </div>
        </div>

        <!-- Main page -->
        <div v-else-if="product" class="min-h-screen bg-gray-50 dark:bg-gray-950 pb-20 transition-colors duration-300">

            <!-- ════════════════════════════════════════════════
             1. HERO — Mesh Gradient + Image BG + Glow Buttons
        ════════════════════════════════════════════════ -->
            <section class="relative min-h-[88vh] flex items-center justify-center overflow-hidden">

                <!-- Background: project image blurred -->
                <div class="absolute inset-0">
                    <img v-if="product.thumbnail" :src="product.thumbnail"
                        class="w-full h-full object-cover scale-110 blur-sm opacity-25" />
                    <div class="absolute inset-0 bg-[#060614]"
                        :class="product.thumbnail ? 'opacity-70' : 'opacity-100'"></div>
                </div>

                <!-- Mesh blobs -->
                <div class="absolute inset-0 overflow-hidden pointer-events-none">
                    <div class="hero-blob hero-blob-1"></div>
                    <div class="hero-blob hero-blob-2"></div>
                    <div class="hero-blob hero-blob-3"></div>
                </div>
                <!-- Grid -->
                <div class="absolute inset-0"
                    style="background-image: linear-gradient(rgba(255,255,255,0.025) 1px,transparent 1px),linear-gradient(90deg,rgba(255,255,255,0.025) 1px,transparent 1px);background-size:60px 60px;">
                </div>

                <!-- Content -->
                <div class="container mx-auto px-4 relative z-10 text-center pt-24 pb-16">

                    <!-- Domain chip -->
                    <div v-if="product.domainName"
                        class="inline-flex items-center gap-2 px-5 py-2 mb-6 rounded-full border border-violet-500/40 bg-violet-500/10 backdrop-blur-md text-xs font-bold tracking-widest uppercase text-violet-300">
                        <span class="w-1.5 h-1.5 bg-violet-400 rounded-full animate-pulse"></span>
                        {{ product.domainName }}
                    </div>

                    <!-- Title -->
                    <h1 class="text-4xl md:text-6xl lg:text-7xl font-bold text-white mb-6 leading-tight tracking-tight">
                        {{ product.name }}
                    </h1>

                    <!-- Subtitle -->
                    <p class="text-lg md:text-xl text-gray-300 mb-10 max-w-2xl mx-auto leading-relaxed font-light">
                        {{ product.shortDescription || product.description }}
                    </p>

                    <!-- Glow CTAs -->
                    <div class="flex flex-wrap items-center justify-center gap-4 mb-10">
                        <a v-if="product.demoUrl" :href="product.demoUrl" target="_blank"
                            class="glow-btn glow-btn-primary">
                            <svg class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M10 6H6a2 2 0 00-2 2v10a2 2 0 002 2h10a2 2 0 002-2v-4M14 4h6m0 0v6m0-6L10 14" />
                            </svg>
                            View Demo
                        </a>
                        <a v-if="product.sourceUrl" :href="product.sourceUrl" target="_blank"
                            class="glow-btn glow-btn-ghost">
                            <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 24 24">
                                <path fill-rule="evenodd"
                                    d="M12 2C6.477 2 2 6.484 2 12.017c0 4.425 2.865 8.18 6.839 9.504.5.092.682-.217.682-.483 0-.237-.008-.868-.013-1.703-2.782.605-3.369-1.343-3.369-1.343-.454-1.158-1.11-1.466-1.11-1.466-.908-.62.069-.608.069-.608 1.003.07 1.531 1.032 1.531 1.032.892 1.53 2.341 1.088 2.91.832.092-.647.35-1.088.636-1.338-2.22-.253-4.555-1.113-4.555-4.951 0-1.093.39-1.988 1.029-2.688-.103-.253-.446-1.272.098-2.65 0 0 .84-.27 2.75 1.026A9.564 9.564 0 0112 6.844c.85.004 1.705.115 2.504.337 1.909-1.296 2.747-1.027 2.747-1.027.546 1.379.202 2.398.1 2.651.64.7 1.028 1.595 1.028 2.688 0 3.848-2.339 4.695-4.566 4.943.359.309.678.92.678 1.855 0 1.338-.012 2.419-.012 2.747 0 .268.18.58.688.482A10.019 10.019 0 0022 12.017C22 6.484 17.522 2 12 2z"
                                    clip-rule="evenodd" />
                            </svg>
                            Source Code
                        </a>
                        <LikeButton v-if="product.id" :product-id="product.id" :show-count="true"
                            class="glow-btn glow-btn-like" />
                    </div>

                    <!-- Tech badges in hero -->
                    <div class="flex flex-wrap justify-center gap-2">
                        <span v-for="tech in product.technologies" :key="tech.id"
                            class="inline-flex items-center gap-1.5 px-3 py-1.5 rounded-full text-xs font-semibold border border-white/10 bg-white/5 backdrop-blur-sm text-gray-300 hover:bg-white/10 transition-colors">
                            <img v-if="tech.icon" :src="tech.icon" class="w-4 h-4 object-contain rounded-full"
                                @error="($event.target as HTMLImageElement).style.display = 'none'" />
                            {{ tech.name }}
                        </span>
                    </div>

                    <!-- Share -->
                    <div class="mt-8 flex justify-center opacity-60 hover:opacity-100 transition-opacity">
                        <ShareButtons :title="product.name" :description="product.shortDescription"
                            :show-label="true" />
                    </div>
                </div>
            </section>

            <!-- ════════════════════════════════════════════════
             2. VIDEO — Glassmorphism Frame + 3D Perspective
        ════════════════════════════════════════════════ -->
            <div v-if="product.demoUrl && isYouTubeUrl(product.demoUrl)"
                class="relative -mt-16 z-20 container mx-auto px-4 mb-20">
                <div class="perspective-wrap max-w-5xl mx-auto">
                    <div class="video-glass-frame">
                        <!-- Outer glow ring -->
                        <div class="video-glow-ring"></div>
                        <!-- Glassmorphism border -->
                        <div class="video-glass-inner">
                            <!-- Dots header bar (macOS style) -->
                            <div class="flex items-center gap-2 px-5 py-3 border-b border-white/10">
                                <div class="w-3 h-3 rounded-full bg-red-400/80"></div>
                                <div class="w-3 h-3 rounded-full bg-yellow-400/80"></div>
                                <div class="w-3 h-3 rounded-full bg-green-400/80"></div>
                                <div class="flex-1 mx-4">
                                    <div
                                        class="bg-white/10 rounded-full px-4 py-1 text-xs text-gray-400 truncate text-center">
                                        {{ product.demoUrl }}
                                    </div>
                                </div>
                            </div>
                            <!-- Video -->
                            <div class="aspect-video bg-black">
                                <YouTubeEmbed :url="product.demoUrl" :title="product.name" class="w-full h-full" />
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- ════════════════════════════════════════════════
             3. MAIN CONTENT GRID
        ════════════════════════════════════════════════ -->
            <main class="container mx-auto px-4">
                <div class="grid grid-cols-1 lg:grid-cols-3 gap-10">

                    <!-- LEFT — Overview + Features -->
                    <div class="lg:col-span-2 space-y-12">

                        <!-- Overview Card -->
                        <div
                            class="bg-white dark:bg-gray-900 rounded-2xl shadow-sm border border-gray-100 dark:border-gray-800 p-8">
                            <h2
                                class="text-2xl font-bold text-gray-900 dark:text-white mb-6 flex items-center gap-3 pb-4 border-b border-gray-100 dark:border-gray-800">
                                <span
                                    class="w-8 h-8 rounded-lg bg-violet-100 dark:bg-violet-900/30 flex items-center justify-center text-violet-600 dark:text-violet-400">
                                    <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                            d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
                                    </svg>
                                </span>
                                Tổng quan dự án
                            </h2>
                            <div
                                class="prose prose-gray dark:prose-invert prose-lg max-w-none whitespace-pre-line text-gray-600 dark:text-gray-300 leading-relaxed">
                                {{ product.longDescription || product.description }}
                            </div>

                            <!-- Specs -->
                            <div v-if="product.specifications"
                                class="mt-8 pt-8 border-t border-gray-100 dark:border-gray-800">
                                <h3 class="text-lg font-bold text-gray-900 dark:text-white mb-4">Thông số kỹ thuật</h3>
                                <div class="grid grid-cols-1 md:grid-cols-2 gap-3">
                                    <div v-for="(val, key) in product.specifications" :key="key"
                                        class="flex justify-between items-center py-3 px-4 bg-gray-50 dark:bg-gray-800 rounded-lg">
                                        <span class="text-gray-500 dark:text-gray-400 text-sm capitalize">{{ key
                                            }}</span>
                                        <span class="text-gray-900 dark:text-white text-sm font-semibold">{{ val
                                            }}</span>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- ── FEATURES — ZigZag Alternating ── -->
                        <div v-if="sortedFeatures.length > 0">
                            <h2
                                class="text-2xl font-bold text-gray-900 dark:text-white mb-10 flex items-center gap-3 pb-4 border-b border-gray-100 dark:border-gray-800">
                                <span
                                    class="w-8 h-8 rounded-lg bg-violet-100 dark:bg-violet-900/30 flex items-center justify-center text-violet-600 dark:text-violet-400">
                                    <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                            d="M13 10V3L4 14h7v7l9-11h-7z" />
                                    </svg>
                                </span>
                                Chức năng nổi bật
                            </h2>

                            <div class="space-y-10">
                                <div v-for="(feature, idx) in sortedFeatures" :key="feature.id || idx"
                                    class="feature-card group"
                                    :class="idx % 2 !== 0 ? 'md:flex-row-reverse' : 'md:flex-row'">

                                    <!-- Text side -->
                                    <div class="flex-1 flex flex-col justify-center p-6 md:p-8">
                                        <div class="inline-flex items-center gap-2 mb-4">
                                            <span
                                                class="px-3 py-1 rounded-full bg-violet-100 dark:bg-violet-900/30 text-violet-700 dark:text-violet-300 text-xs font-bold uppercase tracking-widest">
                                                Feature {{ idx + 1 }}
                                            </span>
                                        </div>
                                        <h3 class="text-xl font-bold text-gray-900 dark:text-white mb-3">{{ feature.name
                                            }}</h3>
                                        <p class="text-gray-500 dark:text-gray-400 leading-relaxed mb-6">{{
                                            feature.description }}</p>
                                        <button @click.stop="openFeatureModal(feature)"
                                            class="self-start inline-flex items-center gap-2 px-5 py-2.5 rounded-lg text-sm font-bold text-violet-600 dark:text-violet-400 border border-violet-200 dark:border-violet-800 hover:bg-violet-50 dark:hover:bg-violet-900/20 transition-all hover:gap-3 group/btn">
                                            Tìm hiểu thêm
                                            <svg class="w-4 h-4 transition-transform group-hover/btn:translate-x-0.5"
                                                fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                                    d="M13 7l5 5m0 0l-5 5m5-5H6" />
                                            </svg>
                                        </button>
                                    </div>

                                    <!-- Media side -->
                                    <div class="flex-1">
                                        <div class="feature-media-wrap group">
                                            <YouTubeEmbed v-if="feature.videoUrl && isYouTubeUrl(feature.videoUrl)"
                                                :url="feature.videoUrl" :title="feature.name"
                                                class="w-full h-full absolute inset-0" />
                                            <video v-else-if="feature.videoUrl && isVideo(feature.videoUrl)"
                                                :src="feature.videoUrl" controls
                                                class="w-full h-full object-contain bg-black absolute inset-0"></video>
                                            <img v-else-if="feature.imagePreview" :src="feature.imagePreview"
                                                class="w-full h-full object-cover absolute inset-0 transition-transform duration-700 group-hover:scale-105" />
                                            <!-- Gradient placeholder with tech logos -->
                                            <div v-else class="absolute inset-0 feature-placeholder">
                                                <div class="feature-placeholder-bg"></div>
                                                <div
                                                    class="absolute inset-0 flex flex-col items-center justify-center gap-4 z-10">
                                                    <div class="flex -space-x-3">
                                                        <div v-for="(tech, ti) in (product?.technologies || []).slice(0, 3)"
                                                            :key="ti"
                                                            class="w-12 h-12 rounded-full border-2 border-white/20 bg-white/10 backdrop-blur-md flex items-center justify-center overflow-hidden shadow-lg">
                                                            <img v-if="tech.icon" :src="tech.icon"
                                                                class="w-8 h-8 object-contain"
                                                                @error="($event.target as HTMLImageElement).style.display = 'none'" />
                                                        </div>
                                                    </div>
                                                    <span class="text-white/60 text-sm font-medium">{{ feature.name
                                                        }}</span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Bento Stats Grid -->
                        <div class="grid grid-cols-2 md:grid-cols-4 gap-4">
                            <div class="bento-stat">
                                <div class="bento-stat-value">{{ product.viewCount || 0 }}</div>
                                <div class="bento-stat-label">Lượt xem</div>
                            </div>
                            <div class="bento-stat">
                                <div class="bento-stat-value">{{ product.sku || '—' }}</div>
                                <div class="bento-stat-label">Mã dự án</div>
                            </div>
                            <div class="bento-stat">
                                <div class="bento-stat-value">{{ (product.teamMembers || []).length }}</div>
                                <div class="bento-stat-label">Thành viên</div>
                            </div>
                            <div class="bento-stat">
                                <div class="bento-stat-value">{{ (product.technologies || []).length }}</div>
                                <div class="bento-stat-label">Công nghệ</div>
                            </div>
                        </div>
                    </div>

                    <!-- RIGHT COLUMN — Team + Meta -->
                    <div class="space-y-6">

                        <!-- ── Team Card with Avatar Stack ── -->
                        <div class="side-card sticky top-24">
                            <h3
                                class="text-base font-bold text-gray-900 dark:text-white mb-5 pb-3 border-b border-gray-100 dark:border-gray-800 flex items-center gap-2">
                                <svg class="w-4 h-4 text-violet-500" fill="none" viewBox="0 0 24 24"
                                    stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                        d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0z" />
                                </svg>
                                Thành viên thực hiện
                            </h3>

                            <!-- Avatar Stack (when >3 members) -->
                            <div v-if="(product.teamMembers || []).length > 0">
                                <div class="flex -space-x-3 mb-4" v-if="product.teamMembers.length > 1">
                                    <div v-for="(member, idx) in product.teamMembers.slice(0, 5)" :key="member.id"
                                        :title="member.fullName"
                                        class="w-10 h-10 rounded-full border-2 border-white dark:border-gray-900 flex items-center justify-center font-bold text-sm shadow-sm cursor-default"
                                        :style="`background: hsl(${(idx * 67) % 360}, 65%, 55%); color: white; z-index:${10 - idx}`">
                                        {{ member.fullName.charAt(0) }}
                                    </div>
                                    <div v-if="product.teamMembers.length > 5"
                                        class="w-10 h-10 rounded-full border-2 border-white dark:border-gray-900 bg-gray-200 dark:bg-gray-700 flex items-center justify-center text-xs font-bold text-gray-600 dark:text-gray-300">
                                        +{{ product.teamMembers.length - 5 }}
                                    </div>
                                </div>

                                <!-- Full list -->
                                <div class="space-y-3 mt-4">
                                    <div v-for="member in product.teamMembers" :key="member.id"
                                        class="flex items-center gap-3 p-3 rounded-lg hover:bg-gray-50 dark:hover:bg-gray-800 transition-colors">
                                        <div class="w-9 h-9 rounded-full flex items-center justify-center font-bold text-sm text-white flex-shrink-0 shadow-sm"
                                            :style="`background: hsl(${member.fullName.charCodeAt(0) * 37 % 360}, 65%, 55%)`">
                                            {{ member.fullName.charAt(0) }}
                                        </div>
                                        <div class="flex-1 min-w-0">
                                            <div class="text-sm font-bold text-gray-900 dark:text-white truncate">{{
                                                member.fullName }}</div>
                                            <div class="text-xs" :class="member.role === 'LEADER'
                                                ? 'text-amber-600 dark:text-amber-400 font-bold'
                                                : 'text-gray-400'">
                                                {{ member.role === 'LEADER' ? '👑 Leader' : 'Developer' }}
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div v-else class="text-gray-400 dark:text-gray-600 text-sm italic text-center py-4">
                                Chưa cập nhật thành viên
                            </div>

                            <!-- ── Tech Stack Badges ── -->
                            <div v-if="(product.technologies || []).length > 0"
                                class="mt-6 pt-6 border-t border-gray-100 dark:border-gray-800">
                                <h4 class="text-sm font-bold text-gray-700 dark:text-gray-300 mb-3">Stack công nghệ</h4>
                                <div class="flex flex-wrap gap-2">
                                    <span v-for="tech in product.technologies" :key="tech.id"
                                        class="inline-flex items-center gap-1.5 px-3 py-1.5 rounded-lg text-xs font-bold text-white shadow-sm"
                                        :style="`background: ${getTechColor(tech.name)}; box-shadow: 0 2px 8px ${getTechColor(tech.name)}40`">
                                        <img v-if="tech.icon" :src="tech.icon" class="w-3.5 h-3.5 object-contain"
                                            @error="($event.target as HTMLImageElement).style.display = 'none'" />
                                        {{ tech.name }}
                                    </span>
                                </div>
                            </div>

                            <!-- Share -->
                            <div class="mt-6 pt-6 border-t border-gray-100 dark:border-gray-800">
                                <ShareButtons :title="product.name" :description="product.shortDescription" />
                            </div>
                        </div>

                    </div>
                </div>

                <!-- Reviews -->
                <div class="max-w-4xl mx-auto mt-16">
                    <ReviewSection :app-id="product.id" />
                </div>

                <!-- Related Products -->
                <div v-if="relatedProducts.length > 0" class="max-w-6xl mx-auto mt-20 pb-10">
                    <h2
                        class="text-2xl font-bold text-gray-900 dark:text-white mb-8 text-center flex items-center justify-center gap-3">
                        <span class="flex-1 h-px bg-gradient-to-r from-transparent to-gray-200 dark:to-gray-800"></span>
                        Sản phẩm liên quan
                        <span class="flex-1 h-px bg-gradient-to-l from-transparent to-gray-200 dark:to-gray-800"></span>
                    </h2>
                    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                        <router-link v-for="rp in relatedProducts" :key="rp.id" :to="`/apps/${encodeId(rp.id)}`"
                            class="group bg-white dark:bg-gray-900 rounded-xl shadow-sm hover:shadow-xl transition-all border border-gray-100 dark:border-gray-800 hover:border-violet-300 dark:hover:border-violet-700 overflow-hidden">
                            <div class="aspect-video bg-gray-100 dark:bg-gray-800 overflow-hidden relative">
                                <img v-if="rp.thumbnail" :src="rp.thumbnail" :alt="rp.name"
                                    class="w-full h-full object-cover group-hover:scale-110 transition-transform duration-500" />
                                <div v-else
                                    class="w-full h-full flex items-center justify-center text-gray-300 dark:text-gray-600">
                                    <svg class="w-10 h-10" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5"
                                            d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
                                    </svg>
                                </div>
                            </div>
                            <div class="p-4">
                                <div v-if="rp.domainName"
                                    class="text-xs font-bold text-violet-600 dark:text-violet-400 mb-1 uppercase tracking-wider">
                                    {{ rp.domainName }}
                                </div>
                                <h3
                                    class="text-base font-bold text-gray-900 dark:text-white group-hover:text-violet-600 transition-colors line-clamp-1">
                                    {{ rp.name }}
                                </h3>
                                <p class="text-xs text-gray-500 dark:text-gray-400 mt-1 line-clamp-2">{{
                                    rp.shortDescription }}</p>
                            </div>
                        </router-link>
                    </div>
                </div>
            </main>

        </div>

        <!-- 404 -->
        <div v-else class="min-h-screen flex flex-col items-center justify-center text-center">
            <div class="text-6xl mb-4">🔍</div>
            <h2 class="text-2xl font-bold text-gray-900 dark:text-white mb-2">Không tìm thấy sản phẩm</h2>
            <p class="text-gray-500 mb-6">Sản phẩm này có thể đã bị xoá hoặc không tồn tại.</p>
            <BaseButton @click="$router.push('/')" variant="primary">Về trang chủ</BaseButton>
        </div>

        <!-- ════════════════════════════════════════════════
         FEATURE DETAIL MODAL (Teleported)
    ════════════════════════════════════════════════ -->
        <Teleport to="body">
            <Transition name="modal">
                <div v-if="selectedFeature" class="feature-modal-backdrop" @click.self="closeFeatureModal">
                    <div class="feature-modal-box">
                        <!-- Header -->
                        <div class="feature-modal-header">
                            <div class="flex items-center gap-3">
                                <span
                                    class="px-3 py-1 rounded-full bg-violet-100 dark:bg-violet-900/40 text-violet-600 dark:text-violet-300 text-xs font-bold uppercase tracking-widest">
                                    Chi tiết chức năng
                                </span>
                            </div>
                            <button @click="closeFeatureModal" class="feature-modal-close">
                                <svg class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                        d="M6 18L18 6M6 6l12 12" />
                                </svg>
                            </button>
                        </div>

                        <!-- Media Preview -->
                        <div class="feature-modal-media">
                            <!-- YouTube -->
                            <YouTubeEmbed v-if="selectedFeature.videoUrl && isYouTubeUrl(selectedFeature.videoUrl)"
                                :url="selectedFeature.videoUrl" :title="selectedFeature.name" class="w-full h-full" />
                            <!-- Direct video -->
                            <video v-else-if="selectedFeature.videoUrl && isVideo(selectedFeature.videoUrl)"
                                :src="selectedFeature.videoUrl" controls
                                class="w-full h-full object-contain bg-black"></video>
                            <!-- Image -->
                            <img v-else-if="selectedFeature.imagePreview" :src="selectedFeature.imagePreview"
                                :alt="selectedFeature.name" class="w-full h-full object-contain" />
                            <!-- Gradient placeholder -->
                            <div v-else class="feature-modal-placeholder">
                                <div class="feature-modal-placeholder-inner">
                                    <div class="modal-blob modal-blob-1"></div>
                                    <div class="modal-blob modal-blob-2"></div>
                                    <div class="relative z-10 text-center">
                                        <div
                                            class="w-20 h-20 rounded-2xl bg-white/10 backdrop-blur-sm flex items-center justify-center mx-auto mb-4 border border-white/20">
                                            <svg class="w-10 h-10 text-white/50" fill="none" viewBox="0 0 24 24"
                                                stroke="currentColor">
                                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5"
                                                    d="M13 10V3L4 14h7v7l9-11h-7z" />
                                            </svg>
                                        </div>
                                        <p class="text-white/50 text-sm">Chưa có preview</p>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Info -->
                        <div class="feature-modal-body">
                            <h2 class="text-2xl font-bold text-gray-900 dark:text-white mb-3">{{ selectedFeature.name }}
                            </h2>
                            <p class="text-gray-600 dark:text-gray-300 leading-relaxed text-base">{{
                                selectedFeature.description }}</p>
                        </div>

                        <!-- Footer -->
                        <div class="feature-modal-footer">
                            <span class="text-xs text-gray-400 flex items-center gap-1.5">
                                <kbd
                                    class="px-2 py-0.5 rounded bg-gray-100 dark:bg-gray-700 text-gray-500 dark:text-gray-400 font-mono text-[10px]">ESC</kbd>
                                để đóng
                            </span>
                            <button @click="closeFeatureModal"
                                class="inline-flex items-center gap-2 px-4 py-2 rounded-lg text-sm font-bold text-violet-600 dark:text-violet-400 border border-violet-200 dark:border-violet-800 hover:bg-violet-50 dark:hover:bg-violet-900/20 transition-colors">
                                Đóng lại
                            </button>
                        </div>
                    </div>
                </div>
            </Transition>
        </Teleport>
    </div>
</template>

<style scoped>
/* ─── Hero Blobs ─────────────────────────────── */
.hero-blob {
    position: absolute;
    border-radius: 50%;
    filter: blur(100px);
    opacity: 0.45;
    animation: blob-drift ease-in-out infinite alternate;
}

.hero-blob-1 {
    width: 600px;
    height: 600px;
    background: radial-gradient(circle, #7c3aed, transparent 70%);
    top: -100px;
    left: -100px;
    animation-duration: 12s;
}

.hero-blob-2 {
    width: 500px;
    height: 500px;
    background: radial-gradient(circle, #2563eb, transparent 70%);
    bottom: -80px;
    right: -80px;
    animation-duration: 16s;
    animation-delay: -5s;
}

.hero-blob-3 {
    width: 400px;
    height: 400px;
    background: radial-gradient(circle, #a855f7, transparent 70%);
    top: 40%;
    left: 50%;
    animation-duration: 20s;
    animation-delay: -8s;
}

@keyframes blob-drift {
    0% {
        transform: translate(0, 0) scale(1);
    }

    100% {
        transform: translate(40px, 40px) scale(1.1);
    }
}

/* ─── Glow Buttons ───────────────────────────── */
.glow-btn {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    gap: 8px;
    padding: 14px 28px;
    border-radius: 12px;
    font-size: 15px;
    font-weight: 700;
    transition: all 0.3s ease;
    cursor: pointer;
    text-decoration: none;
}

.glow-btn-primary {
    background: linear-gradient(135deg, #7c3aed, #6d28d9);
    color: white;
    border: 1px solid rgba(124, 58, 237, 0.5);
    box-shadow: 0 0 20px rgba(124, 58, 237, 0.4), 0 4px 15px rgba(124, 58, 237, 0.3);
}

.glow-btn-primary:hover {
    box-shadow: 0 0 40px rgba(124, 58, 237, 0.7), 0 8px 25px rgba(124, 58, 237, 0.4);
    transform: translateY(-2px) scale(1.02);
}

.glow-btn-ghost {
    background: rgba(255, 255, 255, 0.05);
    color: white;
    border: 1px solid rgba(255, 255, 255, 0.2);
    backdrop-filter: blur(8px);
    box-shadow: 0 0 15px rgba(255, 255, 255, 0.1), inset 0 1px 0 rgba(255, 255, 255, 0.1);
}

.glow-btn-ghost:hover {
    background: rgba(255, 255, 255, 0.1);
    border-color: rgba(255, 255, 255, 0.4);
    box-shadow: 0 0 25px rgba(255, 255, 255, 0.2);
    transform: translateY(-2px);
}

.glow-btn-like {
    background: rgba(239, 68, 68, 0.1);
    color: #f87171;
    border: 1px solid rgba(239, 68, 68, 0.3);
    box-shadow: 0 0 15px rgba(239, 68, 68, 0.2);
}

.glow-btn-like:hover {
    box-shadow: 0 0 30px rgba(239, 68, 68, 0.4);
    transform: translateY(-2px);
}

/* ─── Video Glassmorphism ────────────────────── */
.perspective-wrap {
    perspective: 1200px;
}

.video-glass-frame {
    position: relative;
    transform: rotateX(3deg);
    transform-style: preserve-3d;
    transition: transform 0.5s ease;
}

.video-glass-frame:hover {
    transform: rotateX(0deg);
}

.video-glow-ring {
    position: absolute;
    inset: -3px;
    border-radius: 28px;
    background: linear-gradient(135deg, #7c3aed, #2563eb, #a855f7, #7c3aed);
    background-size: 300% 300%;
    animation: ring-rotate 4s linear infinite;
    filter: blur(2px);
    opacity: 0.8;
}

@keyframes ring-rotate {
    0% {
        background-position: 0% 50%;
    }

    50% {
        background-position: 100% 50%;
    }

    100% {
        background-position: 0% 50%;
    }
}

.video-glass-inner {
    position: relative;
    border-radius: 24px;
    overflow: hidden;
    background: rgba(15, 15, 30, 0.9);
    backdrop-filter: blur(20px);
    border: 1px solid rgba(255, 255, 255, 0.1);
    box-shadow: 0 40px 80px rgba(0, 0, 0, 0.5), inset 0 1px 0 rgba(255, 255, 255, 0.1);
}

/* ─── Feature Cards ──────────────────────────── */
.feature-card {
    display: flex;
    flex-direction: column;
    background: rgba(255, 255, 255, 0.03);
    border: 1px solid rgba(124, 58, 237, 0.1);
    border-radius: 20px;
    overflow: hidden;
    transition: all 0.4s ease;
    position: relative;
}

.feature-card::before {
    content: '';
    position: absolute;
    inset: 0;
    border-radius: 20px;
    background: linear-gradient(135deg, rgba(124, 58, 237, 0.05), transparent);
    opacity: 0;
    transition: opacity 0.4s;
    pointer-events: none;
}

.feature-card:hover {
    border-color: rgba(124, 58, 237, 0.4);
    box-shadow: 0 0 30px rgba(124, 58, 237, 0.1), 0 20px 40px rgba(0, 0, 0, 0.1);
}

.feature-card:hover::before {
    opacity: 1;
}

:global(.dark) .feature-card {
    background: rgba(17, 24, 39, 0.8);
}

@media (min-width: 768px) {
    .feature-card {
        flex-direction: row;
    }
}

.feature-media-wrap {
    position: relative;
    width: 100%;
    aspect-ratio: 16 / 10;
    overflow: hidden;
    background: #111;
}

@media (min-width: 768px) {
    .feature-media-wrap {
        min-height: 280px;
        aspect-ratio: auto;
    }
}

.feature-placeholder {
    background: #0a0a1a;
}

.feature-placeholder-bg {
    position: absolute;
    inset: 0;
    background: linear-gradient(135deg, #1e1b4b, #0f172a);
    animation: placeholder-shift 6s ease-in-out infinite alternate;
}

@keyframes placeholder-shift {
    from {
        background-position: 0% 50%;
    }

    to {
        background-position: 100% 50%;
    }
}

/* ─── Bento Stats ────────────────────────────── */
.bento-stat {
    background: white;
    border: 1px solid #e5e7eb;
    border-radius: 16px;
    padding: 20px;
    text-align: center;
    transition: all 0.3s ease;
}

.bento-stat:hover {
    border-color: #7c3aed;
    box-shadow: 0 0 20px rgba(124, 58, 237, 0.1);
    transform: translateY(-2px);
}

:global(.dark) .bento-stat {
    background: #111827;
    border-color: #1f2937;
}

.bento-stat-value {
    font-size: 28px;
    font-weight: 800;
    color: #7c3aed;
    letter-spacing: -0.02em;
    line-height: 1;
    margin-bottom: 6px;
}

.bento-stat-label {
    font-size: 11px;
    font-weight: 600;
    text-transform: uppercase;
    letter-spacing: 0.08em;
    color: #9ca3af;
}

/* ─── Side Card ──────────────────────────────── */
.side-card {
    background: white;
    border: 1px solid #e5e7eb;
    border-radius: 20px;
    padding: 24px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.04);
}

:global(.dark) .side-card {
    background: #111827;
    border-color: #1f2937;
}

/* ─── Feature Modal ────────────────────────── */
.feature-modal-backdrop {
    position: fixed;
    inset: 0;
    z-index: 9999;
    background: rgba(0, 0, 0, 0.75);
    backdrop-filter: blur(12px);
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 24px;
}

.feature-modal-box {
    width: 100%;
    max-width: 820px;
    background: white;
    border-radius: 24px;
    overflow: hidden;
    box-shadow: 0 40px 80px rgba(0, 0, 0, 0.5),
        0 0 0 1px rgba(124, 58, 237, 0.2);
    display: flex;
    flex-direction: column;
    max-height: 90vh;
}

:global(.dark) .feature-modal-box {
    background: #111827;
}

.feature-modal-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 16px 20px;
    border-bottom: 1px solid #f3f4f6;
}

:global(.dark) .feature-modal-header {
    border-bottom-color: #1f2937;
}

.feature-modal-close {
    width: 36px;
    height: 36px;
    border-radius: 50%;
    background: #f3f4f6;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #6b7280;
    transition: all 0.2s;
    cursor: pointer;
    border: none;
}

.feature-modal-close:hover {
    background: #fee2e2;
    color: #ef4444;
    transform: rotate(90deg);
}

:global(.dark) .feature-modal-close {
    background: #1f2937;
    color: #9ca3af;
}

:global(.dark) .feature-modal-close:hover {
    background: #450a0a;
    color: #f87171;
}

.feature-modal-media {
    position: relative;
    width: 100%;
    aspect-ratio: 16/9;
    background: #000;
    overflow: hidden;
    flex-shrink: 0;
}

.feature-modal-placeholder {
    width: 100%;
    height: 100%;
    background: #0a0a1a;
}

.feature-modal-placeholder-inner {
    position: relative;
    width: 100%;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    overflow: hidden;
}

.modal-blob {
    position: absolute;
    border-radius: 50%;
    filter: blur(60px);
    opacity: 0.4;
}

.modal-blob-1 {
    width: 300px;
    height: 300px;
    background: radial-gradient(circle, #7c3aed, transparent);
    top: -50px;
    left: -50px;
    animation: blob-drift 8s ease-in-out infinite alternate;
}

.modal-blob-2 {
    width: 250px;
    height: 250px;
    background: radial-gradient(circle, #2563eb, transparent);
    bottom: -30px;
    right: -30px;
    animation: blob-drift 10s ease-in-out infinite alternate-reverse;
}

.feature-modal-body {
    padding: 20px 24px 8px;
    overflow-y: auto;
}

.feature-modal-footer {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 12px 24px 20px;
    border-top: 1px solid #f3f4f6;
    margin-top: 8px;
}

:global(.dark) .feature-modal-footer {
    border-top-color: #1f2937;
}

/* ─── Modal Transition ───────────────────── */
.modal-enter-active {
    animation: modal-in 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
}

.modal-leave-active {
    animation: modal-out 0.2s ease-in forwards;
}

@keyframes modal-in {
    from {
        opacity: 0;
        transform: scale(0.92) translateY(20px);
    }

    to {
        opacity: 1;
        transform: scale(1) translateY(0);
    }
}

@keyframes modal-out {
    from {
        opacity: 1;
        transform: scale(1);
    }

    to {
        opacity: 0;
        transform: scale(0.94);
    }
}
</style>