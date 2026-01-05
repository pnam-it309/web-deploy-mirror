<script setup lang="ts">
import { computed } from 'vue';

interface Props {
  currentPage: number;
  totalPages: number;
}
const props = defineProps<Props>();
const emit = defineEmits(['change']);

const pages = computed(() => {
  let start = Math.max(0, props.currentPage - 2);
  let end = Math.min(props.totalPages - 1, props.currentPage + 2);
  const list = [];
  for (let i = start; i <= end; i++) list.push(i);
  return list;
});
</script>

<template>
  <div class="flex items-center gap-2" v-if="totalPages > 1">
    <button 
      @click="emit('change', currentPage - 1)" 
      :disabled="currentPage === 0"
      class="w-8 h-8 flex items-center justify-center rounded-sm border border-gray-200 hover:border-primary disabled:opacity-50 disabled:hover:border-gray-200 transition-colors"
    >
      <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7"></path></svg>
    </button>

    <button 
      v-for="page in pages" 
      :key="page"
      @click="emit('change', page)"
      class="w-8 h-8 flex items-center justify-center rounded-sm text-sm font-medium transition-all duration-300"
      :class="page === currentPage ? 'bg-primary text-white shadow-md' : 'bg-white border border-gray-200 hover:border-primary text-dark'"
    >
      {{ page + 1 }}
    </button>

    <button 
      @click="emit('change', currentPage + 1)" 
      :disabled="currentPage >= totalPages - 1"
      class="w-8 h-8 flex items-center justify-center rounded-sm border border-gray-200 hover:border-primary disabled:opacity-50 disabled:hover:border-gray-200 transition-colors"
    >
      <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"></path></svg>
    </button>
  </div>
</template>