<template>
    <div @click="navigateToDetail"
        class="product-card cursor-pointer group relative flex flex-col h-full"
        :style="`--glow-color: ${glowColor}`">

        <!-- Glow Border Ring -->
        <div class="card-glow-ring"></div>

        <!-- Card Body -->
        <div class="card-body flex flex-col h-full">

            <!-- ── Thumbnail ── -->
            <div class="relative w-full aspect-video overflow-hidden rounded-t-xl">

                <!-- No-thumbnail: Abstract Gradient Pattern -->
                <div v-if="!product.thumbnail" class="abstract-bg absolute inset-0"
                    :style="getAbstractStyle()">
                    <!-- Animated dots pattern -->
                    <div class="abstract-dots"></div>
                    <!-- Floating tech icon -->
                    <div class="absolute inset-0 flex flex-col items-center justify-center gap-3 z-10">
                        <div v-if="primaryTech" class="tech-icon-float">
                            <img :src="primaryTech.icon" :alt="primaryTech.name"
                                class="w-12 h-12 object-contain filter drop-shadow-[0_0_20px_rgba(255,255,255,0.6)]"
                                @error="onTechIconError" />
                        </div>
                        <div v-else class="tech-icon-float">
                            <svg class="w-12 h-12 text-white/60" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5"
                                    d="M10 20l4-16m4 4l4 4-4 4M6 16l-4-4 4-4" />
                            </svg>
                        </div>
                        <span class="text-white/70 text-xs font-semibold tracking-wider uppercase">
                            {{ primaryTech?.name || 'Project' }}
                        </span>
                    </div>
                </div>

                <!-- Actual Image -->
                <img v-if="product.thumbnail" :src="product.thumbnail" :alt="product.name"
                    loading="lazy"
                    class="w-full h-full object-cover transition-transform duration-700 group-hover:scale-110" />

                <!-- Hover Overlay — Info Layer -->
                <div class="hover-overlay absolute inset-0 flex flex-col justify-end p-4">
                    <!-- Tech icons row -->
                    <div class="flex items-center gap-1.5 mb-2">
                        <div v-for="(tech, i) in safeTechnologies.slice(0, 4)" :key="i"
                            class="w-6 h-6 rounded-full bg-white/20 backdrop-blur-sm border border-white/30 flex items-center justify-center overflow-hidden"
                            :title="tech.name">
                            <img :src="tech.icon" class="w-4 h-4 object-contain"
                                @error="($event.target as HTMLImageElement).style.display='none'" />
                        </div>
                        <span v-if="technologiesCount > 4"
                            class="text-white/80 text-[10px] font-bold ml-1">+{{ technologiesCount - 4 }}</span>
                    </div>
                    <!-- Short description -->
                    <p class="text-white/90 text-xs leading-relaxed line-clamp-2">
                        {{ product.description || 'Chưa có mô tả' }}
                    </p>
                    <!-- View count -->
                    <div v-if="(product as any).viewCount" class="flex items-center gap-1 mt-2">
                        <svg class="w-3.5 h-3.5 text-white/60" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                d="M15 12a3 3 0 11-6 0 3 3 0 016 0z M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
                        </svg>
                        <span class="text-white/60 text-[10px]">{{ (product as any).viewCount }} lượt xem</span>
                    </div>
                </div>

                <!-- Gradient bottom fade (always visible) -->
                <div class="absolute inset-0 bg-gradient-to-t from-black/50 via-transparent to-transparent opacity-60 group-hover:opacity-0 transition-opacity duration-300 pointer-events-none"></div>

                <!-- Status Badge -->
                <div v-if="statusBadge" class="absolute top-3 left-3 z-20">
                    <span class="status-badge" :class="statusBadge.class">
                        {{ statusBadge.label }}
                    </span>
                </div>

                <!-- Domain Tag -->
                <div class="absolute bottom-3 left-3 z-10 group-hover:opacity-0 transition-opacity duration-300">
                    <span class="domain-tag" :class="getDomainTagColor()">
                        {{ product.domainName }}
                    </span>
                </div>

                <!-- Like Button -->
                <div class="absolute top-3 right-3 z-20 opacity-0 group-hover:opacity-100 transition-all duration-300 translate-y-[-8px] group-hover:translate-y-0">
                    <LikeButton :product-id="product.id" class="!backdrop-blur shadow-lg" />
                </div>
            </div>

            <!-- ── Content ── -->
            <div class="card-content flex-1 flex flex-col p-4 pt-3">
                <h3 class="card-title line-clamp-1 mb-1">{{ product.name }}</h3>
                <p class="card-desc line-clamp-2 flex-1">{{ product.description }}</p>

                <!-- Footer -->
                <div class="flex items-center justify-between mt-3 pt-3 border-t border-gray-100 dark:border-gray-700/50">
                    <div class="flex -space-x-1.5">
                        <img v-for="(tech, idx) in safeTechnologies.slice(0, 3)" :key="idx" :src="tech.icon"
                            :title="tech.name"
                            class="w-6 h-6 rounded-full border-2 border-white dark:border-gray-800 bg-gray-50 object-cover"
                            @error="($event.target as HTMLImageElement).style.display='none'" />
                        <div v-if="technologiesCount > 3"
                            class="w-6 h-6 rounded-full border-2 border-white dark:border-gray-800 bg-gray-100 dark:bg-gray-700 flex items-center justify-center text-[9px] font-bold text-gray-500">
                            +{{ technologiesCount - 3 }}
                        </div>
                    </div>
                    <svg class="w-4 h-4 text-gray-300 dark:text-gray-600 group-hover:text-violet-500 group-hover:translate-x-0.5 transition-all" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
                    </svg>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router';
