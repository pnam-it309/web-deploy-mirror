<script setup lang="ts">
import { computed, onMounted, onUnmounted } from 'vue';
import { X } from 'lucide-vue-next';

const props = withDefaults(defineProps<{
  class?: string;
  showClose?: boolean;
}>(), {
  showClose: true
});

const emit = defineEmits(['close']);

const handleKeydown = (e: KeyboardEvent) => {
  if (e.key === 'Escape') {
    emit('close');
  }
};

onMounted(() => {
  document.addEventListener('keydown', handleKeydown);
  document.body.style.overflow = 'hidden';
});

onUnmounted(() => {
  document.removeEventListener('keydown', handleKeydown);
  document.body.style.overflow = '';
});

const classes = computed(() => ({
  'relative z-50 grid w-full gap-4 border bg-background p-6 shadow-lg sm:rounded-lg md:w-full': true,
  'max-w-lg': !props.class?.includes('max-w-'),
  [props.class || '']: true
}));
</script>

<template>
  <div :class="classes">
    <slot />
    <button
      v-if="showClose"
      type="button"
      class="absolute right-4 top-4 rounded-sm opacity-70 ring-offset-background transition-opacity hover:opacity-100 focus:outline-none focus:ring-2 focus:ring-ring focus:ring-offset-2 disabled:pointer-events-none"
      @click="$emit('close')"
    >
      <X class="h-4 w-4" />
      <span class="sr-only">Close</span>
    </button>
  </div>
</template>
