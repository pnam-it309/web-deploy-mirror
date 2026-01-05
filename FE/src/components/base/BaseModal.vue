<script setup lang="ts">
import { onMounted, onUnmounted } from 'vue';

interface Props {
  isOpen: boolean;
  title?: string;
  size?: 'sm' | 'md' | 'lg' | 'xl';
}
withDefaults(defineProps<Props>(), { size: 'md' });
const emit = defineEmits(['close']);

// Đóng khi nhấn ESC
const handleKeyup = (e: KeyboardEvent) => { if (e.key === 'Escape') emit('close'); };
onMounted(() => document.addEventListener('keyup', handleKeyup));
onUnmounted(() => document.removeEventListener('keyup', handleKeyup));

const sizes = {
  sm: 'max-w-md',
  md: 'max-w-lg',
  lg: 'max-w-2xl',
  xl: 'max-w-4xl'
};
</script>

<template>
  <Teleport to="body">
    <div v-if="isOpen" class="fixed inset-0 z-50 flex items-center justify-center p-4">
      <div class="absolute inset-0 bg-black/60 backdrop-blur-sm transition-opacity" @click="$emit('close')"></div>
      
      <div class="relative bg-white rounded-sm shadow-2xl w-full flex flex-col max-h-[90vh]" :class="sizes[size]">
        <div class="absolute top-0 left-0 w-full h-1 bg-primary"></div>
        
        <div v-if="title" class="px-6 py-4 border-b border-gray-100 flex justify-between items-center mt-1">
          <h3 class="text-lg font-bold text-dark uppercase tracking-wide">{{ title }}</h3>
          <button @click="$emit('close')" class="text-gray-400 hover:text-dark transition-colors">
            <svg class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" /></svg>
          </button>
        </div>
        
        <div class="p-6 overflow-y-auto">
          <slot />
        </div>
        
        <div v-if="$slots.footer" class="px-6 py-4 bg-gray-50 border-t border-gray-100 flex justify-end gap-3 rounded-b-sm">
          <slot name="footer" />
        </div>
      </div>
    </div>
  </Teleport>
</template>