<template>
    <div @click="navigateToDetail"
        class="bento-card group"
        :class="large ? 'bento-card-large' : 'bento-card-small'">

        <!-- ── Thumbnail / Image with Masking ── -->
        <div class="bento-image-wrap">
            <!-- Gradient Placeholder when no thumbnail -->
            <div v-if="!product.thumbnail" class="absolute inset-0"
                :style="`background: ${gradientBg}`"></div>

            <!-- Actual Image with slow zoom on hover -->
            <img v-if="product.thumbnail"
                :src="product.thumbnail"
                :alt="product.name"
                loading="lazy"
                class="bento-img" />

            <!-- Parallax shimmer overlay on hover -->
            <div class="bento-shimmer"></div>

            <!-- Dark gradient from bottom -->
            <div class="absolute inset-0 bg-gradient-to-t from-black/80 via-black/20 to-transparent"></div>

            <!-- Top badges row -->
            <div class="absolute top-4 left-4 right-4 flex items-start justify-between z-10">
                <!-- Domain tag -->
                <span class="domain-badge" :style="`background: ${domainColor}22; border-color: ${domainColor}55; color: ${domainColor}`">
                    {{ product.domainName || 'Dự án' }}
                </span>
                <!-- Like button placeholder -->
                <div class="opacity-0 group-hover:opacity-100 transition-all duration-300 translate-y-[-8px] group-hover:translate-y-0">
                    <LikeButton :product-id="product.id" class="!backdrop-blur shadow-sm" />
                </div>
            </div>

            <!-- "No thumbnail" centered icon -->
            <div v-if="!product.thumbnail" class="absolute inset-0 flex items-center justify-center z-10">
                <div class="text-white/20 text-center">
                    <svg class="w-16 h-16 mx-auto mb-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1"
                            d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
                    </svg>
                </div>
            </div>
        </div>

        <!-- ── Info Panel (slides up on hover) ── -->
        <div class="bento-info" :class="large ? 'bento-info-large' : 'bento-info-small'">

            <!-- Title -->
            <h3 class="font-bold text-white leading-tight group-hover:text-violet-300 transition-colors"
                :class="large ? 'text-2xl mb-3 line-clamp-2' : 'text-base mb-2 line-clamp-1'">
                {{ product.name }}
            </h3>

            <!-- Description (only on large card or on hover for small) -->
            <p class="text-gray-300 leading-relaxed"
                :class="large ? 'text-sm line-clamp-3 mb-4' : 'text-xs line-clamp-2 mb-3 opacity-0 group-hover:opacity-100 transition-opacity duration-300'">
                {{ product.description }}
            </p>

            <!-- Tech Stack -->
            <div class="flex items-center gap-2 mt-auto">
                <div class="flex -space-x-2">
                    <img v-for="(tech, idx) in safeTechnologies.slice(0, large ? 5 : 3)"
                        :key="idx"
                        :src="tech.icon"
                        :title="tech.name"
                        class="tech-icon border-2 border-white/20 bg-gray-800"
                        :class="large ? 'w-8 h-8' : 'w-6 h-6'"
                        @error="($event.target as HTMLImageElement).style.display = 'none'" />
                    <div v-if="technologiesCount > (large ? 5 : 3)"
                        class="flex items-center justify-center rounded-full border-2 border-white/20 bg-gray-700 text-white font-bold"
                        :class="large ? 'w-8 h-8 text-xs' : 'w-6 h-6 text-[10px]'">
                        +{{ technologiesCount - (large ? 5 : 3) }}
                    </div>
                </div>

                <!-- CTA arrow (large card only) -->
                <div v-if="large" class="ml-auto opacity-0 group-hover:opacity-100 transition-all duration-300 translate-x-4 group-hover:translate-x-0">
                    <span class="inline-flex items-center gap-1 text-violet-400 font-bold text-sm">
                        Xem chi tiết
                        <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 7l5 5m0 0l-5 5m5-5H6" />
                        </svg>
                    </span>
                </div>

                <!-- Arrow for small cards -->
                <div v-else class="ml-auto opacity-0 group-hover:opacity-100 transition-opacity">
                    <svg class="w-4 h-4 text-violet-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 7l5 5m0 0l-5 5m5-5H6" />
                    </svg>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { useRouter } from 'vue-router';
import { ROUTES_CONSTANTS } from '@/constants/path';
import LikeButton from '@/components/common/LikeButton.vue';
import { encodeId } from '@/utils';

interface Product {
    id: string;
    name: string;
    description: string;
    thumbnail?: string;
    domainName?: string;
    technologies?: { name: string; icon: string }[];
}

const props = defineProps<{
    product: Product;
    large?: boolean;
}>();

const router = useRouter();

