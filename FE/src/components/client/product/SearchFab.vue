<template>
    <div ref="fabContainer" class="fixed z-50 flex items-center justify-center pointer-events-none" :style="fabStyle">
        <!-- Backdrop -->
        <Teleport to="body">
            <div v-if="isOpen" @click="toggle"
                class="fixed inset-0 bg-black/30 backdrop-blur-sm pointer-events-auto transition-opacity z-40"></div>
        </Teleport>

        <!-- Popover -->
        <Transition enter-active-class="transition duration-300 ease-out cubic-bezier(0.16, 1, 0.3, 1)"
            :enter-from-class="transitionClasses.enterFrom" :enter-to-class="transitionClasses.enterTo"
            leave-active-class="transition duration-200 ease-in cubic-bezier(0.16, 1, 0.3, 1)"
            :leave-from-class="transitionClasses.enterTo" :leave-to-class="transitionClasses.leaveTo">

            <div v-if="isOpen" :class="popoverClasses" :style="popoverStyles"
                class="absolute pointer-events-auto w-[90vw] md:w-[28rem] bg-white/80 dark:bg-gray-900/80 backdrop-blur-2xl rounded-2xl shadow-2xl shadow-primary/20 border border-white/20 dark:border-gray-700/50 overflow-hidden flex flex-col z-50">

                <!-- Header Gradient -->
                <div class="h-1 w-full bg-gradient-to-r from-primary via-purple-500 to-pink-500 shrink-0"></div>

                <div class="p-5 overflow-y-auto custom-scrollbar flex-1">
                    <!-- Title & Filter Summary -->
                    <div class="flex justify-between items-center mb-4">
                        <div class="flex items-center gap-2 text-primary font-bold">
                            <i class='bx bx-search-alt-2'></i>
                            <span class="text-sm uppercase tracking-wider">Tìm kiếm & Bộ lọc</span>
                        </div>
                        <div v-if="(activeFiltersCount || 0) > 0"
                            class="flex items-center gap-1.5 px-2.5 py-1 bg-primary/10 text-primary rounded-full text-xs font-bold ring-1 ring-inset ring-primary/20">
                            <i class='bx bx-filter-alt hidden sm:inline-block'></i>
                            <span>{{ activeFiltersCount }} Đang chọn</span>
                        </div>
                    </div>

                    <!-- Search Input Field -->
                    <div class="relative group mb-6">
                        <div
                            class="absolute -inset-0.5 bg-gradient-to-r from-primary to-purple-600 rounded-xl opacity-20 group-hover:opacity-40 transition duration-500 blur">
                        </div>
                        <div
                            class="relative flex items-center bg-white/50 dark:bg-gray-900/50 rounded-xl overflow-hidden shadow-inner border border-gray-100 dark:border-gray-700 backdrop-blur-sm">
                            <input ref="searchInput" type="text" :value="modelValue"
                                @input="$emit('update:modelValue', ($event.target as HTMLInputElement).value)"
                                @keyup.enter="handleSearch" placeholder="Nhập tên ứng dụng, công nghệ..."
                                class="w-full px-4 py-3.5 bg-transparent border-none focus:outline-none focus:ring-0 text-gray-900 dark:text-white placeholder-gray-500 font-medium text-base" />
                        </div>
                    </div>

                    <!-- Divider -->
                    <div class="h-px bg-gray-200/50 dark:bg-gray-700/50 mb-6"></div>

                    <!-- Filter Slot -->
                    <slot></slot>

                </div>
            </div>
        </Transition>

        <!-- Floating Action Button -->
        <button ref="fabButton" @click="handleClick"
            class="pointer-events-auto group relative flex items-center justify-center w-14 h-14 bg-primary hover:bg-primary-600 text-white rounded-full shadow-lg shadow-primary/30 hover:shadow-primary/50 transition-colors duration-300 z-50">

            <!-- Pulse Effect -->
            <span v-if="!isOpen && !isDidDrag"
                class="absolute inset-0 rounded-full border border-white/30 animate-[ping_2s_ease-in-out_infinite]"></span>

            <!-- Icons -->
            <div class="relative w-6 h-6 flex items-center justify-center pointer-events-none">
                <!-- Search Icon (Closed State) -->
                <i class='bx bx-search text-3xl absolute transition-all duration-300'
                    :class="isOpen ? 'opacity-0 rotate-180 scale-50' : 'opacity-100 rotate-0 scale-100'"></i>

                <!-- Close Icon (Open State) -->
                <i class='bx bx-x text-3xl absolute transition-all duration-300'
                    :class="isOpen ? 'opacity-100 rotate-0 scale-100' : 'opacity-0 -rotate-180 scale-50'"></i>
            </div>
        </button>
    </div>