import { computed } from 'vue';
import { ROUTES_CONSTANTS } from '@/constants/path';
import LikeButton from '@/components/common/LikeButton.vue';
import { encodeId } from '@/utils';

export interface Product {
    id: string;
    name: string;
    description: string;
    thumbnail?: string;
    domainName?: string;
    technologies?: { name: string; icon: string }[];
    viewCount?: number;
    createdAt?: string;
    likeCount?: number;
}

const props = defineProps<{ product: Product }>();

// ── Abstract background gradients keyed by domain ──
const domainGradients: Record<string, { bg: string; glow: string }> = {
    'Project Management':  { bg: 'linear-gradient(135deg,#10b981,#059669,#047857)', glow: '#10b981' },
    'Business Tools':      { bg: 'linear-gradient(135deg,#8b5cf6,#7c3aed,#6d28d9)', glow: '#8b5cf6' },
    'Development':         { bg: 'linear-gradient(135deg,#3b82f6,#2563eb,#1d4ed8)', glow: '#3b82f6' },
    'Design':              { bg: 'linear-gradient(135deg,#ec4899,#db2777,#be185d)', glow: '#ec4899' },
    'Marketing':           { bg: 'linear-gradient(135deg,#f97316,#ea580c,#c2410c)', glow: '#f97316' },
    'Analytics':           { bg: 'linear-gradient(135deg,#06b6d4,#0891b2,#0e7490)', glow: '#06b6d4' },
    'Education':           { bg: 'linear-gradient(135deg,#6366f1,#4f46e5,#4338ca)', glow: '#6366f1' },
    'Finance':             { bg: 'linear-gradient(135deg,#22c55e,#16a34a,#15803d)', glow: '#22c55e' },
    'Healthcare':          { bg: 'linear-gradient(135deg,#ef4444,#dc2626,#b91c1c)', glow: '#ef4444' },
    'Academic Management': { bg: 'linear-gradient(135deg,#a855f7,#9333ea,#7e22ce)', glow: '#a855f7' },
};