const navigateToDetail = () => {
    router.push({
        name: ROUTES_CONSTANTS.CUSTOMER.children.APP_DETAIL.name,
        params: { id: encodeId(props.product.id) }
    });
};

const safeTechnologies = computed(() =>
    Array.isArray(props.product.technologies) ? props.product.technologies : []
);
const technologiesCount = computed(() => safeTechnologies.value.length);

// ── Color mapping for domains ──
const domainColorMap: Record<string, string> = {
    'E-commerce': '#f59e0b',
    'Project Management': '#10b981',
    'Business Tools': '#8b5cf6',
    'Development': '#3b82f6',
    'Design': '#ec4899',
    'Marketing': '#f97316',
    'Analytics': '#06b6d4',
    'Education': '#6366f1',
    'Finance': '#22c55e',
    'Healthcare': '#ef4444',
    'Social Media': '#a855f7',
    'Productivity': '#14b8a6',
};

const domainColor = computed(() => {
    return domainColorMap[props.product.domainName || ''] || '#94a3b8';
});

// ── Gradient background when no thumbnail ──
const gradients = [
    'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
    'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)',
    'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)',
    'linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)',
    'linear-gradient(135deg, #fa709a 0%, #fee140 100%)',
    'linear-gradient(135deg, #a18cd1 0%, #fbc2eb 100%)',
    'linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%)',
    'linear-gradient(135deg, #a1c4fd 0%, #c2e9fb 100%)',
];

const gradientBg = computed(() => {
    const idx = props.product.id
        ? props.product.id.charCodeAt(0) % gradients.length
        : 0;
    return gradients[idx];
});
</script>

<style scoped>
/* ─── Base Card ───────────────────────────────────── */
.bento-card {
    position: relative;
    overflow: hidden;
    cursor: pointer;
    /* Asymmetric border-radius for "pro" look */
    border-radius: 20px 20px 24px 16px;
    border: 1px solid rgba(255, 255, 255, 0.08);
    background: #111;
    display: flex;
    flex-direction: column;
    transition: transform 0.4s cubic-bezier(0.34, 1.56, 0.64, 1),
                box-shadow 0.4s ease,
                border-color 0.3s ease;
}

.bento-card:hover {
    transform: translateY(-6px) scale(1.01);
    box-shadow: 0 30px 60px rgba(124, 58, 237, 0.25),
                0 0 0 1px rgba(124, 58, 237, 0.3);
    border-color: rgba(124, 58, 237, 0.4);
}

.bento-card-large {
    min-height: 480px;
}

.bento-card-small {
    min-height: 220px;
}

/* ─── Image Wrapper ──────────────────────────────── */
.bento-image-wrap {
    position: absolute;
    inset: 0;
    overflow: hidden;
    border-radius: inherit;
}

/* ─── Thumbnail with slow zoom ───────────────────── */
.bento-img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    transform-origin: center;
    transition: transform 0.8s cubic-bezier(0.25, 0.46, 0.45, 0.94);
}

.bento-card:hover .bento-img {
    transform: scale(1.08);
}

/* ─── Shimmer on hover ───────────────────────────── */
.bento-shimmer {
    position: absolute;
    inset: 0;
    background: linear-gradient(
        105deg,
        transparent 40%,
        rgba(255, 255, 255, 0.06) 50%,
        transparent 60%
    );
    background-size: 200% 100%;
    opacity: 0;
    transition: opacity 0.4s;
}

.bento-card:hover .bento-shimmer {
    opacity: 1;
    animation: shimmer-sweep 1.5s ease-in-out;
}

@keyframes shimmer-sweep {
    from { background-position: 200% 0; }
    to { background-position: -200% 0; }
}

/* ─── Domain Badge ───────────────────────────────── */
.domain-badge {
    display: inline-block;
    padding: 4px 12px;
    border-radius: 9999px;
    border: 1px solid;
    font-size: 10px;
    font-weight: 700;
    letter-spacing: 0.05em;
    text-transform: uppercase;
    backdrop-filter: blur(8px);
}

/* ─── Info Panel ─────────────────────────────────── */
.bento-info {
    position: absolute;
    left: 0;
    right: 0;
    bottom: 0;
    z-index: 10;
    padding: 20px;
    display: flex;
    flex-direction: column;
    transition: transform 0.4s cubic-bezier(0.34, 1.56, 0.64, 1);
}

.bento-info-large {
    transform: translateY(0);
}

.bento-info-small {
    transform: translateY(8px);
}

.bento-card:hover .bento-info-small {
    transform: translateY(0);
}

/* ─── Tech Icon ──────────────────────────────────── */
.tech-icon {
    border-radius: 50%;
    object-fit: cover;
    transition: transform 0.2s;
}

.tech-icon:hover {
    transform: scale(1.2) translateY(-2px);
    z-index: 1;
}
</style>
