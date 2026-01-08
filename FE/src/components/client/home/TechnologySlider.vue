<template>
    <div class="py-12 bg-white border-y border-gray-100 overflow-hidden">
        <div class="container mx-auto px-4 mb-8 text-center">
            <h2 class="text-2xl font-bold text-gray-900 mb-2">Công nghệ sử dụng</h2>
            <div class="w-16 h-1 bg-blue-600 mx-auto rounded"></div>
        </div>

        <div class="relative w-full overflow-hidden">
            <!-- Left Fade -->
            <div class="absolute top-0 left-0 w-20 h-full bg-gradient-to-r from-white to-transparent z-10"></div>

            <!-- Marquee Mover -->
            <div class="marquee-mover flex w-fit" :style="{ '--marquee-duration': duration + 's' }">
                <div v-for="i in 4" :key="i" class="flex items-center gap-8 marquee-part pr-8" :aria-hidden="i > 1">
                    <div v-for="tech in technologies" :key="tech.id + '-' + i"
                        class="flex flex-col items-center justify-center p-4 min-w-[120px] h-[120px] bg-gray-50 rounded-xl hover:bg-white hover:shadow-lg transition-all duration-300 border border-gray-100 group">
                        <img v-if="tech.icon" :src="tech.icon" :alt="tech.name"
                            class="w-12 h-12 object-contain mb-3 filter grayscale group-hover:grayscale-0 transition-all duration-300" />
                        <span v-else class="text-2xl font-bold text-gray-400">?</span>
                        <span class="text-sm font-medium text-gray-600 group-hover:text-gray-900">{{ tech.name }}</span>
                    </div>
                </div>
            </div>

            <!-- Right Fade -->
            <div class="absolute top-0 right-0 w-20 h-full bg-gradient-to-l from-white to-transparent z-10"></div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';
import { getTechnologies, type PublicTechnology } from '@/services/client/client.service';

const technologies = ref<PublicTechnology[]>([]);

const duration = computed(() => {
    // 60s base, add 2s for every item to keep consistent speed
    return 60 + (technologies.value.length * 4);
});

onMounted(async () => {
    try {
        const res = await getTechnologies();
        technologies.value = res;
    } catch (error) {
        console.error("Failed to load technologies", error);
    }
});
</script>

<style scoped>
.marquee-mover {
    display: flex;
    overflow: hidden;
    width: max-content;
    /* Ensure it takes full width of content */
    animation: scroll var(--marquee-duration) linear infinite;
}

.marquee-part {
    flex-shrink: 0;
    display: flex;
    align-items: center;
    /* padding-right via tailwind class works, no specific css needed here */
}

@keyframes scroll {
    from {
        transform: translateX(0);
    }

    to {
        transform: translateX(-25%);
    }
}

/* Pause on hover */
.marquee-mover:hover {
    animation-play-state: paused;
}
</style>