</template>

<script setup lang="ts">
import { ref, nextTick, computed, onMounted } from 'vue';
import { useDraggable, useWindowSize } from '@vueuse/core';

defineProps<{
    modelValue: string;
    activeFiltersCount?: number;
}>();

const emit = defineEmits(['update:modelValue', 'search']);

const isOpen = ref(false);
const searchInput = ref<HTMLInputElement | null>(null);
const fabButton = ref<HTMLElement | null>(null);
const isDidDrag = ref(false);
const { width, height } = useWindowSize();

const { x, y } = useDraggable(fabButton, {
    initialValue: { x: window.innerWidth - 80, y: window.innerHeight - 80 },
    preventDefault: false,
    onMove: () => { isDidDrag.value = true; },
    onEnd: () => { setTimeout(() => { isDidDrag.value = false; }, 100); }
});

const fabStyle = computed(() => ({
    left: `${x.value}px`,
    top: `${y.value}px`,
    position: 'fixed' as const,
    bottom: 'auto',
    right: 'auto',
}));

// Robust Positioning Logic:
// 1. Vertical: Always open UP (bottom-full) unless we are very close to top (top < 300px)
// 2. Horizontal: Always ALIGN RIGHT (right-0) unless we are on left side of screen (left-0)
// This is simpler and covers 99% of cases without complex 4-way logic that might conflict or be buggy.

const isTopHalf = computed(() => y.value < 300);
const isLeftHalf = computed(() => x.value < width.value / 2);

const popoverClasses = computed(() => {
   const classes: string[] = [];
   
   // Vertical
   if (isTopHalf.value) {
       classes.push('top-full mt-4 origin-top-right');
   } else {
       classes.push('bottom-full mb-4 origin-bottom-right');
   }

   // Horizontal
   if (isLeftHalf.value) {
       classes.push('left-0'); // Align left edge to button left edge
       // If bottom aligned
       if (!isTopHalf.value) classes[0] = classes[0].replace('origin-bottom-right', 'origin-bottom-left');
       else classes[0] = classes[0].replace('origin-top-right', 'origin-top-left');
   } else {
       classes.push('right-0'); // Align right edge to button right edge
   }

   return classes;
});

// Animations
const transitionClasses = computed(() => {
    let enterFrom = 'opacity-0 scale-95 ';
    let leaveTo = 'opacity-0 scale-95 ';
    let enterTo = 'transform translate-y-0 opacity-100 scale-100';

    if (isTopHalf.value) {
        // Opening Down
        enterFrom += 'transform -translate-y-4';
        leaveTo += 'transform -translate-y-4';
    } else {
        // Opening Up
        enterFrom += 'transform translate-y-4';
        leaveTo += 'transform translate-y-4';
    }

    return { enterFrom, enterTo, leaveTo };
});

const popoverStyles = computed(() => {
    // Max Height Logic
    // If opening up (default), max height is space available above (y) - margin
    // If opening down (isTopHalf), max height is space available below (height - y - buttonHeight) - margin
    
    const margin = 20;
    const buttonSize = 56;
    let maxContentHeight = 600;

    if (isTopHalf.value) {
        // Down
        maxContentHeight = height.value - y.value - buttonSize - margin;
    } else {
        // Up
        maxContentHeight = y.value - margin;
    }

    // Safety Cap
    const finalHeight = Math.min(maxContentHeight, height.value * 0.8);

    return {
        maxHeight: `${finalHeight}px`
    };
});


const handleClick = () => {
    if (isDidDrag.value) return;
    toggle();
};

const toggle = () => {
    isOpen.value = !isOpen.value;
    if (isOpen.value) {
        nextTick(() => {
            searchInput.value?.focus();
        });
    }
};

const handleSearch = () => {
    emit('search');
};

onMounted(() => {
    x.value = window.innerWidth - 80;
    y.value = window.innerHeight - 80;
});
</script>

<style scoped>
/* Ensure smooth rendering */
</style>
