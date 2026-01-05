<template>
    <div class="relative w-full aspect-video bg-black rounded-2xl overflow-hidden group">
        <!-- Current Slide -->
        <div class="w-full h-full relative">
            <img v-if="currentMedia.type === 'image'" :src="currentMedia.url" class="w-full h-full object-contain" loading="lazy" />
            <iframe v-else-if="currentMedia.type === 'video'" :src="currentMedia.url" class="w-full h-full"
                frameborder="0"
                allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
                allowfullscreen></iframe>
        </div>

        <!-- Navigation (if multiple) -->
        <button v-if="media.length > 1" @click="prev"
            class="absolute left-4 top-1/2 -translate-y-1/2 p-2 rounded-full bg-black/50 text-white hover:bg-black/70 transition-colors opacity-0 group-hover:opacity-100">
            &larr;
        </button>
        <button v-if="media.length > 1" @click="next"
            class="absolute right-4 top-1/2 -translate-y-1/2 p-2 rounded-full bg-black/50 text-white hover:bg-black/70 transition-colors opacity-0 group-hover:opacity-100">
            &rarr;
        </button>

        <!-- Thumbnails -->
        <div v-if="media.length > 1" class="absolute bottom-4 left-1/2 -translate-x-1/2 flex gap-2">
            <button v-for="(item, idx) in media" :key="idx" @click="currentIndex = idx"
                class="w-2 h-2 rounded-full transition-all"
                :class="currentIndex === idx ? 'bg-white w-6' : 'bg-white/50 hover:bg-white/80'"></button>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';

export interface MediaItem {
    type: 'image' | 'video';
    url: string;
}

const props = defineProps<{
    media: MediaItem[]
}>();

const currentIndex = ref(0);

const currentMedia = computed(() => {
    return props.media[currentIndex.value] || { type: 'image', url: '' };
});

const next = () => {
    currentIndex.value = (currentIndex.value + 1) % props.media.length;
};

const prev = () => {
    currentIndex.value = (currentIndex.value - 1 + props.media.length) % props.media.length;
};
</script>
