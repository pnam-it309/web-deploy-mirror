<template>
    <div class="flex items-center gap-1">
        <template v-for="star in 5" :key="star">
            <button v-if="interactive" @click="$emit('rate', star)" @mouseenter="hoverRating = star"
                @mouseleave="hoverRating = 0" class="focus:outline-none transition-transform hover:scale-110"
                :class="{ 'cursor-pointer': interactive }">
                <svg class="w-6 h-6 transition-colors" :class="getStarClass(star)" fill="currentColor"
                    viewBox="0 0 20 20">
                    <path
                        d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z" />
                </svg>
            </button>
            <svg v-else class="w-5 h-5" :class="star <= rating ? 'text-yellow-400' : 'text-gray-300 dark:text-gray-600'"
                fill="currentColor" viewBox="0 0 20 20">
                <path
                    d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z" />
            </svg>
        </template>
        <span v-if="showCount" class="ml-2 text-sm text-gray-500 dark:text-gray-400">
            {{ rating?.toFixed(1) || '0.0' }}
            <span v-if="reviewCount !== undefined">({{ reviewCount }})</span>
        </span>
    </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

defineProps<{
    rating: number
    interactive?: boolean
    showCount?: boolean
    reviewCount?: number
}>()

defineEmits<{
    (e: 'rate', value: number): void
}>()

const hoverRating = ref(0)

const getStarClass = (star: number) => {
    const currentRating = hoverRating.value || 0
    if (star <= currentRating) {
        return 'text-yellow-400'
    }
    return 'text-gray-300 dark:text-gray-600'
}
</script>
