<script setup lang="ts">
interface Item {
  label: string;
  to?: string; // Route path
}
defineProps<{ items: Item[] }>();
</script>

<template>
  <nav class="flex text-sm text-gray-500 mb-6">
    <ol class="flex items-center space-x-2">
      <li v-for="(item, index) in items" :key="index" class="flex items-center">
        <svg v-if="index > 0" class="w-4 h-4 mx-2 text-gray-300" fill="currentColor" viewBox="0 0 20 20"><path fill-rule="evenodd" d="M7.293 14.707a1 1 0 010-1.414L10.586 10 7.293 6.707a1 1 0 011.414-1.414l4 4a1 1 0 010 1.414l-4 4a1 1 0 01-1.414 0z" clip-rule="evenodd"></path></svg>
        
        <router-link 
          v-if="item.to && index < items.length - 1" 
          :to="item.to" 
          class="hover:text-primary transition-colors"
        >
          {{ item.label }}
        </router-link>
        <span v-else class="font-semibold text-dark">{{ item.label }}</span>
      </li>
    </ol>
  </nav>
</template>