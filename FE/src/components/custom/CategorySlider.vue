<template>
    <div class="category-slider-wrapper relative w-full my-8 px-4" v-if="categories.length > 0">
        <div ref="slider"
            class="category-slider flex gap-6 overflow-x-auto pb-8 pt-4 snap-x snap-mandatory scrollbar-hide px-4"
            @scroll="handleScroll">
            <div v-for="(category, index) in infiniteCategories" :key="index"
                class="category-card min-w-[260px] w-[260px] h-[340px] bg-white rounded-lg shadow-lg snap-center flex flex-col justify-between overflow-hidden cursor-pointer hover:-translate-y-2 transition-transform duration-300 border-t-8"
                :class="getThemeColor(index)" @click="selectCategory(category)">
                <div
                    class="card-content flex flex-col items-center justify-center flex-1 p-6 text-center relative z-10">
                    <!-- Decorative Illustration Placeholder -->
                    <div class="illustration mb-6 opacity-80" :class="getTextColor(index)">
                        <svg viewBox="0 0 24 24" fill="currentColor" class="w-24 h-24">
                            <path
                                d="M12 2L2 7l10 5 10-5-10-5zm0 9l2.5-1.25L12 8.5l-2.5 1.25L12 11zm0 2.5l-5-2.5-5 2.5L12 22l10-8.5-5-2.5-5 2.5z" />
                        </svg>
                    </div>

                    <h3 class="text-2xl font-black uppercase tracking-tighter leading-none mb-2 text-slate-800">
                        {{ category.name }}
                    </h3>
                    <p class="text-xs font-bold tracking-[0.2em] opacity-60 text-slate-500">COLLECTION</p>
                </div>
            </div>
        </div>

        <!-- Navigation Arrows -->
        <button @click="scrollLeft"
            class="absolute left-0 top-1/2 -translate-y-1/2 z-20 bg-white/90 hover:bg-white text-slate-800 p-3 rounded-full shadow-lg backdrop-blur-sm transition-all hover:scale-110 ml-2"
            aria-label="Previous categories">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2.5"
                stroke="currentColor" class="w-6 h-6">
                <path stroke-linecap="round" stroke-linejoin="round" d="M15.75 19.5L8.25 12l7.5-7.5" />
            </svg>
        </button>
        <button @click="scrollRight"
            class="absolute right-0 top-1/2 -translate-y-1/2 z-20 bg-white/90 hover:bg-white text-slate-800 p-3 rounded-full shadow-lg backdrop-blur-sm transition-all hover:scale-110 mr-2"
            aria-label="Next categories">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2.5"
                stroke="currentColor" class="w-6 h-6">
                <path stroke-linecap="round" stroke-linejoin="round" d="M8.25 4.5l7.5 7.5-7.5 7.5" />
            </svg>
        </button>
    </div>
</template>

<script setup lang="ts">
import { ref, computed, watch, nextTick, onMounted } from 'vue';

// Define Props
const props = defineProps<{
    categories: any[];
}>();

const slider = ref<HTMLElement | null>(null);
const emit = defineEmits(['select']);

// Create a tripled list for seamless looping [Buffer, Main, Buffer]
const infiniteCategories = computed(() => {
    if (!props.categories || props.categories.length === 0) return [];
    return [...props.categories, ...props.categories, ...props.categories];
});

const selectCategory = (category: any) => {
    emit('select', category.name);
};

const CARD_WIDTH = 260; // w-[260px]
const GAP = 24; // gap-6 = 24px
const ITEM_STRIDE = CARD_WIDTH + GAP;

// Scroll Logic
const scrollLeft = () => {
    if (slider.value) {
        slider.value.scrollBy({ left: -ITEM_STRIDE, behavior: 'smooth' });
    }
};

const scrollRight = () => {
    if (slider.value) {
        slider.value.scrollBy({ left: ITEM_STRIDE, behavior: 'smooth' });
    }
};

let ignoreScroll = false;

const handleScroll = () => {
    if (!slider.value || ignoreScroll || props.categories.length === 0) return;

    const sl = slider.value.scrollLeft;
    const singleSetWidth = props.categories.length * ITEM_STRIDE;

    // Thresholds
    // If we reach the start of the 3rd set (index 2*length), jump back to start of 2nd set (index length)
    const farRightThreshold = singleSetWidth * 2 - 10;

    if (sl >= farRightThreshold) {
        ignoreScroll = true;
        slider.value.scrollLeft = sl - singleSetWidth;
        requestAnimationFrame(() => ignoreScroll = false);
    }
    // If we are at the start of the 1st set...
    else if (sl < singleSetWidth - 280) {
        ignoreScroll = true;
        slider.value.scrollLeft = sl + singleSetWidth;
        requestAnimationFrame(() => ignoreScroll = false);
    }
};

const initScrollPosition = () => {
    if (slider.value && props.categories.length > 0) {
        const singleSetWidth = props.categories.length * ITEM_STRIDE;
        // Start at the beginning of the middle set
        slider.value.scrollLeft = singleSetWidth;
    }
};

// Colors for visual variety matching the screenshot vibe
const borderColors = [
    'border-emerald-700', // Greenish
    'border-amber-700',   // Brownish/Orange
    'border-cyan-700',    // Blueish
    'border-rose-400',    // Pinkish/Red
    'border-indigo-600',
    'border-orange-400',
];

const textColors = [
    'text-emerald-700',
    'text-amber-700',
    'text-cyan-700',
    'text-rose-400',
    'text-indigo-600',
    'text-orange-400',
];

const getThemeColor = (index: number) => {
    return borderColors[index % props.categories.length % borderColors.length];
};

const getTextColor = (index: number) => {
    return textColors[index % props.categories.length % textColors.length];
};

watch(() => props.categories, (newVal) => {
    if (newVal && newVal.length > 0) {
        nextTick(() => {
            initScrollPosition();
        });
    }
}, { immediate: true });

onMounted(() => {
    initScrollPosition();
});
</script>

<style scoped>
.scrollbar-hide::-webkit-scrollbar {
    display: none;
}

.scrollbar-hide {
    -ms-overflow-style: none;
    scrollbar-width: none;
}
</style>
