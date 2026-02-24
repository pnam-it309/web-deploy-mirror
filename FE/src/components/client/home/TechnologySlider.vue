<template>
    <div class="relative py-16 overflow-hidden bg-white dark:bg-gray-900 border-y border-gray-100 dark:border-gray-800 transition-colors">

        <!-- Section header -->
        <div class="container mx-auto px-4 mb-10 text-center">
            <span class="inline-block text-xs font-bold tracking-widest uppercase text-violet-500 mb-2">Tech Stack</span>
            <h2 class="text-2xl font-bold text-gray-900 dark:text-white">Công nghệ sử dụng</h2>
            <div class="w-16 h-1 bg-gradient-to-r from-violet-500 to-blue-500 mx-auto mt-3 rounded-full"></div>
        </div>

        <!-- Row 1: Left → Right -->
        <div class="relative w-full overflow-hidden mb-4">
            <div class="marquee-fade-left"></div>
            <div class="marquee-fade-right"></div>

            <div class="marquee-track" :style="`--dur: ${duration}s`">
                <div v-for="i in 4" :key="`row1-${i}`" class="marquee-row" :aria-hidden="i > 1">
                    <div v-for="tech in technologies" :key="`${tech.id}-row1-${i}`"
                        class="tech-pill group"
                        @click="navigateToTech(tech.id)">
                        <div class="tech-pill-icon">
                            <img v-if="tech.icon" :src="tech.icon" :alt="tech.name"
                                class="w-6 h-6 object-contain filter grayscale group-hover:grayscale-0 transition-all duration-300"
                                @error="($event.target as HTMLImageElement).style.display = 'none'" />
                            <span v-else class="text-gray-400 font-bold text-sm">?</span>
                        </div>
                        <span class="text-sm font-semibold text-gray-600 dark:text-gray-400 group-hover:text-violet-600 dark:group-hover:text-violet-400 transition-colors whitespace-nowrap">
                            {{ tech.name }}
                        </span>
                    </div>
                </div>
            </div>
        </div>

        <!-- Row 2: Right → Left (reverse) -->
        <div class="relative w-full overflow-hidden">
            <div class="marquee-fade-left"></div>
            <div class="marquee-fade-right"></div>

            <div class="marquee-track marquee-reverse" :style="`--dur: ${Math.round(duration * 1.3)}s`">
                <div v-for="i in 4" :key="`row2-${i}`" class="marquee-row" :aria-hidden="i > 1">
                    <div v-for="tech in technologiesReversed" :key="`${tech.id}-row2-${i}`"
                        class="tech-pill group"
                        @click="navigateToTech(tech.id)">
                        <div class="tech-pill-icon">
                            <img v-if="tech.icon" :src="tech.icon" :alt="tech.name"
                                class="w-6 h-6 object-contain filter grayscale group-hover:grayscale-0 transition-all duration-300"
                                @error="($event.target as HTMLImageElement).style.display = 'none'" />
                            <span v-else class="text-gray-400 font-bold text-sm">?</span>
                        </div>
                        <span class="text-sm font-semibold text-gray-600 dark:text-gray-400 group-hover:text-violet-600 dark:group-hover:text-violet-400 transition-colors whitespace-nowrap">
                            {{ tech.name }}
                        </span>
                    </div>
                </div>
            </div>
        </div>

        <!-- CTA -->
        <div class="text-center mt-10">
            <router-link to="/apps"
                class="inline-flex items-center gap-2 px-6 py-2.5 rounded-full text-sm font-bold text-violet-600 dark:text-violet-400 border border-violet-200 dark:border-violet-800 hover:bg-violet-50 dark:hover:bg-violet-900/20 transition-all">
                Lọc theo công nghệ
                <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 7l5 5m0 0l-5 5m5-5H6" />
                </svg>
            </router-link>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import { getTechnologies, type PublicTechnology } from '@/services/client/client.service';

const router = useRouter();
const technologies = ref<PublicTechnology[]>([]);

const technologiesReversed = computed(() => [...technologies.value].reverse());

const duration = computed(() => {
    const count = Array.isArray(technologies.value) ? technologies.value.length : 0;
    return Math.max(30, 20 + count * 3);
});

const navigateToTech = (id: string) => {
    router.push({ path: '/apps', query: { technology: id } });
};

onMounted(async () => {
    try {
        const res = await getTechnologies();
        technologies.value = Array.isArray(res) ? res : [];
    } catch (error) {
        console.error("Failed to load technologies", error);
        technologies.value = [];
    }
});
</script>

<style scoped>
/* ─── Track ─────────────────────────────────────────── */
.marquee-track {
    display: flex;
    width: max-content;
    animation: scroll-left var(--dur, 40s) linear infinite;
}

.marquee-track:hover {
    animation-play-state: paused;
}

.marquee-reverse {
    animation-name: scroll-right;
}

.marquee-row {
    display: flex;
    align-items: center;
    gap: 12px;
    padding-right: 12px;
    flex-shrink: 0;
}

@keyframes scroll-left {
    from { transform: translateX(0); }
    to   { transform: translateX(-25%); }
}
@keyframes scroll-right {
    from { transform: translateX(-25%); }
    to   { transform: translateX(0); }
}

/* ─── Fade masks ─────────────────────────────────────── */
.marquee-fade-left,
.marquee-fade-right {
    position: absolute;
    top: 0;
    width: 120px;
    height: 100%;
    z-index: 10;
    pointer-events: none;
}

.marquee-fade-left {
    left: 0;
    background: linear-gradient(to right, white, transparent);
}
.marquee-fade-right {
    right: 0;
    background: linear-gradient(to left, white, transparent);
}

:global(.dark) .marquee-fade-left {
    background: linear-gradient(to right, #111827, transparent);
}
:global(.dark) .marquee-fade-right {
    background: linear-gradient(to left, #111827, transparent);
}

/* ─── Pill ─────────────────────────────────────────── */
.tech-pill {
    display: inline-flex;
    align-items: center;
    gap: 8px;
    padding: 8px 16px;
    background: white;
    border: 1px solid #e5e7eb;
    border-radius: 9999px;
    cursor: pointer;
    transition: all 0.25s ease;
    white-space: nowrap;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
}

:global(.dark) .tech-pill {
    background: rgba(31, 41, 55, 0.8);
    border-color: #374151;
}

.tech-pill:hover {
    border-color: #7c3aed;
    box-shadow: 0 4px 15px rgba(124, 58, 237, 0.2);
    transform: translateY(-2px);
}

.tech-pill-icon {
    width: 28px;
    height: 28px;
    border-radius: 50%;
    background: #f3f4f6;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-shrink: 0;
    overflow: hidden;
}

:global(.dark) .tech-pill-icon {
    background: #374151;
}
</style>