const fallbackGradients = [
    { bg: 'linear-gradient(135deg,#7c3aed,#6d28d9,#4c1d95)', glow: '#7c3aed' },
    { bg: 'linear-gradient(135deg,#2563eb,#1d4ed8,#1e3a8a)', glow: '#2563eb' },
    { bg: 'linear-gradient(135deg,#0891b2,#0e7490,#164e63)', glow: '#0891b2' },
    { bg: 'linear-gradient(135deg,#16a34a,#15803d,#14532d)', glow: '#16a34a' },
];

const getTheme = () => {
    const domain = props.product.domainName || '';
    if (domainGradients[domain]) return domainGradients[domain];
    const idx = props.product.id ? props.product.id.charCodeAt(0) % fallbackGradients.length : 0;
    return fallbackGradients[idx];
};

const glowColor = computed(() => getTheme().glow);

const getAbstractStyle = () => ({
    background: getTheme().bg,
});

// ── Status Badge logic ──
const statusBadge = computed(() => {
    const daysSinceCreated = props.product.createdAt
        ? (Date.now() - new Date(props.product.createdAt).getTime()) / (1000 * 60 * 60 * 24)
        : 999;
    const likes = (props.product as any).likeCount || 0;

    if (daysSinceCreated < 14) return { label: '✦ Mới', class: 'badge-new' };
    if (likes > 50) return { label: '🏆 Award', class: 'badge-award' };
    if (likes > 20) return { label: '🔥 Hot', class: 'badge-hot' };
    return null;
});

// ── Domain tag color ──
const getDomainTagColor = () => {
    const map: Record<string, string> = {
        'Project Management': 'bg-emerald-500/90',
        'Business Tools': 'bg-purple-500/90',
        'Development': 'bg-blue-500/90',
        'Design': 'bg-pink-500/90',
        'Marketing': 'bg-orange-500/90',
        'Analytics': 'bg-cyan-500/90',
        'Education': 'bg-indigo-500/90',
        'Finance': 'bg-green-500/90',
        'Healthcare': 'bg-red-500/90',
        'Academic Management': 'bg-violet-500/90',
        'Social Media': 'bg-rose-500/90',
        'E-commerce': 'bg-amber-500/90',
        'Productivity': 'bg-teal-500/90',
    };
    return map[props.product.domainName || ''] || 'bg-gray-500/90';
};

const safeTechnologies = computed(() =>
    Array.isArray(props.product.technologies) ? props.product.technologies : []
);
const technologiesCount = computed(() => safeTechnologies.value.length);
const primaryTech = computed(() => safeTechnologies.value[0] || null);

const onTechIconError = (e: Event) => {
    (e.target as HTMLImageElement).style.display = 'none';
};

const router = useRouter();
const navigateToDetail = () => {
    router.push({
        name: ROUTES_CONSTANTS.CUSTOMER.children.APP_DETAIL.name,
        params: { id: encodeId(props.product.id) }
    });
};
</script>

<style scoped>
/* ── Card Shell ── */
.product-card {
    border-radius: 16px;
    position: relative;
    isolation: isolate;
    transition: transform 0.35s cubic-bezier(0.34, 1.56, 0.64, 1);
}

.product-card:hover {
    transform: translateY(-6px);
}

/* ── Glow Ring ── */
.card-glow-ring {
    position: absolute;
    inset: -2px;
    border-radius: 18px;
    background: conic-gradient(
        from var(--angle, 0deg),
        transparent 20%,
        var(--glow-color) 50%,
        transparent 80%
    );
    opacity: 0;
    transition: opacity 0.4s ease;
    z-index: 0;
    filter: blur(1px);
}

.product-card:hover .card-glow-ring {
    opacity: 0.85;
    animation: spin-glow 3s linear infinite;
}

@property --angle {
    syntax: '<angle>';
    inherits: true;
    initial-value: 0deg;
}

@keyframes spin-glow {
    to { --angle: 360deg; }
}

/* ── Card Body ── */
.card-body {
    position: relative;
    z-index: 1;
    border-radius: 16px;
    background: white;
    border: 1px solid rgba(0,0,0,0.07);
    overflow: hidden;
    box-shadow: 0 2px 8px rgba(0,0,0,0.06);
    transition: box-shadow 0.35s ease;
    height: 100%;
}

