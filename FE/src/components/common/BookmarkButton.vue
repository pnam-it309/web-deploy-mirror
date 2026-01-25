<template>
    <button @click.stop="handleToggle"
        class="bookmark-btn p-2 rounded-full transition-all duration-300 focus:outline-none focus:ring-2 focus:ring-yellow-400 focus:ring-offset-2"
        :class="[
            isBookmarked
                ? 'bg-yellow-100 text-yellow-600 hover:bg-yellow-200 dark:bg-yellow-900/30 dark:text-yellow-400'
                : 'bg-gray-100 text-gray-500 hover:bg-gray-200 dark:bg-gray-800 dark:text-gray-400 dark:hover:bg-gray-700'
        ]" :title="isBookmarked ? 'Bỏ lưu sản phẩm' : 'Lưu sản phẩm'">
        <!-- Filled heart when bookmarked -->
        <svg v-if="isBookmarked" class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
            <path fill-rule="evenodd"
                d="M3.172 5.172a4 4 0 015.656 0L10 6.343l1.172-1.171a4 4 0 115.656 5.656L10 17.657l-6.828-6.829a4 4 0 010-5.656z"
                clip-rule="evenodd" />
        </svg>
        <!-- Outline heart when not bookmarked -->
        <svg v-else class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" />
        </svg>
    </button>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useBookmarks, type BookmarkedProduct } from '@/composable/data/useBookmarks'

const props = defineProps<{
    product: Omit<BookmarkedProduct, 'addedAt'>
}>()

const { isBookmarked: checkBookmarked, toggleBookmark } = useBookmarks()

const isBookmarked = computed(() => checkBookmarked(props.product.id))

const handleToggle = () => {
    toggleBookmark(props.product)
}
</script>

<style scoped>
.bookmark-btn {
    transform: scale(1);
}

.bookmark-btn:active {
    transform: scale(0.95);
}
</style>
