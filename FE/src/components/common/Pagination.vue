<template>
    <div class="flex items-center justify-center space-x-2 mt-8" v-if="totalPages > 1">
        <button @click="$emit('change', currentPage - 1)" :disabled="currentPage === 1"
            class="px-3 py-2 rounded-lg border border-gray-200 hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed transition-colors">
            &lt;
        </button>

        <template v-for="page in visiblePages" :key="page">
            <span v-if="page === -1" class="px-2 text-gray-400">...</span>
            <button v-else @click="$emit('change', page)" class="w-10 h-10 rounded-lg border transition-all font-medium"
                :class="currentPage === page ? 'bg-blue-600 border-blue-600 text-white' : 'border-gray-200 hover:border-blue-600 hover:text-blue-600'">
                {{ page }}
            </button>
        </template>

        <button @click="$emit('change', currentPage + 1)" :disabled="currentPage === totalPages"
            class="px-3 py-2 rounded-lg border border-gray-200 hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed transition-colors">
            &gt;
        </button>
    </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';

const props = defineProps<{
    currentPage: number;
    totalPages: number;
}>();

defineEmits<{
    (e: 'change', page: number): void;
}>();

const visiblePages = computed(() => {
    const delta = 2;
    const range: number[] = [];
    const rangeWithDots: number[] = [];
    let l: number | null = null;

    for (let i = 1; i <= props.totalPages; i++) {
        if (i === 1 || i === props.totalPages || (i >= props.currentPage - delta && i <= props.currentPage + delta)) {
            range.push(i);
        }
    }

    for (let i of range) {
        if (l) {
            if (i - l === 2) {
                rangeWithDots.push(l + 1);
            } else if (i - l !== 1) {
                rangeWithDots.push(-1);
            }
        }
        rangeWithDots.push(i);
        l = i;
    }

    return rangeWithDots;
});
</script>