:global(.dark) .card-body {
    background: #1e1e2e;
    border-color: rgba(255,255,255,0.08);
}

.product-card:hover .card-body {
    box-shadow: 0 16px 40px rgba(0,0,0,0.14), 0 0 0 1px color-mix(in srgb, var(--glow-color) 30%, transparent);
}

/* ── Abstract Background ── */
.abstract-bg {
    overflow: hidden;
}

.abstract-dots {
    position: absolute;
    inset: 0;
    background-image: radial-gradient(rgba(255,255,255,0.15) 1px, transparent 1px);
    background-size: 20px 20px;
    animation: dots-drift 8s linear infinite;
}

@keyframes dots-drift {
    to { background-position: 20px 20px; }
}

.tech-icon-float {
    animation: float-icon 3s ease-in-out infinite;
    filter: drop-shadow(0 0 24px rgba(255,255,255,0.4));
}

@keyframes float-icon {
    0%, 100% { transform: translateY(0); }
    50% { transform: translateY(-8px); }
}

/* ── Hover Overlay ── */
.hover-overlay {
    background: linear-gradient(
        to top,
        rgba(0,0,0,0.85) 0%,
        rgba(0,0,0,0.5) 50%,
        rgba(0,0,0,0.1) 100%
    );
    opacity: 0;
    transition: opacity 0.35s ease;
}

.product-card:hover .hover-overlay {
    opacity: 1;
}

/* ── Status Badges ── */
.status-badge {
    display: inline-flex;
    align-items: center;
    padding: 3px 10px;
    border-radius: 20px;
    font-size: 10px;
    font-weight: 800;
    letter-spacing: 0.04em;
    backdrop-filter: blur(8px);
    border: 1px solid rgba(255,255,255,0.25);
    text-transform: uppercase;
}

.badge-new {
    background: linear-gradient(135deg, rgba(34,197,94,0.85), rgba(16,185,129,0.85));
    color: white;
    box-shadow: 0 0 12px rgba(34,197,94,0.5);
}

.badge-hot {
    background: linear-gradient(135deg, rgba(249,115,22,0.9), rgba(239,68,68,0.9));
    color: white;
    box-shadow: 0 0 12px rgba(249,115,22,0.5);
    animation: badge-pulse 2s ease-in-out infinite;
}

.badge-award {
    background: linear-gradient(135deg, rgba(234,179,8,0.9), rgba(245,158,11,0.9));
    color: white;
    box-shadow: 0 0 12px rgba(234,179,8,0.5);
}

@keyframes badge-pulse {
    0%, 100% { box-shadow: 0 0 12px rgba(249,115,22,0.5); }
    50% { box-shadow: 0 0 20px rgba(249,115,22,0.8); }
}

/* ── Domain Tag ── */
.domain-tag {
    display: inline-block;
    padding: 3px 10px;
    border-radius: 20px;
    font-size: 10px;
    font-weight: 700;
    letter-spacing: 0.06em;
    text-transform: uppercase;
    color: white;
    backdrop-filter: blur(8px);
}

/* ── Card Content ── */
.card-content {
    border-top: none;
}

.card-title {
    font-size: 15px;
    font-weight: 700;
    color: #111827;
    transition: color 0.2s;
}

:global(.dark) .card-title {
    color: #f9fafb;
}

.product-card:hover .card-title {
    color: #7c3aed;
}

.card-desc {
    font-size: 12px;
    color: #6b7280;
    line-height: 1.55;
    margin-top: 2px;
}

:global(.dark) .card-desc {
    color: #9ca3af;
}

/* ── Staggered entrance animation ── */
.product-card {
    animation: card-slide-up 0.5s ease both;
}

@keyframes card-slide-up {
    from {
        opacity: 0;
        transform: translateY(24px);
    }
    to {
        opacity: 1;
        transform: translateY(0);
    }
}
</style>